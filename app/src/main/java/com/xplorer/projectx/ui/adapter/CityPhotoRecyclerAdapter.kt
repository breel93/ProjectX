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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.CityPhotoItemBinding
import com.xplorer.projectx.model.unsplash.Photo

class CityPhotoRecyclerAdapter(private var cityPhotoList: List<Photo>, private val context: Context) :
    RecyclerView.Adapter<CityPhotoRecyclerAdapter.CityPhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityPhotoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CityPhotoItemBinding.inflate(layoutInflater, parent, false)
        return CityPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityPhotoViewHolder, position: Int) {
        val photo = cityPhotoList[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return cityPhotoList.size
    }

    fun setList(photos: List<Photo>) {
        cityPhotoList = photos
        notifyDataSetChanged()
    }

    inner class CityPhotoViewHolder(internal var binding: CityPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 12f
            circularProgressDrawable.centerRadius = 60f
            circularProgressDrawable.setColorSchemeColors(context.resources.getColor(R.color.colorPrimary))
            circularProgressDrawable.start()

            Glide.with(context)
                .load(photo.urls.regular)
                .apply(
                    RequestOptions()
                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.placeholder))
                .into(binding.cityPhoto)
        }
    }
}
