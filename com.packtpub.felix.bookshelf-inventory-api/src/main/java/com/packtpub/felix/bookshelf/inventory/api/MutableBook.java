package com.packtpub.felix.bookshelf.inventory.api;

public interface MutableBook extends Book {

	void setIsbn(String isbn);

	void setTitle(String title);

	void setAuthor(String author);

	void setCategory(String category);

	void setRating(int rate);

}
