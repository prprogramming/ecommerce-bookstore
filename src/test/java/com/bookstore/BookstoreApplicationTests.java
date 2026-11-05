package com.bookstore;

import com.bookstore.dto.OrderRequest;
import com.bookstore.dto.OrderResponse;
import com.bookstore.model.Book;
import com.bookstore.repository.BookstoreRepository;
import com.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookstoreApplicationTests {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookstoreRepository bookstoreRepository;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = bookstoreRepository.findAll().stream().findFirst().orElseGet(() ->
            bookstoreRepository.save(new Book("Test Book", "Test Author", 20.0, 10, "Test Description"))
        );
    }

    @Test
    void contextLoads() {
        assertNotNull(bookService);
        assertNotNull(bookstoreRepository);
    }

    @Test
    void testGetAllBooks() {
        assertFalse(bookService.getAllBooks().isEmpty());
    }

    @Test
    void testProcessOrderSuccess() {
        int initialStock = testBook.getStock();
        int orderQuantity = 2;

        OrderRequest request = new OrderRequest(testBook.getId(), orderQuantity);
        OrderResponse response = bookService.processOrder(request);

        assertTrue(response.isSuccess());
        assertEquals(testBook.getId(), response.getBookId());
        assertEquals(orderQuantity, response.getQuantity());
        assertEquals(initialStock - orderQuantity, response.getRemainingStock());
        assertEquals(testBook.getPrice() * orderQuantity, response.getTotalPrice(), 0.001);
    }

    @Test
    void testProcessOrderOutOfStock() {
        Book currentBook = bookService.getRequiredBookById(testBook.getId());
        int excessiveQuantity = currentBook.getStock() + 100;

        OrderRequest request = new OrderRequest(currentBook.getId(), excessiveQuantity);
        OrderResponse response = bookService.processOrder(request);

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Insufficient stock"));
    }
}
