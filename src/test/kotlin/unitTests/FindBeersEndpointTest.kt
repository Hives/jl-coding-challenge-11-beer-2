package unitTests

import Beer
import Beers
import Location
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import findBeersEndpoint
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object FindBeersEndpointTest : Spek({
    describe("Find beers endpoint") {

        val exampleBeer = Beer(
            name = "Example beer",
            pub = "Example pub",
            pubService = "http://example.com/pub",
            regular = true
        )

        val exampleBeers = Beers(listOf(exampleBeer))

        class mockCreateFindBeers() {
            lateinit var calledWith: Location

            val mock = fun(location: Location): List<Beer> {
                calledWith = location
                return listOf(exampleBeer)
            }
        }

        val mockFindBeers = mockCreateFindBeers()

        val endpoint = findBeersEndpoint(mockFindBeers.mock)
        val location = Location(20.0, 40.0, 0.03)
        val request = Request(GET, "/beers?lng=${location.lng}&lat=${location.lat}&deg=${location.deg}")
        val response = endpoint(request)
        val returnedBeers = jacksonObjectMapper().readValue(response.bodyString(), Beers::class.java)

        it("should return success") {
            assertThat(response.status).isEqualTo(OK)
        }

        it("should pass the location through to the beer finder") {
            assertThat(mockFindBeers.calledWith).isEqualTo(location)
        }

        it("should return the extracted beers") {
            assertThat(returnedBeers).isEqualTo(exampleBeers)
        }
    }
})
