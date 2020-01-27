package com.xplorer.projectx.model

import com.google.gson.annotations.SerializedName

data class PhotoResult (
	@SerializedName("total") val total : Int,
	@SerializedName("total_pages") val total_pages : Int,
	@SerializedName("results") val photo : List<Photo>
)