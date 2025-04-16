
package infrastructure.persistence.tbl
import org.jetbrains.exposed.sql.Table
import domain.aggregate.book.valueobject.*
import domain.aggregate.book.entity.Book
import org.jetbrains.exposed.sql.ResultRow



object BooksTable : Table("books") {

    val isbn = varchar("isbn", length = 13).uniqueIndex()
    val title = varchar("title", length = 100)
    val author = varchar("author", length = 100)
    val stock = integer("stock")

    override val primaryKey = PrimaryKey(isbn, name = "PK_BOOK_ISBN")

    fun toBook(row: ResultRow): Book {
        return Book.makeNew(
            isbn = ISBN(row[BooksTable.isbn]),
            title = Title(row[BooksTable.title]),
            author = Author(row[BooksTable.author]),
            stock = Stock(row[BooksTable.stock])
        )
    }
    }
