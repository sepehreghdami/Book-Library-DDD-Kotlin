package domain.service

import domain.aggregate.book.entity.Book
import domain.aggregate.book.valueobject.Author
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.book.valueobject.Stock
import domain.aggregate.book.valueobject.Title
import domain.repository.BookRepository
import domain.repository.TransactionManager


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
}