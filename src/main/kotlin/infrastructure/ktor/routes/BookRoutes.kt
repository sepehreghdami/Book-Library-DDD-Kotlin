package infrastructure.ktor.routes


import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import infrastructure.ktor.dto.BookDto
import domain.repository.BookRepository
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.book.valueobject.Stock
import domain.aggregate.book.valueobject.Title
import domain.aggregate.book.entity.Book
import domain.aggregate.book.valueobject.Author

fun Route.bookRoutes(bookRepository: BookRepository) {
    route("/books") {
        get("/all") {


            val books = bookRepository.getAll()

            call.respond(books.map { book ->
                BookDto(
                    isbn = book.isbn.value,
                    title = book.title.value,
                    author = book.author.value,
                    stock = book.stock.value
                )
            })
        }
        get("/{isbn}") {
            val isbnParam = call.parameters["isbn"]
            if (isbnParam.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ISBN must be provided"))
                return@get
            }
            val book = bookRepository.get(isbn = ISBN(isbnParam))
            if (isbnParam.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ISBN must be provided"))
                return@get
            }

            if (book == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Book not found"))
            } else {
            call.respond(
                BookDto(
                    isbn = book.isbn.value,
                    title = book.title.value,
                    author = book.author.value,
                    stock = book.stock.value
                )
            )
        }}

        post {
            try {
                val book = call.receive<BookDto>()
                bookRepository.save(
                    Book(
                        isbn = ISBN(book.isbn),
                        author = Author(book.author),
                        title = Title(book.title),
                        stock = Stock(book.stock)
                    )
                )

                call.respond(HttpStatusCode.Created, book)
            } catch (ex: IllegalArgumentException) {HttpStatusCode.BadRequest}
        }
//        put("/{isbn}/stock") {
//        }
//        post("/AddBookStock") {
//
//        }
    }
}
