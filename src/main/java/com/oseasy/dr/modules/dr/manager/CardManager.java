package com.oseasy.dr.modules.dr.manager;

import static com.oseasy.dr.modules.dr.vo.DrKey.*;

import java.util.ArrayList;
import java.util.List;

import Net.PC15.FC8800.Command.Door.CloseDoor;
import Net.PC15.FC8800.Command.Door.HoldDoor;
import Net.PC15.FC8800.Command.Door.OpenDoor;
import Net.PC15.FC8800.Command.Door.Parameter.OpenDoor_Parameter;
import Net.PC15.FC8800.Command.Door.Parameter.RemoteDoor_Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardRecord;
import com.oseasy.dr.modules.dr.excetion.MessageExcetion;
import com.oseasy.dr.modules.dr.manager.impl.DrCardCparam;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import Net.PC15.Command.CommandDetial;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Command.INCommand;
import Net.PC15.Command.INCommandParameter;
import Net.PC15.Command.INCommandResult;
import Net.PC15.Connector.ConnectorAllocator;
import Net.PC15.Connector.ConnectorDetial;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Connector.TCPServer.TCPServerClientDetial;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Data.INData;
import Net.PC15.FC8800.Command.Card.DeleteCard;
import Net.PC15.FC8800.Command.Card.ReadCardDetail;
import Net.PC15.FC8800.Command.Card.WriteCardListBySequence;
import Net.PC15.FC8800.Command.Card.WriteCardListBySort;
import Net.PC15.FC8800.Command.Card.Parameter.DeleteCard_Parameter;
import Net.PC15.FC8800.Command.Card.Parameter.ReadCardDetail_Parameter;
import Net.PC15.FC8800.Command.Card.Parameter.WriteCardListBySequence_Parameter;
import Net.PC15.FC8800.Command.Card.Parameter.WriteCardListBySort_Parameter;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDetail_Result;
import Net.PC15.FC8800.Command.Card.Result.WriteCardListBySequence_Result;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.Data.CardTransaction;
import Net.PC15.FC8800.Command.System.ReadVersion;
import Net.PC15.FC8800.Command.System.Result.ReadVersion_Result;
import Net.PC15.FC8800.Command.Transaction.ReadTransactionDatabase;
import Net.PC15.FC8800.Command.Transaction.ReadTransactionDatabaseByIndex;
import Net.PC15.FC8800.Command.Transaction.e_TransactionDatabaseType;
import Net.PC15.FC8800.Command.Transaction.Parameter.ReadTransactionDatabaseByIndex_Parameter;
import Net.PC15.FC8800.Command.Transaction.Parameter.ReadTransactionDatabase_Parameter;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseByIndex_Result;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabase_Result;

public class CardManager<T extends IManager> implements INConnectorEvent {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public static final String UNKOWN_TYPE = "未知类型";
    private static ConnectorAllocator _Allocator = ConnectorAllocator.GetAllocator();
    private static CardManager cardManager;

    public T service;

    public CardManager<T> setService(T service) {
        this.service = service;
        return this;
    }

    public CardManager() {
        //添加监听
        _Allocator.AddListener(this);
    }

    public synchronized static CardManager getInstance() {
        if (cardManager == null) {
            cardManager = new CardManager();
        }
        return cardManager;
    }

    /**
     * 非排序区上传卡片信息.
     *
     * @param card
     * @param commandDetial
     */
    public void uploadCard(DrCard card, CommandDetial commandDetial) {
        uploadCard(card, commandDetial, null);
    }
    public void uploadCard(DrCard card, CommandDetial commandDetial, DrCardRparam rparam) {
       List<DrCard> cards = new ArrayList<DrCard>(1);
        cards.add(card);
        if(rparam != null){
            commandDetial.setDatas(JsonMapper.toJsonString(rparam));
        }
        this.batchUploadCard(cards, commandDetial);
    }

    /**
     * 更新卡状态.
     * @param card
     * @param commandDetial
     */
    public void updateCardState(DrCard card, CommandDetial commandDetial) {
        updateCardState(card, commandDetial, null);
    }

