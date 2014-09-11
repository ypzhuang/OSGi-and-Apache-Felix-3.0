package com.packtpub.felix.bookshelf.service.tui.activator;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.packtpub.felix.bookshelf.service.tui.BookshelfServiceProxy;

public class BookshelfTuiActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		properties.put("osgi.command.scope", BookshelfServiceProxy.SCOPE);
		properties.put("osgi.command.function", BookshelfServiceProxy.FUNCTIONS);
		context.registerService(BookshelfServiceProxy.class.getName(), new BookshelfServiceProxy(context), properties);

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

}
