package domain.aggregate.borrowing.entity

import LateFee
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.borrowing.valueobject.*
import domain.aggregate.member.valueobject.MemberId


import java.time.Instant
import java.util.UUID

class Borrowing private constructor() {

    lateinit var id: BorrowingId
        private set
    lateinit var createdOn: CreatedOn
        private set
    lateinit var memberId: MemberId
        private set
    lateinit var isbn: ISBN
        private set
    lateinit var specifiedReturnTime: SpecifiedReturnTime
        private set
    lateinit var borrowDate: BorrowDate
        private set
    var actualReturnTime: ActualReturnTime? = null
        private set
    var lateFee: LateFee? = null
        private set

    fun returnBook() {

        actualReturnTime = ActualReturnTime(Instant.now())
        val latePerDayRate: Double = 2.0
        val fee = calculateLateFee(latePerDayRate)
        if (fee > 0) {
            lateFee = LateFee(amount = fee, reason = "Late Return")
        }
    }

    companion object {
        fun makeNew(
            memberId: MemberId,
            isbn: ISBN,
            specifiedReturnTime: SpecifiedReturnTime,
            borrowDate: BorrowDate,
        ): Borrowing {
            return Borrowing().apply {
                this.id = BorrowingId(UUID.randomUUID().toString())
                this.memberId = memberId
                this.isbn = isbn
                this.specifiedReturnTime = specifiedReturnTime
                this.borrowDate = borrowDate
                createdOn = CreatedOn(Instant.now())
            }
        }
    }

    private fun calculateLateFee(perDayRate: Double): Double {
        val returnTime = actualReturnTime ?: return 0.0
        val daysLate = java.time.Duration.between(specifiedReturnTime.value, returnTime.value).toDays()
        return if (daysLate > 0) daysLate * perDayRate else 0.0
    }
}
