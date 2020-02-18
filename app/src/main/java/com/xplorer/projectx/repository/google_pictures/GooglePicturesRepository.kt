package com.xplorer.projectx.repository.google_pictures

import com.xplorer.projectx.BuildConfig.GOOGLE_API_KEY
import com.xplorer.projectx.api.GooglePhotoApi
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.getResult
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.networking.CoroutineContextProvider
import com.xplorer.projectx.networking.CoroutineExecutor
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GooglePicturesRepository @Inject
internal constructor(
  private val googlePhotoApi: GooglePhotoApi,
  private val coroutineContextProvider: CoroutineContextProvider
) : GooglePicturesRepo{


  private lateinit var job: Job
  override fun getGooglePicturesData(
    place_id: String,
    onComplete: (Result<List<Photo>>) -> Unit
  ) {
    job = CoroutineExecutor.ioToMain(
      { fetchGooglePhotos(place_id)},
      { onComplete(it!!) },
      coroutineContextProvider
    )
  }
  private fun fetchGooglePhotos(place_id: String) =
    googlePhotoApi.getGooglePhotos(place_id,GOOGLE_API_KEY).getResult()

}