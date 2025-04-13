package infrastructure.ktor.v1.routes
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.member.valueobject.MemberId
import domain.aggregate.borrowing.valueobject.*
import domain.service.BorrowingService
import domain.repository.BorrowingRepository
import domain.repository.BookRepository
import domain.repository.MemberRepository
import infrastructure.ktor.v1.httpresponses.BorrowingHttpResponse
import java.time.Instant
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*




fun Route.borrowingRoutes(borrowingRepository: BorrowingRepository, memberRepository: MemberRepository, bookRepository: BookRepository) {
    route("/borrowings") {
        get("/all"){
            val borrowings = borrowingRepository.getAll()

            call.respond(borrowings.map { borrowing ->
                BorrowingHttpResponse(
                    borrowingId = borrowing.id.value,
                    isbn = borrowing.isbn.value,
                    memberId = borrowing.memberId.value,
                    createdOn = borrowing.createdOn.value.toString(),
                    actualReturnTime = borrowing.actualReturnTime?.value?.toString() ?: "none",
                    specifiedReturnTime = borrowing.specifiedReturnTime.toString()

                )
            })
        }
        post{
            val borrowing = call.receive<BorrowingHttpResponse>()
            val borrowingService = BorrowingService( bookRepository, memberRepository, borrowingRepository)
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
            val borrowingService = BorrowingService( bookRepository, memberRepository, borrowingRepository)
            borrowingService.returnBook(borrowingId = BorrowingId(borrowingIdParam))
            call.respond(HttpStatusCode.OK)
        }
    }

}
