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
package com.xplorer.projectx.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.xplorer.projectx.api.FoursquareAPI
import com.xplorer.projectx.api.GooglePhotoApi
import com.xplorer.projectx.api.UnsplashApi
import com.xplorer.projectx.api.WikipediaAPI
import com.xplorer.projectx.networking.CoroutineContextProvider
import com.xplorer.projectx.networking.CoroutineContextProviderImpl
import com.xplorer.projectx.repository.foursquare.FoursquareRepo
import com.xplorer.projectx.repository.foursquare.FoursquareRepository
import com.xplorer.projectx.repository.google_pictures.GooglePicturesRepo
import com.xplorer.projectx.repository.google_pictures.GooglePicturesRepository
import com.xplorer.projectx.repository.recentCities.RecentCitiesRepo
import com.xplorer.projectx.repository.recentCities.RecentCitiesRepository
import com.xplorer.projectx.repository.unsplash.UnsplashRepo
import com.xplorer.projectx.repository.unsplash.UnsplashRepository
import com.xplorer.projectx.repository.wikipedia.WikipediaRepo
import com.xplorer.projectx.repository.wikipedia.WikipediaRepository
import com.xplorer.projectx.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Robert Anizoba on 2020-01-15.
 */

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideGson(): Gson {
    return Gson()
  }

  @Provides
  fun provideOkHTTPClient(): OkHttpClient {

    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
      .addInterceptor(logging)
      .build()
  }

  @Provides
  @Singleton
  fun provideCoroutineContextProvide(provider: CoroutineContextProviderImpl): CoroutineContextProvider {
    return provider
  }

  @Provides
  @Singleton
  fun provideGsonConverterFactory(gson: Gson): Converter.Factory {
    return GsonConverterFactory.create(gson)
  }

  @Singleton
  @Provides
  fun provideWikipediaRetrofit(okHttpClient: OkHttpClient): WikipediaAPI {
    return Retrofit.Builder()
      .baseUrl(Constants.WIKIPEDIA_API_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttpClient)
      .build()
      .create(WikipediaAPI::class.java)
  }

  @Singleton
  @Provides
  fun provideUnsplashAPIRetrofit(okHttpClient: OkHttpClient): UnsplashApi {
    return Retrofit.Builder()
      .baseUrl(Constants.UNSPLASH_API_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttpClient)
      .build()
      .create(UnsplashApi::class.java)
  }

  @Singleton
  @Provides
  fun provideFoursquareAPI(okHttpClient: OkHttpClient): FoursquareAPI {
    return Retrofit.Builder()
      .baseUrl(Constants.FOURSQUARE_API_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttpClient)
      .build()
      .create(FoursquareAPI::class.java)
  }

  @Singleton
  @Provides
  fun provideGoogleAPIRetrofit(okHttpClient: OkHttpClient): GooglePhotoApi {
    return Retrofit.Builder()
      .baseUrl(Constants.GOOGLE_API_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttpClient)
      .build()
      .create(GooglePhotoApi::class.java)
  }

  @Provides
  fun provideUnsplashRepository(unsplashRepository: UnsplashRepository): UnsplashRepo {
    return unsplashRepository
  }

  @Provides
  fun provideWikipediahRepository(wikipediaRepository: WikipediaRepository): WikipediaRepo {
    return wikipediaRepository
  }

  @Provides
  fun providesFoursquareRepository(foursquareRepository: FoursquareRepository): FoursquareRepo {
    return foursquareRepository
  }

  @Provides
  @Singleton
  fun providesSharedPreferences(application: Application): SharedPreferences {
    return application.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
  }

  @Provides
  @Singleton
  fun providesRecentCityRepository(recentCitiesRepo: RecentCitiesRepository): RecentCitiesRepo {
    return recentCitiesRepo
  }

  @Provides
  fun providesGooglePicturesRepository(googlePicturesRepository: GooglePicturesRepository): GooglePicturesRepo {
    return googlePicturesRepository
  }
}
