package domain.repository

import domain.aggregate.borrowing.entity.Borrowing
import domain.aggregate.book.valueobject.ISBN

interface BorrowingRepository {
    fun getById(memberId: String): List<Borrowing>
    fun getByIsbn(isbn: ISBN): List<Borrowing>
    fun save(borrowing: Borrowing)
}