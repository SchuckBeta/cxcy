/**
 * .
 */

package com.oseasy.util.common.utils.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.Reflections;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public class DbUtil {
    public final static Logger logger = Logger.getLogger(DbUtil.class);
    /**
     * 获取第一条查询结果.
     * @param sql 查询SQL
     * @param clazz 泛型类
     * @param cols 修改列及映射实体列
     * @return
     * @throws SQLException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object get(DbInterface db, String sql, Class<?> clazz, String[][] cols) throws SQLException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException  {
        return get(getConnection(db), sql, clazz, cols);
    }
    public static Object get(Connection conn, String sql, Class<?> clazz, String[][] cols) throws SQLException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException  {
//        Connection conn = getConnection(db);
        //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
        Statement st= conn.createStatement();
        logger.info(sql);
        ResultSet rs=st.executeQuery(sql);
        //4.处理数据库的返回结果(使用ResultSet类)
        Constructor<?> cons = clazz.getConstructor();
        Object obj = null; // 为构造方法传递参数
        Boolean hasInit = false;
        while(rs.next()){
            if(!hasInit){
                obj = cons.newInstance(); // 为构造方法传递参数
            }
            for (String[] col : cols) {
                obj = initObval(conn, st, rs, obj, col);
            }
            break;
        }

        //关闭资源
        rs.close();
        st.close();
        conn.close();
        return obj;
    }

    /**
     * 获取所有查询结果.
     * @param sql 查询SQL
     * @param clazz 泛型类
     * @param cols 修改列及映射实体列
     * @return
     * @throws SQLException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static List<Object> findList(DbInterface db, String sql, Class<?> clazz, String[][] cols) throws SQLException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
        return findList(getConnection(db), sql, clazz, cols);
    }
    public static List<Object> findList(Connection conn, String sql, Class<?> clazz, String[][] cols) throws SQLException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
        //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
        Statement st= conn.createStatement();
        logger.info(sql);
        ResultSet rs=st.executeQuery(sql);
        //4.处理数据库的返回结果(使用ResultSet类)

        List<Object> res = Lists.newArrayList();
        Constructor<?> cons = clazz.getConstructor();
        while(rs.next()){
            Object obj = cons.newInstance(); // 为构造方法传递参数
            for (String[] col : cols) {
                initObval(conn, st, rs, obj, col);
            }
            res.add(obj);
        }

        //关闭资源
        rs.close();
        st.close();
        conn.close();
        return res;
    }

    /**
     *
     * 批量插入表数据.
     * @param table 表名
     * @param objs  实体列表
     * @param cols 修改列及映射实体列
     * @return
     * @throws SQLException
     */
    public static Integer insert(DbInterface db, String table, List<Object> objs, String[][] cols) throws SQLException  {
        return insert(DbUtil.getConnection(db), table, objs, cols);
    }
    public static Integer insert(Connection conn, String table, List<Object> objs, String[][] cols) throws SQLException  {
        if(StringUtil.checkEmpty(objs) || ((cols == null) || (cols.length <=0 ))){
            return 0;
        }

        //拼接插入SQL
        StringBuffer sql = new StringBuffer("insert into ");
        sql.append(table);
        sql.append("(");
        for (int i = 0; i < cols.length; i++) {
            if(i != 0){
                sql.append(", ");
            }
            sql.append(cols[i][1]);
        }
        sql.append(") ");

        for (int i = 0; i < objs.size(); i++) {
            sql.append("value (");
            for (int j = 0; j < cols.length; j++) {
                if(j == 0){
                    sql.append("?");
                }else{
                    sql.append(", ?");
                }
            }
            sql.append(")");
        }
        logger.info(sql.toString());
        //执行插入参数
        PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql.toString());
        int count = 0;
        int num = 0;
        for (int i = 0; i < objs.size(); i++) {
            Object obj = objs.get(i);
            int curnum = 0;
            for (int j = num; j < num + cols.length; j++) {
                Object robj = Reflections.invokeGetter(obj, cols[j][0]);
                if(robj == null){
                    pstmt.setString(j + 1, "");
                }else{
                    if(robj instanceof Date){
                        pstmt.setDate(j + 1, (Date) robj);
                    }else if(robj instanceof Timestamp){
                        pstmt.setTimestamp(j + 1, (Timestamp) robj);
                    }else if(robj instanceof String){
                        pstmt.setString(j + 1, (String) robj);
                    }else{
                        pstmt.setString(j + 1, null);
                    }
                }
                curnum++;
            }
            num = num + curnum;
            count = pstmt.executeUpdate();
        }

        //关闭资源
        pstmt.close();
        conn.close();
        return count;
    }

    /**
     * 批量修改表数据.
     * @param table 表名
     * @param objs 实体列表
     * @param cols 修改列及映射实体列
     * @param key 更新匹配条件（只支持=）
     * @return
     * @throws SQLException
     */
    public static Integer update(DbInterface db, String table, List<Object> objs, String[][] cols, String[] key) throws SQLException  {
        return update(DbUtil.getConnection(db), table, objs, cols, key);
    }
    public static Integer update(Connection conn, String table, List<Object> objs, String[][] cols, String[] key) throws SQLException  {
        if(StringUtil.checkEmpty(objs) || ((cols == null) || (cols.length <=0 ))|| ((key == null) || (key.length <=0 ))){
            return 0;
        }

        int count = 0;
        for (int i = 0; i < objs.size(); i++) {
            //拼接修改SQL
            StringBuffer sql = new StringBuffer("update ");
            sql.append(table);
            sql.append(" set ");
            for (int j = 0; j < cols.length; j++) {
                sql.append(cols[j][1]);
                sql.append(" = '");
                if(j != (cols.length -1)){
                    sql.append((String)Reflections.invokeGetter(objs.get(i), cols[j][0]));
                    sql.append("', ");
                }else{
                    sql.append((String)Reflections.invokeGetter(objs.get(i), cols[j][0]));
                    sql.append("' ");
                }
            }
            sql.append("where ");
            sql.append(key[1]);
            sql.append(" = '");
            sql.append(key[0]);
            sql.append("'");

            logger.info(sql.toString());
            //执行插入参数
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql.toString());
            count = pstmt.executeUpdate();
            //关闭资源
            pstmt.close();
        }

        //关闭资源
        conn.close();
        return count;
    }

    /**
     * 批量修改表列属性数据.
     * @param table 表名
     * @param ids 实体ID列表
     * @param cols 修改列及映射实体列
     * @param key 更新匹配条件（只支持in）
     * @return
     * @throws SQLException
     */
    public static Integer updatePropByKey(DbInterface db, String table, List<String> ids, String[][] cols, String key) throws SQLException  {
        return updatePropByKey(DbUtil.getConnection(db), table, ids, cols, key);
    }
    public static Integer updatePropByKey(Connection conn, String table, List<String> ids, String[][] cols, String key) throws SQLException  {
        if(((cols == null) || (cols.length <=0 ))|| ((key == null) || (ids.size() <=0 ))){
            return 0;
        }

        int count = 0;
        //拼接修改SQL
        StringBuffer sql = new StringBuffer("update ");
        sql.append(table);
        sql.append(" set ");
        for (int j = 0; j < cols.length; j++) {
            sql.append(cols[j][0]);
            sql.append(" = '");
            if(j != (cols.length -1)){
                sql.append(cols[j][1]);
                sql.append("', ");
            }else{
                sql.append(cols[j][1]);
                sql.append("' ");
            }
        }
        sql.append("where ");
        sql.append(key);
        sql.append(" in (");
        sql.append(StringUtil.sqlInByList(ids));
        sql.append(")");

        logger.info(sql.toString());
        //执行插入参数
        PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql.toString());
        count = pstmt.executeUpdate();
        //关闭资源
        pstmt.close();

        //关闭资源
        conn.close();
        return count;
    }

    /**
     * 批量修改表列属性数据.
     * @param table 表名
     * @param ids 实体ID列表
     * @param cols 修改列及映射实体列
     * @param key 更新匹配条件（只支持in）
     * @return
     * @throws SQLException
     */
    public static Integer updatePropByList(DbInterface db, String table, String[][] cols, String key) throws SQLException  {
        return updatePropByList(DbUtil.getConnection(db), table, cols, key);
    }
    public static Integer updatePropByList(Connection conn, String table, String[][] cols, String key) throws SQLException  {
        if(((cols == null) || (cols.length <=0 ))|| ((key == null))){
            return 0;
        }

        int count = 0;
        PreparedStatement pstmt = null;
        for (int i = 0; i < cols.length; i++) {
          //拼接修改SQL
            StringBuffer sql = new StringBuffer("update ");
            sql.append(table);
            sql.append(" set ");
            sql.append(cols[i][0]);
            sql.append(" = '");
            sql.append(cols[i][1]);
            sql.append("' ");
            sql.append("where ");
            sql.append(key);
            sql.append(" in ('");
            sql.append(cols[i][2]);
            sql.append("')");
            //执行插入参数
            logger.info(sql.toString());
            pstmt = (PreparedStatement) conn.prepareStatement(sql.toString());
            count = pstmt.executeUpdate();
        }

        //关闭资源
        pstmt.close();

        //关闭资源
        conn.close();
        return count;
    }

    /**
     * 批量删除表数据.
     * @param table 表名
     * @param keys 删除匹配条件（只支持=）
     * @param table
     * @param keys
     * @return
     * @throws SQLException
     */
    public static Integer delete(DbInterface db, String table, Object obj, String[][] keys) throws SQLException  {
        return delete(DbUtil.getConnection(db), table, obj, keys);
    }
    public static Integer delete(Connection conn, String table, Object obj, String[][] keys) throws SQLException  {
        if((obj == null) || ((keys == null) || (keys.length <=0 ))){
            return 0;
        }

        int count = 0;
        for (int i = 0; i < keys.length; i++) {
            //拼接修改SQL
            StringBuffer sql = new StringBuffer("delete from ");
            sql.append(table);
            sql.append("where ");
            sql.append(keys[i][1]);
            sql.append(" = ");
            sql.append((String)Reflections.invokeGetter(obj, keys[i][0]));

            logger.info(sql.toString());
            //执行插入参数
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql.toString());
            count = pstmt.executeUpdate();
            //关闭资源
            pstmt.close();
        }

        //关闭资源
        conn.close();
        return count;
    }

    /**
     * 获取数据库连接.
     * @return
     */
    public static Connection getConnection(DbInterface db) {
        return getConnection(db.getDbUrl(), db.getDbUsername(), db.getDbPassword(), db.getDbDriver());
    }

    private static Connection getConnection(String url, String user, String psw, String driver)  {
        Connection conn = null;
        try {
            //1.加载驱动程序
            Class.forName(driver);
            //2.获得数据库链接
            logger.info("当前连接的认证数据库为："+ url);
            conn= DriverManager.getConnection(url, user, psw);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 根据数据库结果集初始化对象属性.
     * @param conn
     * @param st
     * @param rs
     * @param obj
     * @param col
     * @return
     * @throws SQLException
     */
    private static Object initObval(Connection conn, Statement st, ResultSet rs, Object obj, String[] col) throws SQLException {
        Class<?> robj = null;
        try{
            robj = Reflections.getFieldGenericType(obj, col[0]);
        } catch (Exception e1) {
            //关闭资源
            rs.close();
            st.close();
            conn.close();
            throw new IllegalArgumentException("类型转换异常(getFieldGenericType)：col[0]=" + col[0] + "->col[1]=" + col[1]);
        }
        if((String.class).equals(robj)){
            Reflections.invokeSetter(obj, col[0], rs.getString(col[1]));
        }else if((Timestamp.class).equals(robj)){
            Reflections.invokeSetter(obj, col[0], rs.getTimestamp(col[1]));
        }else if((Date.class).equals(robj) || (java.util.Date.class).equals(robj)){
            Reflections.invokeSetter(obj, col[0], rs.getDate(col[1]));
        }else if((Boolean.class).equals(robj)){
            Reflections.invokeSetter(obj, col[0], rs.getBoolean(col[1]));
        }else if((Float.class).equals(robj)){
            Reflections.invokeSetter(obj, col[0], rs.getFloat(col[1]));
        }else if((Double.class).equals(robj)){
            Reflections.invokeSetter(obj, col[0], rs.getDouble(col[1]));
        }else if((Integer.class).equals(robj)){
            Reflections.invokeSetter(obj, col[0], rs.getInt(col[1]));
        }else if((Long.class).equals(robj)){
            Reflections.invokeSetter(obj, col[0], rs.getLong(col[1]));
        }else{
            //关闭资源
            rs.close();
            st.close();
            conn.close();
            throw new IllegalArgumentException("类型转换异常：col[0]=" + col[0] + "->col[1]=" + col[1]);
        }
        return obj;
    }
}
