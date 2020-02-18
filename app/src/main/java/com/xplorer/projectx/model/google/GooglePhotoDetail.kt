package com.xplorer.projectx.model.google

import com.google.gson.annotations.SerializedName
import com.xplorer.projectx.model.unsplash.Photo

data class GooglePhotoDetail (
  @SerializedName("photos") val result: List<Photo>
)