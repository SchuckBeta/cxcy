/**
 * .
 */

package com.oseasy.dr.modules.dr.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * .
 * @author chenhao
 */
public class DrCardRecordParam implements Serializable{
    private Date minPcTime;
    private Date maxPcTime;
    public Date getMinPcTime() {
        return minPcTime;
    }
    public void setMinPcTime(Date minPcTime) {
        this.minPcTime = minPcTime;
    }
    public Date getMaxPcTime() {
        return maxPcTime;
    }
    public void setMaxPcTime(Date maxPcTime) {
        this.maxPcTime = maxPcTime;
    }
}
