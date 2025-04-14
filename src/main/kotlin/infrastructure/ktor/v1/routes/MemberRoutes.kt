package infrastructure.ktor.v1.routes


import domain.aggregate.member.entity.Member
import domain.aggregate.member.valueobject.MaxBorrowsAllowed
import domain.aggregate.member.valueobject.MemberName
import domain.repository.MemberRepository
import infrastructure.ktor.v1.httpresponses.MemberHttpResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*


fun Route.memberRoutes(memberRepository: MemberRepository) {
    route("/members"){
        get {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 10


            val members = memberRepository.getAll()
            val totalElements = members.size

            call.respond(members.map { member ->
                MemberHttpResponse(
                    id = member.id.value,
                    name = member.name.value,
                    maxBorrowsAllowed = member.maxBorrowsAllowed.value

                )
            })
        }
        post {
            val member = call.receive<MemberHttpResponse>()
            memberRepository.save(
                Member.makeNew(name = MemberName(member.name),
                                maxBorrowsAllowed = MaxBorrowsAllowed(member.maxBorrowsAllowed)
                )
            )
            call.respond(HttpStatusCode.Created,member)
        }
    }
}