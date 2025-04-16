package domain.aggregate.book.entity
import domain.aggregate.book.valueobject.Author
import domain.aggregate.book.valueobject.Stock
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.book.valueobject.Title

class Book private constructor(){
    lateinit var isbn: ISBN
    lateinit var  title: Title
    lateinit var  author: Author
    lateinit var stock: Stock

    companion object {
        fun makeNew(
            isbn: ISBN,
            title: Title,
            author: Author,
            stock: Stock
        ):Book {
            return Book().apply {
                this.isbn = isbn
                this.title = title
                this.author = author
                this.stock = stock

            }
        }
    }
}