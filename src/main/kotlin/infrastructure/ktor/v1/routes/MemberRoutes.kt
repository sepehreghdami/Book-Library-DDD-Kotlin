package infrastructure.ktor.v1.routes


import domain.aggregate.member.entity.Member
import domain.aggregate.member.valueobject.MaxBorrowsAllowed
import domain.aggregate.member.valueobject.MemberId
import domain.aggregate.member.valueobject.MemberName
import domain.repository.MemberRepository
import domain.crosscutting.Page
import domain.crosscutting.Pageable
import domain.repository.TransactionManager
import domain.service.MemberService
import infrastructure.ktor.v1.httpresponses.MemberHttpResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*


fun Route.memberRoutes(memberRepository: MemberRepository,
                       transactionManager: TransactionManager) {
    route("/members"){
        val memberService = MemberService(memberRepository, transactionManager)
        get {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 10
            val term = call.request.queryParameters["term"]
//
            val memberPage = memberService.findMembers(page, size)

            val memberResponse = memberPage.elements.map {member -> MemberHttpResponse(
                id = member.id.value,
                name = member.name.value,
                maxBorrowsAllowed =  member.maxBorrowsAllowed.value
            )}

            call.respond(
                Page(
                    page = memberPage.page,
                    pageSize = memberPage.pageSize,
                    elements = memberResponse,
                    totalElements = memberPage.totalElements,
                    totalPages = memberPage.totalPages
                )
            )
        }
        get("/{id}") {
            val idParam = call.parameters["id"] ?: "1234"
            val member = memberService.getMember(memberId = MemberId(idParam))
            if (member == null){
                call.respond(HttpStatusCode.BadRequest, "Member with provided ID does not exist")
            } else {
                call.respond(MemberHttpResponse(
                    id = member.id.value,
                    name = member.name.value,
                    maxBorrowsAllowed = member.maxBorrowsAllowed.value
                ))
            }


        }
        post {
            val member = call.receive<MemberHttpResponse>()
            memberService.addMember(MemberId(member.id),
                MemberName(member.name),
                MaxBorrowsAllowed(member.maxBorrowsAllowed)
            )
            call.respond(HttpStatusCode.Created,member)
        }
    }
}