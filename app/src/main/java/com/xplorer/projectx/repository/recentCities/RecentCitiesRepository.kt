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
import javax.inject.Inject
import kotlin.collections.ArrayList

class RecentCitiesRepository @Inject constructor(
  private val gson: Gson,
  private val sharedPreferences: SharedPreferences
) : RecentCitiesRepo {

  override fun updateRecentCities(cityName: String, onComplete: (Boolean) -> Unit) {
    val citiesList = getCitiesFromDataSource()?.let { citiesListString ->

      transformJsonToList(citiesListString)
    } ?: ArrayList<String>()

    if (citiesList.contains(cityName)) {
      return onComplete(false)
    }

    citiesList.add(0, cityName)

    if (citiesList.size > 5) {
      citiesList.removeAt(citiesList.size - 1)
    }

    val cityEditor = sharedPreferences.edit()

    cityEditor.putString("", transformListToString(citiesList))
    return onComplete(cityEditor.commit())
  }

  override fun getRecentCities(): Result<List<String>> {

    getCitiesFromDataSource()?.let { citiesListString ->

      return Success(transformJsonToList(citiesListString))
    }

    return Failure(Throwable("No cities currently found"))
  }

  private fun getCitiesFromDataSource(): String? {
    return sharedPreferences.getString("", null)
  }

  private fun transformJsonToList(cityData: String): ArrayList<String> {
    return gson.fromJson<ArrayList<String>>(
      cityData, object : TypeToken<List<String>>() {}.type)
  }

  private fun transformListToString(cityList: List<String>): String {
    return gson.toJson(cityList, object : TypeToken<List<String>>() {}.type)
  }
}
