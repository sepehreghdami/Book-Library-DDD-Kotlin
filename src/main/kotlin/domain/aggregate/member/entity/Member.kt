package domain.aggregate.member.entity

import domain.aggregate.member.valueobject.MaxBorrowsAllowed
import domain.aggregate.member.valueobject.Balance
import domain.aggregate.member.valueobject.MemberId
import domain.aggregate.member.valueobject.MemberName
import java.util.*

//class Member (val id:String, val name:String, val maxBorrowsAllowed:Int)

class Member private constructor() {
    lateinit var id:MemberId
    lateinit var name:MemberName
    lateinit var maxBorrowsAllowed: MaxBorrowsAllowed
    var balance: Balance? = null

    companion object{
        fun makeNew(
            name:MemberName,
            maxBorrowsAllowed: MaxBorrowsAllowed
        ): Member {
            return Member().apply {
                this.id = MemberId(UUID.randomUUID().toString())
                this.name = name
                this.maxBorrowsAllowed = maxBorrowsAllowed
            }
        }
    }
}