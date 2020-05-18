<%@page import="com.test.model1.vo.Bbs"%>
<%@page import="com.test.model1.dao.BbsDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	request.setCharacterEncoding("UTF-8");
	int bbsId = 0;
	BbsDAO bbsDAO = null;
	Bbs bbs = null;
	if(request.getParameter("bbsId") == null){
		out.println("<script>alert('작성된 글이 없습니다.');location.href='bbs.jsp';</script>"); //history.back();
	} else {
		bbsId = Integer.parseInt(request.getParameter("bbsId"));
		bbsDAO = new BbsDAO();
		bbs = bbsDAO.getBbs(bbsId);
		if(bbs.getBbsId() == 0) out.println("<script>alert('글번호가 없습니다.');location.href='bbs.jsp';</script>");
	} 
%>
<!DOCTYPE HTML>
<html>
<head>
<!-- Required meta tags -->
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" />

<title>글보기 페이지</title>
</head>
<body>
	<nav class="navbar navbar-expand-sm navbar-light bg-light">
		<a class="navbar-brand" href="main.jsp">JSP 게시판</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav">
				<li class="nav-item"><a class="nav-link" href="main.jsp">메인</a>
				</li>
				<li class="nav-item"><a class="nav-link active" href="bbs.jsp">게시판<span
						class="sr-only">(current)</span></a></li>
			</ul>
			<ul class="navbar-nav ml-auto">
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false">접속하기</a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
						<%
							if (session.getAttribute("userId") == null) {
						%>
						<a class="dropdown-item" href="login.jsp">로그인</a>
						<%
							} else {
						%>
						<a class="dropdown-item" href="logoutAction.jsp">로그아웃</a>
						<%
							}
						%>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="join.jsp">회원가입</a>
					</div></li>
			</ul>
		</div>
	</nav>
	<div class="container pt-3">
		<table class="table table-striped text-center table-bordered">
			<thead class="thead-light">
				<tr>
					<th colspan="2">게시판 글보기</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td style="width:100px;">제목</td>
					<td class="text-left"><%=bbs != null ? bbs.getBbsTitle() : "" %></td>
				</tr>
				<tr>
					<td class="align-middle">본문</td>
					<td style="height: 350px; overflow:auto;" class="text-left"><%=bbs != null ? bbs.getBbsContent() : "" %></td>
				</tr>
			</tbody>
		</table>
		<a href="bbs.jsp" class="btn btn-primary float-left">목록</a>
		<%
			String writer = bbs.getUserId();
			String userId = (String)session.getAttribute("userId");
			if(writer.equals(userId)){
		%>		
		<%  } %>
		<a href="delAction.jsp?bbsId=<%=bbs.getBbsId() %>" class="btn btn-danger float-right" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
		<a href="modify.jsp?bbsId=<%=bbs.getBbsId() %>" class="btn btn-warning float-right mr-1">수정</a>
	</div>
	<%
		if(bbsDAO != null){
			bbsDAO.bdClose();
		}
	%>
	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
</body>
</html>