internal fun List<Pub>.toListOfBeers(): List<Beer> =
    this.map { pub ->
        pub.regularBeers.map { beerName ->
            Beer(
                beerName,
                pub.name,
                pub.pubService,
                true
            )
        } + pub.guestBeers.map { beerName ->
            Beer(
                beerName,
                pub.name,
                pub.pubService,
                false
            )
        }
    }.flatten()
