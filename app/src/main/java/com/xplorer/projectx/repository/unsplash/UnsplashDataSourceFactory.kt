package com.xplorer.projectx.repository.unsplash

import androidx.paging.DataSource
import com.xplorer.projectx.model.unsplash.Photo
import javax.inject.Inject

class UnsplashDataSourceFactory@Inject
constructor( val unsplashDataSource: UnsplashDataSource):DataSource.Factory<Int, Photo>(){
    override fun create(): DataSource<Int, Photo> {
        return unsplashDataSource
    }
}


