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
package com.xplorer.projectx.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xplorer.projectx.model.Photo
import com.xplorer.projectx.model.PhotoResult
import com.xplorer.projectx.networkin_exp.Failure
import com.xplorer.projectx.networkin_exp.Result
import com.xplorer.projectx.networkin_exp.Success
import com.xplorer.projectx.networking.CoroutineContextProvider
import com.xplorer.projectx.networking.CoroutineExecutor
import com.xplorer.projectx.repository.UnsplashRepository
import kotlinx.coroutines.Job
import javax.inject.Inject

class SearchViewModel@Inject
constructor(
  private val unsplashRepository: UnsplashRepository,
  application: Application
) : AndroidViewModel(application) {


    private lateinit var job: Job
    private val _successPhotoLiveData = MutableLiveData<List<Photo>>()
    val successPhotoLiveData: LiveData<List<Photo>>
        get() = _successPhotoLiveData
    private val _errorPhotoLiveData = MutableLiveData<String>()
    val errorPhotoLiveData: LiveData<String>
        get() = _errorPhotoLiveData



    fun getPhotoData(query: String) {
        unsplashRepository.getPhotoData(query) { result: Result<PhotoResult> ->
            when(result) {
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


    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }

}
