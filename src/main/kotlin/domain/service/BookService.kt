package domain.service

import domain.aggregate.book.entity.Book
import domain.aggregate.book.valueobject.Author
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.book.valueobject.Stock
import domain.aggregate.book.valueobject.Title
import domain.repository.BookRepository
import domain.repository.TransactionManager
import domain.crosscutting.*


class BookService (private val bookRepository: BookRepository,
    private val transactionManager: TransactionManager) {

    fun addBook(isbn: ISBN,
                title: Title,
                author: Author,
                stock: Stock) {
        transactionManager.performInTransaction {
            val book = bookRepository.get(isbn)
            if (book == null) {
                bookRepository.save(
                    Book.makeNew(
                        isbn = isbn,
                        title = title,
                        author = author,
                        stock = stock
                    )
                )
            } else {
                throw IllegalArgumentException("This book already exists")
            }
        }
    }
    fun findBooks(
        page: Int,
        size: Int
    ): Page<Book> {
        return transactionManager.performInTransaction {
            val pageable = Pageable(page, size)
            bookRepository.find(pageable)

        }
        }

    fun getBook(isbn: ISBN): Book? {
        return transactionManager.performInTransaction {
            bookRepository.get(isbn = isbn)
        }
    }
}