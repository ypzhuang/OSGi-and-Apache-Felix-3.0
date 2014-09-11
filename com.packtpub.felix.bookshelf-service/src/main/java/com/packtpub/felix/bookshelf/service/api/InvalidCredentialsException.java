package com.packtpub.felix.bookshelf.service.api;

public class InvalidCredentialsException extends Exception {

	private static final long serialVersionUID = -1166613455723457289L;

	public InvalidCredentialsException(String username) {
		super("Invalid credentials for " + username);
	}
}
