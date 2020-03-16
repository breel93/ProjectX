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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.PlacesListContainerBinding
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.ui.adapter.poi.venues.VenueListAdapter
import com.xplorer.projectx.ui.adapter.snap.SnapOnScrollListener
import com.xplorer.projectx.ui.city.CityMapViewModel
import com.xplorer.projectx.utils.BrowserUtils
import com.xplorer.projectx.utils.Constants
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class POIListFragment : DaggerFragment() {

  private lateinit var binding: PlacesListContainerBinding
  private val venueList = ArrayList<Venue>()
  private lateinit var venueListAdapter: VenueListAdapter
  private lateinit var venueRecycler: RecyclerView
  private lateinit var loadingIndicator: ProgressBar
  private lateinit var venueErrorLayout: LinearLayout
  private lateinit var errorTextView: TextView
  private lateinit var backButton: ImageButton
  private val snapHelper = LinearSnapHelper()
  private lateinit var bottomNavController: NavController

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
      binding = DataBindingUtil.inflate(LayoutInflater.from(context),
        R.layout.places_list_container,
        container,
        false)

      venueRecycler = binding.placeListRecycler
      loadingIndicator = binding.placeLoadingIndicator
      venueErrorLayout = binding.poiErrorMessage
      errorTextView = binding.errorMessageText
      backButton = binding.backButton

      venueRecycler.layoutManager = LinearLayoutManager(context,
        LinearLayoutManager.HORIZONTAL,
        false)

      savedView = binding.root

      return savedView
    }

    return savedView

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setUpVenueRecyclerView()

    setUpSharedViewModel()

    backButton.setOnClickListener {

      sharedCityMapViewModel.clearErrors()
      sharedCityMapViewModel.clearPlacesOfInterest()
      sharedCityMapViewModel.setCurrentPOI(null)
      bottomNavController.popBackStack()
    }

    val currentPOI = sharedCityMapViewModel.currentPOILiveData.value
    val coordinates = sharedCityMapViewModel.getCurrentCoordnates()

    sharedCityMapViewModel.getVenueData(currentPOI!!, coordinates!!)

    bottomNavController = Navigation.findNavController(view)
  }

  private fun setUpSharedViewModel() {

    // set the viewModel owner to the city map fragment
    sharedCityMapViewModel = parentFragment?.let { parent ->
      parent.parentFragment?.let { cityMapFragment ->
        ViewModelProvider(cityMapFragment, viewModelFactory).get(CityMapViewModel::class.java)
      }
    }!!

    // show the list of places on success
    sharedCityMapViewModel
      .successVenueLiveData
      .observe(viewLifecycleOwner, Observer<List<Venue>> {

        if (it != null) {
          loadingIndicator.visibility = View.GONE
          venueRecycler.visibility = View.VISIBLE

          venueList.addAll(it)
          venueListAdapter.notifyDataSetChanged()
        }

      })

    //
    sharedCityMapViewModel.currentPlaceIndex.observe(viewLifecycleOwner, Observer { index ->
      venueRecycler.smoothScrollToPosition(index)
    })

    // failure
    sharedCityMapViewModel
      .errorVenueLiveData
      .observe(viewLifecycleOwner, Observer { error ->

        if(error != null) {

          loadingIndicator.visibility = View.GONE
          venueRecycler.visibility = View.INVISIBLE
          venueErrorLayout.visibility = View.VISIBLE
          errorTextView.text = error
        }
      })
  }

  private fun setUpVenueRecyclerView() {
    venueListAdapter = VenueListAdapter(venueList) { selectedVenue ->
      val url = Constants.FOURSQUARE_VENUE_INFO_URL + selectedVenue.venueId
      BrowserUtils.launchBrowser(context!!,
        url
        ) {
        // fallback for no chrome on user's device
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
      }
    }

    venueRecycler.adapter = venueListAdapter
    snapHelper.attachToRecyclerView(venueRecycler)

    val snapOnScrollListener = SnapOnScrollListener(
      snapHelper,
      SnapOnScrollListener.NotifyBehavior.NOTIFY_ON_STATE_IDLE
    ) { snapPosition ->
      snapPosition?.let {
        sharedCityMapViewModel.setCurrentMarkerPosition(snapPosition)
      }
    }

    venueRecycler.addOnScrollListener(snapOnScrollListener)
  }

}
