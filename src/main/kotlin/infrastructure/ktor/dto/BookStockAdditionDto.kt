package infrastructure.ktor.dto

import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.borrowing.valueobject.*
import domain.aggregate.member.valueobject.MemberId
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class BookStockAdditionDto(
    val isbn: String,
    val addCount: Int
)