package infrastructure.ktor.v1.routes

import kotlin.math.ceil

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import infrastructure.ktor.v1.httpresponses.BookHttpResponse
import domain.repository.BookRepository
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.book.valueobject.Stock
import domain.aggregate.book.valueobject.Title
import domain.aggregate.book.entity.Book
import domain.aggregate.book.valueobject.Author
import domain.repository.valueobject.*


fun Route.bookRoutes(bookRepository: BookRepository) {
    route("/books") {
        get {

            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 10
            val term = call.request.queryParameters["term"]

            val pageable = Pageable(
                page = page,
                pageSize = size
            )

            val bookPage = bookRepository.find(pageable)

            val bookResponses = bookPage.elements.map { book ->
                BookHttpResponse(
                    isbn = book.isbn.value,
                    title = book.title.value,
                    author = book.author.value,
                    stock = book.stock.value
                )
            }

            call.respond(
                Page(
                    page = bookPage.page,
                    pageSize = bookPage.pageSize,
                    elements = bookResponses,
                    totalElements = bookPage.totalElements,
                    totalPages = bookPage.totalPages
                )
            )
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
                BookHttpResponse(
                    isbn = book.isbn.value,
                    title = book.title.value,
                    author = book.author.value,
                    stock = book.stock.value
                )
            )
        }}

        post {
            try {
                val book = call.receive<BookHttpResponse>()
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
