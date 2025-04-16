package domain.crosscutting


data class Pageable(val page: Int,
                   val pageSize:Int,
                    val sortedBy:String? = null)
