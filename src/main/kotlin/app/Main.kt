//package app
//
//
    //import domain.aggregate.book.entity.Book
//import domain.aggregate.book.valueobject.ISBN
//import domain.aggregate.book.valueobject.Title
//import domain.aggregate.member.entity.Member
//import domain.aggregate.book.valueobject.Author
//import domain.aggregate.book.valueobject.Stock
//import domain.aggregate.borrowing.valueobject.SpecifiedReturnTime
//import domain.aggregate.member.valueobject.MemberId
//import domain.aggregate.member.valueobject.MemberName
//import infrastructure.persistence.InMemoryMemberRepository
//import infrastructure.persistence.InMemoryBookRepository
//import infrastructure.persistence.InMemoryBorrowingRepository
//import domain.service.BorrowingService
//import domain.aggregate.member.valueobject.MaxBorrowsAllowed
//
//import java.time.Instant
//import java.time.Duration
//
//fun main() {
//
//    val memberRepository = InMemoryMemberRepository()
//    val bookRepository = InMemoryBookRepository()
//    val borrowingRepository = InMemoryBorrowingRepository()
//
//    val member1 = Member.makeNew(
//        name = MemberName("Sepehr"),
//        maxBorrowsAllowed = MaxBorrowsAllowed(3)
//    )
//    memberRepository.save(member1)
//
//    memberRepository.getAll().forEach { println("👤 Member: ID=${it.id}, Name='${it.name}', MaxBorrows=${it.maxBorrowsAllowed}") }
//
//
//    val isbn1 = ISBN("1111111111111")
//    val book1 = Book(isbn = isbn1,
//        title = Title("black swan"),
//        author = Author("N.N.T"),
//        stock = Stock(5))
//    bookRepository.save(book1)
//
//
//    bookRepository.getAll().forEach { println("📘 Book: ISBN=${it.isbn}, Title='${it.title}', Author='${it.author}', Stock=${it.stock}") }
//
//    val borrowingService = BorrowingService( bookRepository, memberRepository, borrowingRepository)
//    val tenDaysLater = Instant.now().plus(Duration.ofDays(10))
////    val tenSecondsLater = Instant.now().plus(Duration.ofSeconds(10))
//
//    val borrowing1 = borrowingService.borrowBook(memberId = member1.id,
//        isbn = isbn1,
//        specifiedReturnTime = SpecifiedReturnTime(tenDaysLater)
//        )
//    borrowingRepository.getAll().forEach { println("🔄 Borrowing: ID=${it.id}, MemberID=${it.memberId}, ISBN=${it.isbn}, SpecifiedReturn=${it.specifiedReturnTime}, ActualReturn=${it.actualReturnTime}, LateFee=${it.lateFee}")}
//
////    borrowingService.borrowBook(memberId = "member1",
////        isbn = isbn2,
////        specifiedReturnTime = tenDaysLater
////    )
//    borrowingService.returnBook(borrowingId = borrowing1.id)
//    borrowingRepository.getAll().forEach { println("🔄 Borrowing: ID=${it.id}, MemberID=${it.memberId}, ISBN=${it.isbn}, SpecifiedReturn=${it.specifiedReturnTime}, ActualReturn=${it.actualReturnTime}, LateFee=${it.lateFee}")}
//
////    borrowingRepository.getAll().forEach { println("🔄 Borrowing: ID=${it.id}, MemberID=${it.memberId}, ISBN=${it.isbn}, SpecifiedReturn=${it.specifiedReturnTime}, ActualReturn=${it.actualReturnTime}, LateFee=${it.lateFee}")
////    borrowingService.returnBook(borrowingId = "1e22ceb6-e4d8-46fd-8cd4-73431ae2a0ee")
//
//        }
//
//
//
//
package app

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import infrastructure.ktor.module
import io.ktor.server.application.*


fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module)
        .start(wait = true)
}
