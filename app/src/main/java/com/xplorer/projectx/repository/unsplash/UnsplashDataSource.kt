package com.xplorer.projectx.repository.unsplash

import androidx.paging.PageKeyedDataSource
import com.xplorer.projectx.BuildConfig.UNSPLASH_API_KEY
import com.xplorer.projectx.api.UnsplashApi
import com.xplorer.projectx.extentions.Failure
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.extentions.getResult
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.model.unsplash.PhotoResult
import com.xplorer.projectx.networking.CoroutineContextProvider
import com.xplorer.projectx.networking.CoroutineExecutor
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashDataSource @Inject
internal constructor(private val unsplashApi: UnsplashApi,
                     private val coroutineContextProvider: CoroutineContextProvider)
    : PageKeyedDataSource<Int, Photo>(),UnsplashRepo {

    private lateinit var job: Job

    lateinit var cityQuery: String

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {
        getPhotoData(cityQuery,1,10){
            when(it){
                is Success -> {
                    val photoList = it.data.photo
                    photoList.let {
                       callback.onResult(photoList,null,2)
                    }
                }
                is Failure -> it.error
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>,
                           callback: LoadCallback<Int, Photo>) {
        getPhotoData(cityQuery,params.key, params.requestedLoadSize){
            when(it){
                is Success -> {
                    val photoList = it.data.photo
                    photoList.let{
                        callback.onResult(photoList,params.key + 1)
                    }
                }
                is Failure -> it.error
            }
        }

    }

    override fun loadBefore(params: LoadParams<Int>,
                            callback: LoadCallback<Int, Photo>) {
    }



    override fun getPhotoData(query: String, page:Int, perPage : Int, onComplete: ((Result<PhotoResult>) -> Unit)) {
        job = CoroutineExecutor.ioToMain(
            { fetchPhotos(query, page, perPage) },
            {photoResult ->
                onComplete(photoResult!!)
            },
            coroutineContextProvider
        )
    }

    private fun fetchPhotos(query: String, page : Int, perPage : Int) =
        unsplashApi.getPhotos(UNSPLASH_API_KEY, query, page, perPage).getResult()



}