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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xplorer.projectx.extentions.Failure
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.model.wikipedia.WikiCityInfo
import com.xplorer.projectx.repository.google_pictures.GooglePicturesRepo
import com.xplorer.projectx.repository.wikipedia.WikipediaRepo
import javax.inject.Inject

class CitySearchViewModel @Inject
constructor(
  private val googlePicturesRepo: GooglePicturesRepo,
  private val wikipediaRepository: WikipediaRepo
) : ViewModel() {
  // Photos
  private val _successPhotoLiveData = MutableLiveData<List<Photo>>()
  val successPhotoLiveData: LiveData<List<Photo>>
    get() = _successPhotoLiveData
  private val _errorPhotoLiveData = MutableLiveData<String>()
  val errorPhotoLiveData: LiveData<String>
    get() = _errorPhotoLiveData

  private fun processSuccess(photos: List<Photo>) {
    _successPhotoLiveData.value = photos
  }

  private fun processError(error: Throwable) {
    _errorPhotoLiveData.value = error.localizedMessage
  }

  // google Photos
  fun getGooglePhotos(place_id: String) {
    googlePicturesRepo.getGooglePicturesData(place_id) { result: Result<List<Photo>> ->
      when (result) {
        is Success -> processSuccess(result.data)
        is Failure -> processError(result.error)
      }
    }
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

  private val _altCoordConfirmationLiveData = MutableLiveData<Boolean>()
  val altCoordConfirmationLiveData: LiveData<Boolean>
    get() = _altCoordConfirmationLiveData

  fun altConfirmCoordinatesForCity(queryCityName: String, cityCoordinates: String) {
    wikipediaRepository.getAlternateConfirmation(
      queryCityName,
      cityCoordinates
    ) { confirmedResult ->
      when (confirmedResult) {
        is Success -> _altCoordConfirmationLiveData.value = confirmedResult.data
        is Failure -> _errorCoordConfirmationLiveData.value = confirmedResult.error.localizedMessage
      }
    }
  }

  private val _cityInfoLiveData = MutableLiveData<WikiCityInfo>()
  val cityInfoLiveData: LiveData<WikiCityInfo>
    get() = _cityInfoLiveData

  private val _errorCityInfoLiveData = MutableLiveData<String>()
  val errorCityInfoLiveData: LiveData<String>
    get() = _errorCityInfoLiveData

  fun setCityInformationData(cityName: String) {
    wikipediaRepository.getCityInformation(cityName) { result ->
      when (result) {
        is Success -> _cityInfoLiveData.value = result.data
        is Failure -> _errorCityInfoLiveData.value = result.error.localizedMessage
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
}
