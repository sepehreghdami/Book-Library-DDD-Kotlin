package infrastructure.persistence

import domain.aggregate.book.entity.Book
import domain.aggregate.book.valueobject.Author
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.book.valueobject.Stock
import domain.aggregate.book.valueobject.Title
import infrastructure.persistence.tbl.BooksTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import domain.aggregate.member.entity.Member
import domain.aggregate.member.valueobject.MaxBorrowsAllowed
import domain.aggregate.member.valueobject.MemberId
import domain.aggregate.member.valueobject.MemberName

object MembersTable: Table("members") {
    val id = varchar("id",length = 36).uniqueIndex()
    val name = varchar("name", length = 100)
    val maxBorrowsAllowed = integer("maxBorrowsAllowed")

    override val primaryKey = PrimaryKey(id, name = "PK_MEMBER_ID")

    fun toMember(row: ResultRow): Member {
        return Member.makeNew(
            id = MemberId(row[MembersTable.id]),
            name = MemberName(row[MembersTable.name]),
            maxBorrowsAllowed = MaxBorrowsAllowed(row[MembersTable.maxBorrowsAllowed]),

        )
    }

}


