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
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.repository.unsplash.UnsplashDataSourceFactory
import javax.inject.Inject

class CityPhotoViewModel @Inject
constructor(
  private val unsplashDataSourceFactory: UnsplashDataSourceFactory,
  application: Application
) : AndroidViewModel(application) {
    private var photoList: LiveData<PagedList<Photo>>

    init {
        photoList = createInitialPhotoList()
    }

    fun setSearchQuery(query: String) {
        unsplashDataSourceFactory.unsplashDataSource.cityQuery = query
    }

    fun setPlaceId(placeId: String) {
        unsplashDataSourceFactory.unsplashDataSource.placeId = placeId
    }

    fun refreshPhoto(): LiveData<PagedList<Photo>> {
        photoList = createInitialPhotoList()
        return photoList
    }

    private fun createInitialPhotoList() =
        LivePagedListBuilder(unsplashDataSourceFactory, createPagingConfig())
            .build()
    private fun createPagingConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPrefetchDistance(10)
            .setPageSize(10)
            .build()
    }
}
