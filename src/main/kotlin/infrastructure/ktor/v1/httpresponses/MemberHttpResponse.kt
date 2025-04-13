package infrastructure.ktor.v1.httpresponses
import kotlinx.serialization.Serializable


@Serializable
data class MemberHttpResponse(
    val id: String? =  null,
    val name: String,
    val maxBorrowsAllowed: Int
)