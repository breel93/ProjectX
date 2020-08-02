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
package com.xplorer.projectx.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentSearchStartBinding
import com.xplorer.projectx.model.CityModel
import com.xplorer.projectx.ui.adapter.recent.RecentCitiesAdapter
import com.xplorer.projectx.utils.toCityModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SearchStartFragment : DaggerFragment() {

  private lateinit var binding: FragmentSearchStartBinding
  private lateinit var placesClients: PlacesClient

  lateinit var navController: NavController

  @Inject
  lateinit var searchViewModel: SearchStartViewModel
  private lateinit var recentCityAdapter: RecentCitiesAdapter
  private val recentCities = ArrayList<CityModel>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(
      inflater,
      R.layout.fragment_search_start, container, false
    )

    getPlaceAutocomplete()

    recentCityAdapter = RecentCitiesAdapter(context!!, recentCities) { cityModel ->
      navigateToCityFragment(cityModel)
    }

    binding.recentCitiesListView.adapter = recentCityAdapter
    observeRecentCities()

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    navController = Navigation.findNavController(view)
    observeRecentCities()
    searchViewModel.getRecentlySearchedCities()
  }

  private fun observeRecentCities() {
    searchViewModel.recentCitiesLiveData.observe(viewLifecycleOwner, Observer { cityList ->
      recentCityAdapter.setRecentCities(cityList)
    })

    searchViewModel.addCityLiveData.observe(viewLifecycleOwner, Observer { updateCompleted ->
      if (updateCompleted) {
        searchViewModel.getRecentlySearchedCities()
      }
    })
  }

  private fun getPlaceAutocomplete() {
    if (!Places.isInitialized()) {
      Places.initialize(context!!, getString(R.string.google_api_key))
    }
    placesClients = Places.createClient(context!!)
    // Initialize the AutocompleteSupportFragment.
    val autocompleteFragment = childFragmentManager
      .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
    // Specify the types of place data to return.
    // Missing autocomplete Place field = Place.Field.LAT_LNG
    // getting places from autocomplete returns null values for latlong without this field
    autocompleteFragment.setPlaceFields(
      listOf(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS,
        Place.Field.LAT_LNG,
        Place.Field.ADDRESS_COMPONENTS
      )
    )
    autocompleteFragment.setTypeFilter(TypeFilter.CITIES)

    // Set up a PlaceSelectionListener to handle the response.
    autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
      override fun onPlaceSelected(place: Place) {

        val cityModel = place.toCityModel()

        navigateToCityFragment(cityModel)
      }

      override fun onError(status: Status) {
      }
    })
  }

  private fun navigateToCityFragment(cityModel: CityModel) {
    val bundle = bundleOf(
      "place" to cityModel
    )

    searchViewModel.addCityToRecent(cityModel)
    navController.navigate(R.id.cityFragment, bundle)
  }

  fun onBackPressed() {
    // handle back press event
  }
}
