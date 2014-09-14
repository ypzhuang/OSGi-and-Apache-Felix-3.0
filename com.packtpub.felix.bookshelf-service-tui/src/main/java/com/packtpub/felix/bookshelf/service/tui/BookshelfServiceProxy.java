package com.packtpub.felix.bookshelf.service.tui;


import java.util.Set;

import com.packtpub.felix.bookshelf.inventory.api.Book;
import com.packtpub.felix.bookshelf.inventory.api.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.InvalidBookException;

import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;

public interface BookshelfServiceProxy {
	public static final String SCOPE = "book";
	public static final String[] FUNCTIONS = new String[] { "search", "add", "get", "remove" };
	String FUNCTIONS_STR = "[add,search,get,remove]";

	public String add(String username, String password, String isbn, String title, String author, String category,
			int rating) throws InvalidCredentialsException, BookAlreadyExistsException, InvalidBookException;

	public Set<Book> search(String username, String password, String attribute, String filter)
			throws InvalidCredentialsException;

	public Set<Book> search(String username, String password, String attribute, int lower, int upper)
			throws InvalidCredentialsException;

	public Book get(String username, String password, String isbn) throws InvalidCredentialsException,
			BookNotFoundException;

	public void remove(String username, String password, String isbn) throws InvalidCredentialsException,
			BookNotFoundException;

}
