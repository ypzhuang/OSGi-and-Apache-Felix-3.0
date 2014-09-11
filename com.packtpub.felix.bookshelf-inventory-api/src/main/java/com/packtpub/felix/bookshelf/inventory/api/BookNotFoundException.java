package com.packtpub.felix.bookshelf.inventory.api;

public class BookNotFoundException extends Exception {

  
	private static final long serialVersionUID = -5155088717583622072L;

	public BookNotFoundException(String isbn) {
        super("Book not found: " + isbn);
    }
}
