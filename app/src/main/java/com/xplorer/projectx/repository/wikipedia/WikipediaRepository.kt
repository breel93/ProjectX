package com.xplorer.projectx.repository.wikipedia

import android.util.Log
import com.xplorer.projectx.api.WikipediaAPI
import com.xplorer.projectx.extentions.getResult
import com.xplorer.projectx.networkin_exp.Failure
import com.xplorer.projectx.networkin_exp.Result
import com.xplorer.projectx.networkin_exp.Success
import com.xplorer.projectx.networking.CoroutineContextProvider
import com.xplorer.projectx.networking.CoroutineExecutor
import com.xplorer.projectx.utils.CoordinatesUtils
import org.jsoup.Jsoup
import javax.inject.Inject

class WikipediaRepository @Inject
    internal constructor(private val wikipediaAPI: WikipediaAPI,
                         private val coroutineContextProvider: CoroutineContextProvider): WikipediaRepo {




    // Coordinates confirmation
    override fun confirmCityCoordinates(cityName: String,
                                        cityCoordinates: String,
                                        onComplete: ((Result<Boolean>) -> Unit)) {

        CoroutineExecutor.ioToMain(
            {getWikiCoordinates(cityName) },
            { coordString ->
                if(coordString == "n/a") {
                    val error = Failure(Throwable("No coordinates found for this location. Please try a different location."))
                    onComplete(error)
                } else {
                    val autoCompleteCoordinates = CoordinatesUtils.convertToCoordinates(cityCoordinates)
                    val wikiCoordinates = CoordinatesUtils.convertToCoordinates(coordString!!)
                    val coordinatesDiff = CoordinatesUtils.calculateCoordsDiff(autoCompleteCoordinates, wikiCoordinates)

                    onComplete(Success(coordinatesDiff < 0.1f))
                }
            },
            coroutineContextProvider
        )
    }

    private fun getWikiCoordinates(cityName: String): String {
        val pageDocument = Jsoup
            .connect("https://en.wikipedia.org/wiki/$cityName")
            .followRedirects(true)
            .get()

        val coordinateElements = pageDocument.getElementsByClass("geo")
        if(coordinateElements.size > 0) {
            val coordinatesString = coordinateElements.first().text()
            val coordinates = coordinatesString.replace("; ", ",")
            Log.e("WikipediaRepository", "Coordinates found: $coordinates")
            return coordinates
        }

        Log.e("WikipediaRepository", "No coordinates found for this city within Wikipedia")
        return "n/a"
    }

    // alternative fallback for incorrect query title due to city name inconsistencies
    // i.e. Lagos, Nigeria has different coordinates from Lagos, Portugal
    // find a match for Lagos, Portugal in wikipedia, and make a confirmation request if a match is found
    override fun getAlternateConfirmation(query: String,
                                 cityCoordinates: String,
                                   onComplete: (Result<Boolean>) -> Unit) {

        CoroutineExecutor.ioToMain(
            { wikipediaAPI.getWikiTitle(query).getResult() },
            { redirectResult ->
                when(redirectResult) {
                    // If there is a redirect
                    is Success -> confirmCityCoordinates(redirectResult.data, cityCoordinates, onComplete)
                    is Failure -> onComplete(Failure(Throwable("No alternative titles found. Please use list of articles fallback")))
                }
            },
            coroutineContextProvider
        )

    }

    // If all fails, and a wikipedia title cannot be found for a city, get a list of possible lists
    override fun getRelevantPosts(cityCoordinates: String,
                         onComplete: (Result<List<String>>) -> Unit) {
        CoroutineExecutor.ioToMain(
            { wikipediaAPI.getNearbyWikiTitles(cityCoordinates, 20).getResult() },
            { postTitles ->
                onComplete(postTitles!!)
            },
            coroutineContextProvider
        )
    }


}