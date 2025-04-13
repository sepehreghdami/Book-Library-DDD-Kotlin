package infrastructure.ktor.dto

import domain.aggregate.book.valueobject.*
import kotlinx.serialization.Serializable

@Serializable
data class BookDto (val isbn: String,
                    val title: String,
                    val author: String,
                    val stock: Int)
//@Serializable
//data class BookHttpResponse (val isbn: String,
//                    val title: String,
//                    val author: String,
//                    val stock: Int)