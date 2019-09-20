/**
 * .
 */

package com.oseasy.pro.modules.promodel.vo;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.workflow.impl.SpiltPost;
import com.oseasy.pro.modules.workflow.impl.SpiltPref;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public class GgjBusInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final String BUSINFOS = "businfos";
    private String cname;//匹配列名
    private Integer srow;//开始行
    private String cyname;//企业名称
    private String name;//企业法人
    private String no;//统一社会代码
    private String area;// 注册所在地
    private String money;//高校
    private String regTime;//注册时间

    public String getCyname() {
        return cyname;
    }
    public void setCyname(String cyname) {
        this.cyname = cyname;
    }
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    public Integer getSrow() {
        return srow;
    }
    public void setSrow(Integer srow) {
        this.srow = srow;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public String getRegTime() {
        return regTime;
    }
    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }
    /**
     * 生成工商数据.
     * [公司名称, 注册所在地, 社会代码, 法人代表, 注册资金, 注册时间]
     */
    public static String toBusInfos(List<GgjBusInfo> busInfos) {
        ItOper oper =  new ItOper();
        oper.setReqParam(new ItReqParam());
        oper.getReqParam().setPrefix(SpiltPref.SPREF_LINE.getRemark());
        oper.getReqParam().setSpre(SpiltPref.SPREF_LINE);
        oper.getReqParam().setPostfix(SpiltPost.SPOST_DUN.getRemark());
        oper.getReqParam().setSpost(SpiltPost.SPOST_DUN);

        StringBuffer buffer = new StringBuffer();
        for (GgjBusInfo cur : busInfos) {
            buffer.append(oper.getReqParam().getPostfix());
            buffer.append(cur.getCyname());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getArea());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getNo());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getName());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getMoney());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getRegTime());
        }

        if(StringUtil.isEmpty(buffer.toString())){
            return null;
        }
        return buffer.toString().replaceFirst(oper.getReqParam().getPostfix(), StringUtil.EMPTY);
    }

    public static List<GgjBusInfo> mtoGgjBusInfo(String busInfos) {
        ItOper oper =  new ItOper();
        oper.setReqParam(new ItReqParam());
        oper.getReqParam().setPrefix(SpiltPref.SPREF_LINE.getRemark());
        oper.getReqParam().setSpre(SpiltPref.SPREF_LINE);
        oper.getReqParam().setPostfix(SpiltPost.SPOST_FH.getRemark());
        oper.getReqParam().setSpost(SpiltPost.SPOST_FH);

        List<GgjBusInfo> ggjts = Lists.newArrayList();
        List<String[]> sts = StringUtil.splitByPF(busInfos, oper.getReqParam().getPrefix(), oper.getReqParam().getPostfix());
        for (String[] cur : sts) {
            if((cur == null) || (cur.length != 6)){
                continue;
            }
            GgjBusInfo curst = new GgjBusInfo();
            curst.setCyname(cur[0]);
            curst.setArea(cur[1]);
            curst.setNo(cur[2]);
            curst.setName(cur[3]);
            curst.setMoney(cur[4]);
            curst.setRegTime(cur[5]);
            ggjts.add(curst);
        }
        return ggjts;
    }

    public static void main(String[] args) {
        String cur = checkEncode("中国");
        System.out.println("cur = "+cur);
        String cur1 = checkEncode("张三");
        System.out.println("cur = "+cur1);
    }

    public static String checkEncode(String str) {
        String cur = null;
        String[] encodes = new String[]{"iso8859-1", "gbk", "utf-8"};
        try {
            String curestr = null;
            for (int i=0; i < encodes.length; i++) {
                cur = encodes[i];
                curestr = new String(str.toString().getBytes(cur));
                if((curestr).equals(str.toString())){
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cur;
    }
}
