package com.packtpub.felix.bookshelf.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.packtpub.felix.bookshelf.inventory.api.Book;
import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.log.api.BookshelfLogHelper;
import com.packtpub.felix.bookshelf.service.api.BookshelfService;
import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;

public class BookshelfServletImpl extends HttpServlet {
	private static final long serialVersionUID = -3152244753777234196L;

	@SuppressWarnings("unused")
	private String alias;

	private BookshelfService service;

	private BookshelfLogHelper logger;

	private String sessionId;

	private static final String PARAM_OP = "op";

	private static final String OP_CATEGORIES = "categories";

	private static final String OP_BYCATEGORY = "byCategory";

	private static final String OP_BYAUTHOR = "byAuthor";

	private static final String PARAM_CATEGORY = "category";

	private static final String PARAM_AUTHOR = "author";

	private static final String OP_ADDBOOKFORM = "addBookForm";

	private static final String PARAM_ISBN = "isbn";

	private static final String PARAM_TITLE = "title";

	private static final String PARAM_RATING = "rating";

	private static final String OP_ADDBOOK = "addBook";

	private static final String OP_LOGINFORM = "loginForm";

	private static final String OP_LOGIN = "login";

	private static final String PARAM_USER = "user";

	private static final String PARAM_PASS = "pass";

	private static final String LOG_OPERATION_REQUEST = "[op={0}], [session={1}]";

	private static final String PREFIX = "<html><body>";

	private static final String POSTFIX = "</body></html>";

	public void init(ServletConfig config) {
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String op = req.getParameter(PARAM_OP);
		resp.setContentType("text/html");
		this.logger.debug(LOG_OPERATION_REQUEST, op, this.sessionId);

		if (OP_LOGIN.equals(op)) {
			String user = req.getParameter(PARAM_USER);
			String pass = req.getParameter(PARAM_PASS);
			try {
				doLogin(user, pass);
				htmlMainPage(resp.getWriter());
			} catch (InvalidCredentialsException e) {
				htmlLoginForm(resp.getWriter(), e.getMessage());
			}
			return;
		} else if (OP_LOGINFORM.equals(op) || !sessionIsValid()) {
			htmlLoginForm(resp.getWriter(), null);
			return;
		}
		try {
			if (op == null) {
				htmlMainPage(resp.getWriter());
			} else if (OP_CATEGORIES.equals(op)) {
				htmlCategories(resp.getWriter());
			} else if (OP_BYCATEGORY.equals(op)) {
				String category = req.getParameter(PARAM_CATEGORY);
				htmlByCategory(resp.getWriter(), category);
			} else if (OP_BYAUTHOR.equals(op)) {
				String author = req.getParameter(PARAM_AUTHOR);
				htmlByAuthor(resp.getWriter(), author);
			} else if (OP_ADDBOOKFORM.equals(op)) {
				htmlAddBookForm(resp.getWriter());
			} else if (OP_ADDBOOK.equals(op)) {
				htmlTop(resp.getWriter());
				doAddBook(req, resp);
				htmlBottom(resp.getWriter());
			} else {
				htmlMainPage(resp.getWriter());
			}
		} catch (InvalidCredentialsException e) {
			resp.getWriter().write(e.getMessage());
		}
	}

	private boolean sessionIsValid() {
		return this.sessionId != null;
	}

	private void htmlCategories(PrintWriter printWriter) throws InvalidCredentialsException {
		htmlTop(printWriter);

		printWriter.println("<h3>Categories:</h3>");
		printWriter.println("<ul>");
		for (String category : this.service.getCategories(this.sessionId)) {
			printWriter.println("<li><a href=\"" + browseByCategoryUrl(category) + "\">" + category + "</li>");
		}
		printWriter.println("</ul>");

		htmlBottom(printWriter);
	}

	private void doLogin(String user, String pass) throws InvalidCredentialsException {
		this.sessionId = this.service.login(user, pass.toCharArray());
	}

	private void htmlLoginForm(PrintWriter printWriter, String errorMessage) {
		printWriter.println("<h3>Login</h3>");

		if (errorMessage != null) {
			printWriter.println("Login failed: " + errorMessage);
		}
		printWriter.println("<form method=\"get\" action=\"" + loginUrl() + "\">");

		printWriter.println("<input type=\"hidden\" name=\"" + PARAM_OP + "\" value=\"" + OP_LOGIN + "\">");
		printWriter.println("User: <input type=\"text\" name=\"" + PARAM_USER + "\"><br />");
		printWriter.println("Pass: <input type=\"password\" name=\"" + PARAM_PASS + "\"><br />");

		printWriter.println("<input type=\"submit\" value=\"Submit\">");
		printWriter.println("</form>");
	}

	private String loginUrl() {
		return "?" + PARAM_OP + "=" + OP_LOGIN;
	}

	private void htmlTop(PrintWriter printWriter) {
		printWriter.println(PREFIX);

		printWriter.println("<table border=\"1\">");
		printWriter.println("<tr>");

		printWriter.println("<td><a href=\"?\">Home</a></td>");
		printWriter.println("<td><a href=\"" + categoriesUrl() + "\">Browse by category</a></td>");
		printWriter.println("<td><a href=\"" + searchByAuthorUrl(null) + "\">Search by author</a></td>");
		printWriter.println("<td><a href=\"" + addBookFormUrl() + "\">Add book</a></td>");

		printWriter.println("<tr>");
		printWriter.println("</table>");
	}

	private void htmlBottom(PrintWriter printWriter) {
		printWriter.println(POSTFIX);
	}

	private void htmlMainPage(PrintWriter printWriter) {
		htmlTop(printWriter);
		printWriter.println("<h3>Welcome to the Bookshelf</h3>");
		htmlBottom(printWriter);
	}

