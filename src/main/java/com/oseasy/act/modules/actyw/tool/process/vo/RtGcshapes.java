/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;

/**
 * .
 * @author chenhao
 *
 */
public class RtGcshapes {
    private ActYwGnode gnode;
    private RtChildShapes cshapes;

    public RtGcshapes() {
        super();
    }
    public RtGcshapes(ActYwGnode gnode, RtChildShapes cshapes) {
        super();
        this.gnode = gnode;
        this.cshapes = cshapes;
    }
    public ActYwGnode getGnode() {
        return gnode;
    }
    public void setGnode(ActYwGnode gnode) {
        this.gnode = gnode;
    }
    public RtChildShapes getCshapes() {
        return cshapes;
    }
    public void setCshapes(RtChildShapes cshapes) {
        this.cshapes = cshapes;
    }
}
