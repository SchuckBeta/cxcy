/**
 *
 */

package com.oseasy.com.pcore.modules.sys.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.modules.sys.dao.SysDao;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;


/**
 * 系统常用工具类.
 */
@Service
@Transactional(readOnly = true)
public class SysService {
    /**
     * 时分秒.
     */
    private static final String FMT_2 = "2";
    /**
     * 年月日.
     */
    private static final String FMT_1 = "1";
    /**
     * 年月日 时分秒.
     */
    private static final String FMT_0 = "0";
    /**
     * 系统Dao.
     */
    @Autowired
    private SysDao sysDao;

    /**
     * 根据数据库获取时间.
     * type=0 :获取时间戳 就是日期时间
     * type=1 :获取当前 日期
     * type=2 :获取当前 时间
     *
     * @param type 类型
     * @return Date
     */
    public Date getDbCurDate(final String type) {
        if (StringUtil.isNotEmpty(type)) {
            return sysDao.getDBCurDate(type);
        }
        return null;
    }

    /**
     * 根据数据库获取时间. type=0 :获取时间戳 就是日期时间
     *
     * @return Date
     */
    public Date getDbCurDateYmdHms() {
        return getDbCurDate(FMT_0);
    }

    /**
     * 根据数据库获取时间.
     * type=1 :获取当前 日期
     *
     * @return Date
     */
    public Date getDbCurDateYmd() {
        return getDbCurDate(FMT_1);
    }

    /**
     * 根据数据库获取时间.
     * type=2 :获取当前 时间
     *
     * @return Date
     */
    public Date getDbCurDateHms() {
        return getDbCurDate(FMT_2);
    }

    /**
     * 根据数据库获取时间.
     * @return Long
     */
    public Long getDbCurLong() {
        return sysDao.getDbCurLong();
    }

    /**
     * 根据数据库获取时间 * 10000 + 随机数.
     * @return Long
     */
    public Long getDbCurRlong() {
        return sysDao.getDbCurLong() * 100000 + Math.round(Math.random() * 100000);
    }

    /**
     * 根据系统获取时间.
     * type=0 :获取时间戳 就是日期时间
     * type=1 :获取当前 日期
     * type=2 :获取当前 时间
     * @param type 类型
     * @return Date
     */
    public Date getSysCurDate(final String type) {
        if (StringUtil.isNotEmpty(type)) {
            if((FMT_1).equals(type)){
                return DateUtil.getCurDateYMD000();
            }else if((FMT_0).equals(type)){
                return new Date();
            }else{
                return new Date();
            }
        }
        return new Date();
    }

    /**
     * 根据系统获取时间. type=0 :获取时间戳 就是日期时间
     *
     * @return Date
     */
    public Date getSysCurDateYmdHms() {
        return getDbCurDate(FMT_0);
    }

    /**
     * 根据系统获取时间.
     * type=1 :获取当前 日期
     *
     * @return Date
     */
    public Date getSysCurDateYmd() {
        return getDbCurDate(FMT_1);
    }
}
