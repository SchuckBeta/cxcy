/**
 * 场地Map.
 */

package com.oseasy.pw.modules.pw.vo;

import java.io.Serializable;
import java.util.List;

import com.oseasy.pw.modules.pw.entity.PwSpace;

/**
 * 场地Map.
 * @author chenhao
 *
 */
public class PwSpaceMap implements Serializable{
    private static final long serialVersionUID = 1L;

    private List<PwSpace> campuses;
    private List<PwSpace> bases;
    private List<PwSpace> buildings;
    private List<PwSpace> floors;
    public List<PwSpace> getCampuses() {
        return campuses;
    }
    public void setCampuses(List<PwSpace> campuses) {
        this.campuses = campuses;
    }
    public List<PwSpace> getBases() {
        return bases;
    }
    public void setBases(List<PwSpace> bases) {
        this.bases = bases;
    }
    public List<PwSpace> getBuildings() {
        return buildings;
    }
    public void setBuildings(List<PwSpace> buildings) {
        this.buildings = buildings;
    }
    public List<PwSpace> getFloors() {
        return floors;
    }
    public void setFloors(List<PwSpace> floors) {
        this.floors = floors;
    }
}
