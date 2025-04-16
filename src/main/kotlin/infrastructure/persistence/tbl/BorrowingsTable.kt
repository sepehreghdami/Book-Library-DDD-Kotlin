package infrastructure.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import infrastructure.persistence.tbl.BooksTable



object BorrowingsTable: Table("Borrowings") {
    val borrowingId = varchar("borrowingId", length = 36).uniqueIndex()
    val createdOn = timestamp("createdOn")
    val memberId = varchar("memberId", length = 36).references(MembersTable.id)
    val isbn = varchar("isbn", length = 13).references(BooksTable.isbn)
    val specifiedReturnTime = timestamp("specifiedReturnTime")
    val actualReturnTime = timestamp("actualReturnTime")
    val borrowDate = timestamp("borrowDate")
    val lateFee = varchar("lateFee", length = 100)
}