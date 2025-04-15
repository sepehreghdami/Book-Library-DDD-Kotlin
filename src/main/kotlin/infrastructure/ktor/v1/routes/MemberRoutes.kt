package infrastructure.ktor.v1.routes


import domain.aggregate.member.entity.Member
import domain.aggregate.member.valueobject.MaxBorrowsAllowed
import domain.aggregate.member.valueobject.MemberName
import domain.repository.MemberRepository
import domain.repository.valueobject.Page
import domain.repository.valueobject.Pageable
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
            val term = call.request.queryParameters["term"]

            val pageable = Pageable(
                page = page,
                pageSize = size
            )

            val memberPage = memberRepository.find(pageable = pageable)
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