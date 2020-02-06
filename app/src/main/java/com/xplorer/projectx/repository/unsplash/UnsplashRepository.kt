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
package com.xplorer.projectx.repository.unsplash

import com.xplorer.projectx.BuildConfig.UNSPLASH_API_KEY
import com.xplorer.projectx.api.UnsplashApi
import com.xplorer.projectx.model.unsplash.PhotoResult
import com.xplorer.projectx.networkin_exp.Result
import com.xplorer.projectx.extentions.getResult
import com.xplorer.projectx.networking.CoroutineContextProvider
import com.xplorer.projectx.networking.CoroutineExecutor
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject
internal constructor(
    private val unsplashApi: UnsplashApi,
    private val coroutineContextProvider: CoroutineContextProvider ): UnsplashRepo {
    private lateinit var job: Job

    override fun getPhotoData(query: String, onComplete: ((Result<PhotoResult>) -> Unit)) {
        job = CoroutineExecutor.ioToMain(
            { fetchPhotos(query) },
            {photoResult ->
                onComplete(photoResult!!)
            },
            coroutineContextProvider
        )
    }

   private fun fetchPhotos(query: String) =
        unsplashApi.getPhotos(UNSPLASH_API_KEY, query, 1, 10).getResult()
}
