package com.xplorer.projectx.model.wikipedia

import com.google.gson.annotations.SerializedName

data class WikiGeoQuery(@SerializedName("geosearch") val geoSearchList: List<WikiGeoItem>)