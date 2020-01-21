/**
*/
package com.xplorer.projectx

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
        const val WIKIPEDIA_API_BASE_URL = "https://en.wikipedia.org/w/api.php"
        const val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"
        const val FOURSQUARE_API_BASE_URL = "https://api.foursquare.com/"
        const val GOOGLE_API_BASE_URL = "https://maps.googleapis.com/"
    }
}
