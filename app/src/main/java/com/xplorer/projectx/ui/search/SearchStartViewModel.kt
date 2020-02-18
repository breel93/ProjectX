package com.xplorer.projectx.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.repository.recentCities.RecentCitiesRepo
import javax.inject.Inject

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
class SearchStartViewModel @Inject constructor(
    private val recentCitiesRepo: RecentCitiesRepo): ViewModel() {

    private val recentCitiesList = ArrayList<String>()

    private var _recentCitiesLiveData = MutableLiveData<List<String>>()
    val recentCitiesLiveData: LiveData<List<String>>
        get() = _recentCitiesLiveData

    fun getRecentlySearchedCities() {
        when(val recentCities = recentCitiesRepo.getRecentCities()) {
            is Success -> {
                recentCitiesList.clear()
                recentCitiesList.addAll(recentCities.data)
                _recentCitiesLiveData.value = recentCitiesList
            }
        }
    }

    private var _addCityLiveData = MutableLiveData<Boolean>()
    val addCityLiveData: LiveData<Boolean>
        get() = _addCityLiveData

    fun addCityToRecent(cityName: String) {
        recentCitiesRepo.updateRecentCities(cityName) { updateCompleted ->
            _addCityLiveData.value = updateCompleted
        }
    }
}