    public void updateCardState(DrCard card, CommandDetial commandDetial, DrCardRparam rparam) {
        deleteCard(Long.valueOf(card.getNo()), commandDetial);
        uploadCard(card, CardFactory.getCommandDetial(rparam.getEtpId()), rparam);
    }




    /***
     * 非排序区批量上传卡片信息
     *
     * @param cards
     * @param commandDetial
     */
    public void batchUploadCard(List<DrCard> cards, CommandDetial commandDetial) {
        if (!cards.isEmpty()) {
            ArrayList<CardDetail> cardDetails = new ArrayList<>(cards.size());
            for (DrCard card : cards) {
                cardDetails.add(DrCard.toCardDetail(card));
            }

            WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(commandDetial, cardDetails);
            WriteCardListBySequence cmd = new WriteCardListBySequence(par);
            _Allocator.AddCommand(cmd);
        }
    }


    /**
     * 排序区上传卡片信息
     *
     * @param commandDetial
     */
    public void uploadSortCard(DrCard card, CommandDetial commandDetial) {
        ArrayList<DrCard> cards = new ArrayList<DrCard>(1);
        cards.add(card);
        this.batchUploadSortCard(cards, commandDetial);
    }

    /**
     * 排序区批量上传卡片信息
     *
     * @param cards
     * @param commandDetial
     */
    public void batchUploadSortCard(ArrayList<DrCard> cards, CommandDetial commandDetial) {
        if (!cards.isEmpty()) {
            ArrayList<CardDetail> cardDetails = new ArrayList<>(cards.size());
            for (DrCard card : cards) {
                cardDetails.add(DrCard.toCardDetail(card));
            }
            WriteCardListBySort_Parameter par = new WriteCardListBySort_Parameter(commandDetial, cardDetails);//初始化卡片参数
            WriteCardListBySort cmd = new WriteCardListBySort(par);//初始化命令对象
            _Allocator.AddCommand(cmd);
        }
    }

    /**
     * 获取卡信息
     *
     * @param card
     * @param commandDetial
     */
    public void getCard(DrCard card, CommandDetial commandDetial) {
        CardDetail cardDetail = DrCard.toCardDetail(card);
        ReadCardDetail_Parameter par = new ReadCardDetail_Parameter(commandDetial, cardDetail.CardData);
        ReadCardDetail cmd = new ReadCardDetail(par);
        _Allocator.AddCommand(cmd);
    }

    /**
    * 获取卡信息
    *
    * @param card
    * @param commandDetial
    */
    public void getCard(DrCard card, CommandDetial commandDetial, DrCardRparam rparam) {
        CardDetail cardDetail = DrCard.toCardDetail(card);
        if(rparam != null){
            commandDetial.setDatas(JsonMapper.toJsonString(rparam));
        }
        ReadCardDetail_Parameter par = new ReadCardDetail_Parameter(commandDetial, cardDetail.CardData);
        ReadCardDetail cmd = new ReadCardDetail(par);
        _Allocator.AddCommand(cmd);
   }


    public Boolean deleteCard(Long cardNo, String equipmentId) {
        try {
            deleteCard(cardNo, CardFactory.getCommandDetial(equipmentId));
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return false;
        }
        return true;
    }

    /**
     * 删除一张指定卡号的卡
     *
     * @param cardNo
     * @param commandDetial
     */
    public void deleteCard(long cardNo, CommandDetial commandDetial) {
        deleteCard(cardNo, commandDetial, null);
    }
    public void deleteCard(long cardNo, CommandDetial commandDetial, DrCardRparam rparam) {
        long[] cardNos = new long[1];
        cardNos[0] = cardNo;
        if(rparam != null){
            commandDetial.setDatas(JsonMapper.toJsonString(rparam));
        }
        this.deleteCards(cardNos, commandDetial);
    }

    /**
     * 批量删除指定卡号的卡
     * 卡片删除后相关的打卡记录并未删除
     *
     * @param cardNos
     * @param commandDetial
     */
    public void deleteCards(long[] cardNos, CommandDetial commandDetial) {
        if (cardNos.length > 0) {
            DeleteCard_Parameter par = new DeleteCard_Parameter(commandDetial, cardNos);
            DeleteCard cmd = new DeleteCard(par);
            _Allocator.AddCommand(cmd);
        }
    }

