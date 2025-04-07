package infrastructure.persistence
import domain.repository.BorrowingRepository
import domain.aggregate.borrowing.entity.Borrowing
import domain.aggregate.book.valueobject.ISBN


class InMemoryBorrowingRepository : BorrowingRepository {
    private val borrowings = mutableListOf<Borrowing>()

    override fun getById(memberId: String): List<Borrowing> {
        return borrowings.filter { it.memberId == memberId && it.actualReturnTime == null }
    }

    override fun getByIsbn(isbn: ISBN): List<Borrowing> {
        return borrowings.filter { it.isbn == isbn && it.actualReturnTime == null }
    }

    override fun save(borrowing: Borrowing) {
        borrowings.removeIf { it.id == borrowing.id }
        borrowings.add(borrowing)
    }

    fun preload(vararg borrowingList: Borrowing) {
        borrowingList.forEach { save(it) }
    }
    fun getAll(): List<Borrowing> = borrowings.toList()

}
