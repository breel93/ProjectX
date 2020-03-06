package com.xplorer.projectx.ui.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xplorer.projectx.extentions.Failure
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.repository.foursquare.FoursquareRepo
import javax.inject.Inject

class CityMapViewModel @Inject constructor(
  private val foursquareRepository: FoursquareRepo
  ): ViewModel() {

  // for shared viewModel

  private val _currentPOILiveData = MutableLiveData<String>()
  val currentPOILiveData: LiveData<String>
    get() = _currentPOILiveData

  private val _currentPOIIndex = MutableLiveData<Int>()
  val currentPOIIndex: LiveData<Int> get() = _currentPOIIndex

  fun setCurrentPOI(poi: String?) {
    _currentPOILiveData.value = poi
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
        is Success -> processVenueSuccess(result.data)
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