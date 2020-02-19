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
package com.xplorer.projectx.repository.foursquare

import com.xplorer.projectx.BuildConfig.FOURSQUARE_API_KEY
import com.xplorer.projectx.BuildConfig.FOURSQUARE_SECRET
import com.xplorer.projectx.api.FoursquareAPI
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.getResult
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.networking.CoroutineContextProvider
import com.xplorer.projectx.networking.CoroutineExecutor
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Singleton

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
@Singleton
class FoursquareRepository @Inject
internal constructor(
  private val foursquareAPI: FoursquareAPI,
  private val coroutineContextProvider: CoroutineContextProvider
) : FoursquareRepo {
  private lateinit var job: Job

  override fun cancelRequests() {
    if (::job.isInitialized) job.cancel()
  }

  override fun getVenueData(
    query: String,
    coordinates: String,
    offset: Int,
    resultLimit: Int,
    onComplete: (Result<List<Venue>>) -> Unit
  ) {
    job = CoroutineExecutor.ioToMain(
      {
        foursquareAPI.getVenues(
          query,
          FOURSQUARE_API_KEY,
          FOURSQUARE_SECRET,
          coordinates,
          offset,
          resultLimit
        ).getResult()
      }, // make network call to get venues
      { venues ->
        onComplete(venues!!) // return venues to callback
      }, coroutineContextProvider
    )
  }
}
