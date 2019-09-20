/**
 *
 */
package com.oseasy.com.mqserver.modules.oa.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.util.common.utils.Collections3;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 通知通告Entity

 * @version 2014-05-16
 */
public class OaNotify extends DataEntity<OaNotify> {
	public enum Type_Enum {
		TYPE1("1", "普通消息"),
		TYPE3("3", "通知公告"),
		TYPE4("4", "双创动态"),
		TYPE8("8", "双创通知"),
		TYPE9("9", "省市动态"),
		TYPE5("5", "申请加入"),
		TYPE6("6", "邀请加入"),
		TYPE7("7", "团队发布"),
		TYPE13("13", "删除成员"),
		TYPE11("11", "拒绝加入"),
		TYPE10("10", "同意加入"),
		TYPE14("14", "评审消息"),
		TYPE15("15", "预约消息"),
		TYPE61("61", "入驻申请资料消息"),
		TYPE62("62", "入驻申请警告消息"),
		TYPE16("16", "证书下发"),
		TYPE17("17", "门禁消息"),
		TYPE18("18", "项目申报"),
		TYPE19("19", "项目提交材料"),
		TYPE20("20", "任务指派"),
		TYPE21("21", "学分申请"),
		TYPE22("22", "评论管理"),
		TYPE23("23", "留言管理"),
		TYPE24("24", "团队审核"),
		TYPE25("25", "入驻申请"),
		TYPE26("26", "变更申请"),
		TYPE27("27", "续期申请"),
		TYPE28("28", "退孵申请"),
		TYPE29("29", "管理员审核"),
		TYPE30("30", "管理员分配房间"),
		TYPE31("31", "通知专家"),
		TYPE32("32", "学分审核");


		private String value;
		private String name;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private Type_Enum(String value, String name) {
			this.value=value;
			this.name=name;
		}

	  public static Type_Enum getByValue(String value) {
	    Type_Enum[] entitys = Type_Enum.values();
	    for (Type_Enum entity : entitys) {
	      if ((value).equals(entity.getValue())) {
	        return entity;
	      }
	    }
	    return null;
	  }
		public static List<Type_Enum> getAll() {
			return getAll(true);
		}
		public static List<Type_Enum> getAll(Boolean enable) {
			if(enable == null){
				enable = true;
			}
			List<Type_Enum> enty = Lists.newArrayList();
			Type_Enum[] entitys = Type_Enum.values();
			for (Type_Enum entity : entitys) {
				if (entity.getValue() != null) {
					enty.add(entity);
				}
			}
			return enty;
		}
		@Override
		public String toString() {
			return "{\"value\":\"" + this.value + "\",\"label\":\"" + this.name + "\"}";
		}
	}

	private static final long serialVersionUID = 1L;
	private String otype;		// 功能类型
	private String type;		// 类型
	private String title;		// 标题
	private String content;		// 类型
	private String files;		// 附件
	private String status;		// 状态 0草稿 1发布

	private String readNum;		// 已读
	private String unReadNum;	// 未读
	private String isReCall;	// 是否撤回
	private boolean isSelf;		// 是否只查询自己的通知

	private String readFlag;	// 本人阅读状态
	private String operateFlag;	// 本人操作状态

	private Date effectiveDate;//生效日期
	private Date endDate;//结束日期

	private String sendType;//发送类型 1：广播  2：定向

	private List<OaNotifyRecord> oaNotifyRecordList = Lists.newArrayList();//定向发送时保存接收人集合



	private List<OaNotifyRecord> oaNotifyRecordListBroadcast = Lists.newArrayList();//广播发送时保存接收人集合

	private String oaNotifyRecordIdsBroadCast;//广播时的所以接收人ids

	private String sId;//大赛id或项目id 团队id

	private String protype;

	private String userId;


	private User createUser;

	private User receiveUser;

	private String publishDate;

	private String source;//来源

	private String views;//浏览量
	private List<String> keywords;

