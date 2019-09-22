package unitTests

import Location
import assertk.assertThat
import assertk.assertions.isEqualTo
import findBeersEndpoint
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object FindBeersEndpointTest : Spek({
    describe("Find beers endpoint") {
        val endpoint = findBeersEndpoint()
        val location = Location(20.0, 40.0, 0.03)
        val request = Request(GET, "/beers?lat=${location.lat}&lng=${location.lng}&deg=${location.deg}")
        val response = endpoint(request)

        it("should return success") {
            assertThat(response.status).isEqualTo(OK)
        }
    }
})
