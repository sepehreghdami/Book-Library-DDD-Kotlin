package domain.service
import LateFee
import domain.aggregate.book.entity.Book
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
import domain.crosscutting.Page
import domain.crosscutting.Pageable
import domain.repository.TransactionManager
import java.util.UUID

class BorrowingService(
    private val transactionManager: TransactionManager,
    private val bookRepository: BookRepository,
    private val memberRepository: MemberRepository,
    private val borrowingRepository: BorrowingRepository
) {
//    fun addBook(...,isbn: ISBN){
//        transactionManager.performInTransaction {
//           val book= bookRepository.get(isbn)
//            if (book==null){
//                bookRepository.save(newBook)
//            }
//            else{
//                throw Exception
//            }
//        }
//    }
    fun borrowBook(
        memberId: MemberId,
        isbn: ISBN,
        specifiedReturnTime: SpecifiedReturnTime
    ) {
    transactionManager.performInTransaction {
    val book = bookRepository.get(isbn)
        ?: throw IllegalArgumentException("Book with ISBN $isbn not found")

    val member = memberRepository.get(memberId)
        ?: throw IllegalArgumentException("Member with ID $memberId does not exist")

    val activeMemberBorrowings : Int = borrowingRepository.findByMemberId(memberId ,pageable = Pageable(1,1)).totalElements
    if (activeMemberBorrowings >= member.maxBorrowsAllowed.value) {
        throw IllegalStateException("Member has reached the borrow limit")
    }

    val hasMemberBorrowedBook = if (borrowingRepository.get(memberId, isbn) != null) true else false
    if (hasMemberBorrowedBook) {throw IllegalStateException("Member can not borrow a book more than once")}


    val activeBookBorrowings = borrowingRepository.findByIsbn(isbn, Pageable(1,1))
    val availableCount = book.stock.value - activeBookBorrowings.totalElements
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

    }}


    fun returnBook(borrowingId: BorrowingId) {
        transactionManager.performInTransaction {
            val borrowing = borrowingRepository.get(borrowingId)
                ?: throw IllegalStateException("No active borrowing found for this borrowing id")
            borrowing.returnBook()
            borrowingRepository.save(borrowing)
        }
    }

    fun findBorrowings(
        page: Int,
        size: Int
    ): Page<Borrowing> {
        return transactionManager.performInTransaction {
            val pageable = Pageable(page, size)
            borrowingRepository.find(pageable)

        }
    }
    }


