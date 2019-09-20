/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.impl;

import java.util.List;
import java.util.Map;

import com.oseasy.pie.modules.impdata.entity.ImpInfo;

/**
 * .
 * @author chenhao
 *
 */
public class IeDmap {
    private ImpInfo info;
    private List<Map<String,String>> einfos;
    private List<Map<String,String>> edinfos;

    public IeDmap() {
        super();
    }
    public IeDmap(ImpInfo info) {
        super();
        this.info = info;
    }
    public IeDmap(ImpInfo info, List<Map<String,String>> einfos, List<Map<String,String>> edinfos) {
        super();
        this.info = info;
        this.einfos = einfos;
        this.edinfos = edinfos;
    }
    public ImpInfo getInfo() {
        return info;
    }
    public void setInfo(ImpInfo info) {
        this.info = info;
    }
    public List<Map<String,String>> getEinfos() {
        return einfos;
    }
    public void setEinfos(List<Map<String,String>> einfos) {
        this.einfos = einfos;
    }
    public List<Map<String,String>> getEdinfos() {
        return edinfos;
    }
    public void setEdinfos(List<Map<String,String>> edinfos) {
        this.edinfos = edinfos;
    }
}
