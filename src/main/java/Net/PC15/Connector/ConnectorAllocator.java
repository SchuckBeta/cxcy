//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Net.PC15.Connector;

import Net.PC15.Command.*;
import Net.PC15.Connector.TCPClient.TCPClientAllocator;
import Net.PC15.Connector.TCPClient.TCPClientConnector;
import Net.PC15.Connector.TCPClient.TCPClientDetial;
import Net.PC15.Connector.TCPServer.IPEndPoint;
import Net.PC15.Connector.TCPServer.TCPServerAllocator;
import Net.PC15.Connector.TCPServer.TCPServerClientDetial;
import Net.PC15.Connector.TCPServer.TCPServer_ClientConnector;
import Net.PC15.Connector.UDP.UDPAllocator;
import Net.PC15.Connector.UDP.UDPConnector;
import Net.PC15.Connector.UDP.UDPDetial;
import Net.PC15.Data.INData;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBufAllocator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectorAllocator {
    protected Logger logger = LoggerFactory.getLogger(ConnectorAllocator.class);
    private static ConnectorAllocator staticConnectorAllocator;
    public static final ByteBufAllocator ALLOCATOR;
    protected TCPClientAllocator _TCPClientAllocator = new TCPClientAllocator();
    protected UDPAllocator _UDPAllocator = new UDPAllocator();
    protected TCPServerAllocator _ServerAllocator;
    protected ConcurrentHashMap<String, INConnector> _ConnectorMap = new ConcurrentHashMap();
    protected ExecutorService mainService = Executors.newFixedThreadPool(2);
    protected ExecutorService workService = Executors.newFixedThreadPool(60);
    protected INConnectorEvent _EventHeandler = new ConnectorAllocator.ConnectorAllocatorEventCallback(this);
    protected ArrayList<INConnectorEvent> _EventListener = new ArrayList(10);
    protected ConcurrentLinkedQueue<IOEvent> _EventList = new ConcurrentLinkedQueue();
    protected boolean _IsRelease = false;

    public static synchronized ConnectorAllocator GetAllocator() {
        if (staticConnectorAllocator == null) {
            staticConnectorAllocator = new ConnectorAllocator();
        }

        return staticConnectorAllocator;
    }

    private ConnectorAllocator() {
        this._ServerAllocator = new TCPServerAllocator(this._EventHeandler);
        this.mainService.submit(() -> {
            this.CheckConnectorStatus();
        });
        this.mainService.submit(() -> {
            this.CheckEventCallblack();
        });
    }

    public void AddListener(INConnectorEvent listener) {
        if (!this._IsRelease) {
            ArrayList var2 = this._EventListener;
            synchronized(this._EventListener) {
                if (!this._EventListener.contains(listener)) {
                    this._EventListener.add(listener);
                }

            }
        }
    }

    public void DeleteListener(INConnectorEvent listener) {
        if (!this._IsRelease) {
            ArrayList var2 = this._EventListener;
            synchronized(this._EventListener) {
                if (this._EventListener.contains(listener)) {
                    this._EventListener.remove(listener);
                }

            }
        }
    }

    public void Release() {
        if (!this._IsRelease) {
            this._IsRelease = true;
            this._EventListener.clear();
            this._EventListener = null;

            try {
                this._ConnectorMap.forEach((k, v) -> {
                    v.Release();
                });
                this._ConnectorMap.clear();
                this._ConnectorMap = null;
            } catch (Exception var2) {
                ;
            }

            this._TCPClientAllocator.shutdownGracefully();
            this._TCPClientAllocator = null;
            this._UDPAllocator.shutdownGracefully();
            this._UDPAllocator = null;
            NettyAllocator.shutdownGracefully();
            this.workService.shutdownNow();
            this.mainService.shutdown();
            this.mainService.shutdownNow();
            staticConnectorAllocator = null;
        }
    }


    public void ReleaseConn() {
        if (!this._IsRelease) {
            this._IsRelease = true;
            this._EventListener.clear();
            this._EventListener = null;

            try {
                this._ConnectorMap.forEach((k, v) -> {
                    v.Release();
                });
                this._ConnectorMap.clear();
                this._ConnectorMap = null;
            } catch (Exception var2) {
                ;
            }
        }
    }

    public void ReleaseTCPClient() {
        if (this._IsRelease) {
            this._TCPClientAllocator.shutdownGracefully();
            this._TCPClientAllocator = null;
            this._UDPAllocator.shutdownGracefully();
            this._UDPAllocator = null;
            NettyAllocator.shutdownGracefully();
            this.workService.shutdownNow();
            this.mainService.shutdown();
            this.mainService.shutdownNow();
            staticConnectorAllocator = null;
        }
    }


    public boolean IsRelease() {
        return this._IsRelease;
    }

    protected void AddEvent(ConnectorAllocator.IOEvent event) {
        ConcurrentLinkedQueue var2 = this._EventList;
        synchronized(this._EventList) {
            this._EventList.offer(event);
        }
    }

    public synchronized boolean AddCommand(INCommand cmd) {
        if (this._IsRelease) {
            return false;
        } else if (cmd == null) {
            return false;
        } else {
            INCommandParameter par = cmd.getCommandParameter();
            if (par == null) {
                return false;
            } else {
                CommandDetial detial = par.getCommandDetial();
                if (detial == null) {
                    return false;
                } else {
                    ConnectorDetial connDetial = detial.Connector;
                    if (connDetial == null) {
                        return false;
                    } else {
                        INConnector connector = this.GetConnector(connDetial, true);
                        if (connector == null) {
                            return false;
                        } else {
                            connector.AddCommand(cmd);
                            return true;
                        }
                    }
                }
            }
        }
    }

    protected synchronized INConnector GetConnector(ConnectorDetial detial, boolean bNew) {
        switch(detial.GetConnectorType()) {
            case OnComm:
            case OnFile:
            default:
                return null;
            case OnTCPClient:
                if (!(detial instanceof TCPClientDetial)) {
                    return null;
                }

                return this.SearchTCPClient((TCPClientDetial)detial, bNew);
            case OnTCPServer_Client:
                if (!(detial instanceof TCPServerClientDetial)) {
                    return null;
                }

                return this._ServerAllocator.SearchClient((TCPServerClientDetial)detial);
            case OnUDP:
                return !(detial instanceof UDPDetial) ? null : this.SearchUDPClient((UDPDetial)detial, bNew);
        }
    }

    protected INConnector SearchTCPClient(TCPClientDetial detial, boolean bNew) {
        StringBuilder keybuf = new StringBuilder(100);
        keybuf.append("TCPClient:");
        keybuf.append(detial.IP);
        keybuf.append(":");
        keybuf.append(detial.Port);
        String sKey = keybuf.toString();
        if (this._ConnectorMap.containsKey(sKey)) {
            return (INConnector)this._ConnectorMap.get(sKey);
        } else if (bNew) {
            try {
                TCPClientConnector Connector = new TCPClientConnector(this._TCPClientAllocator, detial);
                Connector.SetEventHandle(this._EventHeandler);
                this.AddConnector(sKey, Connector);
                return (INConnector)this._ConnectorMap.get(sKey);
            } catch (Exception var6) {
                return null;
            }
        } else {
            return null;
        }
    }

    protected INConnector SearchUDPClient(UDPDetial detial, boolean bNew) {
        StringBuilder keybuf = new StringBuilder(100);
        keybuf.append("UDP:");
        keybuf.append(detial.LocalIP);
        keybuf.append(":");
        keybuf.append(detial.LocalPort);
        String sKey = keybuf.toString();
        if (this._ConnectorMap.containsKey(sKey)) {
            return (INConnector)this._ConnectorMap.get(sKey);
        } else if (bNew) {
            try {
                UDPConnector Connector = new UDPConnector(this._UDPAllocator, detial);
                Connector.SetEventHandle(this._EventHeandler);
                this.AddConnector(sKey, Connector);
                return (INConnector)this._ConnectorMap.get(sKey);
            } catch (Exception var6) {
                return null;
            }
        } else {
            return null;
        }
    }

    protected void AddConnector(String key, INConnector conn) {
        synchronized(this) {
            this._ConnectorMap.put(key, conn);
        }
    }

    protected void CheckConnectorStatus() {
        ArrayList arrayList = new ArrayList(this._ConnectorMap.size());

        while(!this._IsRelease) {
            synchronized(this) {
                if (this._ConnectorMap.size() > 0) {
                    this._ConnectorMap.entrySet().forEach((valuex) -> {
                        INConnector connector = (INConnector)valuex.getValue();
                        if (!connector.IsInvalid()) {
                            switch(connector.GetStatus()) {
                                case OnError:
                                    if (!connector.IsForciblyConnect()) {
                                        arrayList.add(valuex.getKey());
                                    }
                                case OnConnectTimeout:
                                case OnClosed:
                                case OnConnected:
                                case OnConnecting:
                                    this.workService.submit(() -> {
                                        connector.run();
                                    });
                            }
                        } else {
                            arrayList.add(valuex.getKey());
                        }

                    });
                    if (arrayList.size() > 0) {
                        Iterator var3 = arrayList.iterator();

                        while(var3.hasNext()) {
                            String value = (String)var3.next();
                            ((INConnector)this._ConnectorMap.get(value)).Release();
                            this._ConnectorMap.remove(value);
                        }

                        arrayList.clear();
                    }
                }

                this._ServerAllocator.CheckClientStatus(this.workService);
            }

            try {
                Thread.sleep(5L);
                if (this._IsRelease) {
                    return;
                }
            } catch (Exception var6) {
                ;
            }
        }

    }

    protected void CheckEventCallblack() {
        while(!this._IsRelease) {
            while(true) {
                ConcurrentLinkedQueue var2 = this._EventList;
                ConnectorAllocator.IOEvent event;
                synchronized(this._EventList) {
                    event = (ConnectorAllocator.IOEvent)this._EventList.poll();
                }

                if (event == null) {
                    try {
                        Thread.sleep(50L);
                        if (this._IsRelease) {
                            return;
                        }
                    } catch (Exception var8) {
                        ;
                    }
                } else {
                    ArrayList var12 = this._EventListener;
                    synchronized(this._EventListener) {
                        int iLen = this._EventListener.size();
                        int i = 0;

                        while(true) {
                            if (i >= iLen) {
                                break;
                            }

                            INConnectorEvent Listener = (INConnectorEvent)this._EventListener.get(i);

                            try {
                                switch(event.EventType) {
                                    case eCommandCompleteEvent:
                                        Listener.CommandCompleteEvent(event.Command, event.Result);
                                        break;
                                    case eCommandProcessEvent:
                                        Listener.CommandProcessEvent(event.Command);
                                        break;
                                    case eCommandTimeout:
                                        Listener.CommandTimeout(event.Command);
                                        break;
                                    case eChecksumErrorEvent:
                                        Listener.ChecksumErrorEvent(event.Command);
                                        break;
                                    case ePasswordErrorEvent:
                                        Listener.PasswordErrorEvent(event.Command);
                                        break;
                                    case eConnectorErrorEvent:
                                        if (event.connectorDetial == null) {
                                            Listener.ConnectorErrorEvent(event.Command, event.isStop.booleanValue());
                                        } else {
                                            Listener.ConnectorErrorEvent(event.connectorDetial);
                                        }
                                        break;
                                    case eWatchEvent:
                                        Listener.WatchEvent(event.connectorDetial, event.EventData);
                                        break;
                                    case eClientOnline:
                                        Listener.ClientOnline((TCPServerClientDetial)event.connectorDetial);
                                        break;
                                    case eClientOffline:
                                        Listener.ClientOffline((TCPServerClientDetial)event.connectorDetial);
                                }
                            } catch (Exception var10) {
                            	if(var10 != null){
                            	    System.out.println("Net.PC15.Connector.ConnectorAllocator.CheckEventCallblack() -- 发送事件时发生错误" + var10);
                            	    logger.error(var10.getMessage(), var10);
                            	}else{
                            	    System.out.println("Net.PC15.Connector.ConnectorAllocator.CheckEventCallblack() -- 发送事件时发生错误-var10 is null");
                            	}
                            }

                            if (this._IsRelease) {
                                return;
                            }

                            ++i;
                        }
                    }

                    if (this._IsRelease) {
                        return;
                    }
                }
            }
        }

    }

    public int GetCommandCount(ConnectorDetial detial) {
        if (this._IsRelease) {
            return 0;
        } else if (detial == null) {
            return 0;
        } else {
            INConnector connector = this.GetConnector(detial, false);
            return connector == null ? 0 : connector.GetCommandCount();
        }
    }

    public boolean IsForciblyConnect(ConnectorDetial detial) {
        if (this._IsRelease) {
            return false;
        } else if (detial == null) {
            return false;
        } else {
            INConnector connector = this.GetConnector(detial, false);
            return connector == null ? false : connector.IsForciblyConnect();
        }
    }

    public void OpenForciblyConnect(ConnectorDetial detial) {
        if (!this._IsRelease) {
            if (detial != null) {
                INConnector connector = this.GetConnector(detial, true);
                if (connector != null) {
                    connector.OpenForciblyConnect();
                }
            }
        }
    }

    public void CloseForciblyConnect(ConnectorDetial detial) {
        if (!this._IsRelease) {
            if (detial != null) {
                INConnector connector = this.GetConnector(detial, false);
                if (connector != null) {
                    connector.CloseForciblyConnect();
                }
            }
        }
    }

    public void StopCommand(ConnectorDetial detial, INIdentity dtl) {
        if (!this._IsRelease) {
            if (detial != null) {
                INConnector connector = this.GetConnector(detial, false);
                if (connector != null) {
                    connector.StopCommand(dtl);
                }
            }
        }
    }

    public boolean Listen(String IP, int Port) {
        return this._IsRelease ? false : this._ServerAllocator.Listen(IP, Port);
    }

    public boolean Listen(int Port) {
        return this._IsRelease ? false : this._ServerAllocator.Listen(Port);
    }

    public void StopListen(String IP, int Port) {
        if (!this._IsRelease) {
            this._ServerAllocator.StopListen(IP, Port);
        }
    }

    public void StopListen(int Port) {
        this.StopListen("0.0.0.0", Port);
    }

    public ArrayList<IPEndPoint> GetTCPServerList() {
        return this._IsRelease ? null : this._ServerAllocator.getServerList();
    }

    public boolean AddWatchDecompile(ConnectorDetial detial, INWatchResponse decompile) {
        INConnector connector = this.GetConnector(detial, false);
        if (connector == null) {
            return false;
        } else {
            connector.AddWatchDecompile(decompile);
            return true;
        }
    }

    public boolean CloseConnector(ConnectorDetial detial) {
        INConnector connector = this.GetConnector(detial, false);
        if (connector == null) {
            return false;
        } else {
            connector.CloseForciblyConnect();
            connector.StopCommand((INIdentity)null);
            if (detial.GetConnectorType() == E_ConnectorType.OnTCPServer_Client) {
                TCPServer_ClientConnector client = (TCPServer_ClientConnector)connector;
                client.Close();
            }

            return true;
        }
    }

    static {
        ALLOCATOR = ByteUtil.ALLOCATOR;
    }

    private class ConnectorAllocatorEventCallback implements INConnectorEvent {
        ConnectorAllocator _Allocator;

        public ConnectorAllocatorEventCallback(ConnectorAllocator allocator) {
            this._Allocator = allocator;
        }

        public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
            ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent();
            event.EventType = ConnectorAllocator.IOEvent.eEventType.eCommandCompleteEvent;
            event.Command = cmd;
            event.Result = result;
            this._Allocator.AddEvent(event);
        }

        public void CommandProcessEvent(INCommand cmd) {
            ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent();
            event.EventType = ConnectorAllocator.IOEvent.eEventType.eCommandProcessEvent;
            event.Command = cmd;
            this._Allocator.AddEvent(event);
        }

        public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
            ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent();
            event.EventType = ConnectorAllocator.IOEvent.eEventType.eConnectorErrorEvent;
            event.Command = cmd;
            event.isStop = isStop;
            this._Allocator.AddEvent(event);
        }

        public void ConnectorErrorEvent(ConnectorDetial detial) {
            ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent();
            event.EventType = ConnectorAllocator.IOEvent.eEventType.eConnectorErrorEvent;
            event.connectorDetial = detial;
            this._Allocator.AddEvent(event);
        }

        public void CommandTimeout(INCommand cmd) {
            ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent();
            event.EventType = ConnectorAllocator.IOEvent.eEventType.eCommandTimeout;
            event.Command = cmd;
            this._Allocator.AddEvent(event);
        }

        public void PasswordErrorEvent(INCommand cmd) {
            ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent();
            event.EventType = ConnectorAllocator.IOEvent.eEventType.ePasswordErrorEvent;
            event.Command = cmd;
            this._Allocator.AddEvent(event);
        }

        public void ChecksumErrorEvent(INCommand cmd) {
            ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent();
            event.EventType = ConnectorAllocator.IOEvent.eEventType.eChecksumErrorEvent;
            event.Command = cmd;
            this._Allocator.AddEvent(event);
        }

        public void WatchEvent(ConnectorDetial detial, INData eventData) {
            ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent();
            event.EventType = ConnectorAllocator.IOEvent.eEventType.eWatchEvent;
            event.connectorDetial = detial;
            event.EventData = eventData;
            this._Allocator.AddEvent(event);
        }

        public void ClientOnline(TCPServerClientDetial client) {
            ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent();
            event.EventType = ConnectorAllocator.IOEvent.eEventType.eClientOnline;
            event.connectorDetial = client;
            this._Allocator.AddEvent(event);
        }

        public void ClientOffline(TCPServerClientDetial client) {
            ConnectorAllocator.IOEvent event = new ConnectorAllocator.IOEvent();
            event.EventType = ConnectorAllocator.IOEvent.eEventType.eClientOffline;
            event.connectorDetial = client;
            this._Allocator.AddEvent(event);
        }
    }

    private static class IOEvent {
        public ConnectorAllocator.IOEvent.eEventType EventType;
        public INCommand Command;
        public INCommandResult Result;
        public ConnectorDetial connectorDetial;
        public Boolean isStop;
        public INData EventData;

        private IOEvent() {
        }

        public static enum eEventType {
            eCommandCompleteEvent,
            eCommandProcessEvent,
            eConnectorErrorEvent,
            eCommandTimeout,
            ePasswordErrorEvent,
            eChecksumErrorEvent,
            eWatchEvent,
            eClientOnline,
            eClientOffline;

            private eEventType() {
            }
        }
    }
}