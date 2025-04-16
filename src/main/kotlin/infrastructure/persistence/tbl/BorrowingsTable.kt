package infrastructure.persistence

import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.borrowing.entity.Borrowing
import domain.aggregate.borrowing.valueobject.BorrowDate
import domain.aggregate.borrowing.valueobject.SpecifiedReturnTime
import domain.aggregate.member.valueobject.MemberId
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import infrastructure.persistence.tbl.BooksTable
import org.jetbrains.exposed.sql.ResultRow


object BorrowingsTable: Table("Borrowings") {
    val borrowingId = varchar("borrowingId", length = 36).uniqueIndex()
    val createdOn = timestamp("createdOn")
    val memberId = varchar("memberId", length = 36).references(MembersTable.id)
    val isbn = varchar("isbn", length = 13).references(BooksTable.isbn)
    val specifiedReturnTime = timestamp("specifiedReturnTime")
    val actualReturnTime = timestamp("actualReturnTime").nullable()
    val borrowDate = timestamp("borrowDate")
    val lateFee = float("lateFee").nullable()

    fun toBorrowing(row: ResultRow): Borrowing {
        return Borrowing.makeNew(
            memberId = MemberId(row[BorrowingsTable.memberId]),
            isbn = ISBN(row[BorrowingsTable.isbn]),
            specifiedReturnTime = SpecifiedReturnTime(row[BorrowingsTable.specifiedReturnTime]),
            borrowDate = BorrowDate(row[BorrowingsTable.borrowDate])
        )
    }
}