package domain.repository

import domain.aggregate.book.entity.Book
import domain.aggregate.book.valueobject.ISBN
import domain.crosscutting.Page
import domain.crosscutting.Pageable

interface BookRepository {
    fun get(isbn:ISBN): Book?
//    fun findAll(term: String? = null): List<Book>
    fun find(pageable: Pageable): Page<Book>
    fun save(book: Book)
}