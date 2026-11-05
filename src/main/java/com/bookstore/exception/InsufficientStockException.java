package com.bookstore.exception;

public class InsufficientStockException extends RuntimeException {

    private final Long bookId;
    private final String bookTitle;
    private final Integer requestedQuantity;
    private final Integer availableStock;

    public InsufficientStockException(Long bookId, String bookTitle, Integer requestedQuantity, Integer availableStock) {
        super(String.format("Insufficient stock for book '%s' (ID: %d). Requested: %d, Available: %d", bookTitle, bookId, requestedQuantity, availableStock));
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.requestedQuantity = requestedQuantity;
        this.availableStock = availableStock;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }
}
