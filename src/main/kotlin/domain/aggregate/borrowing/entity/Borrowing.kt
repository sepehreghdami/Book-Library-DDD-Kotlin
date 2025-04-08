package domain.aggregate.borrowing.entity

import LateFee
import domain.aggregate.book.valueobject.ISBN


import java.time.Instant
import java.util.UUID

class Borrowing private constructor() {

    lateinit var id: String
        private set
    lateinit var createdOn: Instant
        private set
    lateinit var memberId: String
        private set
    lateinit var isbn: ISBN
        private set
    lateinit var specifiedReturnTime: Instant
        private set
    lateinit var borrowDate: Instant
        private set
    var actualReturnTime: Instant? = null
        private set
    var lateFee: LateFee? = null
        private set

    fun returnBook() {

        actualReturnTime = Instant.now()
        val latePerDayRate: Double = 2.0
        val fee = calculateLateFee(latePerDayRate)
        if (fee > 0) {
            lateFee = LateFee(amount = fee, reason = "Late Return")
        }
    }

    companion object {
        fun makeNew(
            memberId: String,
            isbn: ISBN,
            specifiedReturnTime: Instant,
            borrowDate: Instant,
        ): Borrowing {
            return Borrowing().apply {
                this.id = UUID.randomUUID().toString()
                this.memberId = memberId
                this.isbn = isbn
                this.specifiedReturnTime = specifiedReturnTime
                this.borrowDate = borrowDate
                createdOn = Instant.now()
            }
        }
    }

    private fun calculateLateFee(perDayRate: Double): Double {
        val daysLate = java.time.Duration.between(specifiedReturnTime, actualReturnTime).toDays()
        return if (daysLate > 0) daysLate * perDayRate else 0.0
    }
}
