package com.xplorer.projectx.ui

import com.xplorer.projectx.model.unsplash.Photo

interface PhotoClickListener {
  fun showFullPhoto(photo: Photo)
}