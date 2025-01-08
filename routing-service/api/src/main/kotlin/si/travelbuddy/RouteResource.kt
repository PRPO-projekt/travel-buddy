package si.travelbuddy

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.routing.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import java.time.LocalDateTime
import java.time.LocalTime

@Resource("/route")
class RouteResource(
    /* Departing station id */
    val fromId: String,
    /* Arriving station id */
    val toId: String,
    /* Departure time */
    val depTime: String
) {

}

@Serializable
data class Stop(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double
)

const val ncupApiEndpoint = "https://www.ncup.si/dc/ojp.webapi/api/getItineraries"
private val client = OkHttpClient()

fun findStopCoord(id: String): Pair<Double, Double> {
    val timetableMicroservice = "http://localhost:8080"

    val request = Request.Builder()
        .url(timetableMicroservice + "/stops/${id}")
        .build()

    return client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val obj = Json.decodeFromString<Stop>(response.body!!.string())

        return@use Pair(obj.lat, obj.lon);
    }
}

fun getItinerary(from: Pair<Double, Double>, to: Pair<Double, Double>, depTime: LocalDateTime): JsonElement? {
    val request = Request.Builder()
        .url(ncupApiEndpoint +
                "?fromPlace=${from.first},${from.second}" +
                "&toPlace=${to.first},${to.second}" +
                "&mode=WALK,BUS,RAIL,TRAM" +
                "&endmode=WALK&maxWalkDistance=800&maxWalkDistanceEnd=800&walkSpeed=1.389" +
                "&wheelchair=false" +
                "&showIntermediateStops=true" +
                "&date=${depTime.year}-${depTime.monthValue}-${depTime.dayOfMonth}" +
                "&time=${depTime.hour}:${depTime.minute}" +
                "&arriveBy=false" +
                "&additionalmodes=" +
                "&intermediatePlaces=")
        .build()

    return client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val obj = Json.parseToJsonElement(response.body!!.string())

        return@use obj.jsonObject["PrimaryTrip"]
            ?.jsonObject?.get("Itineraries")
            ?.jsonArray?.get(0)
    }
}

fun Route.stops() {
    get<RouteResource> { route ->
        val fromCoords = findStopCoord(route.fromId)
        val toCoords = findStopCoord(route.toId)

        val obj = getItinerary(fromCoords, toCoords, LocalDateTime.parse(route.depTime))

        call.respond(HttpStatusCode.OK, obj.toString())
    }
}