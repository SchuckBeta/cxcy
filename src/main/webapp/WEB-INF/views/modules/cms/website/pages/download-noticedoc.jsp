<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>

<%
	String type = request.getParameter("type");
  	response.setContentType("application/x-download");//设置为下载application/x-download
 	String filedisplay = "";
 	String filedownload = "";
 	if((type).equals("1")){
		//filedisplay = new String("湖南大学入驻工训中心创新创业项目中期检查表.doc".getBytes("UTF-8"), "ISO_8859_1");
	 	//filedownload = "/test/湖南大学入驻工训中心创新创业项目中期检查表.doc";

		filedisplay = new String("湖南大学入驻工训中心创新创业项目中期检查表.doc".getBytes("UTF-8"), "ISO_8859_1");
	 	filedownload = "/test/projectInterimChecklist.doc";
 	}else if((type).equals("2")){
	 	filedisplay = new String("重点项目阶段性研究报告书写格式-见群通知.doc".getBytes("UTF-8"), "ISO_8859_1");
	 	filedownload = "/test/projectPhaseStudyReport.doc";
 	}


  	String filenamedisplay = URLEncoder.encode(filedisplay, "UTF-8");
  	response.addHeader("Content-Disposition","attachment;filename="+filedisplay);
  	try{
  		RequestDispatcher dis = application.getRequestDispatcher(filedownload);
		if(dis!= null){
			dis.forward(request,response);
		}
  		response.flushBuffer();
  	}catch(Exception e){
  		e.printStackTrace();
  	}
%>