    /**
     * 从指定索引位置开始读取打卡记录
     *
     * @param index
     * @param size
     * @param type
     * @param commandDetial
     */
//    public void getRecordsByIndex(int index, int size, int type, CommandDetial commandDetial) {
////        if (index < 0 || index > 160000) {
////            throw new MessageExcetion("读记录起始索引号取值范围0-160000");
////        }
//        if (size < 0 || size > 5000) {
//            throw new MessageExcetion("读记录数量取值范围1-5000");
//        }
//        ReadTransactionDatabaseByIndex_Parameter par = new ReadTransactionDatabaseByIndex_Parameter(commandDetial, e_TransactionDatabaseType.valueOf(type));
//        par.ReadIndex = index;
//        par.Quantity = size;
//        ReadTransactionDatabaseByIndex cmd = new ReadTransactionDatabaseByIndex(par);
//        _Allocator.AddCommand(cmd);
//    }

    //新的
    public void getRecordsByIndex(int index, int size, int type, CommandDetial commandDetial) {
        getRecordsByIndex(index, size, type, commandDetial, null);
    }
    public void getRecordsByIndex(int index, int size, int type, CommandDetial commandDetial, DrCardRparam rparam) {
    //  if (index < 0 || index > 160000) {
    //      throw new MessageExcetion("读记录起始索引号取值范围0-160000");
    //  }

        if (size < 0 || size > 5000) {
            throw new MessageExcetion("读记录数量取值范围1-5000");
        }

        if(rparam != null){
            commandDetial.setDatas(JsonMapper.toJsonString(rparam));
        }
        ReadTransactionDatabaseByIndex_Parameter par = new ReadTransactionDatabaseByIndex_Parameter(commandDetial, e_TransactionDatabaseType.valueOf(type));
        par.ReadIndex = index;
        par.Quantity = size;
        ReadTransactionDatabaseByIndex cmd = new ReadTransactionDatabaseByIndex(par);
        _Allocator.AddCommand(cmd);
    }

    /**
     * 读取新的打卡记录
     * 索引由控制器控制
     *
     * @param packetSize
     * @param size
     * @param type          1读卡记录2出门开关记录3
     * @param commandDetial
     */
    public void getNewRecords(int packetSize, int size, int type, CommandDetial commandDetial) {
        getNewRecords(packetSize, size, type, commandDetial);
    }
    public void getNewRecords(int packetSize, int size, int type, CommandDetial commandDetial, DrCardRparam rparam) {
        if (packetSize < 0 || packetSize > 300) {
            throw new MessageExcetion("单次读取数量范围1-300");
        }
        if (size < 0 || size > 160000) {
            throw new MessageExcetion("读新记录数量范围0-160000");
        }
        if(rparam != null){
            commandDetial.setDatas(JsonMapper.toJsonString(rparam));
        }
        ReadTransactionDatabase_Parameter par = new ReadTransactionDatabase_Parameter(commandDetial, e_TransactionDatabaseType.valueOf(type));
        par.PacketSize = packetSize;
        par.Quantity = size;
        ReadTransactionDatabase cmd = new ReadTransactionDatabase(par);
        _Allocator.AddCommand(cmd);

    }

    /***
     **读取版本号
     */
    public void getVersion(CommandDetial dt) {
        getVersion(dt, null);
    }
    public void getVersion(CommandDetial dt, DrCardRparam rparam) {
        if(rparam != null){
            dt.setDatas(JsonMapper.toJsonString(rparam));
        }
        CommandParameter par = new CommandParameter(dt);
        INCommand cmd = new ReadVersion(par);
        _Allocator.AddCommand(cmd);
    }

