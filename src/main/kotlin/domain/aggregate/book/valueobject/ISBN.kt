package domain.aggregate.book.valueobject




data class ISBN(val value:String) {
 init {
     require(value.isNotBlank()){"ISBN could not be blank"}
     require(value.length == 13 && value.all{it.isDigit()}) {"ISBN must be 13 digits"}
 }
}