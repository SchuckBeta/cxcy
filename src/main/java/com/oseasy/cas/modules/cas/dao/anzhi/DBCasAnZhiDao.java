/**
 * .
 */

package com.oseasy.cas.modules.cas.dao.anzhi;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.cas.common.utils.jdbc.impl.DbCas;
import com.oseasy.cas.modules.cas.entity.SysCasAnZhi;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.jdbc.DbUtil;



/**
 * .
 * @author chenhao
 *
 */
public class DBCasAnZhiDao {
    private static final DBCasAnZhiDao dbCanZhiDao = new DBCasAnZhiDao();
    public static final String MAPPER_TABLE = "sys_cas_anzhi";//实体表名
    public static final String MAPPER_KEY = "id";//主键
    private static final String[][] MAPPER_COLS = new String[][]{
        {"id", "id"},
        {"ruid", "ruid"},
        {"rutype", "rutype"},
        {"rname", "rname"},
        {"rcname", "rcname"},
        {"rou", "rou"},
        {"time", "time"},
        {"rcontainerId", "rcontainerId"},
        {"rjson", "rjson"},
        {"enable", "enable"},
        {"delFlag", "del_flag"}
    };//实体表列映射

    private DBCasAnZhiDao() {
        super();
    }

    public static DBCasAnZhiDao init() {
        return dbCanZhiDao;
    }

    public static SysCasAnZhi get(String id) {
        return get(DbUtil.getConnection(DbCas.init()), new SysCasAnZhi(id));
    }
    public static SysCasAnZhi get(SysCasAnZhi obj) {
        return get(DbUtil.getConnection(DbCas.init()), obj);
    }
    public static SysCasAnZhi get(Connection conn, String id) {
        return get(conn, new SysCasAnZhi(id));
    }
    public static SysCasAnZhi get(Connection conn, SysCasAnZhi obj) {
        if((obj == null) || StringUtil.isEmpty(obj.getId())){
            return null;
        }
        String sql = "select * from " + MAPPER_TABLE + " where id = "+obj.getId();
        try {
            return (SysCasAnZhi) DbUtil.get(conn, sql, SysCasAnZhi.class, MAPPER_COLS);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<SysCasAnZhi> findAllList() {
        return findList(DbUtil.getConnection(DbCas.init()), false);
    }
    public static List<SysCasAnZhi> findList(boolean openFilter) {
        return findList(DbUtil.getConnection(DbCas.init()), openFilter);
    }
    public static List<SysCasAnZhi> findList(Connection conn, boolean openFilter) {
        String sql = "select * from " + MAPPER_TABLE ;
        if(openFilter){
            sql = sql + " where del_flag = 0 and enable = " + Const.NO;
        }
        try {
            List<Object> objs = DbUtil.findList(conn, sql, SysCasAnZhi.class, MAPPER_COLS);
            List<SysCasAnZhi> list = Lists.newArrayList();
            for (Object obj : objs) {
                list.add((SysCasAnZhi)obj);
            }
            return list;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer save(SysCasAnZhi entity) {
        if(entity == null){
            return 0;
        }

        String table = MAPPER_TABLE;
        List<Object> objs = Lists.newArrayList();
        String[][] cols = MAPPER_COLS;
        Connection conn = DbUtil.getConnection(DbCas.init());
        try {
            SysCasAnZhi dbentity = get(conn, entity.getId());
            if(dbentity == null){
                if(StringUtil.isEmpty(entity.getId())){
                    entity.setId(DbCas.init().getId());
                }
                entity.setCreateDate(new Date());
                objs.add(entity);
                return DbUtil.insert(conn, table, objs, cols);
            }else{
                if(StringUtil.isEmpty(entity.getId())){
                    entity.setId(DbCas.init().getId());
                }
                entity.setCreateDate(new Date());
                objs.add(entity);
                String[] key = new String[]{entity.getId(), MAPPER_KEY};//更新条件
                return DbUtil.update(conn, table, objs, cols, key);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer insertPl(List<Object> objs) {
        if(StringUtil.checkEmpty(objs)){
            return 0;
        }

        String table = MAPPER_TABLE;
        String[][] cols = MAPPER_COLS;
        try {
            return DbUtil.insert(DbCas.init(), table, objs, cols);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除.
     * @param entity
     * @return Integer
     */
    public static Integer delete(SysCasAnZhi obj) {
        String table = MAPPER_TABLE;
        String[][] keys = MAPPER_COLS;
        try {
            return DbUtil.delete(DbCas.init(), table, obj, keys);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static Integer delete(String id) {
        return delete(new SysCasAnZhi(id));
    }

    /**
     * 批量修改Enable属性.
     * @param ids
     * @param cols
     * @param key
     * @return
     */
    public static int updatePLPropEnable(List<String> ids, String status) {
        return updatePLPropByKey(ids, new String[][]{
            {"enable", status}
        }, MAPPER_KEY);
    }
    public static int updatePLPropByKey(List<String> ids, String[][] cols, String key) {
        String table = MAPPER_TABLE;
        try {
            return DbUtil.updatePropByKey(DbUtil.getConnection(DbCas.init()), table, ids, cols, key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 批量修改Time属性.
     * @param ids
     * @param cols
     * @param key
     * @return
     */
    public static int updatePLPropTime(List<SysCasAnZhi> entitys) {
        String[][] cols =  new String[entitys.size()][3];
        SysCasAnZhi cur = null;
        for (int i = 0; i < cols.length; i++) {
            cur = entitys.get(i);
            cols[i][0] = "time";
            cols[i][1] = cur.getTime()+"";
            cols[i][2] = cur.getId();

        }
        return updatePLPropByList(cols, MAPPER_KEY);
    }
    public static int updatePLPropByList(String[][] cols, String key) {
        String table = MAPPER_TABLE;
        try {
            return DbUtil.updatePropByList(DbUtil.getConnection(DbCas.init()), table, cols, key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static void main(String[] args) {
//        System.out.println(new DBCasAnZhiDao().get("1"));
//        List<SysCasAnZhi> disenableSysCasAnZhis = new DBCasAnZhiDao().findList(true);
//        DBCasAnZhiDao.updatePLPropEnable(StringUtil.sqlInByListIdss(disenableSysCasAnZhis), CoreSval.YES);

        SysCasAnZhi entity = new SysCasAnZhi();
        entity.setId("999");
        entity.setRname("张三");
        DBCasAnZhiDao.save(entity);
    }
}
