package infrastructure.persistence.dao

import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.borrowing.entity.Borrowing
import domain.aggregate.borrowing.valueobject.BorrowingId
import domain.aggregate.member.valueobject.MemberId
import domain.crosscutting.Page
import domain.crosscutting.Pageable
import domain.repository.BorrowingRepository
import infrastructure.persistence.BorrowingsTable
import infrastructure.persistence.MembersTable
import infrastructure.persistence.tbl.BooksTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.selectAll
import kotlin.math.ceil

class ExposedBorrowingRepository: BorrowingRepository {

    override fun save(borrowing: Borrowing){
        BorrowingsTable.insert {
            it[borrowingId] = borrowing.id.value
            it[createdOn] = borrowing.createdOn.value
            it[memberId] = borrowing.memberId.value
            it[isbn] = borrowing.isbn.value
            it[specifiedReturnTime] = borrowing.specifiedReturnTime.value
            it[actualReturnTime] = borrowing.actualReturnTime?.value
            it[borrowDate] = borrowing.borrowDate.value
            it[lateFee] = borrowing.lateFee?.amount?.toFloat()
        }
    }


    override fun find(pageable: Pageable): Page<Borrowing> {
        val query = BorrowingsTable.selectAll()

        val totalElements = query.count().toInt()
        val totalPages = if (pageable.pageSize > 0) ceil(totalElements / pageable.pageSize.toFloat()).toInt() else 0

        val pagedQuery = query.limit(pageable.pageSize, offset = ((pageable.page - 1) * pageable.pageSize).toLong())

        val borrowingsList = pagedQuery.map { BorrowingsTable.toBorrowing(it) }
        val pagedResult = Page(
            page = pageable.page,
            pageSize = pageable.pageSize,
            elements = borrowingsList,
            totalElements = totalElements,
            totalPages = totalPages
        )
        return pagedResult
    }

    override fun get(borrowingId: BorrowingId): Borrowing? {
        return BorrowingsTable.selectAll().apply {
            andWhere { BorrowingsTable.borrowingId.eq(borrowingId.value) }
        }.map { BorrowingsTable.toBorrowing(it) }.singleOrNull()
    }

    override fun get(memberId: MemberId, isbn: ISBN): Borrowing? {
        return BorrowingsTable.selectAll().apply{
            andWhere { BorrowingsTable.memberId.eq(memberId.value)}.
            andWhere { BorrowingsTable.isbn.eq(isbn.value) }
        }.map {BorrowingsTable.toBorrowing(it)}.singleOrNull()
    }

    override fun findByIsbn(isbn: ISBN,pageable: Pageable): Page<Borrowing> {
        val query = BorrowingsTable.selectAll().apply {
            andWhere { BorrowingsTable.isbn.eq(isbn.value) }
        }

        val totalElements = query.count().toInt()
        val totalPages = if (pageable.pageSize > 0) ceil(totalElements / pageable.pageSize.toFloat()).toInt() else 0

        val pagedQuery = query.limit(pageable.pageSize, offset = ((pageable.page - 1) * pageable.pageSize).toLong())

        val borrowingsList = pagedQuery.map { BorrowingsTable.toBorrowing(it) }
        val pagedResult = Page(
            page = pageable.page,
            pageSize = pageable.pageSize,
            elements = borrowingsList,
            totalElements = totalElements,
            totalPages = totalPages
        )
        return pagedResult

    }

    override fun findByMemberId(memberId: MemberId, pageable: Pageable): Page<Borrowing> {

        val query = BorrowingsTable.selectAll().apply {
            andWhere { BorrowingsTable.memberId.eq(memberId.value) }
        }

        val totalElements = query.count().toInt()
        val totalPages = if (pageable.pageSize > 0) ceil(totalElements / pageable.pageSize.toFloat()).toInt() else 0

        val pagedQuery = query.limit(pageable.pageSize, offset = ((pageable.page - 1) * pageable.pageSize).toLong())

        val borrowingsList = pagedQuery.map { BorrowingsTable.toBorrowing(it) }
        val pagedResult = Page(
            page = pageable.page,
            pageSize = pageable.pageSize,
            elements = borrowingsList,
            totalElements = totalElements,
            totalPages = totalPages
        )
        return pagedResult

    }
}

