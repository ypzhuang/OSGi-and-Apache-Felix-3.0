<jsp:useBean id="sessionBean" class="com.packtpub.felix.bookshelf.webapp.beans.SessionBean" scope="session">
	<%
		sessionBean.initialize(getServletContext());
	%>
</jsp:useBean>