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