package com.xplorer.projectx.api

import com.xplorer.projectx.model.google.GooglePhotoResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePhotoApi {
  @GET("maps/api/place/details/json")
  fun getGooglePhotos(
    @Query("place_id") place_id: String,
    @Query("key") key: String
  ): Call<GooglePhotoResults>
}