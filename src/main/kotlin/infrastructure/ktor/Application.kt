package infrastructure.ktor
import io.ktor.server.response.respondText

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import infrastructure.ktor.routes.*
import domain.repository.*

import domain.repository.BorrowingRepository
import infrastructure.persistence.InMemoryBookRepository
import infrastructure.persistence.InMemoryMemberRepository
import infrastructure.persistence.InMemoryBorrowingRepository

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    val bookRepository: BookRepository = InMemoryBookRepository()
    val memberRepository: MemberRepository = InMemoryMemberRepository()
    val borrowingRepository: BorrowingRepository = InMemoryBorrowingRepository()

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        bookRoutes(bookRepository)
        memberRoutes(memberRepository)
        borrowingRoutes(borrowingRepository, memberRepository, bookRepository)
    }
}
