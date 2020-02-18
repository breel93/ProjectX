package com.xplorer.projectx.repository.recentCities

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.xplorer.projectx.ProjectX
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.model.CityModel
import com.xplorer.projectx.utils.Constants
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
/**
 * Designed and developed by ProjectX
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@RunWith(AndroidJUnit4::class)
class RecentCitiesRepositoryTest {

    private lateinit var recentCityRepo: RecentCitiesRepo
    private lateinit var sharedPrefs: SharedPreferences

    @Before
    fun setup() {

        // set shared preferences
        sharedPrefs = ApplicationProvider
            .getApplicationContext<ProjectX>()
            .getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)

        // start with a clean slate before each test run
        val sharedPrefEditor = sharedPrefs.edit()
        sharedPrefEditor.putString(Constants.RECENT_CITY_STRING_LIST_KEY, null)
        sharedPrefEditor.commit()

        recentCityRepo = RecentCitiesRepository(Gson(), sharedPrefs)
    }

    @After
    fun tearDown() {
        val sharedPrefEditor = sharedPrefs.edit()
        sharedPrefEditor.putString(Constants.RECENT_CITY_STRING_LIST_KEY, null)
        sharedPrefEditor.commit()
    }

    @Test
    fun test_updateRecentCities_returns_with_true_onComplete_response_on_first_city_name_update() {

        // arrange, act
        recentCityRepo.updateRecentCities(CityModel()) { updateCompleted ->

            // assert
            assertTrue(updateCompleted)
        }
    }

    @Test
    fun test_updateRecentCities_pushes_city_to_first_on_list() {

        // arrange, act
        recentCityRepo.updateRecentCities(CityModel("", "Istanbul")) {} // persist to shared preferences
        recentCityRepo.updateRecentCities(CityModel("", "Rabat")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Lagos")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Istanbul")) {}
        // act
        val cityListResult = recentCityRepo.getRecentCities()

        // assert
        val cityListData = (cityListResult as Success).data
        assertTrue(cityListData[2].cityName == "Rabat")
        assertTrue(cityListData[0].cityName == "Istanbul")
    }

    @Test
    fun test_getRecentCities_returns_with_success_result_when_cities_exists_in_preferences() {

        // arrange
        recentCityRepo.updateRecentCities(CityModel("", "Istanbul")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Rabat")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Lagos")) {}

        // act
        val cityListResult = recentCityRepo.getRecentCities()

        // assert
        assertTrue(cityListResult is Success)
        assertTrue((cityListResult as Success).data.size == 3)

    }

    @Test
    fun test_getRecentCities_always_returns_max_of_5_cities_in_success_result() {

        // arrange
        recentCityRepo.updateRecentCities(CityModel("", "Istanbul")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Rabat")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Lagos")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Accra")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Abuja")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Sokoto")) {}

        // act
        val cityListResult = recentCityRepo.getRecentCities()

        // assert
        assertTrue((cityListResult as Success).data.size == 5)

    }

    @Test
    fun test_updateRecentCities_removes_last_item_on_adding_new_city() {

        // arrange
        recentCityRepo.updateRecentCities(CityModel("", "Istanbul")) {} // last city
        recentCityRepo.updateRecentCities(CityModel("", "Rabat")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Lagos")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Accra")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Abuja")) {}
        recentCityRepo.updateRecentCities(CityModel("", "Sokoto")) {} // latest city

        // act
        val cityListResult = recentCityRepo.getRecentCities()

        // assert
        assertTrue((cityListResult as Success).data[4].cityName == "Rabat")

    }
}