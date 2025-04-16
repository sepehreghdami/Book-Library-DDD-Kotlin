package infrastructure.ktor
import io.ktor.server.response.respondText

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import infrastructure.ktor.v1.routes.*
import domain.repository.*

import domain.repository.BorrowingRepository
import infrastructure.persistence.dao.ExposedBookRepository
import infrastructure.`persistence(InMemory)`.InMemoryMemberRepository
import infrastructure.`persistence(InMemory)`.InMemoryBorrowingRepository
import infrastructure.persistence.DatabaseFactory
import infrastructure.persistence.dao.ExposedTransactionManager
import infrastructure.persistence.tbl.BooksTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils
fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    DatabaseFactory.init()

    transaction {
        SchemaUtils.create(BooksTable)
    }
    val transactionManager: TransactionManager = ExposedTransactionManager()
    val bookRepository: BookRepository = ExposedBookRepository()
    val memberRepository: MemberRepository = InMemoryMemberRepository()
    val borrowingRepository: BorrowingRepository = InMemoryBorrowingRepository()

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("/v1/library") {
            bookRoutes(bookRepository, transactionManager)
            memberRoutes(memberRepository)
            borrowingRoutes(borrowingRepository, memberRepository, bookRepository, transactionManager)
        }
    }

}
