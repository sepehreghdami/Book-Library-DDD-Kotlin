package infrastructure.`persistence(InMemory)`

import domain.repository.BookRepository
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.book.entity.Book
import domain.repository.valueobject.*
import kotlin.math.ceil

class InMemoryBookRepository : BookRepository {
    private val books = mutableMapOf<ISBN, Book>()

    override fun get(isbn: ISBN): Book? {
        return books[isbn]
    }

    override fun save(book: Book) {
        books[book.isbn] = book
    }

    fun preload(vararg bookList: Book) {
        bookList.forEach { save(it) }
    }
//    override fun findAll(term: String?): List<Book> = books.values.toList()

    override fun find(pageable: Pageable): Page<Book> {
        val booksList = books.values.toList()

        val totalElements = booksList.size
        val totalPages = ceil(totalElements / pageable.pageSize.toFloat()).toInt()

        val fromIndex = (pageable.page - 1) * pageable.pageSize
        if (fromIndex >= totalElements) {
            return Page(
                page = pageable.page,
                pageSize = pageable.pageSize,
                elements = emptyList(),
                totalElements = totalElements,
                totalPages = totalPages
            )
        }

        val toIndex = minOf(fromIndex + pageable.pageSize, totalElements)
        val paginatedBooks = booksList.subList(fromIndex, toIndex)

        return Page(
            page = pageable.page,
            pageSize = pageable.pageSize,
            elements = paginatedBooks,
            totalElements = totalElements,
            totalPages = totalPages
        )
    }

}
