/**
 * .
 */

package com.oseasy.dr.modules.dr.entity;

import java.io.Serializable;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * .
 * @author chenhao
 *
 */
public class DrEmentNo extends DataEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final String DR_EMENTNOS = "drEmentNos";
	private String etId;//设备ID
    private String etNo;//设备No
    private String drNo;//门编号

    public DrEmentNo() {
        super();
    }
    public DrEmentNo(String etId, String etNo, String drNo) {
        super();
        this.etId = etId;
        this.etNo = etNo;
        this.drNo = drNo;
    }

    public String getEtNo() {
        return etNo;
    }
    public void setEtNo(String etNo) {
        this.etNo = etNo;
    }
    public String getEtId() {
        return etId;
    }
    public void setEtId(String etId) {
        this.etId = etId;
    }
    public String getDrNo() {
        return drNo;
    }
    public void setDrNo(String drNo) {
        this.drNo = drNo;
    }
}
