    package infrastructure.ktor.dto

    import domain.aggregate.book.valueobject.ISBN
    import domain.aggregate.borrowing.valueobject.*
    import domain.aggregate.member.valueobject.MemberId
    import kotlinx.serialization.Serializable
    import java.time.Instant

    @Serializable
    data class BorrowingDto (
        val borrowingId: String? = null,
        val specifiedReturnTime: String? = null,
        val actualReturnTime: String? = null,
        val createdOn: String? = null,
        val memberId: String,
        val isbn: String,

    )