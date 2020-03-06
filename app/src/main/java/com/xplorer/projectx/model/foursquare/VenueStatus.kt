package com.xplorer.projectx.model.foursquare

import com.google.gson.annotations.SerializedName

data class VenueStatus(@SerializedName("isOpen") val isOpen: Boolean,
                       @SerializedName("status") val currentStatus: String)