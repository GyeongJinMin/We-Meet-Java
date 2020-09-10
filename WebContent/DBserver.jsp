<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="scheduleApp.scheduleAppServer"%>
<%@ page import="scheduleApp.Schedule"%>
<%@ page import="java.util.ArrayList"%>


<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("id");
	String pwd = request.getParameter("pwd");
	String name = request.getParameter("name");
	String type = request.getParameter("type");
	String date = request.getParameter("date");
	String schedule = request.getParameter("schedule");
	String memo = request.getParameter("memo");
	String old = request.getParameter("old");
	String friendName = request.getParameter("friendName");
	String sche_name = request.getParameter("sche_name");

	//싱글톤 방식으로 자바 클래스를 불러옵니다.
	scheduleAppServer connectDB = scheduleAppServer.getInstance();

	if (type.equals("login")) {
		String returns = connectDB.logindb(id, pwd);
		out.print(returns);
	} else if (type.equals("join")) {
		String returns = connectDB.joindb(id, pwd, name);
		out.print(returns);
	} else if (type.equals("calendar_main")) {
		String returns = connectDB.calendardb(id, date, schedule, memo);
		out.print(returns);
	} else if (type.equals("calendar_add")) {
		String returns = connectDB.addCalendar(id, date, schedule, memo);
		out.print(returns);
	} else if (type.equals("calendar_edit")) {
		String returns = connectDB.editCalendar(id, date, schedule, memo, old);
		out.print(returns);
	} else if (type.equals("loadSche")) {
		String returns = connectDB.loadSchedule();
		out.print(returns);
	} else if (type.equals("addSche")) {
	      String returns = connectDB.addSchedule(id, sche_name, "", "", "");
	      out.print(returns);
	} else if (type.equals("loadUser")) {
		String returns = connectDB.loadUser(id);
		out.print(returns);
	} else if (type.equals("loadFriends")) {
		String returns = connectDB.loadFriends(id);
		out.print(returns);
	} else if (type.equals("loadOthers")) {
		String returns = connectDB.loadOthers(id);
		out.print(returns);
	} else if (type.equals("loadWaiters")) {
		String returns = connectDB.loadWaiters(id);
		out.print(returns);
	} else if (type.equals("loadAllUsers")) {
		String returns = connectDB.loadAllUsers();
		out.print(returns);
	} else if (type.equals("addFriend")) {
		String returns = connectDB.addFriend(id, friendName);
		out.print(returns);
	} else if (type.equals("friendRequest")) {
		String returns = connectDB.friendRequest(id, friendName);
		out.print(returns);
	}
	
%>