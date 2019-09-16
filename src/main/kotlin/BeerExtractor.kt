internal fun List<Pub>.extractBeers(): List<Beer> = this
    .flatMap { it.extractBeers() }

private fun Pub.extractBeers(): List<Beer> =
    (this.regularBeers.map { it to true } + this.guestBeers.map { it to false })
        .map {
            val (beerName, isRegular) = it
            Beer(
                beerName,
                this.name,
                this.pubService,
                isRegular
            )
        }
