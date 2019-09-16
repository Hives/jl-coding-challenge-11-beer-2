import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.format.Jackson.auto

internal typealias PubFinder = (Location) -> List<Pub>

internal fun createPubFinder(client: HttpHandler): PubFinder =
    fun(location: Location): List<Pub> {
        val (lng, lat, deg) = location
        val response = client(Request(GET, "/pubcache/?lat=${lat}&lng=${lng}&deg=${deg}"))
        return pubsLens(response).pubs
    }

private val pubsLens = Body.auto<Pubs>().toLens()

