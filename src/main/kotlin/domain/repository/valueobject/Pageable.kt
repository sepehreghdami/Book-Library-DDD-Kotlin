package domain.repository.valueobject


data class Pageable(val page: Int,
                   val pageSize:Int,
                    val sortedBy:String? = null)
