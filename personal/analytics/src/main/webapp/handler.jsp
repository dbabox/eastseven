<%@page import="org.dongq.analytics.model.*"%>
<%@page import="org.dongq.analytics.service.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	final String prefix_question     = "question_";
	final String prefix_matrix       = "matrix_";
	final String prefix_matrix_net   = "matrixNet_";
	final String prefix_matrix_plus  = "matrixPlus_";
	final String prefix_property     = "property_";
	Map params = request.getParameterMap();
	Set keys = params.keySet();

	Map answer = new HashMap();
	
	for(Iterator iter = keys.iterator(); iter.hasNext(); ) {
		String key = (String)iter.next();
		String[] value = (String[])params.get(key);
		String text = "";
		for(int index = 0; index < value.length; index++) {
			text += "," + value[index];
		}
		
		if(key.startsWith(prefix_question)) {
			answer.put(key, value[0]);
			//out.print(prefix_question+"="+key + ":" + value[0] + "<br/>");
		}
		
		if(key.startsWith(prefix_matrix)) {
			answer.put(key, text);
			//out.print(prefix_matrix+"="+key + ":" + text + "<br/>");
		}
		
		if(key.startsWith(prefix_matrix_net)) {
			answer.put(key, text);
			//out.print(prefix_matrix_net+"="+key + ":" + text + "<br/>");
		}
		
		if(key.startsWith(prefix_matrix_plus)) {
			answer.put(key, value[0]);
			//out.print(prefix_matrix_plus+"="+key + ":" + value[0] + "<br/>");
		}
		
		if(key.startsWith(prefix_property)) {
			text = text.replace(",", "");
			answer.put(prefix_property+text, text);
			//out.print(prefix_property+"="+key+":"+text+"<br/>");
		}
		
	}
	
	String responderId = request.getParameter("responderId");
	String version = request.getParameter("version");
	Responder responder = new Responder();
	responder.setId(Long.valueOf(responderId));
	responder.setVersion(Long.valueOf(version));
	
	boolean bln = new QuestionnairePaperServiceImpl().saveQuestionnairePaper(responder, answer);
	//TODO 没有做跳转
	if(bln) {
		out.print("<h1>thanks</h1>");
	} else {
		out.print("<h1>ops! exception, oh no!!! please try again</h1>");
	}
%>