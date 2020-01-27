package com.xplorer.projectx.repository

import androidx.lifecycle.MutableLiveData
import com.xplorer.projectx.BuildConfig.UNSPLASH_API_KEY
import com.xplorer.projectx.api.UnsplashApi
import com.xplorer.projectx.model.Photo
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject
internal constructor(private val unsplashApi: UnsplashApi){

    var job: CompletableJob? = null

    fun fetchPhotos(query: String):MutableLiveData<List<Photo>>{
        job = Job()
        return object : MutableLiveData<List<Photo>>(){
            override fun onActive() {
                super.onActive()
                job?.let { completableJob ->
                    CoroutineScope(IO + completableJob).launch {
                        val result = unsplashApi.getPhotos(UNSPLASH_API_KEY,query,1,5)
                        withContext(Main){
                            value = result.body()!!.photo
                            completableJob.complete()
                        }
                    }
                }
            }
        }

    }
    fun cancelJobs(){
        job?.cancel()
    }

}