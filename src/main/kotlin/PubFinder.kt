import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.format.Jackson.auto

internal fun createPubFinder(client: HttpHandler) =
    fun(lat: Double, lng: Double, range: Double): List<Pub> {
        val response = client(Request(GET, "/pubcache?lat=${lat}&lng=${lng}&deg=${range}"))
        return pubsLens(response).pubs
    }

private val pubsLens = Body.auto<Pubs>().toLens()

