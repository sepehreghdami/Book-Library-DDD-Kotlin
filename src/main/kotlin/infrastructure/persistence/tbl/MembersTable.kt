package infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object MembersTable: Table("members") {
    val id = varchar("id",length = 36).uniqueIndex()
    val name = varchar("name", length = 100)
    val maxBorrowsAllowed = integer("maxBorrowsAllowed")

    override val primaryKey = PrimaryKey(id, name = "PK_MEMBER_ID")
}
