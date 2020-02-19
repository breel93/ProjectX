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
package com.xplorer.projectx.api

import com.xplorer.projectx.model.foursquare.FoursquareResponse
import com.xplorer.projectx.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

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

// places of interest using foursquare's api
interface FoursquareAPI {

  @GET("v2/venues/explore")
  fun getVenues(
    @Query("query") query: String,
    @Query("client_id") clientId: String,
    @Query("client_secret") clientSecret: String,
    @Query("ll") latLong: String,
    @Query("offset") offset: Int,
    @Query("limit") perPage: Int = 10,
    @Query("v") apiVersion: String = Constants.FOURSQUARE_API_VERSION
  ): Call<FoursquareResponse>
}
