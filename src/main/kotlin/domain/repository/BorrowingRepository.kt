package domain.repository

import domain.aggregate.borrowing.entity.Borrowing
import domain.aggregate.book.valueobject.ISBN

interface BorrowingRepository {
    fun get(borrowingId: String): Borrowing?
    fun findByMemberId(memberId: String): List<Borrowing>
    fun findByIsbn(isbn: ISBN): List<Borrowing>
    fun save(borrowing: Borrowing)
}