package com.xplorer.projectx.model.google

import com.google.gson.annotations.SerializedName
import com.xplorer.projectx.extentions.Mappable
import com.xplorer.projectx.model.unsplash.Photo

data class GooglePhotoResults (
  @SerializedName("result") val result: GooglePhotoDetail
) : Mappable<List<Photo>> {
  override fun mapToData(): List<Photo> {
    return result.result
    }
  }