<%@page import="com.test.model1.vo.Bbs"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.test.model1.dao.BbsDAO"%>
<%@ page import="javax.naming.*"%>
<%
	request.setCharacterEncoding("UTF-8");
	BbsDAO bbsDAO = new BbsDAO();
	int bbsId = Integer.parseInt(request.getParameter("bbsId"));
	Bbs bbs = bbsDAO.getBbs(bbsId);
	String writer = bbs.getUserId();
	String userId = (String)session.getAttribute("userId");
	if(!writer.equals(userId)){
		out.println("<script>");
		out.println("alert('글 작성자만 삭제가 가능합니다.')");
		out.println("location.href='bbs.jsp'");
		out.println("</script>");
		return;
	}
	int result = bbsDAO.delete(bbsId);
	if (result != -1) {
		out.println("<script>");
		out.println("location.href='bbs.jsp'");
		out.println("</script>");
	} else {
		out.println("<script>");
		out.println("alert('글삭제 오류입니다. 관리자에게 문의바랍니다.')");
		out.println("history.back()");
		out.println("</script>");
	} 
	bbsDAO.bdClose();
%>