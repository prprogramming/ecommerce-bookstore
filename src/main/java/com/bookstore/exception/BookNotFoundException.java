package com.bookstore.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super(String.format("Book with ID %d not found", id));
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
