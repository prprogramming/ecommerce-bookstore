# eCommerce Bookstore Backend & Storefront

A modern, full-stack Spring Boot and Maven eCommerce bookstore application featuring a clean multi-package layered architecture, an in-memory H2 database with automatic sample data seeding, RESTful APIs, and an interactive, responsive HTML/Tailwind CSS storefront dashboard.

---

## 1. Prerequisites

Before building and running this project, ensure you have the following installed on your machine:

- **Java Development Kit (JDK)**: Version **17** or higher (verify via `java -version`)
- **Apache Maven**: Version **3.8+** (verify via `mvn -version`)
- **Web Browser**: Any modern browser (Chrome, Firefox, Safari, Edge)

---

## 2. Step-by-Step Build & Run Commands

You can run the application directly using Maven or package it as an executable JAR file.

### Option A: Run Directly with Maven (Recommended for Development)

Open your terminal or command prompt in the project root directory (`ecommerce-bookstore/`) and run:

```bash
# Clean, compile, and launch the Spring Boot application
mvn clean spring-boot:run
```

The application will start, seed the H2 database with sample books via `DataLoader`, and bind to `http://localhost:8080`.

---

### Option B: Build and Run as a Standalone Executable JAR

1. **Clean and Package the Application**:
   ```bash
   mvn clean package
   ```
   *(This compiles the source code, runs automated tests, and generates the JAR in the `target/` directory).*

2. **Execute the Packaged JAR**:
   ```bash
   java -jar target/ecommerce-bookstore-0.0.1-SNAPSHOT.jar
   ```

---

### Option C: Run Automated Tests

To execute the unit and integration test suite:

```bash
mvn clean test
```

---

## 3. Accessing the Application in the Browser

Once the application is running, access the following URLs in your web browser:

### 🛍️ Storefront Web Dashboard
- **URL**: [http://localhost:8080](http://localhost:8080) *(or [http://127.0.0.1:8080](http://127.0.0.1:8080))*
- **Features**:
  - Live catalog grid displaying all seeded books with real-time stock indicators.
  - Dynamic search bar with live filtering by title or author.
  - Interactive **"Buy Now"** modal with quantity adjustments and instant total price calculation.
  - Real-time purchase confirmation toast notifications with stock status updates.

---

### 🗄️ H2 Database Console
- **URL**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **Login Credentials & Connection Settings**:
  - **Saved Settings**: `Generic H2 (Embedded)`
  - **Driver Class**: `org.h2.Driver`
  - **JDBC URL**: `jdbc:h2:mem:bookstoredb`
  - **User Name**: `sa`
  - **Password**: *(leave blank)*
- **Inspecting Data**: Click **Connect**, then run SQL queries such as:
  ```sql
  SELECT * FROM books;
  ```

---

## 4. Project Architecture & Package Structure

The codebase follows enterprise Spring Boot clean architecture principles:

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── bookstore/
│   │           ├── BookstoreApplication.java       # Spring Boot main entry point
│   │           ├── DataLoader.java                 # Pre-populates H2 database on startup
│   │           ├── config/
│   │           │   └── WebConfig.java              # Global CORS configuration
│   │           ├── controller/                     # Web REST Controllers
│   │           │   └── BookController.java         # /api/books and /api/orders endpoints
│   │           ├── dto/                            # Data Transfer Objects
│   │           │   ├── OrderRequest.java           # Order placement payload
│   │           │   └── OrderResponse.java          # Order confirmation / out-of-stock response
│   │           ├── exception/                      # Global exception handling & error responses
│   │           │   ├── BookNotFoundException.java
│   │           │   ├── InsufficientStockException.java
│   │           │   ├── ErrorResponse.java
│   │           │   └── GlobalExceptionHandler.java # @RestControllerAdvice error handler
│   │           ├── model/                          # JPA Entities & Domain models
│   │           │   └── Book.java                   # Book entity (id, title, author, price, stock, description)
│   │           ├── repository/                     # Spring Data JPA Repository layer
│   │           │   └── BookstoreRepository.java    # JpaRepository for Book CRUD & query methods
│   │           └── service/                        # Business Logic & Transactions
│   │               └── BookService.java            # Catalog management & atomic order processing
│   └── resources/
│       ├── application.properties                  # Database, server, and JPA configuration
│       └── static/
│           └── index.html                          # Responsive storefront UI (Tailwind CSS + Fetch API)
└── test/
    └── java/
        └── com/
            └── bookstore/
                └── BookstoreApplicationTests.java  # Integration test suite
```

---

## 5. REST API Documentation

### Available Endpoints

| HTTP Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/books` | Fetch all books (optional query filters: `?title=...` and `?author=...`) |
| `GET` | `/api/books/{id}` | Fetch a specific book by its ID |
| `POST` | `/api/orders` | Place a purchase order, deduct stock, and return status |
| `POST` | `/api/books` | Create a new book |
| `PUT` | `/api/books/{id}` | Update an existing book |
| `DELETE` | `/api/books/{id}` | Delete a book by ID |

---

### Order Placement API (`POST /api/orders`)

#### Request Payload
```json
{
  "bookId": 1,
  "quantity": 2
}
```

#### Success Response (`200 OK`)
```json
{
  "success": true,
  "message": "Order processed successfully",
  "bookId": 1,
  "bookTitle": "The Great Gatsby",
  "quantity": 2,
  "totalPrice": 29.98,
  "remainingStock": 23
}
```

#### Out-of-Stock Response (`400 Bad Request`)
```json
{
  "success": false,
  "message": "Insufficient stock for book 'The Great Gatsby'. Requested: 50, Available: 25",
  "bookId": 1,
  "bookTitle": "The Great Gatsby",
  "quantity": 50,
  "totalPrice": 0.0,
  "remainingStock": 25
}
```

---

## 6. Pre-Populated Sample Books

On startup, [`DataLoader.java`](src/main/java/com/bookstore/DataLoader.java:1) automatically seeds the database with the following titles:

1. **The Great Gatsby** - *F. Scott Fitzgerald* ($14.99, Stock: 25)
2. **To Kill a Mockingbird** - *Harper Lee* ($12.50, Stock: 30)
3. **1984** - *George Orwell* ($15.99, Stock: 40)
4. **Clean Code: A Handbook of Agile Software Craftsmanship** - *Robert C. Martin* ($34.99, Stock: 15)
5. **Effective Java** - *Joshua Bloch* ($42.50, Stock: 20)

---

## 7. Security & CORS Configuration

- **Service Binding**: Bound to local loopback interface (`127.0.0.1:8080`).
- **CORS Support**: [`WebConfig.java`](src/main/java/com/bookstore/config/WebConfig.java:1) allows standard local frontend origins (`http://localhost:3000`, `http://localhost:5173`, `http://localhost:4200`, `http://127.0.0.1:*`) for decoupled frontend development.
- **Exception Sanitization**: [`GlobalExceptionHandler.java`](src/main/java/com/bookstore/exception/GlobalExceptionHandler.java:1) suppresses internal stack traces and returns clean, structured error responses.
