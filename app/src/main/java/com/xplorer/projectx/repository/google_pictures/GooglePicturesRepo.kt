package com.xplorer.projectx.repository.google_pictures

import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.model.unsplash.Photo

interface GooglePicturesRepo {
  fun getGooglePicturesData(place_id: String, onComplete: ((Result<List<Photo>>) -> Unit))
}