package domain.repository
import domain.aggregate.member.entity.Member

interface MemberRepository {
    fun get(memberId: String): Member?

}