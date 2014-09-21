<%  // get authentication paramters
    String user = request.getParameter("user");
    String pass = request.getParameter("pass");
    if (user==null || user.equals("")) {
        response.sendRedirect("login.jsp");
    }
%>

<%@ include file="init-no-check.inc.jsp" %>

<%  try {
        sessionBean.setSessionId(
            sessionBean.getBookshelf().login(user, pass.toCharArray()));
    }
    catch (Throwable t) {
        response.sendRedirect("login.jsp");
    } 
    // if success then forward to index.jsp
    response.sendRedirect("index.jsp");
%>
