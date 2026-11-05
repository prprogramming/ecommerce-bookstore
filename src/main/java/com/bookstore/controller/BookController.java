package com.bookstore.controller;

import com.bookstore.dto.OrderRequest;
import com.bookstore.dto.OrderResponse;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://127.0.0.1:3000", "http://127.0.0.1:5173", "http://localhost:8080", "http://127.0.0.1:8080"})
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * GET /api/books: Fetch all available books in the bookstore.
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {

        if (title != null && !title.isBlank()) {
            return ResponseEntity.ok(bookService.searchBooksByTitle(title));
        }
        if (author != null && !author.isBlank()) {
            return ResponseEntity.ok(bookService.searchBooksByAuthor(author));
        }
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    /**
     * GET /api/books/{id}: Fetch a specific book by its ID.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getRequiredBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * POST /api/orders: Accept a JSON payload containing order details (book ID and quantity)
     * to process a purchase, returning a success or out-of-stock response and updating inventory.
     */
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> processOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse response = bookService.processOrder(orderRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * POST /api/books: Create a new book.
     */
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book created = bookService.createBook(book);
        return ResponseEntity.created(URI.create("/api/books/" + created.getId())).body(created);
    }

    /**
     * PUT /api/books/{id}: Update an existing book.
     */
    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        return bookService.updateBook(id, book)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/books/{id}: Delete a book by its ID.
     */
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
