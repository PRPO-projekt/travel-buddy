package si.travelbuddy

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import si.travelbuddy.dto.poiDto


@Resource("/pois")
class poiResource (
    val name: String =".*"
) {

    @Resource("{id}")
    class Id(val parent : poiResource = poiResource(), val id: Int) {

    }

}
fun Route.pois(poiServiceHandle: poiService) {
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
