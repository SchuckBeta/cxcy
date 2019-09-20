/**
 * .
 */

package com.oseasy.cas.modules.cas.service;

import com.oseasy.util.common.utils.IidEntity;

public interface ISysCas extends IidEntity{
    public Integer getTime();
    public void setTime(Integer time);
    public String getRuid();
}
