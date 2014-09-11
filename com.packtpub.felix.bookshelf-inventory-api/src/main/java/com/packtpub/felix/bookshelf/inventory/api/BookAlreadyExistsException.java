package com.packtpub.felix.bookshelf.inventory.api;

public class BookAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 5120358203999152018L;

	public BookAlreadyExistsException(String isbn) {
		super("Book already exists: " + isbn);
	}
}
