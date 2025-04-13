package infrastructure.ktor.dto
import domain.aggregate.member.valueobject.MaxBorrowsAllowed
import domain.aggregate.member.valueobject.MemberName
import kotlinx.serialization.Serializable


@Serializable
data class MemberDto(
    val id: String? =  null,
    val name: String,
    val maxBorrowsAllowed: Int
)