package com.xplorer.projectx.extentions

import android.util.Log
import com.xplorer.projectx.networkin_exp.Failure
import com.xplorer.projectx.networkin_exp.Mappable
import com.xplorer.projectx.networkin_exp.Result
import com.xplorer.projectx.networkin_exp.Success
import retrofit2.Call
import retrofit2.HttpException

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

// T is the response from the network call, R is the data to return to the viewModel
fun <T: Mappable<R>, R: Any> Call<T>.getResult(): Result<R> {
    val call = clone()
    return try {
        val response = call.execute()
        val result = response.body()?.run { Success(mapToData()) }
        val errorResult = response.errorBody()?.run { Failure(HttpException(response)) }

        result ?: errorResult!!
//
//            Timber.e("Error getting data:..${response.body(}")
//            Failure(Throwable("Error with network call"))

    } catch (error: Throwable) {
        error.printStackTrace()
        Failure(error)
    }
}