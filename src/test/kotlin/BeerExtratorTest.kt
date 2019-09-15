import assertk.assertThat
import assertk.assertions.isEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object BeerExtratorTest : Spek({
    val pubs = listOf(
        Pub(
            name = "Example pub 1",
            regularBeers = listOf(
                "Regular beer"
            ),
            guestBeers = listOf(),
            pubService = "http://example.com/pub1",
            id = 1,
            createTS = "pub 1 created timestamp"
        ),
        Pub(
            name = "Example pub 2",
            regularBeers = listOf(),
            guestBeers = listOf(
                "Guest beer"
            ),
            pubService = "http://example.com/pub2",
            id = 1,
            createTS = "pub 2 created timestamp"
        )
    )

    describe("extracts a list of beers from a list of pubs") {
        val actualOutputSorted = pubs.toListOfBeers().sortedBy { it.name }

        it("two beers are extracted") {
            assertThat(actualOutputSorted.size).isEqualTo(2)
        }

        it("first extracted beer has right details") {
            val firstExtractedBeer = actualOutputSorted[0]
            assertThat(firstExtractedBeer).isEqualTo(Beer(
                name = "Guest beer",
                pub = "Example pub 2",
                pubService = "http://example.com/pub2",
                isRegular = false
            ))
        }

        it("second extracted beer has right details") {
            val secondExtractedBeer = actualOutputSorted[1]
            assertThat(secondExtractedBeer).isEqualTo(Beer(
                name = "Regular beer",
                pub = "Example pub 1",
                pubService = "http://example.com/pub1",
                isRegular = true
            ))
        }
    }
})
