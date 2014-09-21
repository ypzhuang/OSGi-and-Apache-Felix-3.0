<%@page import="java.util.*" %>

<%@ include file="init.inc.jsp" %>

<html>
  <head>
    <title>Bookshelf - Search Authors</title>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
  </head>
<body>
  <%@ include file="menu.inc.jsp" %>
  <h2>Bookshelf - Search Authors</h2>

<%  String authorLike = request.getParameter("authorLike");
    if (authorLike==null) authorLike = "";
%>

  <form action="searchAuthors.action.jsp" method="get">
    Author: <input type="text" name="authorLike" value="<%= authorLike %>" />(use % for start and end wildcards)<br />
    <input type="submit" value="Search" />
  </form>

<%  if (!authorLike.equals("")) {
      Set<String> isbns = sessionBean.getBookshelf().searchBooksByAuthor(
          sessionBean.getSessionId(), authorLike); %>
      <table class="BookList">
      <%@ include file="bookListHeader.inc.jsp" %>
      <%  for (String isbn : isbns) { %>
            <jsp:include page="bookListEntry.inc.jsp">
              <jsp:param name="isbn" value="<%=isbn %>" />
            </jsp:include>
      <%  } %>
      </table>
<%  } %>
</body>
</html>