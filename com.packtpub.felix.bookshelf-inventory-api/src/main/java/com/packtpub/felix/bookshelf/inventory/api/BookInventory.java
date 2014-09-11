package com.packtpub.felix.bookshelf.inventory.api;

import java.util.Map;
import java.util.Set;

public interface BookInventory {
	enum SearchCriteria {
		ISBN_LIKE, TITLE_LIKE, AUTHOR_LIKE, CATEGORY_LIKE, RATING_GT, RATING_LT
	}

	Set<String> getCategories();

	MutableBook createBook(String isbn) throws BookAlreadyExistsException;

	MutableBook loadBookForEdit(String isbn) throws BookNotFoundException;

	String storeBook(MutableBook book) throws InvalidBookException;

	Book loadBook(String isbn) throws BookNotFoundException;

	void removeBook(String isbn) throws BookNotFoundException;

	Set<String> searchBooks(Map<SearchCriteria, String> criteria);

}
