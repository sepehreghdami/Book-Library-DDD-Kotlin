package infrastructure.ktor.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookStockAdditionHttpResponse(
    val isbn: String,
    val addCount: Int
)