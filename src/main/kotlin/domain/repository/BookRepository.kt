package domain.repository

import domain.aggregate.book.entity.Book
import domain.aggregate.book.valueobject.ISBN

interface BookRepository {
    fun get(isbn:ISBN): Book?

}