package com.packtpub.felix.bookshelf.service.impl.activator;

import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.packtpub.felix.bookshelf.inventory.api.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.InvalidBookException;
import com.packtpub.felix.bookshelf.service.api.BookshelfService;
import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;
import com.packtpub.felix.bookshelf.service.impl.BookshelfServiceImpl;

public class BookshelfServiceImplActivator implements BundleActivator {
	private ServiceRegistration reg = null;

	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("\nStarting Bookshelf Service Impl");
		this.reg = context.registerService(BookshelfService.class.getName(), new BookshelfServiceImpl(context),	null);

		//testService(context);
	}

	private void testService(BundleContext context) {
		// retrieve service
		String name = BookshelfService.class.getName();
		ServiceReference ref = context.getServiceReference(name);
		if (ref == null) {
			throw new RuntimeException("Service not registered: " + name);
		}
		BookshelfService service = (BookshelfService) context.getService(ref);

		String sessionId;
		try {
			System.out.println("\nSigning in . . .");
			sessionId = service.login("admin", "admin".toCharArray());
		} catch (InvalidCredentialsException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("\nAdding books . . .");

		try {
			service.addBook(sessionId, "123-4567890100", "Book 1 Title", "John Doe", "Group 1", 0);
			service.addBook(sessionId, "123-4567890101", "Book 2 Title", "Will Smith", "Group 1", 0);
			service.addBook(sessionId, "123-4567890200", "Book 3 Title", "John Doe", "Group 2", 0);
			service.addBook(sessionId, "123-4567890201", "Book 4 Title", "Jane Doe", "Group 2", 0);
		} catch (BookAlreadyExistsException e) {			
			e.printStackTrace();
			return;
		} catch (InvalidBookException e) {			
			e.printStackTrace();
			return;
		}
		
		// and test search
		String authorLike = "%Doe";
		System.out.println("Searching for books with author like :" + authorLike);
		Set<String> results = service.searchBooksByAuthor(sessionId, authorLike);
		for (String isbn: results) {
			try {
				System.out.println(" - " + service.getBook(sessionId, isbn));
			} catch (BookNotFoundException e) {				
				System.err.println(e.getMessage());
			}
		}

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("\nStoping Bookshelf Service Impl");
		if (this.reg != null) {
			context.ungetService(this.reg.getReference());
			this.reg = null;
		}

	}

}
