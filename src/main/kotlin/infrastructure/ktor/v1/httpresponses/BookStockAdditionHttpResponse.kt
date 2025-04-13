package infrastructure.ktor.v1.httpresponses

import kotlinx.serialization.Serializable

@Serializable
data class BookStockAdditionHttpResponse(
    val isbn: String,
    val addCount: Int
)