<%@ page import="java.util.*"%>

<%@ include file="init.inc.jsp" %>

<%  // get category to browse, if none go to categories view
    String category = request.getParameter("category");
    if (category==null || category.equals("")) {
        response.sendRedirect("index.jsp");
    }
%>

<html>
  <head>
    <title>Bookshelf - Browse Category: <%= category %></title>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
  </head>
  <body>
	  <%@ include file="menu.inc.jsp" %>
	  <h2>Bookshelf - Browse Category: <%= category %></h2>
	
	  <%  
	  Set<String> isbns =
	      sessionBean.getBookshelf().searchBooksByCategory(
	          sessionBean.getSessionId(), category); %>
	  <table class="BookList">
	  <%@ include file="bookListHeader.inc.jsp" %>
	  <%  for (String isbn : isbns) { %>
	        <jsp:include page="bookListEntry.inc.jsp">
	          <jsp:param name="sessionBean" value="<%=sessionBean%>"/>
	          <jsp:param name="isbn" value="<%=isbn %>" />
	        </jsp:include>
	  <%  } %>
	  </table>
  </body>
</html>