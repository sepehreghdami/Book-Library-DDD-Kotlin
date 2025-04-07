package domain.aggregate.borrowing.entity
import domain.aggregate.book.valueobject.ISBN


import java.time.Instant

class Borrowing(
    val id: String,
    val memberId: String,
    val isbn: ISBN,
    val specifiedReturnTime: Instant,
    val borrowDate:Instant,
    var actualReturnTime: Instant? = null
)
