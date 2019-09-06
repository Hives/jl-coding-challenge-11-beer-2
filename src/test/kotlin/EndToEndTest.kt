import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object EndToEndTest: Spek({
    describe ("Endpoints") {
        val port = 9999
        val server = BeerServer(port)
        val client = OkHttp()

        it ("GET '/' should return 200 (Success)") {
            server.start()
            assertThat(client(Request(GET, "http://localhost:$port")).status).isEqualTo(OK)
            server.stop()
        }

        it ("GET '/beerapi/beers' should return 200 (Success)") {
            server.start()
            assertThat(client(Request(GET, "http://localhost:$port/beerapi/beers")).status).isEqualTo(OK)
            server.stop()
        }
    }
})