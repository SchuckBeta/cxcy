/**
 * .
 */

package com.oseasy.cas.modules.cas.dao.kda;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.cas.common.utils.jdbc.impl.DbCas;
import com.oseasy.cas.modules.cas.entity.SysCasKda;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.jdbc.DbUtil;



/**
 * .
 * @author chenhao
 *
 */
public class DBCasKdaDao {
    private static final DBCasKdaDao dbCkdaDao = new DBCasKdaDao();
    public static final String MAPPER_TABLE = "sys_cas_kda";//实体表名
    public static final String MAPPER_KEY = "id";//主键
    private static final String[][] MAPPER_COLS = new String[][]{
        {"id", "id"},
        {"ruid", "ruid"},
        {"rutype", "rutype"},
        {"rname", "rname"},
        {"rcname", "rcname"},
        {"rjson", "rjson"},
        {"sex", "sex"},
        {"idNumber", "idNumber"},
        {"birthday", "birthday"},
        {"rarray", "rarray"},
        {"time", "time"},
        {"enable", "enable"},
        {"delFlag", "del_flag"}
    };//实体表列映射

    private DBCasKdaDao() {
        super();
    }

    public static DBCasKdaDao init() {
        return dbCkdaDao;
    }

    public static SysCasKda get(String id) {
        return get(DbUtil.getConnection(DbCas.init()), new SysCasKda(id));
    }
    public static SysCasKda get(SysCasKda obj) {
        return get(DbUtil.getConnection(DbCas.init()), obj);
    }
    public static SysCasKda get(Connection conn, String id) {
        return get(conn, new SysCasKda(id));
    }
    public static SysCasKda get(Connection conn, SysCasKda obj) {
        if((obj == null) || StringUtil.isEmpty(obj.getId())){
            return null;
        }
        String sql = "select * from " + MAPPER_TABLE + " where id = "+obj.getId();
        try {
            return (SysCasKda) DbUtil.get(conn, sql, SysCasKda.class, MAPPER_COLS);
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

    public static List<SysCasKda> findAllList() {
        return findList(DbUtil.getConnection(DbCas.init()), false);
    }
    public static List<SysCasKda> findList(boolean openFilter) {
        return findList(DbUtil.getConnection(DbCas.init()), openFilter);
    }
    public static List<SysCasKda> findList(Connection conn, boolean openFilter) {
        String sql = "select * from " + MAPPER_TABLE ;
        if(openFilter){
            sql = sql + " where del_flag = 0 and enable = " + Const.NO;
        }
        try {
            List<Object> objs = DbUtil.findList(conn, sql, SysCasKda.class, MAPPER_COLS);
            List<SysCasKda> list = Lists.newArrayList();
            for (Object obj : objs) {
                list.add((SysCasKda)obj);
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

    public static Integer save(SysCasKda entity) {
        if(entity == null){
            return 0;
        }

        String table = MAPPER_TABLE;
        List<Object> objs = Lists.newArrayList();
        String[][] cols = MAPPER_COLS;
        Connection conn = DbUtil.getConnection(DbCas.init());
        try {
            SysCasKda dbentity = get(conn, entity.getId());
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
    public static Integer delete(SysCasKda obj) {
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
        return delete(new SysCasKda(id));
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
    public static int updatePLPropTime(List<SysCasKda> entitys) {
        String[][] cols =  new String[entitys.size()][3];
        SysCasKda cur = null;
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
//        List<SysCasKda> disenableSysCasKdas = new DBCasAnZhiDao().findList(true);
//        DBCasAnZhiDao.updatePLPropEnable(StringUtil.sqlInByListIdss(disenableSysCasKdas), CoreSval.YES);

        SysCasKda entity = new SysCasKda();
        entity.setId("999");
        entity.setRname("张三");
        DBCasKdaDao.save(entity);
    }
}
