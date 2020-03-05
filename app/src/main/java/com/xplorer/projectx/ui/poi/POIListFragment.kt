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
package com.xplorer.projectx.ui.poi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.PlacesListContainerBinding
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.model.foursquare.VenueLocation
import com.xplorer.projectx.ui.adapter.poi.venues.VenueListAdapter
import com.xplorer.projectx.ui.adapter.snap.SnapOnScrollListener

class POIListFragment : Fragment() {

  private lateinit var binding: PlacesListContainerBinding
  private val venueList = ArrayList<Venue>()
  private lateinit var venueListAdapter: VenueListAdapter
  private lateinit var venueRecycler: RecyclerView
  private val snapHelper = LinearSnapHelper()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate(LayoutInflater.from(context),
      R.layout.places_list_container,
      container,
      false)

    val venue1 = Venue("",
      "The Place",
      VenueLocation("South Side", 0f, 0f))

    for (x in 0 until 10) {
      venueList.add(venue1)
    }

    venueRecycler = binding.placeListRecycler

    venueListAdapter = VenueListAdapter(context!!, venueList) { selectedVenue ->
      Toast.makeText(context,
        "Clicked on venue: ${selectedVenue.venueName}",
        Toast.LENGTH_SHORT)
        .show()
    }

    venueRecycler.layoutManager = LinearLayoutManager(context,
      LinearLayoutManager.HORIZONTAL,
      false)

    venueRecycler.adapter = venueListAdapter
    snapHelper.attachToRecyclerView(venueRecycler)

    val snapOnScrollListener = SnapOnScrollListener(snapHelper,
      SnapOnScrollListener.NotifyBehavior.NOTIFY_ON_STATE_IDLE
    ) { snapPosition ->
      Toast.makeText(context, "Snapped at position: $snapPosition", Toast.LENGTH_SHORT).show()
    }

    venueRecycler.addOnScrollListener(snapOnScrollListener)

    return binding.root
  }
}
