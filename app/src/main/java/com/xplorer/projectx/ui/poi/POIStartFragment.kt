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
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.PlacesOfInterestLayoutBinding
import com.xplorer.projectx.ui.adapter.PlacesOfInterestAdapter
import com.xplorer.projectx.ui.city.CityMapViewModel
import com.xplorer.projectx.utils.RecyclerViewUtils
import dagger.android.support.DaggerFragment
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.ArrayList

class POIStartFragment : DaggerFragment(), POISelectionListener {



  private lateinit var bottomNavController: NavController
  private lateinit var binding: PlacesOfInterestLayoutBinding
  private lateinit var poiAdapter: PlacesOfInterestAdapter
  private val poiList = ArrayList<String>()

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var sharedCityMapViewModel: CityMapViewModel

  private var savedView: View? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    if(savedView == null) {

      binding = DataBindingUtil.inflate(inflater,
        R.layout.places_of_interest_layout,
        container,
        false)

      val placesOfInterest = resources.getStringArray(R.array.place_of_interest_list).toList() as ArrayList<String>
      placesOfInterest.shuffle()

      for (i in 0 until 5) {
        poiList.add(placesOfInterest[i])
      }

      poiList.add(getString(R.string.more_poi_text))

      val spanCount = RecyclerViewUtils.getDynamicSpanCount(context,
        resources.getDimensionPixelSize(R.dimen.poi_item_size),
        resources.getDimensionPixelSize(R.dimen.poi_home_column_spacing))

      val gridLM = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)
      binding.poiFullRecyclerView.layoutManager = gridLM

      savedView = binding.root
      return savedView
    }

    return savedView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    observePOIChanges()

    poiAdapter = PlacesOfInterestAdapter(poiList) { selectedPOI ->
      if (selectedPOI.toLowerCase(Locale.getDefault()) ==
        getString(R.string.more_poi_text).toLowerCase(Locale.getDefault())) {
        val actionBottomSheetFragment = FullPOIListFragment(this)
        actionBottomSheetFragment.show(childFragmentManager, "")
      } else {
        sharedCityMapViewModel.clearPlacesOfInterest()
        sharedCityMapViewModel.setCurrentPOI(selectedPOI)
      }
    }

    binding.poiFullRecyclerView.adapter = poiAdapter

    requireActivity().onBackPressedDispatcher.addCallback(this,
      object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

          savedView = null

          if(isEnabled) {
            isEnabled = false
            requireActivity().onBackPressed()
          }
        }
      })

    bottomNavController = Navigation.findNavController(view)
  }

  private fun observePOIChanges() {
    sharedCityMapViewModel = parentFragment?.let {
      ViewModelProvider(it, viewModelFactory).get(CityMapViewModel::class.java)
    }!!

    sharedCityMapViewModel
      .currentPOILiveData
      .observe(viewLifecycleOwner, Observer { selectedPOI ->

        if (selectedPOI != null) {
          val poiBundle = bundleOf(
            "poi" to selectedPOI
          )

          bottomNavController.navigate(R.id.poiListFragment, poiBundle)
        }
      })
  }

  override fun onSelectPOI(poi: String) {
    sharedCityMapViewModel.setCurrentPOI(poi)
  }
}
