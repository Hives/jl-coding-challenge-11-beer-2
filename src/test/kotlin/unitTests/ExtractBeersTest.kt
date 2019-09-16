package unitTests

import Beer
import Pub
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import extractBeers

object ExtractBeersTest : Spek({
    val pub1 = Pub(
        name = "Example pub 1",
        regularBeers = listOf(
            "Regular beer"
        ),
        guestBeers = listOf(),
        pubService = "http://example.com/pub1",
        id = 1,
        createTS = "pub 1 created timestamp"
    )

    val pub2 = Pub(
        name = "Example pub 2",
        regularBeers = listOf(),
        guestBeers = listOf(
            "Guest beer"
        ),
        pubService = "http://example.com/pub2",
        id = 1,
        createTS = "pub 2 created timestamp"
    )

    val pubs = listOf(pub1, pub2)

    describe("extracts a list of beers from a list of pubs") {
        val actualOutput = pubs.extractBeers().sortedBy { it.name }

        it("two beers are extracted") {
            assertThat(actualOutput).hasSize(2)
        }

        it("output contains first beer") {
            assertThat(actualOutput).contains(
                Beer(
                    name = "Guest beer",
                    pub = "Example pub 2",
                    pubService = "http://example.com/pub2",
                    isRegular = false
                )
            )
        }

        it("output contains second beer") {
            assertThat(actualOutput).contains(
                Beer(
                    name = "Regular beer",
                    pub = "Example pub 1",
                    pubService = "http://example.com/pub1",
                    isRegular = true
                )
            )
        }
    }
})
