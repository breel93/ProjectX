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
import com.xplorer.projectx.model.CityModel
import com.xplorer.projectx.utils.Constants.Companion.RECENT_CITY_STRING_LIST_KEY
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentCitiesRepository @Inject constructor(
  private val gson: Gson,
  private val sharedPreferences: SharedPreferences
) : RecentCitiesRepo {

  override fun updateRecentCities(city: CityModel, onComplete: (Boolean) -> Unit) {
    val citiesList = getCitiesFromDataSource()?.let { citiesListString ->

      transformJsonToList(citiesListString)
    } ?: LinkedList()

    var cityToRemove: CityModel? = null
    for (cityModel in citiesList) {
      if (cityModel.cityName == city.cityName &&
        cityModel.countryName == city.countryName
      ) {
        cityToRemove = cityModel
      }
    }

    if (cityToRemove != null) {
      citiesList.remove(city)
    }

    citiesList.addFirst(city)

    if (citiesList.size > 5) {
      citiesList.pollLast()
    }

    val cityEditor = sharedPreferences.edit()

    cityEditor.putString(RECENT_CITY_STRING_LIST_KEY, transformListToString(citiesList))
    return onComplete(cityEditor.commit())
  }

  override fun getRecentCities(): Result<LinkedList<CityModel>> {

    getCitiesFromDataSource()?.let { citiesListString ->

      return Success(transformJsonToList(citiesListString))
    }

    return Failure(Throwable("No cities currently found"))
  }

  private fun getCitiesFromDataSource(): String? {
    return sharedPreferences.getString(RECENT_CITY_STRING_LIST_KEY, null)
  }

  private fun transformJsonToList(cityData: String): LinkedList<CityModel> {
    return gson.fromJson<LinkedList<CityModel>>(
      cityData, object : TypeToken<LinkedList<CityModel>>() {}.type
    )
  }

  private fun transformListToString(cityList: LinkedList<CityModel>): String {
    return gson.toJson(cityList, object : TypeToken<LinkedList<CityModel>>() {}.type)
  }
}
