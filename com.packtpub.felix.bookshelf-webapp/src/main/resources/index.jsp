<%@ page import="java.util.Set"%>

<%@ include file="init.inc.jsp" %>

<html>
  <head>
    <title>Bookshelf - Browse Categories</title>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
  </head>
<body>
  <%@ include file="menu.inc.jsp" %>
  <h2>Bookshelf - Browse Categories</h2>

  <% Set<String> categories = sessionBean.getBookshelf().getCategories(sessionBean.getSessionId());
  %><ul>
	  <% for (String category : categories) { %>
	          <li><a href="byCategory.jsp?category=<%= category %>"><%=category%></a></li>
	  <% }
  %></ul>


</body>
</html>