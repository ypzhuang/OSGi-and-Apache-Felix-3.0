<%@ include file="init.inc.jsp" %>

<html>
  <head>
    <title>Bookshelf - Add Book</title>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
  </head>
<body>
  <%@ include file="menu.inc.jsp" %>
  <h2>Bookshelf - Add Book</h2>

<%  String isbn = request.getParameter("isbn");
    String title = request.getParameter("title");
    String author = request.getParameter("author");
    String category = request.getParameter("category");
    String ratingStr = request.getParameter("rating");
    
    int rating = 0;
    try {
      rating = Integer.parseInt(ratingStr);
    
      // add book
      sessionBean.getBookshelf().addBook(
          sessionBean.getSessionId(), isbn, title, author, category, rating);
    }
    catch (Exception e) { %>
        Error: <%=e.getMessage()%>, go back and try again.
    <%  return;
    }
%>
	<b>Book added:</b>
	
	<table class="BookList">
		<%@ include file="bookListHeader.inc.jsp" %>
		<jsp:include page="bookListEntry.inc.jsp">
  		<jsp:param name="isbn" value="<%=isbn %>" />
		</jsp:include>
	</table>
</body>
</html>