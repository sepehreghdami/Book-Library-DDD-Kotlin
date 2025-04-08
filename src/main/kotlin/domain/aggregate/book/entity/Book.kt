package domain.aggregate.book.entity
import domain.aggregate.book.valueobject.Author
import domain.aggregate.book.valueobject.Stock
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.book.valueobject.Title

class Book(val isbn:ISBN, val title:Title, val author:Author, val stock:Stock)