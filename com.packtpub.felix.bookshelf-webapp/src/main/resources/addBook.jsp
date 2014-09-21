<%@ include file="init.inc.jsp" %>

<html>
  <head>
    <title>Bookshelf - Add Book</title>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
  </head>
<body>
  <%@ include file="menu.inc.jsp" %>
  <h2>Bookshelf - Add Book</h2>

  <form action="addBook.action.jsp" method="get">
  <table width="60%">
    <tr><td>ISBN*: </td><td><input type="text" name="isbn" size="80" /></td></tr>
    <tr><td>Title: </td><td><input type="text" name="title" size="80" /></td></tr>
    <tr><td>Author: </td><td><input type="text" name="author" size="80" /></td></tr>
    <tr><td>Category: </td><td><input type="text" name="category" size="80" /></td></tr>
    <tr><td>Rating: </td><td><input type="text" name="rating" /></td></tr>
    
    <tr><td></td><td><input type="submit" value="Add" /></td></tr>
  </table>
  </form>
</body>
</html>