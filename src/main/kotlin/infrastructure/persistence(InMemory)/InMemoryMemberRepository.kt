//package infrastructure.`persistence(InMemory)`
//
//import domain.repository.MemberRepository
//import domain.aggregate.member.entity.Member
//import domain.aggregate.member.valueobject.MemberId
//import domain.crosscutting.Page
//import domain.crosscutting.Pageable
//import kotlin.math.ceil
//
//class InMemoryMemberRepository : MemberRepository {
//    private val members = mutableMapOf<MemberId, Member>()
//
//    override fun get(memberId: MemberId): Member? {
//        return members[memberId]
//    }
//
//    override fun save(member: Member) {
//        members[member.id] = member
//    }
//
//    fun preload(vararg memberList: Member) {
//        memberList.forEach { save(it) }
//    }
//
//    override fun find(pageable: Pageable): Page<Member> {
//        val memberList = members.values.toList()
//
//        val totalElements = memberList.size
//        val totalPages = ceil(totalElements / pageable.pageSize.toFloat()).toInt()
//
//        val fromIndex = (pageable.page - 1) * pageable.pageSize
//        if (fromIndex >= totalElements) {
//            return Page(
//                page = pageable.page,
//                pageSize = pageable.pageSize,
//                elements = emptyList(),
//                totalElements = totalElements,
//                totalPages = totalPages
//            )
//        }
//
//        val toIndex = minOf(fromIndex + pageable.pageSize, totalElements)
//        val paginatedMember = memberList.subList(fromIndex, toIndex)
//
//        return Page(
//            page = pageable.page,
//            pageSize = pageable.pageSize,
//            elements = paginatedMember,
//            totalElements = totalElements,
//            totalPages = totalPages
//        )
//
//    }
////    override fun getAll(): List<Member> = members.values.toList()
//}
