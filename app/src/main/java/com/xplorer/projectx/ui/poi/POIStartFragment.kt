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
import androidx.recyclerview.widget.GridLayoutManager
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.PlacesOfInterestLayoutBinding
import com.xplorer.projectx.model.poi.PlaceOfInterest
import com.xplorer.projectx.ui.adapter.PlacesOfInterestAdapter
import com.xplorer.projectx.utils.RecyclerViewUtils
import java.util.Random
import java.util.Locale
import kotlin.collections.ArrayList

class POIStartFragment : Fragment() {

  private lateinit var binding: PlacesOfInterestLayoutBinding
  private lateinit var poiAdapter: PlacesOfInterestAdapter
  private val poiList = ArrayList<PlaceOfInterest>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater,
      R.layout.places_of_interest_layout,
      container,
      false)

    val placesOfInterest = resources.getStringArray(R.array.place_of_interest_list)

    for (i in 0 until 5) {
      poiList.add(PlaceOfInterest(placesOfInterest[i]))
    }

    poiList.shuffle(Random(4))
    poiList.add(PlaceOfInterest(getString(R.string.more_poi_text)))

    poiAdapter = PlacesOfInterestAdapter(poiList) { selectedPOI ->
      if (selectedPOI.toLowerCase(Locale.getDefault()) ==
        getString(R.string.more_poi_text).toLowerCase(Locale.getDefault())) {
        val actionBottomSheetFragment = FullPOIListFragment()
        actionBottomSheetFragment.show(childFragmentManager, "")
      } else {
        Toast.makeText(context, "Selected this place: $selectedPOI", Toast.LENGTH_SHORT).show()
      }
    }

    val spanCount = RecyclerViewUtils.getDynamicSpanCount(context,
      resources.getDimensionPixelSize(R.dimen.poi_item_size),
      resources.getDimensionPixelSize(R.dimen.poi_home_column_spacing))

    val gridLM = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)
    binding.poiFullRecyclerView.layoutManager = gridLM
    binding.poiFullRecyclerView.adapter = poiAdapter

    return binding.root
  }
}
