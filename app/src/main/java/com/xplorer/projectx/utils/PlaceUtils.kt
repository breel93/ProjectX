package com.xplorer.projectx.utils

import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.AddressComponents
import com.google.android.libraries.places.api.model.Place

class PlaceUtils {
  companion object {
    fun getAreaNameForCity(place: Place): String {
      val addressComponents = place.addressComponents

      val countryComponent = getAddressComponent(addressComponents!!
        , "country") ?: return "n/a"

      if (countryComponent.shortName == "US" || countryComponent.shortName == "CA") {
        val stateComponent =
          getAddressComponent(addressComponents,
            "administrative_area_level_1")

        if (stateComponent != null) {
          return stateComponent.name
        }
      } else {
        return countryComponent.name
      }

      return "n/a"
    }

    fun getAddressComponent(
      addressComponents: AddressComponents,
      componentName: String
    ): AddressComponent? {

      val components = addressComponents.asList()
      for (x in 0 until components.size) {
        if (components[x].types[0] == componentName) {
          return components[x]
        }
      }

      return null
    }

  }


}