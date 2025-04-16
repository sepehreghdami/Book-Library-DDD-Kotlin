package infrastructure.persistence.dao

import domain.aggregate.book.entity.Book
import domain.aggregate.member.entity.Member
import domain.aggregate.member.valueobject.MaxBorrowsAllowed
import domain.aggregate.member.valueobject.MemberId
import domain.aggregate.member.valueobject.MemberName
import domain.crosscutting.Page
import domain.crosscutting.Pageable
import domain.repository.MemberRepository
import infrastructure.persistence.MembersTable
import infrastructure.persistence.tbl.BooksTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import kotlin.math.ceil

class ExposedMemberRepository: MemberRepository {

    override fun get(memberId: MemberId): Member? {
        return MembersTable.selectAll().apply {
            andWhere { MembersTable.id.eq(memberId.value) }
        }.map { MembersTable.toMember(it) }.singleOrNull()
    }

    override fun save(member:Member){
        MembersTable.insert {
            it[id] = member.id.value
            it[name] = member.name.value
            it[maxBorrowsAllowed] = member.maxBorrowsAllowed.value
        }

    }

    override fun find(pageable: Pageable): Page<Member> {
        val query = MembersTable.selectAll()

        val totalElements = query.count().toInt()
        val totalPages = if (pageable.pageSize > 0) ceil(totalElements / pageable.pageSize.toFloat()).toInt() else 0

        val pagedQuery = query.limit(pageable.pageSize, offset = ((pageable.page - 1) * pageable.pageSize).toLong())

        val memberList = pagedQuery.map { MembersTable.toMember(it) }
        val pagedResult = Page(
            page = pageable.page,
            pageSize = pageable.pageSize,
            elements = memberList,
            totalElements = totalElements,
            totalPages = totalPages
        )
        return pagedResult

    }
}