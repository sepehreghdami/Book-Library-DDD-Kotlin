package domain.service
import LateFee
import domain.aggregate.book.valueobject.ISBN
import domain.repository.BookRepository
import domain.repository.MemberRepository
import domain.repository.BorrowingRepository
import domain.aggregate.borrowing.entity.Borrowing
import domain.aggregate.borrowing.valueobject.SpecifiedReturnTime
import java.time.Instant
import domain.aggregate.borrowing.valueobject.BorrowDate
import domain.aggregate.borrowing.valueobject.BorrowingId
import domain.aggregate.member.valueobject.MemberId
import java.util.UUID

class BorrowingService(
    private val bookRepository: BookRepository,
    private val memberRepository: MemberRepository,
    private val borrowingRepository: BorrowingRepository
) {

    fun borrowBook(
        memberId: MemberId,
        isbn: ISBN,
        specifiedReturnTime: SpecifiedReturnTime
    ): Borrowing {

        val book = bookRepository.get(isbn)
            ?: throw IllegalArgumentException("Book with ISBN $isbn not found")

        val member = memberRepository.get(memberId)
            ?: throw IllegalArgumentException("Member with ID $memberId does not exist")

        val activeMemberBorrowings = borrowingRepository.findByMemberId(memberId)
        if (activeMemberBorrowings.size >= member.maxBorrowsAllowed.value) {
            throw IllegalStateException("Member has reached the borrow limit")
        }

        if (activeMemberBorrowings.any { it.isbn == isbn }) {
            throw IllegalStateException("Member has already borrowed a copy of this book and has not returned it")
        }

        val activeBookBorrowings = borrowingRepository.findByIsbn(isbn)
        val availableCount = book.stock.value - activeBookBorrowings.size
        if (availableCount <= 0) {
            throw IllegalStateException("No copies of book '${book.title}' available")
        }


        val borrowDate = BorrowDate(Instant.now())
        val borrowing = Borrowing.makeNew(
            memberId = memberId,
            isbn = isbn,
            specifiedReturnTime = specifiedReturnTime,
            borrowDate = borrowDate
        )

        borrowingRepository.save(borrowing)
        return borrowing
    }


    fun returnBook(borrowingId: BorrowingId) {
        val borrowing = borrowingRepository.get(borrowingId) ?: throw IllegalStateException("No active borrowing found for this borrowing id")
        borrowing.returnBook()
        borrowingRepository.save(borrowing)
    }
    }


