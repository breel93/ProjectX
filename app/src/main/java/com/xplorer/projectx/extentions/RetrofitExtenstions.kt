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
package com.xplorer.projectx.extentions

import com.google.gson.JsonParseException
import com.xplorer.projectx.BuildConfig
import com.xplorer.projectx.networking.error.ApiDataTransformException
import com.xplorer.projectx.networking.error.AuthError
import com.xplorer.projectx.networking.error.NetworkException
import com.xplorer.projectx.networking.error.ServerError
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
// T is the response from the network call, R is the data to return to the viewModelCity
fun <T : Mappable<R>, R : Any> Call<T>.getResult(): Result<R> {
  val call = clone()
  return try {
    val response = call.execute()
    val result = response.body()?.run { Success(mapToData()) }
    val errorResult = response.errorBody()?.run { Failure(mapError(HttpException(response))!!) }

    result ?: errorResult!!
  } catch (error: Throwable) {
    if (BuildConfig.DEBUG) {
      error.printStackTrace()
    }
    Failure(mapError(error)!!)
  }
}

private val serverErrorCodes = 500..600
private val authErrorCodes = 400..499

private fun mapError(error: Throwable?): Throwable? = when {
  error is JsonParseException -> ApiDataTransformException
  error is IOException || error is ConnectException -> NetworkException
  error is HttpException && error.code() in serverErrorCodes -> ServerError
  error is HttpException && error.code() in authErrorCodes -> AuthError
  else -> error
}
