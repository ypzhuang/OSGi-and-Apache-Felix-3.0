<%@ include file="init-no-check.inc.jsp"%>
<%
	if (!sessionBean.sessionIsValid()) {
		response.sendRedirect("login.jsp");
	}
%>