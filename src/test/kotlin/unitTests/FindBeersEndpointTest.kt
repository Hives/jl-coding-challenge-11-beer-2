package unitTests

import Beer
import Location
import assertk.assertThat
import assertk.assertions.isEqualTo
import findBeersEndpoint
import io.mockk.mockk
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object FindBeersEndpointTest : Spek({
    describe("Find beers endpoint") {
        val mockBeers = mockk<List<Beer>>()

        class mockCreateFindBeers() {
            lateinit var calledWith: Location

            val mock = fun(location: Location): List<Beer> {
                calledWith = location
                return mockBeers
            }
        }

        val mockFindBeers = mockCreateFindBeers()

        val endpoint = findBeersEndpoint(mockFindBeers.mock)
        val location = Location(20.0, 40.0, 0.03)
        val request = Request(GET, "/beers?lng=${location.lng}&lat=${location.lat}&deg=${location.deg}")
        val response = endpoint(request)

        it("should return success") {
            assertThat(response.status).isEqualTo(OK)
        }

        it("should call findBeers with the right location") {
            assertThat(mockFindBeers.calledWith).isEqualTo(location)
        }
    }
})
