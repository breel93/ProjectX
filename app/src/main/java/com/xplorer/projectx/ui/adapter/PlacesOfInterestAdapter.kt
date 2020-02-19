package com.xplorer.projectx.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xplorer.projectx.databinding.PoiItemSmallBinding
import com.xplorer.projectx.model.poi.PlaceOfInterest

class PlacesOfInterestAdapter(private val places: List<PlaceOfInterest>,
                              val onPlaceSelected: (String) -> Unit):
  RecyclerView.Adapter<PlacesOfInterestAdapter.PlacesOfInterestViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesOfInterestViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = PoiItemSmallBinding.inflate(inflater, parent, false)
    return PlacesOfInterestViewHolder(binding)
  }

  override fun getItemCount(): Int {
    return places.size
  }

  override fun onBindViewHolder(holder: PlacesOfInterestViewHolder, position: Int) {
    val poi = places[position]
    holder.bind(poi)
  }

  inner class PlacesOfInterestViewHolder(internal val binding: PoiItemSmallBinding):
    RecyclerView.ViewHolder(binding.root)  {

    fun bind(poi: PlaceOfInterest) {
      binding.poiTypeButton.text = poi.placeType
      binding.poiTypeButton.setOnClickListener { onPlaceSelected(poi.placeType) }
      //    holder.binding.poiImageView
//      .setImageDrawable(context.resources.getDrawable(poi.image, null))
    }
  }
}