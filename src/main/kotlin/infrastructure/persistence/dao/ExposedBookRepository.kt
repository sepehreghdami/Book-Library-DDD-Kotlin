
package infrastructure.persistence.dao
import domain.aggregate.book.entity.Book
import domain.aggregate.book.valueobject.ISBN
import domain.repository.BookRepository
import domain.repository.valueobject.Page
import domain.repository.valueobject.Pageable
import infrastructure.persistence.tbl.BooksTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.ceil


class ExposedBookRepository : BookRepository {

    override fun get(isbn: ISBN): Book? {
        return BooksTable.selectAll().apply {
            andWhere { BooksTable.isbn.eq(isbn.value) }
        }.map { BooksTable.toBook(it) }.singleOrNull()
    }

    override fun find(pageable: Pageable): Page<Book>  {
        val query = BooksTable.selectAll()

        val totalElements = query.count().toInt()
        val totalPages = if (pageable.pageSize > 0) ceil(totalElements / pageable.pageSize.toFloat()).toInt() else 0

        val pagedQuery = query.limit(pageable.pageSize, offset = ((pageable.page - 1) * pageable.pageSize).toLong())

        val booksList = pagedQuery.map { BooksTable.toBook(it) }
        val pagedResult = Page(
            page = pageable.page,
            pageSize = pageable.pageSize,
            elements = booksList,
            totalElements = totalElements,
            totalPages = totalPages
        )
        return pagedResult

    }

//    override fun save(book: Book) {
//        BooksTable.insert {
//            it[isbn] = book.isbn.value
//            it[author] = book.author.value
//            it[title] = book.title.value
//            it[stock] = book.stock.value
//        }
//    }

    override fun save(book: Book) {

            BooksTable.insert {
                it[isbn] = book.isbn.value
                it[author] = book.author.value
                it[title] = book.title.value
                it[stock] = book.stock.value

        }
    }

}