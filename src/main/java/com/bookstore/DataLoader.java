package com.bookstore;

import com.bookstore.model.Book;
import com.bookstore.repository.BookstoreRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final BookstoreRepository bookstoreRepository;

    public DataLoader(BookstoreRepository bookstoreRepository) {
        this.bookstoreRepository = bookstoreRepository;
    }

    @Override
    public void run(String... args) {
        if (bookstoreRepository.count() == 0) {
            logger.info("Pre-populating H2 database with sample bookstore data...");

            List<Book> sampleBooks = List.of(
                new Book(
                    "The Great Gatsby",
                    "F. Scott Fitzgerald",
                    14.99,
                    25,
                    "A classic novel set in the Jazz Age, telling the tragic story of Jay Gatsby and his unrequited love for Daisy Buchanan."
                ),
                new Book(
                    "To Kill a Mockingbird",
                    "Harper Lee",
                    12.50,
                    30,
                    "A Pulitzer Prize-winning masterpiece exploring racial injustice and the destruction of innocence in the Deep South."
                ),
                new Book(
                    "1984",
                    "George Orwell",
                    15.99,
                    40,
                    "A dystopian social science fiction novel depicting a totalitarian regime led by Big Brother."
                ),
                new Book(
                    "Clean Code: A Handbook of Agile Software Craftsmanship",
                    "Robert C. Martin",
                    34.99,
                    15,
                    "A definitive guide packed with best practices and refactoring techniques to write readable, maintainable software."
                ),
                new Book(
                    "Effective Java",
                    "Joshua Bloch",
                    42.50,
                    20,
                    "Essential guidance and industry best practices for writing robust, high-performance Java applications."
                )
            );

            bookstoreRepository.saveAll(sampleBooks);
            logger.info("Successfully loaded {} sample books into H2 database.", sampleBooks.size());
        }
    }
}
