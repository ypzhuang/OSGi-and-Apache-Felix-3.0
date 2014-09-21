<%@ include file="init.inc.jsp" %>

<%  sessionBean.getBookshelf().logout(sessionBean.getSessionId());
    sessionBean.setSessionId(null);
    response.sendRedirect("login.jsp"); %>
