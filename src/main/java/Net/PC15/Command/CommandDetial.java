//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Net.PC15.Command;

import java.util.Calendar;

import Net.PC15.Connector.ConnectorDetial;
import Net.PC15.Connector.INConnectorEvent;

public class CommandDetial {
    public ConnectorDetial Connector;
    public INIdentity Identity;
    public String Desc;
    public int ID;
    public String Name;
    public Object UserPar;
    public Calendar EndTime;
    public Calendar BeginTime;
    public INConnectorEvent Event;
    public int Timeout = 500;
    public int RestartCount = 3;

    public String datas;

    public CommandDetial() {
        this.init();
    }

    private void init() {
        this.Identity = null;
        this.Connector = null;
        this.Event = null;
        this.EndTime = null;
        this.BeginTime = null;
        this.UserPar = null;
        this.Desc = null;
        this.Name = null;
        this.datas = null;
    }

    public ConnectorDetial getConnector() {
        return Connector;
    }

    public void setConnector(ConnectorDetial connector) {
        Connector = connector;
    }

    public INIdentity getIdentity() {
        return Identity;
    }

    public void setIdentity(INIdentity identity) {
        Identity = identity;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Object getUserPar() {
        return UserPar;
    }

    public void setUserPar(Object userPar) {
        UserPar = userPar;
    }

    public Calendar getEndTime() {
        return EndTime;
    }

    public void setEndTime(Calendar endTime) {
        EndTime = endTime;
    }

    public Calendar getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(Calendar beginTime) {
        BeginTime = beginTime;
    }

    public INConnectorEvent getEvent() {
        return Event;
    }

    public void setEvent(INConnectorEvent event) {
        Event = event;
    }

    public int getTimeout() {
        return Timeout;
    }

    public void setTimeout(int timeout) {
        Timeout = timeout;
    }

    public int getRestartCount() {
        return RestartCount;
    }

    public void setRestartCount(int restartCount) {
        RestartCount = restartCount;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public void release() {
        this.init();
    }
}
