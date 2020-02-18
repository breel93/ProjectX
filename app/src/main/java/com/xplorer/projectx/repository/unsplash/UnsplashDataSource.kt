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

import androidx.paging.PageKeyedDataSource
import com.xplorer.projectx.extentions.Failure
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.repository.google_pictures.GooglePicturesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashDataSource @Inject
internal constructor(
    private val unsplashRepository: UnsplashRepository,
    private val googlePicturesRepository: GooglePicturesRepository
    ) :
    PageKeyedDataSource<Int, Photo>() {

    lateinit var cityQuery: String
    lateinit var placeId: String

    override fun loadInitial(
      params: LoadInitialParams<Int>,
      callback: LoadInitialCallback<Int, Photo>
    ) {
//        unsplashRepository.getPhotoData(cityQuery, 1, 10) {
//            when (it) {
//                is Success -> {
//                    val photoList = it.data.photo
//                    photoList.let {
//                        callback.onResult(photoList, null, 2)
//                    }
//                }
//                is Failure -> it.error
//            }
//        }
        googlePicturesRepository.getGooglePicturesData(placeId) {
            when (it) {
                is Success -> {
                    val photoList = it.data
                    photoList.let {
                        callback.onResult(photoList, null, 1)
                    }
                }
                is Failure -> it.error
            }
        }
    }

    override fun loadAfter(
      params: LoadParams<Int>,
      callback: LoadCallback<Int, Photo>
    ) {
        unsplashRepository.getPhotoData(cityQuery, params.key, params.requestedLoadSize) {
            when (it) {
                is Success -> {
                    val photoList = it.data.photo
                    photoList.let {

                        callback.onResult(photoList, params.key + 1)
                    }
                }
                is Failure -> it.error
            }
        }
    }

    override fun loadBefore(
      params: LoadParams<Int>,
      callback: LoadCallback<Int, Photo>
    ) {
    }
}