    /**
     * 远程一键开门，门会自动关闭，此方法默认开启当前设备下4个门
     * @param commandDetial 设备连接参数对象
     */
    public void openDoorAction(CommandDetial commandDetial, DrCardRparam rparam) {
        if(rparam != null){
            commandDetial.setDatas(JsonMapper.toJsonString(rparam));
        }
        OpenDoor_Parameter par = new OpenDoor_Parameter(commandDetial);
        //iValue:1、当前门执行操作;0、当前门不执行操作
        rparam.getDoors().forEach(door -> par.Door.SetDoor(door, 1));
        OpenDoor cmd = new OpenDoor(par);
        _Allocator.AddCommand(cmd);
    }

    /**
     * 远程一键关，此方法默认关闭当前设备下4个门
     * @param commandDetial 设备连接参数对象
     */
    public void closeDoorAction(CommandDetial commandDetial, DrCardRparam rparam) {
        if(rparam != null){
            commandDetial.setDatas(JsonMapper.toJsonString(rparam));
        }
        RemoteDoor_Parameter par = new RemoteDoor_Parameter(commandDetial);
        //iValue:1、当前门执行操作;0、当前门不执行操作
        rparam.getDoors().forEach(door -> par.Door.SetDoor(door, 1));
        CloseDoor cmd = new CloseDoor(par);
        _Allocator.AddCommand(cmd);
    }

    /**
     *远程常开， 门不会自动关闭
     * @param commandDetial
     * @param rparam 传入参数，返回使用
     */
    public void holdDoorOpenAction(CommandDetial commandDetial, DrCardRparam rparam) {
        if(rparam != null){
            commandDetial.setDatas(JsonMapper.toJsonString(rparam));
        }
        RemoteDoor_Parameter par = new RemoteDoor_Parameter(commandDetial);
        //iValue:1、当前门执行操作;0、当前门不执行操作
        rparam.getDoors().forEach(door -> par.Door.SetDoor(door, 1));
        HoldDoor cmd = new HoldDoor(par);
        _Allocator.AddCommand(cmd);
    }

