package domain.repository
import domain.aggregate.book.valueobject.ISBN
import domain.aggregate.member.entity.Member
import domain.aggregate.member.valueobject.MemberId
import domain.crosscutting.Page
import domain.crosscutting.Pageable
//import java.time.Instant
interface MemberRepository {
//    fun get(active: Boolean?=null,maxExpireDate:Instant?=null): Member?
    fun get(memberId: MemberId): Member?
    fun save(member: Member)
//    fun update(member: Member)
//    fun remove(memberId: MemberId)
//    fun getAll(): List<Member>
    fun find(pageable: Pageable): Page<Member>
}