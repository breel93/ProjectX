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
package com.xplorer.projectx.utils

import com.xplorer.projectx.BuildConfig
import com.xplorer.projectx.model.unsplash.Photo

/**
 * Created by Robert Anizoba on 2020-01-15.
 */
class Constants {
  companion object {

    // dagger 'Named' annotation keys
    const val UNSPLASH_RETROFIT_KEY = "unsplash"
    const val WIKIPEDIA_RETROFIT_KEY = "wikipedia"
    const val FOURSQUARE_RETROFIT_KEY = "foursquare"
    const val GOOGLE_RETROFIT_KEY = "google"

    // Base URLS
    const val WIKIPEDIA_API_BASE_URL = "https://en.wikipedia.org/"
    const val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"
    const val FOURSQUARE_API_BASE_URL = "https://api.foursquare.com/"
    const val GOOGLE_API_BASE_URL = "https://maps.googleapis.com/"

    // FOURSQUARE API
    const val FOURSQUARE_API_VERSION = "20180323"

    // Wikipedia info link
    const val WIKIPEDIA_INFO_URL = "$WIKIPEDIA_API_BASE_URL/wiki/"

    // Shared Preferences
    const val SHARED_PREF_KEY = "shared_prefs"
    const val RECENT_CITY_STRING_LIST_KEY = "recent_cities"

    fun getPhotoWithReferenceURl(photo: Photo): String {
      val mapBasedUrl = "https://maps.googleapis.com/maps/api/place/photo?"
      val maxwidth = "maxwidth=700&"
      val photoreference = "photoreference=${photo.photo_reference}&"
      val apiKey = "key=${BuildConfig.GOOGLE_API_KEY}"
      return mapBasedUrl + maxwidth + photoreference + apiKey
    }
  }
}