    /**
     * 回调处理命令执行的结果
     *
     * @param cmd
     * @param result
     */
    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        System.out.println("CommandCompleteEvent(cmd, result)===============>" + service.getClass());
        String sKey = cmd.getClass().getName();
        DrCardRparam rparam = initDrCardRparamByCmd(cmd);
        String sn = rparam.getEtpSn();
        logger.info("*** skey : " + sKey);
        if (("Net.PC15.FC8800.Command.Card.ReadCardDetail").equals(sKey)) { //  读卡
            ReadCardDetail_Result cardResult = (ReadCardDetail_Result) result;
            if (cardResult.IsReady) {
                DrCard card = DrCard.fromCardDetail(cardResult.Card);
                service.call(new DrCardCparam(sn, card, rparam, true)); //业务操作
            } else {
                logger.info("卡片未在数据库中存储！");
                throw new MessageExcetion("卡片未在数据库中存储！");
            }
        } else if (("Net.PC15.FC8800.Command.Card.WriteCardListBySequence").equals(sKey) || ("Net.PC15.FC8800.Command.Card.WriteCardListBySort_Result").equals(sKey)) {//写卡
            WriteCardListBySequence_Result writeResult = (WriteCardListBySequence_Result) result;
            String resu = "fail";
            if (writeResult.FailTotal > 0) {
                logger.info("失败数量：" + writeResult.FailTotal);
                logger.info("失败卡号列表：\r\n");
                writeResult.CardList.stream().forEach(e -> logger.info(e.CardData+""));
            } else {
                resu = "ok";
                logger.info("开卡成功,参数:sn=" + sn);
            }

            DrCardCparam cparam = new DrCardCparam(sn, rparam, true);
            cparam.setResult(resu);
            service.call(cparam); //传回写卡结果
        } else if ("Net.PC15.FC8800.Command.Transaction.ReadTransactionDatabaseByIndex".equals(sKey)) {  //从指定索引开始读打卡记录
            ReadTransactionDatabaseByIndex_Result recordResult = (ReadTransactionDatabaseByIndex_Result) result;
            if (recordResult.Quantity > 0) {
                ArrayList<AbstractTransaction> transactionList = recordResult.TransactionList;
                collectMyRecords(mWatchTypeNameList[recordResult.DatabaseType.getValue()], transactionList,sn, rparam);

            }
        } else if ("Net.PC15.FC8800.Command.Transaction.ReadTransactionDatabase".equals(sKey)) {  //读新的打卡记录  初始记录索引号由控制器来维护
            ReadTransactionDatabase_Result recordResult = (ReadTransactionDatabase_Result) result;
            if (recordResult.Quantity > 0) {
                ArrayList<AbstractTransaction> transactionList = recordResult.TransactionList;
                collectMyRecords(mWatchTypeNameList[recordResult.DatabaseType.getValue()], transactionList,sn, rparam);
            }
        } else if ("Net.PC15.FC8800.Command.System.ReadVersion".equals(sKey)) {
            ReadVersion_Result version_result = (ReadVersion_Result) result;
            logger.info("设备版本是：" + version_result.Version);
            service.call(new DrCardCparam(sn, rparam, true)); //传回写卡结果
        } else if ("Net.PC15.FC8800.Command.Card.DeleteCard".equals(sKey)) {
            service.call(new DrCardCparam(sn, rparam, true)); //传回写卡结果
            logger.info("删除成功，删除设备是：" + sn);
        } else if ("Net.PC15.FC8800.Command.Door.OpenDoor".equals(sKey)) {
            service.call(new DrCardCparam(sn, rparam, true)); //传回开门结果
            logger.info("开门成功，开启设备是：" + sn);
        } else if ("Net.PC15.FC8800.Command.Door.CloseDoor".equals(sKey)) {
            service.call(new DrCardCparam(sn, rparam, true)); //传回关门结果
            logger.info("关门成功，关闭设备是：" + sn);
        } else if ("Net.PC15.FC8800.Command.Door.HoldDoor".equals(sKey)) {
            service.call(new DrCardCparam(sn, rparam, true)); //传回门常开结果
            logger.info("远程门常开成功，开启设备是：" + sn);
        } else{
            logger.info("操作响应标识未定义：" + sKey);
        }
    }

    private void closeConnetor(INCommand cmd) {
        INCommandParameter par = cmd.getCommandParameter();
        if (par != null) {
            CommandDetial detial = par.getCommandDetial();
            if (detial != null) {
                ConnectorDetial connDetial = detial.Connector;;
                _Allocator.CloseConnector(connDetial);
            }
        }
    }

    private DrCardRparam getDrCardRparam(DrCardRparam rparam, String sn) {
        if(rparam==null){
            rparam = new DrCardRparam();
            rparam.setEtpSn(sn);
            rparam.setSuccess(true);
        }
        return rparam;
    }

    private void collectMyRecords(String recordType, ArrayList<AbstractTransaction> transactionList,String SN, DrCardRparam rparam) {
        DrCardCparam param = null;
        try {
            List<DrCardRecord> cardRecords = new ArrayList<>();
            for (AbstractTransaction transaction : transactionList) {
                if (!transaction.IsNull()) {
                    if (transaction instanceof CardTransaction) {
                        cardRecords.add(DrCardRecord.fromCardTransaction(SN, recordType, (CardTransaction) transaction));
                    }
                }
            }
            //按时间排倒序
//            Collections.sort(cardRecords);
//            cardRecords.stream().forEach(e -> System.out.println(e.toString()));

            rparam.setEtpSn(SN);
            rparam.setSuccess(true);
            param = new DrCardCparam(SN, cardRecords);
            param.setDatas(rparam);
            service.call(param);
        } catch (Exception e) {
            throw new MessageExcetion("返回结果集不正确！\n"+ ExceptionUtil.getStackTrace(e));
        }
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
//        System.out.println("CommandProcessEvent:::::::");
       /* try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("<html>");
            strBuf.append("当前命令：");
            strBuf.append("<br/>正在处理： ");
            strBuf.append(cmd.getProcessStep());
            strBuf.append(" / ");
            strBuf.append(cmd.getProcessMax());
            strBuf.append("</html>");
            System.out.println(strBuf.toString());
        } catch (Exception e) {
            System.out.println("CommandProcessEvent() -- 发生错误：" + e.toString());
        }
*/
    }


    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        System.out.println("ConnectorErrorEvent(cmd, isStop)===============>" + service.getClass());
        System.out.println("ConnectError***********************************连接出现错误，请检查");
