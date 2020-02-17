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

class CityPhotoPagedRecyclerAdapter(internal var context: Context) :
    PagedListAdapter<Photo, CityPhotoPagedRecyclerAdapter.PhotoViewHolder>(PhotoDiffCallback){

    internal lateinit var binding: PhotoItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = PhotoItemBinding.inflate(layoutInflater,parent,false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo!!)
    }



    inner class PhotoViewHolder(binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo){
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 10f
            circularProgressDrawable.centerRadius = 40f
            circularProgressDrawable.setColorSchemeColors(
                context.resources.getColor(R.color.colorAccent))
            binding.cityPhoto.aspectRatio = photo.height.toDouble() / photo.width.toDouble()
            Glide.with(context)
                .load(photo.urls.regular)
                .apply(
                    RequestOptions()
                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.placeholder))
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