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
package com.xplorer.projectx.utils

import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.Place
import com.xplorer.projectx.model.CityModel

fun Place.toCityModel(): CityModel {
  return CityModel().apply {
    placeId = id!!
    cityName = name!!
    countryName = getCountry()?.name ?: "n/a"
    shortCountryName = getCountry()?.shortName ?: "n/a"
    adminArea = getAreaName()
    shortAdminArea = if (shortCountryName == "US" || shortCountryName == "CA") {
      getState()?.shortName ?: shortCountryName
    } else {
      shortCountryName
    }

    latLng?.let {
      lat = it.latitude
      lon = it.longitude
    }
  }
}

fun Place.getState(): AddressComponent? {
  return getAddressComponent("administrative_area_level_1")
}

fun Place.getCountry(): AddressComponent? {
  return getAddressComponent("country")
}

fun Place.getAddressComponent(componentName: String): AddressComponent? {
  val components = addressComponents!!.asList()
  for (x in 0 until components.size) {
    if (components[x].types[0] == componentName) {
      return components[x]
    }
  }

  return null
}

fun Place.getAreaName(): String {

  val countryComp = getCountry() ?: return "n/a"

  if (countryComp.shortName == "US" || countryComp.shortName == "CA") {
    val stateComp = getState() ?: return countryComp.name
    return stateComp.name
  }

  return countryComp.name
}
