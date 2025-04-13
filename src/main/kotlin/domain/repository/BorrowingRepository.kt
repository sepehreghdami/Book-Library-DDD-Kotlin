package domain.repository

import domain.aggregate.borrowing.entity.Borrowing
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.borrowing.valueobject.BorrowingId
import domain.aggregate.member.valueobject.MemberId

interface BorrowingRepository {
    fun get(borrowingId: BorrowingId): Borrowing?
    fun findByMemberId(memberId: MemberId): List<Borrowing>
    fun findByIsbn(isbn: ISBN): List<Borrowing>
    fun save(borrowing: Borrowing)
    fun getAll(): List<Borrowing>
}