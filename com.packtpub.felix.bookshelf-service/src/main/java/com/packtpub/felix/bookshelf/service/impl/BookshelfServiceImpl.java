package com.packtpub.felix.bookshelf.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//import org.osgi.framework.BundleContext;
//import org.osgi.framework.ServiceReference;



import com.packtpub.felix.bookshelf.inventory.api.Book;
import com.packtpub.felix.bookshelf.inventory.api.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.BookInventory;
import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.InvalidBookException;
import com.packtpub.felix.bookshelf.inventory.api.MutableBook;
import com.packtpub.felix.bookshelf.inventory.api.BookInventory.SearchCriteria;
import com.packtpub.felix.bookshelf.log.api.BookshelfLogHelper;
import com.packtpub.felix.bookshelf.service.api.BookshelfService;
import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;

public class BookshelfServiceImpl implements BookshelfService {

	private String sessionId;

	BookInventory inventory;
	
	BookshelfLogHelper logger;
	//private BundleContext context;

	//public BookshelfServiceImpl(BundleContext context) {
	//	this.context = context;
	//}
	public BookshelfServiceImpl(){
		
	}

	
	public BookshelfLogHelper getLogger() {
		return this.logger;
	}


	@Override
	public String login(String username, char[] password) throws InvalidCredentialsException {
		if ("admin".equals(username) && Arrays.equals(password, "admin".toCharArray())) {
			this.sessionId = Long.toString(System.currentTimeMillis());
			return this.sessionId;
		}
		throw new InvalidCredentialsException(username);
	}

	@Override
	public void logout(String sessionId) {
		checkSession(sessionId);
		sessionId = null;

	}

	private void checkSession(String sessionId) {
		if (!sessionIsValid(sessionId)) {
			throw new SessionNotValidRuntimeException(sessionId);
		}
	}

	@Override
	public boolean sessionIsValid(String sessionId) {
		return this.sessionId != null && this.sessionId.equals(sessionId);

	}

	@Override
	public Set<String> getGroups(String sessionId) {
		checkSession(sessionId);
		BookInventory inv = lookupBookInventory();
		return inv.getCategories();
	}

	@Override
	public void addBook(String session, String isbn, String title, String author, String category, int rating)
			throws BookAlreadyExistsException, InvalidBookException {
		getLogger().debug(LoggerConstants.LOG_ADD_BOOK, isbn, title, author, category, rating);
		checkSession(sessionId);
		BookInventory inv = lookupBookInventory();
		getLogger().debug(LoggerConstants.LOG_CREATE_BOOK, isbn);
		MutableBook book = inv.createBook(isbn);
		book.setTitle(title);
		book.setAuthor(author);
		book.setCategory(category);
		book.setRating(rating);
		getLogger().debug(LoggerConstants.LOG_STORE_BOOK, isbn);
		inv.storeBook(book);
	}

	@Override
	public void modifyBookCategory(String session, String isbn, String category) throws BookNotFoundException,
			InvalidBookException { //here is a bug, I think
		checkSession(sessionId);
		BookInventory inv = lookupBookInventory();
		MutableBook book = inv.loadBookForEdit(isbn);
		book.setCategory(category);
		inv.storeBook(book);
	}

	@Override
	public void modifyBookRating(String session, String isbn, int rating) throws BookNotFoundException,
			InvalidBookException {
		checkSession(sessionId);
		BookInventory inv = lookupBookInventory();
		MutableBook book = inv.loadBookForEdit(isbn);
		book.setRating(rating);
		inv.storeBook(book);
	}

	@Override
	public void removeBook(String session, String isbn) throws BookNotFoundException {
		checkSession(sessionId);
		BookInventory inventory = lookupBookInventory();
		inventory.removeBook(isbn);
	}

	@Override
	public Book getBook(String session, String isbn) throws BookNotFoundException {
		checkSession(sessionId);
		BookInventory inventory = lookupBookInventory();
		return inventory.loadBook(isbn);
	}
	
	@Override
	public MutableBook getBookForEdit(String sessionId, String isbn) throws BookNotFoundException{
		getLogger().debug(LoggerConstants.LOG_EDIT_BY_ISBN, isbn);
		checkSession(sessionId);
		BookInventory inventory = lookupBookInventory();
		return inventory.loadBookForEdit(isbn);
	}

	private BookInventory lookupBookInventory() {
		/**
		String name = BookInventory.class.getName();
		ServiceReference ref = this.context.getServiceReference(name);
		if (ref == null) {
			throw new BookInventoryNotRegisteredRuntimeException(name);
		}
		return (BookInventory) this.context.getService(ref);
		*/
		return this.inventory;
	}

	@Override
	public Set<String> searchBooksByCategory(String session, String categoryLike) {
		checkSession(sessionId);
		BookInventory inv = lookupBookInventory();
		Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
		criteria.put(SearchCriteria.CATEGORY_LIKE, categoryLike);
		return inv.searchBooks(criteria);
	}

	@Override
	public Set<String> searchBooksByAuthor(String session, String authorLike) {
		checkSession(sessionId);
		BookInventory inv = lookupBookInventory();
		Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
		criteria.put(SearchCriteria.AUTHOR_LIKE, authorLike);
		return inv.searchBooks(criteria);
	}

	@Override
	public Set<String> searchBooksByTitle(String session, String titleLike) {
		checkSession(sessionId);
		BookInventory inv = lookupBookInventory();
		Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
		criteria.put(SearchCriteria.TITLE_LIKE, titleLike);
		return inv.searchBooks(criteria);
	}

	@Override
	public Set<String> searchBooksByRating(String session, int ratingLower, int ratingUpper) {
		checkSession(sessionId);
		BookInventory inv = lookupBookInventory();
		Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
		criteria.put(SearchCriteria.RATING_GT, Integer.toString(ratingLower));
		criteria.put(SearchCriteria.RATING_LT, Integer.toString(ratingUpper));
		return inv.searchBooks(criteria);
	}

}
