package com.packtpub.felix.bookshelf.service.impl;

public class BookInventoryNotRegisteredRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 7512368046636976240L;

	public BookInventoryNotRegisteredRuntimeException(String className) {
		super("BookInventory not registered, looking under: " + className);
	}
}