//        String sKey = cmd.getClass().getName();
//        if (("Net.PC15.FC8800.Command.Card.ReadCardDetail").equals(sKey)) { //  读卡
        service.callFail(new DrCardCparam(initDrCardRparamByCmd(cmd), false)); //传回写卡结果
        if (isStop) {
            throw new MessageExcetion("命令已手动停止");
        } else {
            throw new MessageExcetion("连接出现错误，请检查IP、端口");
        }
    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetial detial) {
        System.out.println("ConnectorErrorEvent(detial)===============>" + service.getClass());
        System.out.println("ConnectError***********************************连接出现错误，请检查");
        throw new MessageExcetion("连接失败，请检查");
    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        System.out.println("Timeout*******************************CommandTimeout");
        service.callFail(new DrCardCparam(initDrCardRparamByCmd(cmd), false)); //传回写卡结果
        throw new MessageExcetion("连接超时，请检查");
    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        System.out.println("PasswordError*******************************密码错误");
        service.callFail(new DrCardCparam(initDrCardRparamByCmd(cmd), false)); //传回写卡结果
        throw new MessageExcetion("通讯密码错误");
    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        System.out.println("ChecksumError*******************************命令返回的校验和错误");
        service.callFail(new DrCardCparam(initDrCardRparamByCmd(cmd), false)); //传回写卡结果
        throw new MessageExcetion("命令返回的校验和错误");

    }

    @Override
    public void WatchEvent(ConnectorDetial detial, INData event) {
        System.out.println("Watch*******************************WatchEvent");
//        if (event instanceof FC8800WatchTransaction) {
//            FC8800WatchTransaction WatchTransaction = (FC8800WatchTransaction) event;
//            Card card = new Card();
//            card.setNo(WatchTransaction.SN);
//            AbstractTransaction transaction = WatchTransaction.EventData;
//            if (!transaction.IsNull()) {
//                DrCardRecord  myRecord = new DrCardRecord ();
//                if (transaction instanceof CardTransaction) {
//                    CardTransaction cardTrn = (CardTransaction) transaction;
//                    myRecord.setCardNo(cardTrn.CardData + "");
//                    myRecord.setDoorNo(cardTrn.DoorNum() + "");
//                    myRecord.setEnter(cardTrn.IsEnter());
//                    myRecord.setTime(cardTrn.TransactionDate());
//                    short code = cardTrn.TransactionCode();
//                    if (code < 255) {
//                        String sCode = mCardTransactionList[code];
//                        if (StringUtil.IsNullOrEmpty(sCode)) {
//                            myRecord.setOpenType("未知类型");
//                        }
//                        myRecord.setOpenType(sCode);
//                    } else {
//                        myRecord.setOpenType("未知类型");
//                    }
//                }
//            }
//        } else {
//            System.out.println("未知事件");
//        }

    }

    @Override
    public void ClientOnline(TCPServerClientDetial client) {
        System.out.println("ClientOnline*******************************客户端在线");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ClientOffline(TCPServerClientDetial client) {
        System.out.println("ClientOffline*******************************客户端离线");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ConnectorAllocator get_Allocator() {
        return _Allocator;
    }

    public void set_Allocator(ConnectorAllocator _Allocator) {
        this._Allocator = _Allocator;
    }

    /**
     * 根据响应CMD初始化 DrCardRparam.
     * @param cmd
     * @return DrCardRparam
     */
    private DrCardRparam initDrCardRparamByCmd(INCommand cmd) {
        CommandDetial det = cmd.getCommandParameter().getCommandDetial();
        String sn = det.Identity.GetIdentity();
        DrCardRparam rparam = (DrCardRparam)JsonMapper.fromJsonString(det.getDatas(), DrCardRparam.class);
        if(rparam == null){
            rparam = new DrCardRparam();
        }
        rparam.setEtpSn(sn);
        return rparam;
    }

    public static String[] mCardTransactionList;
    static String[] mWatchTypeNameList = new String[]{"", "读卡信息", "出门开关信息", "门磁信息", "远程开门信息", "报警信息", "系统信息", "连接保活消息", "连接确认信息"};

    static {
        mCardTransactionList = new String[50];
        mCardTransactionList[1] = "合法开门";//
        mCardTransactionList[2] = "密码开门";//------------卡号为密码
        mCardTransactionList[3] = "卡加密码";//
        mCardTransactionList[4] = "手动输入卡加密码";//
        mCardTransactionList[5] = "首卡开门";//
        mCardTransactionList[6] = "门常开";//   ---  常开工作方式中，刷卡进入常开状态
        mCardTransactionList[7] = "多卡开门";//  --  多卡验证组合完毕后触发
        mCardTransactionList[8] = "重复读卡";//
        mCardTransactionList[9] = "有效期过期";//
        mCardTransactionList[10] = "开门时段过期";//
        mCardTransactionList[11] = "节假日无效";//
        mCardTransactionList[12] = "未注册卡";//
        mCardTransactionList[13] = "巡更卡";//  --  不开门
        mCardTransactionList[14] = "探测锁定";//
        mCardTransactionList[15] = "无有效次数";//
        mCardTransactionList[16] = "防潜回";//
        mCardTransactionList[17] = "密码错误";//------------卡号为错误密码
        mCardTransactionList[18] = "密码加卡模式密码错误";//----卡号为卡号。
        mCardTransactionList[19] = "锁定时(读卡)或(读卡加密码)开门";//
        mCardTransactionList[20] = "锁定时(密码开门)";//
        mCardTransactionList[21] = "首卡未开门";//
        mCardTransactionList[22] = "挂失卡";//
        mCardTransactionList[23] = "黑名单卡";//
        mCardTransactionList[24] = "门内上限已满，禁止入门。";//
        mCardTransactionList[25] = "开启防盗布防状态(设置卡)";//
        mCardTransactionList[26] = "撤销防盗布防状态(设置卡)";//
        mCardTransactionList[27] = "开启防盗布防状态(密码)";//
        mCardTransactionList[28] = "撤销防盗布防状态(密码)";//
        mCardTransactionList[29] = "互锁时(读卡)或(读卡加密码)开门";//
        mCardTransactionList[30] = "互锁时(密码开门)";//
        mCardTransactionList[31] = "全卡开门";//
        mCardTransactionList[32] = "多卡开门--等待下张卡";//
        mCardTransactionList[33] = "多卡开门--组合错误";//
        mCardTransactionList[34] = "非首卡时段刷卡无效";//
        mCardTransactionList[35] = "非首卡时段密码无效";//
        mCardTransactionList[36] = "禁止刷卡开门";//  --  【开门认证方式】验证模式中禁用了刷卡开门时
        mCardTransactionList[37] = "禁止密码开门";//  --  【开门认证方式】验证模式中禁用了密码开门时
        mCardTransactionList[38] = "门内已刷卡，等待门外刷卡。";//（门内外刷卡验证）
        mCardTransactionList[39] = "门外已刷卡，等待门内刷卡。";//（门内外刷卡验证）
        mCardTransactionList[40] = "请刷管理卡";//(在开启管理卡功能后提示)(电梯板)
        mCardTransactionList[41] = "请刷普通卡";//(在开启管理卡功能后提示)(电梯板)
        mCardTransactionList[42] = "首卡未读卡时禁止密码开门。";//
        mCardTransactionList[43] = "控制器已过期_刷卡";//
        mCardTransactionList[44] = "控制器已过期_密码";//
        mCardTransactionList[45] = "合法卡开门—有效期即将过期";//
        mCardTransactionList[46] = "拒绝开门--区域反潜回失去主机连接。";//
        mCardTransactionList[47] = "拒绝开门--区域互锁，失去主机连接";//
        mCardTransactionList[48] = "区域防潜回--拒绝开门";//
        mCardTransactionList[49] = "区域互锁--有门未关好，拒绝开门";//
    }
}
