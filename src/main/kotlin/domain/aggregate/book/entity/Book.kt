package domain.aggregate.book.entity
import domain.aggregate.book.valueobject.ISBN

class Book(val isbn:ISBN, val title:String, val author:String, val stock:Int)