package com.bookstore.service;

import com.bookstore.dto.OrderRequest;
import com.bookstore.dto.OrderResponse;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.model.Book;
import com.bookstore.repository.BookstoreRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookService {

    private final BookstoreRepository bookstoreRepository;

    public BookService(BookstoreRepository bookstoreRepository) {
        this.bookstoreRepository = bookstoreRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookstoreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Book> getBookById(Long id) {
        return bookstoreRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Book getRequiredBookById(Long id) {
        return bookstoreRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooksByTitle(String title) {
        return bookstoreRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooksByAuthor(String author) {
        return bookstoreRepository.findByAuthorContainingIgnoreCase(author);
    }

    public Book createBook(Book book) {
        return bookstoreRepository.save(book);
    }

    public Optional<Book> updateBook(Long id, Book updatedBook) {
        return bookstoreRepository.findById(id).map(existingBook -> {
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPrice(updatedBook.getPrice());
            existingBook.setStock(updatedBook.getStock());
            existingBook.setDescription(updatedBook.getDescription());
            return bookstoreRepository.save(existingBook);
        });
    }

    public boolean deleteBook(Long id) {
        if (bookstoreRepository.existsById(id)) {
            bookstoreRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public OrderResponse processOrder(OrderRequest request) {
        Book book = getRequiredBookById(request.getBookId());

        if (book.getStock() < request.getQuantity()) {
            return OrderResponse.outOfStock(book.getId(), book.getTitle(), request.getQuantity(), book.getStock());
        }

        // Deduct inventory
        book.setStock(book.getStock() - request.getQuantity());
        bookstoreRepository.save(book);

        double totalPrice = Math.round(book.getPrice() * request.getQuantity() * 100.0) / 100.0;
        return OrderResponse.success(book.getId(), book.getTitle(), request.getQuantity(), totalPrice, book.getStock());
    }
}
