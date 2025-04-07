package domain.service
import domain.aggregate.book.valueobject.ISBN
import domain.repository.BookRepository
import domain.repository.MemberRepository
import domain.repository.BorrowingRepository
import domain.aggregate.borrowing.entity.Borrowing
import java.time.Instant
import java.util.UUID

class BorrowingService(
    private val bookRepository: BookRepository,
    private val memberRepository: MemberRepository,
    private val borrowingRepository: BorrowingRepository
) {

    fun borrowBook(
        memberId: String,
        isbn: ISBN,
        specifiedReturnTime: Instant
    ): Borrowing {

        val book = bookRepository.get(isbn)
            ?: throw IllegalArgumentException("Book with ISBN $isbn not found")

        val member = memberRepository.get(memberId)
            ?: throw IllegalArgumentException("Member with ID $memberId does not exist")

        val activeMemberBorrowings = borrowingRepository.getById(memberId)
        if (activeMemberBorrowings.size >= member.maxBorrowsAllowed) {
            throw IllegalStateException("Member has reached the borrow limit")
        }

        if (activeMemberBorrowings.any { it.isbn == isbn }) {
            throw IllegalStateException("Member has already borrowed a copy of this book and has not returned it")
        }

        val activeBookBorrowings = borrowingRepository.getByIsbn(isbn)
        val availableCount = book.stock - activeBookBorrowings.size
        if (availableCount <= 0) {
            throw IllegalStateException("No copies of book '${book.title}' available")
        }

        val borrowId = UUID.randomUUID().toString()

        val borrowDate = Instant.now()
        val borrowing = Borrowing(
            id = borrowId,
            memberId = memberId,
            isbn = isbn,
            specifiedReturnTime = specifiedReturnTime,
            actualReturnTime = null,
            borrowDate = borrowDate
        )

        borrowingRepository.save(borrowing)

        return borrowing
    }


    fun returnBook(memberId: String, isbn: ISBN) {
        val borrowings = borrowingRepository.getById(memberId)
            .filter { it.isbn == isbn && it.actualReturnTime == null }

        val borrowing = borrowings.firstOrNull()
            ?: throw IllegalStateException("No active borrowing found for member $memberId and ISBN $isbn")

        borrowing.actualReturnTime = Instant.now()
        borrowingRepository.save(borrowing)
    }

    }


