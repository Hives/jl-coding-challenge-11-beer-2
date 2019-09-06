import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object LivenessTest: Spek({
    describe ("GET /") {
        val server = BeerServer(9999)
        val client = OkHttp()

        it ("should return 200 (Success)") {
            server.start()

            val url = "http://localhost:${server.port()}"
            val response = client(Request(GET, url))
            assertThat(response.status).isEqualTo(OK)

            server.stop()
        }
    }
})