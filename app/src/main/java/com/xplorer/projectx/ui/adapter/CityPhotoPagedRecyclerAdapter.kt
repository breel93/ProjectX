/**
 *  Designed and developed by ProjectX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.xplorer.projectx.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.PhotoItemBinding
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.ui.PhotoClickListener
import com.xplorer.projectx.utils.Constants.Companion.getPhotoWithReferenceURl

class CityPhotoPagedRecyclerAdapter(
  internal var context: Context,
  private val photoClickListener: PhotoClickListener
) :
  PagedListAdapter<Photo, CityPhotoPagedRecyclerAdapter.PhotoViewHolder>(PhotoDiffCallback) {

  internal lateinit var binding: PhotoItemBinding

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    binding = PhotoItemBinding.inflate(layoutInflater, parent, false)
    return PhotoViewHolder(binding)
  }

  override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
    val photo = getItem(position)
    holder.bind(photo!!, photoClickListener)
  }

  inner class PhotoViewHolder(binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(photo: Photo, photoClickListener: PhotoClickListener) {
      itemView.setOnClickListener { photoClickListener.showFullPhoto(photo) }
      val circularProgressDrawable = CircularProgressDrawable(context)
      circularProgressDrawable.strokeWidth = 10f
      circularProgressDrawable.centerRadius = 40f
      circularProgressDrawable.setColorSchemeColors(R.color.colorAccent)
      circularProgressDrawable.start()
      binding.cityPhoto.aspectRatio = photo.height.toDouble() / photo.width.toDouble()

      val photoUrls: String = if (photo.photo_reference != null) {
        getPhotoWithReferenceURl(photo)
      } else {
        photo.urls.regular
      }
      Glide.with(context)
        .load(photoUrls)
        .apply(
          RequestOptions()
            .placeholder(circularProgressDrawable)
            .error(R.drawable.placeholder)
        )
        .into(binding.cityPhoto)
    }
  }

  companion object {
    val PhotoDiffCallback = object : DiffUtil.ItemCallback<Photo>() {
      override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
      }
    }
  }
}
