package infrastructure.ktor.v1.httpresponses
import kotlinx.serialization.Serializable

@Serializable
data class Page<T>(val page: Int,
                   val pageSize:Int,
                   val elements:List<T>,
                   val totalElements: Int,
                   val totalPages: Int)