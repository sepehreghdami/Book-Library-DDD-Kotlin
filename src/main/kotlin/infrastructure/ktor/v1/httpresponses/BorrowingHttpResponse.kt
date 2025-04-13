    package infrastructure.ktor.v1.httpresponses

    import kotlinx.serialization.Serializable

    @Serializable
    data class BorrowingHttpResponse (
        val borrowingId: String? = null,
        val specifiedReturnTime: String? = null,
        val actualReturnTime: String? = null,
        val createdOn: String? = null,
        val memberId: String,
        val isbn: String,

    )