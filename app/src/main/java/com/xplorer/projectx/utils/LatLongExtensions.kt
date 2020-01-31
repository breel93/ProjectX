package com.xplorer.projectx.utils

import com.google.android.gms.maps.model.LatLng

fun LatLng.convertToString(): String {
    return "${this.latitude},${this.longitude}"
}