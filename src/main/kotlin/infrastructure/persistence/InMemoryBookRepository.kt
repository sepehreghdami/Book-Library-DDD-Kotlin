package infrastructure.persistence

import domain.repository.BookRepository
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.book.entity.Book
class InMemoryBookRepository : BookRepository {
    private val books = mutableMapOf<ISBN, Book>()

    override fun get(isbn: ISBN): Book? {
        return books[isbn]
    }

    fun save(book: Book) {
        books[book.isbn] = book
    }

    fun preload(vararg bookList: Book) {
        bookList.forEach { save(it) }
    }
    fun getAll(): List<Book> = books.values.toList()

}
