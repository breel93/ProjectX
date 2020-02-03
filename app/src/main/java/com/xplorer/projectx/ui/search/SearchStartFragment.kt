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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.AddressComponents
import com.xplorer.projectx.databinding.FragmentSearchStartBinding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.xplorer.projectx.R
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.ui.city.CitySearchViewModel
import com.xplorer.projectx.utils.convertToString
import com.xplorer.projectx.utils.convertToStringForNearbyPosts
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SearchStartFragment : DaggerFragment() {

    private lateinit var binding: FragmentSearchStartBinding
    private lateinit var placesClients: PlacesClient

    lateinit var navController: NavController

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search_start, container, false)

        getPlaceAutocomplete()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
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
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS_COMPONENTS))
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES)

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val bundle = bundleOf(
                    "place" to place
                )
                navController.navigate(R.id.cityFragment,bundle)
            }
            override fun onError(status: Status) {
            }
        })
    }

}
