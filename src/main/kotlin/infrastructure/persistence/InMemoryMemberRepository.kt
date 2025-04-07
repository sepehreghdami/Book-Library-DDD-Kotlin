package infrastructure.persistence

import domain.repository.MemberRepository
import domain.aggregate.member.entity.Member
class InMemoryMemberRepository : MemberRepository {
    private val members = mutableMapOf<String, Member>()

    override fun get(memberId: String): Member? {
        return members[memberId]
    }

    fun save(member: Member) {
        members[member.id] = member
    }

    fun preload(vararg memberList: Member) {
        memberList.forEach { save(it) }
    }
    fun getAll(): List<Member> = members.values.toList()
}
