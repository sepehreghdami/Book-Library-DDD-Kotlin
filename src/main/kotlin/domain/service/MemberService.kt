package domain.service

import domain.aggregate.member.entity.Member
import domain.aggregate.member.valueobject.MaxBorrowsAllowed
import domain.aggregate.member.valueobject.MemberId
import domain.aggregate.member.valueobject.MemberName
import domain.repository.MemberRepository
import domain.repository.TransactionManager
import domain.crosscutting.Page
import domain.crosscutting.Pageable

class MemberService (private val memberRepository: MemberRepository,
    private val transactionManager: TransactionManager) {

    fun addMember(id: MemberId,
                  name: MemberName,
                  maxBorrowsAllowed: MaxBorrowsAllowed) {
        return transactionManager.performInTransaction {
            val member = memberRepository.get(memberId = id)
            if (member == null) {
                memberRepository.save(
                    Member.makeNew(
                        id = id,
                        name = name,
                        maxBorrowsAllowed = maxBorrowsAllowed
                    )
                )
            } else {
                throw IllegalArgumentException("This member already Exists")

            }
        }
    }
    fun findMembers(page: Int,
                    size: Int):Page<Member>{
        return transactionManager.performInTransaction {
            memberRepository.find(pageable = Pageable(page,size))
        }


    }
    fun getMember(memberId : MemberId): Member? {
        return transactionManager.performInTransaction {
            memberRepository.get(memberId = memberId)
        }
    }
}