package com.packtpub.felix.bookshelf.service.impl;

public class SessionNotValidRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 2485522868147036543L;

	public SessionNotValidRuntimeException(String session) {
		super("Session not valid (" + session + "), or session expired; you must login.");
	}

}
