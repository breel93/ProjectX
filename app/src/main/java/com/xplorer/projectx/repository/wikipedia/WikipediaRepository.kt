/**
 *  Designed and developed by ProjectX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.xplorer.projectx.repository.wikipedia

import com.xplorer.projectx.BuildConfig
import com.xplorer.projectx.api.WikipediaAPI
import com.xplorer.projectx.extentions.getResult
import com.xplorer.projectx.extentions.Failure
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.networking.CoroutineContextProvider
import com.xplorer.projectx.networking.CoroutineExecutor
import com.xplorer.projectx.utils.CoordinatesUtils
import org.jsoup.Jsoup
import javax.inject.Inject

class WikipediaRepository @Inject
    internal constructor(
      private val wikipediaAPI: WikipediaAPI,
      private val coroutineContextProvider: CoroutineContextProvider
    ) : WikipediaRepo {

    // Coordinates confirmation
    override fun confirmCityCoordinates(
      cityName: String,
      cityCoordinates: String,
      onComplete: ((Result<Boolean>) -> Unit)
    ) {

        CoroutineExecutor.ioToMain(
            { getWikiCoordinates(cityName) },
            { coordString ->
                if (coordString == "n/a") {
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

        val call = wikipediaAPI.findCoordinates(cityName)

        val callCloned = call.clone()
        return try {
            val response = callCloned.execute()
            val result = response.body()?.run {

                val pageDocument = Jsoup.parse(string())

                val coordinateElement = pageDocument.getElementsByClass("geo").first() ?: return "n/a"

                val coordinatesString = coordinateElement.text()

                if (coordinatesString.isEmpty()) {
                    return "n/a"
                }

                coordinatesString.replace("; ", ",")
            }

            val errorResult = response.errorBody()?.run { "n/a" }

            result ?: errorResult!!
        } catch (error: Throwable) {
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }

            "n/a"
        }
    }

    // alternative fallback for incorrect query title due to city name inconsistencies
    // i.e. Lagos, Nigeria has different coordinates from Lagos, Portugal
    // find a match for Lagos, Portugal in wikipedia, and make a confirmation request if a match is found
    override fun getAlternateConfirmation(
      query: String,
      cityCoordinates: String,
      onComplete: (Result<Boolean>) -> Unit
    ) {

        CoroutineExecutor.ioToMain(
            { wikipediaAPI.getWikiTitle(query).getResult() },
            { redirectResult ->
                when (redirectResult) {
                    // If there is a redirect
                    is Success -> confirmCityCoordinates(redirectResult.data, cityCoordinates, onComplete)
                    is Failure -> onComplete(Failure(Throwable("No alternative titles found. Please use list of articles fallback")))
                }
            },
            coroutineContextProvider
        )
    }

    // If all fails, and a wikipedia title cannot be found for a city, get a list of possible lists
    override fun getRelevantPosts(
      cityCoordinates: String,
      onComplete: (Result<List<String>>) -> Unit
    ) {
        CoroutineExecutor.ioToMain(
            { wikipediaAPI.getNearbyWikiTitles(cityCoordinates, 20).getResult() },
            { postTitles ->
                onComplete(postTitles!!)
            },
            coroutineContextProvider
        )
    }
}
