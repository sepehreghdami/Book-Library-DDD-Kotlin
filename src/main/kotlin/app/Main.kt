package app

import domain.aggregate.book.entity.Book
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.member.entity.Member
import infrastructure.persistence.InMemoryMemberRepository
import infrastructure.persistence.InMemoryBookRepository
import infrastructure.persistence.InMemoryBorrowingRepository
import domain.service.BorrowingService

import java.time.Instant
import java.time.Duration

fun main() {

    val memberRepository = InMemoryMemberRepository()
    val bookRepository = InMemoryBookRepository()
    val borrowingRepository = InMemoryBorrowingRepository()

    val member1 = Member(id = "member1",
        name = "Sepehr",
        maxBorrowsAllowed = 5)
    memberRepository.save(member1)
    memberRepository.save(member1)

    memberRepository.getAll().forEach { println("👤 Member: ID=${it.id}, Name='${it.name}', MaxBorrows=${it.maxBorrowsAllowed}") }


    val isbn1 = ISBN("1111111111111")
    val book1 = Book(isbn = isbn1,
        title = "Implementation of Domain Driven Design",
        author = "Vernon",
        stock = 5)
    bookRepository.save(book1)
    val isbn2 = ISBN("2222222222222")
    val book2 = Book(
        isbn = isbn2,
        title = "Clean Code: A Handbook of Agile Software Craftsmanship",
        author = "Robert C. Martin",
        stock = 8
    )
    bookRepository.save(book2)
    val isbn3 = ISBN("3333333333333")
    val book3 = Book(
        isbn = isbn3,
        title = "Design Patterns: Elements of Reusable Object-Oriented Software",
        author = "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
        stock = 3
    )
    bookRepository.save(book3)
    val isbn4 = ISBN("4444444444444")
    val book4 = Book(
        isbn = isbn4,
        title = "The Pragmatic Programmer",
        author = "Andrew Hunt, David Thomas",
        stock = 6
    )
    bookRepository.save(book4)
    val isbn5 = ISBN("5555555555555")
    val book5 = Book(
        isbn = isbn5,
        title = "Refactoring: Improving the Design of Existing Code",
        author = "Martin Fowler",
        stock = 4
    )
    bookRepository.save(book5)
    val isbn6 = ISBN("6666666666666")
    val book6 = Book(
        isbn = isbn6,
        title = "Head First Design Patterns",
        author = "Eric Freeman, Elisabeth Robson",
        stock = 7
    )
    bookRepository.save(book6)

    bookRepository.getAll().forEach { println("📘 Book: ISBN=${it.isbn}, Title='${it.title}', Author='${it.author}', Stock=${it.stock}") }

    val borrowingService = BorrowingService( bookRepository, memberRepository, borrowingRepository)
    val tenDaysLater = Instant.now().plus(Duration.ofDays(10))
    borrowingService.borrowBook(memberId = "member1",
        isbn = isbn3,
        specifiedReturnTime = tenDaysLater
        )
    borrowingService.borrowBook(memberId = "member1",
        isbn = isbn3,
        specifiedReturnTime = tenDaysLater
    )

    borrowingRepository.getAll().forEach { println("🔄 Borrowing: ID=${it.id}, MemberID=${it.memberId}, ISBN=${it.isbn}, SpecifiedReturn=${it.specifiedReturnTime}, ActualReturn=${it.actualReturnTime}") }
}