	private String schOorPro; //所在门户：学校1，省2

	public String getSchOorPro() {
		return schOorPro;
	}

	public void setSchOorPro(String schOorPro) {
		this.schOorPro = schOorPro;
	}

	public String getIsReCall() {
		return isReCall;
	}

	public void setIsReCall(String isReCall) {
		this.isReCall = isReCall;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProtype() {
		return protype;
	}

	public void setProtype(String protype) {
		this.protype = protype;
	}

	public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public OaNotify() {
		super();
	}

	public OaNotify(String id) {
		super(id);
	}

	@Length(min=0, max=200, message="标题长度必须介于 0 和 200 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(min=0, max=2, message="类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOperateFlag() {
		return operateFlag;
	}

	public void setOperateFlag(String operateFlag) {
		this.operateFlag = operateFlag;
	}

	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Length(min=0, max=2000, message="附件长度必须介于 0 和 2000 之间")
	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReadNum() {
		return readNum;
	}

	public void setReadNum(String readNum) {
		this.readNum = readNum;
	}

	public String getUnReadNum() {
		return unReadNum;
	}

	public void setUnReadNum(String unReadNum) {
		this.unReadNum = unReadNum;
	}

	public List<OaNotifyRecord> getOaNotifyRecordList() {
		return oaNotifyRecordList;
	}

	public void setOaNotifyRecordList(List<OaNotifyRecord> oaNotifyRecordList) {
		this.oaNotifyRecordList = oaNotifyRecordList;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public User getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(User receiveUser) {
		this.receiveUser = receiveUser;
	}

	/**
	 * 获取通知发送记录用户ID
	 * @return
	 */
	public String getOaNotifyRecordIds() {
		return Collections3.extractToString(oaNotifyRecordList, "user.id", ",") ;
	}

	/**
	 * 设置通知发送记录用户ID
	 * @return
	 */
	public void setOaNotifyRecordIds(String oaNotifyRecord) {
		this.oaNotifyRecordList = Lists.newArrayList();
		for (String id : StringUtil.split(oaNotifyRecord, ",")) {
			OaNotifyRecord entity = new OaNotifyRecord();
			entity.setId(IdGen.uuid());
			entity.setOaNotify(this);
			entity.setUser(new User(id));
			entity.setReadFlag("0");
			this.oaNotifyRecordList.add(entity);
		}
	}




	public String getOaNotifyRecordIdsBroadCast() {
		return Collections3.extractToString(oaNotifyRecordListBroadcast, "user.id", ",");
	}


	public void setOaNotifyRecordIdsBroadCast(String oaNotifyRecordIdsBroadCast) {
		this.oaNotifyRecordListBroadcast  = Lists.newArrayList();
		for (String id : StringUtil.split(oaNotifyRecordIdsBroadCast, ",")) {
			OaNotifyRecord entity = new OaNotifyRecord();
			entity.setId(IdGen.uuid());
			entity.setOaNotify(this);
			entity.setUser(new User(id));
			entity.setReadFlag("0");
			this.oaNotifyRecordListBroadcast.add(entity);
		}
	}

	public List<OaNotifyRecord> getOaNotifyRecordListBroadcast() {
		return oaNotifyRecordListBroadcast;
	}

	public void setOaNotifyRecordListBroadcast(List<OaNotifyRecord> oaNotifyRecordListBroadcast) {
		this.oaNotifyRecordListBroadcast = oaNotifyRecordListBroadcast;
	}

	/**
	 * 获取通知发送记录用户Name
	 * @return
	 */
	public String getOaNotifyRecordNames() {
		return Collections3.extractToString(oaNotifyRecordList, "user.name", ",") ;
	}



	/**
	 * 设置通知发送记录用户Name
	 * @return
	 */
	public void setOaNotifyRecordNames(String oaNotifyRecord) {
		// 什么也不做
	}

	public boolean getIsSelf() {
		return isSelf;
	}

	public void setIsSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}