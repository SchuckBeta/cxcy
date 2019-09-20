package com.oseasy.pw.modules.pw.entity;

import java.util.List;

/**
 * Created by victor on 2017/11/28.
 */
public class PwFloorRooms implements java.io.Serializable {
    private String floorId;
    private List<PwFloorDesigner> rooms ;

    public PwFloorRooms buildFloorRooms(String floorid, List<PwFloorDesigner> floorDesigners){
        PwFloorRooms floorRooms = new PwFloorRooms();
        floorRooms.setFloorId(floorid);
        floorRooms.setRooms(floorDesigners);
        return floorRooms;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public List<PwFloorDesigner> getRooms() {
        return rooms;
    }

    public void setRooms(List<PwFloorDesigner> rooms) {
        this.rooms = rooms;
    }
}
