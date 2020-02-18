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
package com.xplorer.projectx.repository.recentCities

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xplorer.projectx.extentions.Failure
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.utils.Constants
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@Singleton
class RecentCitiesRepository @Inject constructor(
  private val gson: Gson,
  private val sharedPreferences: SharedPreferences
) : RecentCitiesRepo {

  override fun updateRecentCities(cityName: String, onComplete: (Boolean) -> Unit) {
    val citiesList = getCitiesFromDataSource()?.let { citiesListString ->

      transformJsonToList(citiesListString)
    } ?: LinkedList<String>()


    if (citiesList.contains(cityName)) {
      citiesList.remove(cityName)
    }

    citiesList.addFirst(cityName)

    if (citiesList.size > 5) {
      citiesList.pollLast()
    }

    val cityEditor = sharedPreferences.edit()

    cityEditor.putString(Constants.RECENT_CITY_STRING_LIST_KEY, transformListToString(citiesList))
    return onComplete(cityEditor.commit())
  }

  override fun getRecentCities(): Result<LinkedList<String>> {

    getCitiesFromDataSource()?.let { citiesListString ->

      return Success(transformJsonToList(citiesListString))
    }

    return Failure(Throwable("No cities currently found"))
  }

  private fun getCitiesFromDataSource(): String? {
    return sharedPreferences.getString(Constants.RECENT_CITY_STRING_LIST_KEY, null)
  }

  private fun transformJsonToList(cityData: String): LinkedList<String> {
    return gson.fromJson<LinkedList<String>>(
      cityData, object : TypeToken<LinkedList<String>>() {}.type)
  }

  private fun transformListToString(cityList: LinkedList<String>): String {
    return gson.toJson(cityList, object : TypeToken<LinkedList<String>>() {}.type)
  }
}
