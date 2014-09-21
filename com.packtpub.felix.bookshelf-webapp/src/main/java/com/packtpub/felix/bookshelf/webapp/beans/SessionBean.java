package com.packtpub.felix.bookshelf.webapp.beans;

import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.packtpub.felix.bookshelf.service.api.BookshelfService;

public class SessionBean {
	static final String OSGI_BUNDLECONTEXT = "osgi-bundlecontext";

	private BundleContext ctx;

	private String sessionId;

	public void initialize(ServletContext context) {
		this.ctx = (BundleContext) context.getAttribute(OSGI_BUNDLECONTEXT);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BookshelfService getBookshelf() {
		String name = BookshelfService.class.getName();
		ServiceReference ref = ctx.getServiceReference(name);
		if (ref == null) {
			throw new RuntimeException("Service not registered: " + name);
		}
		BookshelfService service = (BookshelfService) ctx.getService(ref);
		return service;
	}

	public boolean sessionIsValid() {
		return getBookshelf().sessionIsValid(getSessionId());
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
