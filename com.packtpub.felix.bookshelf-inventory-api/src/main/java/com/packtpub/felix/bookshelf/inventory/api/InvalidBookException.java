package com.packtpub.felix.bookshelf.inventory.api;

public class InvalidBookException extends Exception {

	private static final long serialVersionUID = 8321472268063571076L;

	public InvalidBookException(String message) {
		super("Book invalid: " + message);
	}
}
