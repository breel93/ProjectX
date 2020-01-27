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
package com.xplorer.projectx.repository

import androidx.lifecycle.MutableLiveData
import com.xplorer.projectx.BuildConfig.UNSPLASH_API_KEY
import com.xplorer.projectx.api.UnsplashApi
import com.xplorer.projectx.model.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject
internal constructor(private val unsplashApi: UnsplashApi) {

    var job: CompletableJob? = null

    fun fetchPhotos(query: String): MutableLiveData<List<Photo>> {
        job = Job()
        return object : MutableLiveData<List<Photo>>() {
            override fun onActive() {
                super.onActive()
                job?.let { completableJob ->
                    CoroutineScope(IO + completableJob).launch {
                        val result = unsplashApi.getPhotos(UNSPLASH_API_KEY, query, 1, 5)
                        withContext(Main) {
                            value = result.body()!!.photo
                            completableJob.complete()
                        }
                    }
                }
            }
        }
    }
    fun cancelJobs() {
        job?.cancel()
    }
}
