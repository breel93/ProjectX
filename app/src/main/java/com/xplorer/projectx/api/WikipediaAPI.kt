package com.xplorer.projectx.api

import com.xplorer.projectx.model.wikipedia.WikiCitySummaryResponse
import com.xplorer.projectx.model.wikipedia.WikiGeoSearchResponse
import com.xplorer.projectx.model.wikipedia.WikiQueryTitleResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WikipediaAPI {

    @GET("w/api.php?action=query&redirects=1&format=json")
    fun getWikiTitle(@Query("titles") title: String): Call<WikiQueryTitleResponse>

    @GET("wiki/{city_name}")
    fun findCoordinates(@Path("city_name") cityName: String): Call<ResponseBody>

    @GET("w/api.php?action=query&action=query&format=json&list=geosearch")
    fun getNearbyWikiTitles(@Query("gscoord") coordinates: String,
                            @Query("gslimit") titleCount: Int,
                            @Query("gsradius") radius: Int = 10000): Call<WikiGeoSearchResponse>

  @GET("w/api.php/?action=query&redirects=1&format=json&prop=extracts&exintro&explaintext")
  fun getCityInfo(@Query("titles") title: String): Call<WikiCitySummaryResponse>
}