/**
 * .
 */

package com.oseasy.cas.modules.cas.service;

import java.util.List;

import com.oseasy.cas.modules.cas.entity.SysCasUser;

public interface ISysCasServive<T extends ISysCas> {
    public void casJob(List<SysCasUser> casUsers);
    public String getCasType();
    public List<T> findList(boolean openFilter);
    public int updatePLPropEnable(List<String> ids, String status);
    public int updatePLPropTime(List<T> entitys);
}
