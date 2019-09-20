/**
 * .
 */

package com.oseasy.com.pcore.modules.authorize.vo;

import java.io.Serializable;

/**
 * 机器信息.
 * @author chenhao
 *
 */
public class Minfo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String mac;
    private String cpu;
    public Minfo() {
        super();
    }
    public Minfo(String mac, String cpu) {
        super();
        this.mac = mac;
        this.cpu = cpu;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getCpu() {
        return cpu;
    }
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }
}
