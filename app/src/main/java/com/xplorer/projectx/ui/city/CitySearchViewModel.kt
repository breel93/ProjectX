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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.model.unsplash.PhotoResult
import com.xplorer.projectx.extentions.Failure
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.repository.foursquare.FoursquareRepo
import com.xplorer.projectx.repository.unsplash.UnsplashRepo
import com.xplorer.projectx.repository.wikipedia.WikipediaRepo
import kotlinx.coroutines.Job
import javax.inject.Inject

class CitySearchViewModel@Inject
constructor(
    private val unsplashRepository: UnsplashRepo,
    private val foursquareRepository: FoursquareRepo,
    private val wikipediaRepository: WikipediaRepo,
    application: Application
) : AndroidViewModel(application) {

    private lateinit var job: Job
    // Photos
    private val _successPhotoLiveData = MutableLiveData<List<Photo>>()
    val successPhotoLiveData: LiveData<List<Photo>>
        get() = _successPhotoLiveData
    private val _errorPhotoLiveData = MutableLiveData<String>()
    val errorPhotoLiveData: LiveData<String>
        get() = _errorPhotoLiveData

    fun getPhotoData(query: String) {
        unsplashRepository.getPhotoData(query) { result: Result<PhotoResult> ->
            when (result) {
                is Success -> processSuccess(result.data.photo)
                is Failure -> processError(result.error)
            }
        }
    }

    private fun processSuccess(photos: List<Photo>) {
        _successPhotoLiveData.value = photos
    }
    private fun processError(error: Throwable) {
        _errorPhotoLiveData.value = error.localizedMessage
    }

    // Venues / foursquare

    private val _successVenueLiveData = MutableLiveData<List<Venue>>()
    val successVenueLiveData: LiveData<List<Venue>>
        get() = _successVenueLiveData
    private val _errorVenueLiveData = MutableLiveData<String>()
    val errorVenueLiveData: LiveData<String>
        get() = _errorVenueLiveData

    fun getVenueData(query: String, latLong: LatLng) {
        val coordinates = "${latLong.latitude},${latLong.longitude}"
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

    // Location confirmation from Wikipedia

    private val _coordConfirmationLiveData = MutableLiveData<Boolean>()
    val coordConfirmationLiveData: LiveData<Boolean>
        get() = _coordConfirmationLiveData

    private val _errorCoordConfirmationLiveData = MutableLiveData<String>()
    val errorCoordConfirmationLiveData: LiveData<String>
        get() = _errorCoordConfirmationLiveData

    fun confirmCoordinatesForCity(cityName: String, cityCoordinates: String) {
        wikipediaRepository.confirmCityCoordinates(cityName, cityCoordinates) { confirmedResult ->

            when (confirmedResult) {
                is Success -> _coordConfirmationLiveData.value = confirmedResult.data
                is Failure -> _errorCoordConfirmationLiveData.value = confirmedResult.error.localizedMessage
            }
        }
    }

    fun altConfirmCoordinatesForCity(cityName: String, areaName: String, cityCoordinates: String) {
        wikipediaRepository.getAlternateConfirmation("$cityName, $areaName", cityCoordinates) { confirmedResult ->
            when (confirmedResult) {
                is Success -> _coordConfirmationLiveData.value = confirmedResult.data
                is Failure -> _errorCoordConfirmationLiveData.value = confirmedResult.error.localizedMessage
            }
        }
    }

    private val _relatedTitlesLiveData = MutableLiveData<List<String>>()
    val successRelatedTitlesLiveData: LiveData<List<String>>
        get() = _relatedTitlesLiveData

    private val _errorRelatedTitlesLiveData = MutableLiveData<String>()
    val errorRelatedTitlesLiveData: LiveData<String>
        get() = _errorCoordConfirmationLiveData

    fun getRelevantPostTitlesForCity(cityCoordinates: String) {
        wikipediaRepository.getRelevantPosts(cityCoordinates) { result ->
            when (result) {
                is Success -> _relatedTitlesLiveData.value = result.data
                is Failure -> _errorRelatedTitlesLiveData.value = result.error.localizedMessage
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
        foursquareRepository.cancelRequests() // cancel requests in foursquare
    }
}
