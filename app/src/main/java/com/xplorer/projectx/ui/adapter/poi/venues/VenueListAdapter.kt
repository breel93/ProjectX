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
package com.xplorer.projectx.ui.adapter.poi.venues

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.LocationListItemBinding
import com.xplorer.projectx.model.foursquare.Venue

class VenueListAdapter(
  private val context: Context,
  private val venueList: List<Venue>,
  private val onItemSelect: (Venue) -> Unit
) :
  RecyclerView.Adapter<VenueListAdapter.VenueViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
    val binding: LocationListItemBinding = DataBindingUtil.inflate(
      LayoutInflater.from(parent.context),
      R.layout.location_list_item,
      parent,
      false)

    return VenueViewHolder(binding, onItemSelect)
  }

  override fun getItemCount(): Int {
    return venueList.size
  }

  override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
    val currentVenue = venueList[position]
    holder.bind(currentVenue)
  }

  inner class VenueViewHolder(
    internal val binding: LocationListItemBinding,
    private val onItemSelect: (Venue) -> Unit
  ) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(venue: Venue) {
      binding.placeName.text = venue.venueName
      binding.placeStatus.run {

        val status = venue.venueStatus?.let { venueStatus ->
          if(venueStatus.isOpen) "open" else "closed"
        } ?: "n/a"

        when(status) {
          "open" -> setTextColor(context.resources.getColor(R.color.quantum_googred))
          "closed" -> setTextColor(context.resources.getColor(R.color.quantum_googgreen))
          "n/a" -> setTextColor(context.resources.getColor(R.color.quantum_grey))

        }

        text = "${context.resources.getString(R.string.open_status)} $status"

      }


      binding.placeCategory.text = venue.venueCategories?.first()?.categoryName ?: "No Category Available"

      itemView.setOnClickListener {
        onItemSelect(venue)
      }
    }
  }
}
