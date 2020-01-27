package com.xplorer.projectx.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.xplorer.projectx.model.Photo
import com.xplorer.projectx.repository.UnsplashRepository
import javax.inject.Inject

class SearchViewModel@Inject
constructor( private val unsplashRepository: UnsplashRepository,
             application: Application
) : AndroidViewModel(application){

    fun getPhotos(query: String) : MutableLiveData<List<Photo>>{
        return unsplashRepository.fetchPhotos(query)
    }

    fun cancelJobs(){
        unsplashRepository.cancelJobs()
    }

}