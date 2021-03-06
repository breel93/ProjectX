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

import com.google.gson.annotations.SerializedName
import com.xplorer.projectx.extentions.Mappable

data class PhotoResult(
  @SerializedName("total") val total: Int,
  @SerializedName("total_pages") val total_pages: Int,
  @SerializedName("results") val photo: List<Photo>,
  @SerializedName("photos") val photos: List<Photo>
) : Mappable<PhotoResult> {
  override fun mapToData(): PhotoResult {
    return this
  }
}
