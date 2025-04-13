package domain.repository
import domain.aggregate.member.entity.Member
import domain.aggregate.member.valueobject.MemberId

interface MemberRepository {
    fun get(memberId: MemberId): Member?
    fun save(member: Member)
    fun getAll(): List<Member>
}