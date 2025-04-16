package domain.repository

import domain.aggregate.borrowing.entity.Borrowing
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.borrowing.valueobject.BorrowingId
import domain.aggregate.member.valueobject.MemberId
import domain.crosscutting.Page
import domain.crosscutting.Pageable

interface BorrowingRepository {
    fun get(borrowingId: BorrowingId): Borrowing?
    fun get(memberId: MemberId, isbn :ISBN): Borrowing?
    fun findByMemberId(memberId: MemberId, pageable: Pageable): Page<Borrowing>
    fun findByIsbn(isbn: ISBN, pageable: Pageable): Page<Borrowing>
    fun save(borrowing: Borrowing)
//    fun getAll(): List<Borrowing>
    fun find(pageable: Pageable): Page<Borrowing>
}