	private String categoriesUrl() {
		return "?" + PARAM_OP + "=" + OP_CATEGORIES;
	}

	private String browseByCategoryUrl(String category) {
		return "?" + PARAM_OP + "=" + OP_BYCATEGORY + "&" + PARAM_CATEGORY + "=" + category;
	}

	private void htmlByCategory(PrintWriter printWriter, String category) throws InvalidCredentialsException {
		htmlTop(printWriter);

		printWriter.println("<h3>In Category: " + category + "</h3>");
		if (category != null) {
			printWriter.println("<table border=\"1\">");
			printWriter.println("<tr><th>ISBN</th><th>Rating</th><th>Title</th><th>Author</th></tr>");
			for (String isbn : this.service.searchBooksByCategory(this.sessionId, category)) {
				printWriter.println("<tr><td>" + isbn + "</td>");
				Book book;
				try {
					book = this.service.getBook(this.sessionId, isbn);
					printWriter.println("<td>" + book.getRating() + "</td><td>" + book.getTitle() + "</td><td>"
							+ book.getAuthor() + "</td>");
				} catch (BookNotFoundException e) {
					printWriter.println("<td>-</td><td>" + e.getMessage() + "</td><td>-</td>");
				}

				printWriter.println("</tr>");
			}
			printWriter.println("</table>");
		}

		htmlBottom(printWriter);
	}

	private String searchByAuthorUrl(String author) {
		if (author == null) {
			return "?" + PARAM_OP + "=" + OP_BYAUTHOR;
		} else {
			return "?" + PARAM_OP + "=" + OP_BYAUTHOR + "&" + PARAM_AUTHOR + "=" + author;
		}
	}

	private void htmlByAuthor(PrintWriter printWriter, String author) throws InvalidCredentialsException {
		htmlTop(printWriter);

		printWriter.println("<h3>Search by author</h3>");

		printWriter.println("<form method=\"get\" action=\"" + searchByAuthorUrl(null) + "\">");

		printWriter.println("<input type=\"hidden\" name=\"" + PARAM_OP + "\" value=\"" + OP_BYAUTHOR + "\">");
		printWriter.print("<input type=\"text\" name=\"" + PARAM_AUTHOR + "\"");
		if (author != null) {
			printWriter.println(" value=\"" + author + "\">");
		}else {
			printWriter.println(">");
		}
		

		printWriter.println("<input type=\"submit\" value=\"Search\">");
		printWriter.println("</form>");

		if (author != null) {
			printWriter.println("<h4>Results for " + author + "</h4>");
			printWriter.println("<table border=\"1\">");
			printWriter.println("<tr><th>ISBN</th><th>Rating</th><th>Title</th><th>Category</th></tr>");
			for (String isbn : this.service.searchBooksByAuthor(this.sessionId, author)) {
				printWriter.println("<tr><td>" + isbn + "</td>");
				Book book;
				try {
					book = this.service.getBook(this.sessionId, isbn);
					printWriter.println("<td>" + book.getRating() + "</td><td>" + book.getTitle() + "</td><td>"
							+ book.getRating() + "</td>");
				} catch (BookNotFoundException e) {
					printWriter.println("<td>-</td><td>-</td><td>" + e.getMessage() + "</td>");
				}

				printWriter.println("</tr>");
			}
			printWriter.println("</table>");
		}

		htmlBottom(printWriter);
	}

	private String addBookFormUrl() {
		return "?" + PARAM_OP + "=" + OP_ADDBOOKFORM;
	}

	private void htmlAddBookForm(PrintWriter printWriter) {
		htmlTop(printWriter);

		printWriter.println("<h3>Add Book</h3>");

		printWriter.println("<form method=\"get\" action=\"" + addBookUrl() + "\"><table>");

		printWriter.println("<tr><td><input type=\"hidden\" name=\"" + PARAM_OP + "\" value=\"" + OP_ADDBOOK + "\">");
		printWriter.println("<tr><td>ISBN:</td><td><input type=\"text\" name=\"" + PARAM_ISBN + "\"></td>");
		printWriter.println("<tr><td>Author:</td><td><input type=\"text\" name=\"" + PARAM_AUTHOR + "\"></td>");
		printWriter.println("<tr><td>Title:</td><td><input type=\"text\" name=\"" + PARAM_TITLE + "\"></td>");
		printWriter.println("<tr><td>Category:</td><td><input type=\"text\" name=\"" + PARAM_CATEGORY + "\"></td>");
		printWriter.println("<tr><td>Rating:</td><td><input type=\"text\" name=\"" + PARAM_RATING + "\"></td>");

		printWriter.println("<tr><td colspan=\"2\"><input type=\"submit\" value=\"Add\">");
		printWriter.println("</table></form>");
	}

	private String addBookUrl() {
		return "?" + PARAM_OP + "=" + OP_ADDBOOK;
	}

	private void doAddBook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String isbn = req.getParameter(PARAM_ISBN);
		String category = req.getParameter(PARAM_CATEGORY);
		String author = req.getParameter(PARAM_AUTHOR);
		String title = req.getParameter(PARAM_TITLE);
		String ratingStr = req.getParameter(PARAM_RATING);
		int rating = 0;
		try {
			rating = Integer.parseInt(ratingStr);
		} catch (NumberFormatException e) {
			resp.getWriter().println(e.getMessage());
			return;
		}

		try {
			this.service.addBook(this.sessionId, isbn, title, author, category, rating);
		} catch (Exception e) {
			resp.getWriter().println(e.getMessage());
			return;
		}
		resp.getWriter().println("Added!");
	}
}
