package com.xplorer.projectx.model

import com.google.gson.annotations.SerializedName

data class Photo (
	@SerializedName("id") val id : String,
	@SerializedName("created_at") val created_at : String,
	@SerializedName("updated_at") val updated_at : String,
	@SerializedName("promoted_at") val promoted_at : String,
	@SerializedName("width") val width : Int,
	@SerializedName("height") val height : Int,
	@SerializedName("color") val color : String,
	@SerializedName("description") val description : String,
	@SerializedName("alt_description") val alt_description : String,
	@SerializedName("urls") val urls : Urls,
	@SerializedName("likes") val likes : Int
)