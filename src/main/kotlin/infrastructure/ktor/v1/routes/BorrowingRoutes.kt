package infrastructure.ktor.v1.routes
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.member.valueobject.MemberId
import domain.aggregate.borrowing.valueobject.*
import domain.service.BorrowingService
import domain.repository.BorrowingRepository
import domain.repository.BookRepository
import domain.repository.MemberRepository
import domain.repository.TransactionManager
import domain.crosscutting.Page
import domain.crosscutting.Pageable
import infrastructure.ktor.v1.httpresponses.BorrowingHttpResponse
import java.time.Instant
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*


fun Route.borrowingRoutes(borrowingRepository: BorrowingRepository,
                          memberRepository: MemberRepository,
                          bookRepository: BookRepository,
                          transactionManager: TransactionManager ) {
    route("/borrowings") {
        val borrowingService = BorrowingService(transactionManager, bookRepository, memberRepository, borrowingRepository)
        get{
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 10
            val term = call.request.queryParameters["term"]

            val borrowingPage = borrowingService.findBorrowings(page, size)
            val borrowingResponse = borrowingPage.elements.map {borrowing -> BorrowingHttpResponse(
                borrowingId = borrowing.id.value,
                specifiedReturnTime = borrowing.specifiedReturnTime.value.toString(),
                memberId = borrowing.memberId.value,
                isbn = borrowing.isbn.value

            )
            }

            call.respond(
                Page(
                    page = borrowingPage.page,
                    pageSize = borrowingPage.pageSize,
                    elements = borrowingResponse,
                    totalElements = borrowingPage.totalElements,
                    totalPages = borrowingPage.totalPages
                )
            )
            }

        post{
            val borrowing = call.receive<BorrowingHttpResponse>()
            val tenDaysLater = Instant.now().plus(Duration.ofDays(10))

            borrowingService.borrowBook(memberId = MemberId(borrowing.memberId),
            isbn = ISBN(borrowing.isbn),
            specifiedReturnTime = SpecifiedReturnTime(tenDaysLater)
            )
            call.respond(HttpStatusCode.Created,borrowing)

        }
        post("/{borrowingId}/return") {
            val borrowingIdParam = call.parameters["borrowingId"]
            if (borrowingIdParam.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "borrowing id must be provided"))
                return@post
            }
            borrowingService.returnBook(borrowingId = BorrowingId(borrowingIdParam))
            call.respond(HttpStatusCode.OK)
        }
    }}


