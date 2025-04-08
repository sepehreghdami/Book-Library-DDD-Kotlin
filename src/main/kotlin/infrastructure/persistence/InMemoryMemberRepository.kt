package infrastructure.persistence

import domain.repository.MemberRepository
import domain.aggregate.member.entity.Member
import domain.aggregate.member.valueobject.MemberId

class InMemoryMemberRepository : MemberRepository {
    private val members = mutableMapOf<MemberId, Member>()

    override fun get(memberId: MemberId): Member? {
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
