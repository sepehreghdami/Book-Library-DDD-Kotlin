package domain.repository

import domain.aggregate.book.entity.Book
import domain.aggregate.book.valueobject.ISBN

interface BookRepository {
    fun get(isbn:ISBN): Book?
    fun getAll(): List<Book>
    fun save(book: Book)

}