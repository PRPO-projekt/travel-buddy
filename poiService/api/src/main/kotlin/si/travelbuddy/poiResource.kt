package si.travelbuddy

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.poiDto
import si.travelbuddy.entity.poiDao


@Resource("/pois")
class poiResource () {
    @Resource("{id}")
    class Id(val parent : poiResource = poiResource(), val id: Int) {
    }
}
fun Route.pois(poiServiceHandle: poiService) {
    get("/pois"){
        call.respond(transaction {
            poiDao.all().toList()
        }.map{ dao -> dao.toModel() })
    }

    get<poiResource.Id> { poiId ->
        val poi = poiServiceHandle.getById(poiId.id)
        if (poi != null) {
            call.respond(poi)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
    delete<poiResource.Id> { poiId ->
        val poi = poiServiceHandle.getById(poiId.id)
    }
    post("/pois") {
        val poiDto = call.receive<poiDto>()
        poiServiceHandle.create(poiDto)
        call.respond(HttpStatusCode.Created)
    }

}
