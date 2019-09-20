package com.oseasy.pw.modules.pw.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.TreeService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.pw.modules.pw.dao.PwSpaceDao;
import com.oseasy.pw.modules.pw.entity.PwAppointment;
import com.oseasy.pw.modules.pw.entity.PwDesignerCanvas;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.entity.PwSpace;
import com.oseasy.pw.modules.pw.exception.SpaceException;
import com.oseasy.pw.modules.pw.vo.PwSpaceMap;
import com.oseasy.pw.modules.pw.vo.PwSpaceType;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 设施Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwSpaceService extends TreeService<PwSpaceDao, PwSpace> {

    @Autowired
    private PwSpaceDao pwSpaceDao;

    @Autowired
    private PwRoomService pwRoomService;

    @Autowired
    private PwFassetsService pwFassetsService;

    @Autowired
    private PwAppointmentService pwAppointmentService;

    @Autowired
    private PwEnterRoomService pwEnterRoomService;

    @Autowired
    private PwDesignerCanvasService pwDesignerCanvasService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public PwSpace get(String id) {
        PwSpace pwSpace = super.get(id);
        if (PwSpaceType.SCHOOL.getValue().equals(pwSpace.getType())) {
            pwSpace.setName(CoreUtils.getOffice(CoreIds.NCE_SYS_TREE_ROOT.getId()).getName());
        }
        return pwSpace;
    }

    public List<PwSpace> findList(PwSpace pwSpace) {
        if (StringUtil.isNotBlank(pwSpace.getParentIds())) {
            pwSpace.setParentIds("," + pwSpace.getParentIds() + ",");
        }
        List<PwSpace> list = super.findList(pwSpace);
        for (PwSpace space : list) {
            if (PwSpaceType.SCHOOL.getValue().equals(space.getType())) {
                space.setName(CoreUtils.getOffice(CoreIds.NCE_SYS_TREE_ROOT.getId()).getName());
                break;
            }
        }
        return list;
    }

    public List<PwSpace> findListRooms(PwSpace entity){
        return dao.findListRooms(entity);
    }

    @Transactional(readOnly = false)
    public void save(PwSpace pwSpace) {
        validatePwSpace(pwSpace);
        if (StringUtils.isBlank(pwSpace.getId())) {
            pwSpace.setIsNewRecord(true);
            pwSpace.setId(IdGen.uuid());
        }
        /**
         * 添加楼层时不能超过总楼层数
         */
        if (PwSpaceType.FLOOR.getValue().equals(pwSpace.getType()) && pwSpace.getIsNewRecord()) {
            PwSpace s = new PwSpace();
            PwSpace p = new PwSpace(pwSpace.getParentId());
            s.setParent(p);
            List<PwSpace> children = findList(s);
            String f = StringUtils.isBlank(pwSpace.getParent().getFloorNum()) ? get(pwSpace.getParentId()).getFloorNum() : pwSpace.getParent().getFloorNum();
            int floorNum = Integer.valueOf(f);
            if (floorNum == children.size()) {
                logger.warn("已达到最大楼层数，不能再添加");
                throw new SpaceException("已达到最大楼层数，不能再添加");
            }
        }
        /**
         * 处理背景图片
         */
        if (StringUtils.isNotBlank(pwSpace.getImageUrl())) {
            try {
                String imageUrl = FtpUtil.moveFile( pwSpace.getImageUrl());
                pwSpace.setImageUrl(imageUrl);
            } catch (Exception e) {
                logger.warn(e.getMessage());
                throw new SpaceException("保存附件失败，请检查");
            }
        }
        super.save(pwSpace);
        /**
         * 创建楼栋时根据楼层数添加楼层
         */
        if (PwSpaceType.BUILDING.getValue().equals(pwSpace.getType()) && pwSpace.getIsNewRecord()) {
            if (StringUtils.isNotBlank(pwSpace.getFloorNum())) {
                int floor = Integer.valueOf(pwSpace.getFloorNum());
                for (int i = 0; i < floor; i++) {
                    PwSpace space = new PwSpace();
                    space.setParent(pwSpace);
                    String s = i + 1 + "";
                    if (s.length() < 2) {
                        s = "0" + s;
                    }
                    space.setName(s + "层");
                    space.setType(PwSpaceType.FLOOR.getValue());
                    super.save(space);
                }
            }
        }

    }

    @Transactional(readOnly = false)
    public void delete(PwSpace pwSpace) {
        PwSpace newPwSpace = this.get(pwSpace.getId());
        if (newPwSpace == null) {
            logger.warn("指定的设施不存在");
            throw new SpaceException("指定的设施不存在");
        }

        /**
         * 删除规则：下级的楼层中的房间如果有关联资产，需要先解除资产关联关系，再删除房间，最后删除设施;
         *          房间相关的预约全部取消; 入驻有使用到，不能删除
         *
         */
        List<String> roomIds = new ArrayList<>();//存放所有子设施的房间ID
        if (newPwSpace.getType().equals(PwSpaceType.FLOOR.getValue())) {
            findRooms(newPwSpace, roomIds);
        } else {
            String parentIds = newPwSpace.getParentIds();//当前设施的parentIds
            parentIds += newPwSpace.getId();
            PwSpace p = new PwSpace();
            p.setParentIds(parentIds);
            List<PwSpace> children = pwSpaceDao.findByParentIdsLike(p);//当前设施所有的子
            if (!children.isEmpty()) {
                for (PwSpace child : children) {
                    if (child.getType().equals(PwSpaceType.FLOOR.getValue())) {
                        findRooms(child, roomIds);
                    }
                }
            }
        }
        if (!roomIds.isEmpty()) {
            //pwAppointmentService.deleteByRoomIds(roomIds);//删除预约
            pwFassetsService.clearByRoomIds(roomIds);//回收所有已关联房间的固定资产
            pwRoomService.deleteByRoomIds(roomIds);//删除房间
        }
        super.delete(pwSpace);
    }

    private void findRooms(PwSpace pwSpace, List<String> roomIds) {
        PwRoom pwRoom = new PwRoom();
        pwRoom.setPwSpace(pwSpace);
        List<PwRoom> roomList = pwRoomService.findList(pwRoom);
        if (!roomList.isEmpty()) {
            for (PwRoom room : roomList) {
                PwEnterRoom pwEnterRoom = new PwEnterRoom(new PwRoom(room.getId()));
                if (!pwEnterRoomService.findList(pwEnterRoom).isEmpty() ) {
                    logger.warn(String.format("%s/%s/%s有入驻信息，无法删除",
                            room.getPwSpace().getParent().getName(), room.getPwSpace().getName(), room.getName()));
                    throw new SpaceException(String.format("%s/%s/%s有入驻信息，无法删除",
                            room.getPwSpace().getParent().getName(), room.getPwSpace().getName(), room.getName()));
                }
                PwAppointment pa = new PwAppointment();
                pa.setStatus(Const.YES);
                pa.setPwRoom(new PwRoom(room.getId()));
                List<PwAppointment> paList = pwAppointmentService.findList(pa);
                if(!pwAppointmentService.findList(pa).isEmpty()){
                    for(PwAppointment pwA : paList){
                        if(!(pwA.getEndDate().getTime() < System.currentTimeMillis())){
                            logger.warn(String.format("%s/%s/%s有预约信息，无法删除",
                                    room.getPwSpace().getParent().getName(), room.getPwSpace().getName(), room.getName()));
                            throw new SpaceException(String.format("%s/%s/%s有预约信息，无法删除",
                                    room.getPwSpace().getParent().getName(), room.getPwSpace().getName(), room.getName()));
                        }
                    }
                }
                roomIds.add(room.getId());
            }
        }
    }


    private void validatePwSpace(PwSpace pwSpace) {
        List<PwSpace> list = this.findList(pwSpace);
        if (!list.isEmpty()) {
            for (PwSpace space : list) {
                if (space.getName().equals(pwSpace.getName())) {
                    logger.warn("同级下名称不能重复");
                    throw new SpaceException("同级下名称不能重复");
                }
            }
        }

    }

    /**
     * 查询指定pwSpace的所有子
     *
     * @param id
     * @return
     */
    public List<PwSpace> findChildren(String id) {
        if (StringUtils.isBlank(id)) {
            return new ArrayList<>(0);
        }
        PwSpace newPwSpace = get(id);
        if (newPwSpace == null) {
            return new ArrayList<>(0);
        }
        PwSpace p = new PwSpace();
        p.setParentIds(newPwSpace.getParentIds() + newPwSpace.getId());
        return pwSpaceDao.findByParentIdsLike(p);
    }

    /**
     * 场地缩略图服务方法，根据场地ID查询父和子
     *
     * @param id
     * @return
     */
    public Map<String, Object> parentAndChildren(String id) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(id)) {
            return map;
        }
        PwSpace self = this.get(id);
        if (PwSpaceType.FLOOR.getValue().equals(self.getType())) {
            PwDesignerCanvas pwDesignerCanvas = pwDesignerCanvasService.getPwDesignerCanvasByFloorId(self.getId());
            self.setPwDesignerCanvas(pwDesignerCanvas);
        }
        map.put("self", self);
        /**
         * 父，取两级，只取到基地一级
//         */
//        Map<String, Object> parents = new HashMap<>();
//        PwSpace p = self.getParent();
//        if (PwSpaceType.BASE.getValue().equals(p.getType())) {
//            parents.put("base", p);
//        } else if (PwSpaceType.BUILDING.getValue().equals(p.getType())) {
//            parents.put("building", p);
//            PwSpace pp = p.getParent();
//            if (pp != null && PwSpaceType.BASE.getValue().equals(pp.getType())) {
//                parents.put("base", pp);
//            }
//        }
//        map.put("parents", parents);
//        /**
//         * 同级（同一个父）
//         */
//        PwSpace s = new PwSpace();
//        s.setType(self.getType());
//        s.setParent(new PwSpace(p.getId()));
//        List<PwSpace> brothers = super.findList(s);
//        Iterator<PwSpace> iterator = brothers.iterator();
//        while (iterator.hasNext()) {
//            PwSpace brother = iterator.next();
//            if (brother.getId().equals(self.getId())) {
//                iterator.remove();
//                break;
//            }
//
//        }
//        map.put("brothers", brothers);
//        /**
//         * 查询所有的子,只取楼栋和层
//         */
//        List<PwSpace> buildings = new ArrayList();
//        List<PwSpace> floors = new ArrayList();
//        PwSpace space = new PwSpace();
//        space.setParentIds(self.getParentIds() + self.getId());
//        List<PwSpace> list = pwSpaceDao.findByParentIdsLike(space);
//        for (PwSpace child : list) {
//            if (PwSpaceType.BUILDING.getValue().equals(child.getType())) {
//                buildings.add(child);
//            } else if (PwSpaceType.FLOOR.getValue().equals(child.getType())) {
//                PwDesignerCanvas pwDesignerCanvas = pwDesignerCanvasService.getPwDesignerCanvasByFloorId(child.getId());
//                child.setPwDesignerCanvas(pwDesignerCanvas);
//                floors.add(child);
//            } else {
//                continue;
//            }
//        }
//        Map<String, Object> children = new HashMap<>();
//        if (PwSpaceType.BASE.getValue().equals(self.getType())) {
//            children.put("buildings", buildings);
//            children.put("floors", floors);
//        } else if (PwSpaceType.BUILDING.getValue().equals(self.getType())) {
//            children.put("floors", floors);
//        } else {
//            //do nothing
//        }
//        map.put("children", children);
        return map;
    }

    /**
     * 场地缩略图服务方法，查询所有的基地和楼栋
     *
     * @return
     */
    public Map<String, Object> basesAndBuildings() {
        Map<String, Object> map = new HashMap<>();
        PwSpace pwSpace = new PwSpace();
        Page<PwSpace> page = new Page<>();
        page.setOrderBy("a.parent_id");
        page.setOrderByType("ASC");
        pwSpace.setPage(page);
        pwSpace.setType(PwSpaceType.BASE.getValue());
        List<PwSpace> base = this.findList(pwSpace);
        pwSpace.setType(PwSpaceType.BUILDING.getValue());
        List<PwSpace> building = this.findList(pwSpace);
        pwSpace.setType(PwSpaceType.FLOOR.getValue());
        List<PwSpace> floorList = this.findList(pwSpace);
        map.put("bases", base);
        map.put("buildings", building);
        map.put("floorList", floorList);
        return map;
    }

    /**
     * 所有的场地，按类型分类返回
     * @return
     */
    public PwSpaceMap allSpaces() {
        List<PwSpace> list = dao.findAllList();

        PwSpaceMap map = new PwSpaceMap();
        if(StringUtil.checkEmpty(list)){
            return map;
        }

        List<PwSpace> campuses = new ArrayList<>();
        List<PwSpace> bases = new ArrayList<>();
        List<PwSpace> buildings = new ArrayList<>();
        List<PwSpace> floors = new ArrayList<>();
        for (PwSpace pwSpace : list) {
            if (PwSpaceType.CAMPUS.getValue().equals(pwSpace.getType())) {
                campuses.add(pwSpace);
            } else if (PwSpaceType.BASE.getValue().equals(pwSpace.getType())) {
                bases.add(pwSpace);
            } else if (PwSpaceType.BUILDING.getValue().equals(pwSpace.getType())) {
                buildings.add(pwSpace);
            } else if (PwSpaceType.FLOOR.getValue().equals(pwSpace.getType())) {
                floors.add(pwSpace);
            } else {
                continue;
            }
        }
        map.setCampuses(campuses);
        map.setBases(bases);
        map.setBuildings(buildings);
        map.setFloors(floors);
        return map;
    }


    public List<Map<String,String>> findALLPwSpaceAndPwRoom(List<PwSpace> pwSpacelist, List<PwRoom> pwRoomlist) {
        List<Map<String,String>> allList=new ArrayList<Map<String,String>>();
        for(int i=0;i<pwSpacelist.size();i++){
            PwSpace pwSpace=pwSpacelist.get(i);
            Map<String,String> pwSpacemap=new HashMap<String,String>();
            pwSpacemap.put("id",pwSpace.getId());
            pwSpacemap.put("parentId",pwSpace.getParent().getId());
            pwSpacemap.put("name",pwSpace.getName());
            pwSpacemap.put("type",pwSpace.getType());
            allList.add(pwSpacemap);
            for(int j=0;j<pwRoomlist.size();j++){
                PwRoom pwRoom=pwRoomlist.get(j);
                if(pwSpace.getId().equals(pwRoom.getPwSpace().getId())){
                    Map<String,String> roomMap=new HashMap<String,String>();
                    roomMap.put("id",pwRoom.getId());
                    roomMap.put("parentId",pwSpace.getId());
                    roomMap.put("name",pwRoom.getName());
                    allList.add(roomMap);
                }
            }
        }
        return allList;
    }
}