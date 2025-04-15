package infrastructure.persistence
import domain.repository.BorrowingRepository
import domain.aggregate.borrowing.entity.Borrowing
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.borrowing.valueobject.BorrowingId
import domain.aggregate.member.entity.Member
import domain.aggregate.member.valueobject.MemberId
import domain.repository.valueobject.Page
import domain.repository.valueobject.Pageable
import kotlin.math.ceil


class InMemoryBorrowingRepository : BorrowingRepository {
    private val borrowings = mutableListOf<Borrowing>()



    override fun findByMemberId(memberId: MemberId): List<Borrowing> {
        return borrowings.filter { it.memberId == memberId && it.actualReturnTime == null }
    }

    override fun findByIsbn(isbn: ISBN): List<Borrowing> {
        return borrowings.filter { it.isbn == isbn && it.actualReturnTime == null }
    }

    override fun get(borrowingId: BorrowingId): Borrowing? {
        return borrowings.find { it.id == borrowingId }
    }


    override fun save(borrowing: Borrowing) {
        borrowings.removeIf { it.id == borrowing.id }
        borrowings.add(borrowing)
    }

    fun preload(vararg borrowingList: Borrowing) {
        borrowingList.forEach { save(it) }
    }
//    override fun getAll(): List<Borrowing> = borrowings.toList()

    override fun find(pageable: Pageable): Page<Borrowing> {
        val borrowingList = borrowings

        val totalElements = borrowingList.size
        val totalPages = ceil(totalElements / pageable.pageSize.toFloat()).toInt()

        val fromIndex = (pageable.page - 1) * pageable.pageSize
        if (fromIndex >= totalElements) {
            return Page(
                page = pageable.page,
                pageSize = pageable.pageSize,
                elements = emptyList(),
                totalElements = totalElements,
                totalPages = totalPages
            )
        }

        val toIndex = minOf(fromIndex + pageable.pageSize, totalElements)
        val paginatedBorrowing = borrowingList.subList(fromIndex, toIndex)

        return Page(
            page = pageable.page,
            pageSize = pageable.pageSize,
            elements = paginatedBorrowing,
            totalElements = totalElements,
            totalPages = totalPages
        )

    }
}
