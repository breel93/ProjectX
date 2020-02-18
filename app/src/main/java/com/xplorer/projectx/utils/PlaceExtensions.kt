package com.xplorer.projectx.utils

import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.Place
import com.xplorer.projectx.model.CityModel

fun Place.toCityModel(): CityModel {
  return CityModel().apply {
    placeId = id!!
    cityName = name!!
    countryName = getCountry()?.name ?: "n/a"
    adminArea = getAreaName()

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

  if(countryComp.shortName == "US" || countryComp.shortName == "CA") {
    val stateComp = getState() ?: return countryComp.name

    return stateComp.name

  } else {
     return countryComp.name
  }
}