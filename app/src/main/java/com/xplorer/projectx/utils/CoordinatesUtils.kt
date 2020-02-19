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

import com.google.android.gms.maps.model.LatLng

class CoordinatesUtils {
  companion object {
    fun calculateCoordsDiff(coords1: LatLng, coords2: LatLng): Double {
      return Math.abs((coords1.latitude - coords2.latitude) + (coords1.longitude - coords2.longitude))
    }

    fun convertToCoordinates(coordinates: String): LatLng {
      val coordinateBlock = coordinates.split(",")
      val lat = formatCoordinates(coordinateBlock[0].toDouble())
      val lon = formatCoordinates(coordinateBlock[1].toDouble())
      return LatLng(lat, lon)
    }

    fun formatCoordinates(coordValue: Double): Double {
      val digitFormat = "%.3f"
      return String.format(digitFormat, coordValue).toDouble()
    }
  }
}
