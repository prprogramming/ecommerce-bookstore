package com.bookstore.dto;

public class OrderResponse {

    private boolean success;
    private String message;
    private Long bookId;
    private String bookTitle;
    private Integer quantity;
    private Double totalPrice;
    private Integer remainingStock;

    public OrderResponse() {
    }

    public OrderResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public OrderResponse(boolean success, String message, Long bookId, String bookTitle, Integer quantity, Double totalPrice, Integer remainingStock) {
        this.success = success;
        this.message = message;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.remainingStock = remainingStock;
    }

    public static OrderResponse success(Long bookId, String bookTitle, Integer quantity, Double totalPrice, Integer remainingStock) {
        return new OrderResponse(true, "Order processed successfully", bookId, bookTitle, quantity, totalPrice, remainingStock);
    }

    public static OrderResponse outOfStock(Long bookId, String bookTitle, Integer requestedQuantity, Integer availableStock) {
        return new OrderResponse(
            false,
            String.format("Insufficient stock for book '%s'. Requested: %d, Available: %d", bookTitle, requestedQuantity, availableStock),
            bookId,
            bookTitle,
            requestedQuantity,
            0.0,
            availableStock
        );
    }

    public static OrderResponse failed(String message) {
        return new OrderResponse(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }
}
