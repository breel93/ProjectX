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
package com.xplorer.projectx.model.unsplash

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
  @SerializedName("id") val id: String,
  @SerializedName("created_at") val created_at: String,
  @SerializedName("updated_at") val updated_at: String,
  @SerializedName("width") val width: Int,
  @SerializedName("height") val height: Int,
  @SerializedName("color") val color: String,
  @SerializedName("description") val description: String,
  @SerializedName("alt_description") val alt_description: String,
  @SerializedName("urls") val urls: Urls,
  @SerializedName("likes") val likes: Int,
  @SerializedName("photo_reference") val photo_reference: String?
) : Parcelable
