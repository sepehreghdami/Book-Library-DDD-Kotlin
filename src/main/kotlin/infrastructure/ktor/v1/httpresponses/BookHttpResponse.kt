package infrastructure.ktor.v1.httpresponses

import kotlinx.serialization.Serializable

@Serializable
data class BookHttpResponse (val isbn: String,
                                  val title: String,
                                  val author: String,
                                  val stock: Int)
//@Serializable
//data class BookHttpResponse (val isbn: String,
//                    val title: String,
//                    val author: String,
//                    val stock: Int)