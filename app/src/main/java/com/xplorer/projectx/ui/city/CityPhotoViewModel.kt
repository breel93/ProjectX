package com.xplorer.projectx.ui.city

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.repository.unsplash.UnsplashDataSourceFactory
import com.xplorer.projectx.networking.AppExecutors
import javax.inject.Inject

class CityPhotoViewModel @Inject
constructor(private val unsplashDataSourceFactory: UnsplashDataSourceFactory,
            private val appsExecutor: AppExecutors,
            application: Application
): AndroidViewModel(application){
    private var photoList: LiveData<PagedList<Photo>>

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPrefetchDistance(10)
            .setPageSize(10)
            .build()
        photoList = LivePagedListBuilder(unsplashDataSourceFactory, config)
            .setFetchExecutor(appsExecutor.networkIO())
            .build()
    }

    fun setSearchQuery(query:String){
        unsplashDataSourceFactory.unsplashDataSource.cityQuery = query
    }

    fun refreshPhoto() : LiveData<PagedList<Photo>>{
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPrefetchDistance(10)
            .setPageSize(10)
            .build()
        photoList = LivePagedListBuilder(unsplashDataSourceFactory, config)
            .setFetchExecutor(appsExecutor.networkIO())
            .build()
        return photoList
    }
}