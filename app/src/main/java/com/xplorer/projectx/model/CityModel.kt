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
package com.xplorer.projectx.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityModel(
  @SerializedName("place_id") var placeId: String = "",
  @SerializedName("name") var cityName: String = "",
  @SerializedName("country") var countryName: String = "",
  @SerializedName("country_short") var shortCountryName: String = "",
  @SerializedName("admin_area") var adminArea: String? = "",
  @SerializedName("admin_area_short") var shortAdminArea: String? = "",
  @SerializedName("address") var address: String = "",
  @SerializedName("latitude") var lat: Double = 0.0,
  @SerializedName("longitude") var lon: Double = 0.0
) : Parcelable {

  fun getLatLongString(): String {
    return "${this.lat},${this.lon}"
  }

  fun getLatLongForNearbyPosts(): String {
    return "${this.lat}|${this.lon}"
  }
}

fun CityModel.latLong(): LatLng {
  return LatLng(this.lat, this.lon)
}
