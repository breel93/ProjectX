package com.xplorer.projectx.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityModel(@SerializedName("place_id") var placeId: String = "",
                     @SerializedName("name") var cityName: String = "",
                     @SerializedName("country") var countryName: String = "",
                     @SerializedName("admin_area") var adminArea: String? = "",
                     @SerializedName("address") var address: String = "",
                     @SerializedName("latitude") var lat: Double = 0.0,
                     @SerializedName("longitude") var lon: Double = 0.0): Parcelable {

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