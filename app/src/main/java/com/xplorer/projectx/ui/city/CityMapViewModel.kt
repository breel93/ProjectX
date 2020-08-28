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
package com.xplorer.projectx.ui.city

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.xplorer.projectx.extentions.Failure
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.repository.foursquare.FoursquareRepo

class CityMapViewModel @ViewModelInject constructor(
  private val foursquareRepository: FoursquareRepo
) : ViewModel() {

  // for shared viewModel

  // For selecting place from full list of Places of Interest
  private val _currentPOILiveData = MutableLiveData<String>()
  val currentPOILiveData: LiveData<String>
    get() = _currentPOILiveData

  fun setCurrentPOI(poi: String?) {
    _currentPOILiveData.value = poi
  }

  // For scrolling to marker position based on place of interest results
  private val _currentMarkerPosition = MutableLiveData<Int>()
  val currentMarkerPosition: LiveData<Int> get() = _currentMarkerPosition

  fun setCurrentMarkerPosition(placeIndex: Int) {
    _currentMarkerPosition.value = placeIndex
  }

  // For scrolling to place of interest index based on marker clicked
  private val _currentPlaceIndex = MutableLiveData<Int>()
  val currentPlaceIndex: LiveData<Int> get() = _currentPlaceIndex
  fun selectPlaceOnPOIList(placeIndex: Int) {
    _currentPlaceIndex.value = placeIndex
  }

  fun clearErrors() {
    _errorVenueLiveData.value = null
  }

  fun clearPlacesOfInterest() {
    _successVenueLiveData.value = null
  }

  // Please check this for an alternative to provide coordinates to the
  // places of interest recycler view fragment

  private var currentCoordinates: String? = null

  fun setCurrentCoordinates(coordinates: String) {
    this.currentCoordinates = coordinates
  }

  fun getCurrentCoordnates(): String? {
    return this.currentCoordinates
  }

  // Venues / foursquare

  private val _successVenueLiveData = MutableLiveData<List<Venue>>()
  val successVenueLiveData: LiveData<List<Venue>>
    get() = _successVenueLiveData
  private val _errorVenueLiveData = MutableLiveData<String>()
  val errorVenueLiveData: LiveData<String>
    get() = _errorVenueLiveData

  fun getVenueData(query: String, coordinates: String) {

    foursquareRepository.getVenueData(query, coordinates, 0) { result: Result<List<Venue>> ->
      when (result) {
        is Success -> {
          if (result.data.isNotEmpty()) {
            processVenueSuccess(result.data)
          } else {
            processVenueError(Throwable("No ${_currentPOILiveData.value} Found in this location"))
          }
        }
        is Failure -> processVenueError(result.error)
      }
    }
  }

  private fun processVenueSuccess(venues: List<Venue>) {
    _successVenueLiveData.value = venues
  }

  private fun processVenueError(error: Throwable) {
    _errorVenueLiveData.value = error.localizedMessage
  }

  override fun onCleared() {
    super.onCleared()
    foursquareRepository.cancelRequests() // cancel requests in foursquare
  }
}
