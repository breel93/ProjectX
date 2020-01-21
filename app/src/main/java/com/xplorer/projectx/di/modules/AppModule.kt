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
package com.xplorer.projectx.di.modules

import com.google.gson.Gson
import com.xplorer.projectx.utils.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Robert Anizoba on 2020-01-15.
 */
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Named(Constants.WIKIPEDIA_RETROFIT_KEY)
    fun provideWikipediaRetrofit(gsonConverter: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.WIKIPEDIA_API_BASE_URL)
            .addConverterFactory(gsonConverter)
            .build()
    }

    @Provides
    @Named(Constants.UNSPLASH_RETROFIT_KEY)
    fun provideUnsplashAPIRetrofit(gsonConverter: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.UNSPLASH_API_BASE_URL)
            .addConverterFactory(gsonConverter)
            .build()
    }

    @Provides
    @Named(Constants.GOOGLE_RETROFIT_KEY)
    fun provideGoogleAPIRetrofit(gsonConverter: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.GOOGLE_API_BASE_URL)
            .addConverterFactory(gsonConverter)
            .build()
    }

    @Provides
    @Named(Constants.FOURSQUARE_RETROFIT_KEY)
    fun provideFoursquareAPIRetrofit(gsonConverter: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.FOURSQUARE_API_BASE_URL)
            .addConverterFactory(gsonConverter)
            .build()
    }
}
