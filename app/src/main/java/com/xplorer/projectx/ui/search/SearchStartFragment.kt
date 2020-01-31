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
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import com.xplorer.projectx.utils.convertToString
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SearchStartFragment : DaggerFragment() {

    private lateinit var binding: FragmentSearchStartBinding
    private lateinit var placesClients: PlacesClient
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SearchViewModel

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search_start, container, false)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
        observeViewState()
        getPlaceAutocomplete()
//        getUnsplashCall()
//        getFourSquareCall()

        return binding.root
    }

    private fun getFourSquareCall() {
        val coordinates = LatLng(6.5243793, 3.3792057) // coordinates for lagos, nigeria
        viewModel.getVenueData("restaurants", coordinates)
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
//                viewModel.confirmCoordinatesForCity(place.name!!, place.latLng!!.convertToString()) // used to confirm city post on wikipedia
                getAlternateConfirmation(place)
            }

            override fun onError(status: Status) {
            }
        })
    }

    private fun getAlternateConfirmation(place: Place) {
        val addressComponents = place.addressComponents
        val countryComponent = getAddressComponent(addressComponents!!, "country")
        if(countryComponent == null) {
            Toast.makeText(activity, "Location cannot be confirmed. No country available for this city", Toast.LENGTH_SHORT).show()
            return
        }

        var areaName = "n/a"
        if(countryComponent.shortName == "US" || countryComponent.shortName == "CA") {
            val stateComponent = getAddressComponent(addressComponents, "administrative_area_level_1")
            if(stateComponent != null) {
                areaName = stateComponent.name
            }
        } else {
            areaName = countryComponent.name
        }

        if(areaName == "n/a") {
            Toast.makeText(activity, "Location cannot be confirmed. No area name could be confirmed", Toast.LENGTH_SHORT).show()
        }

        viewModel.altConfirmCoordinatesForCity(place.name!!, areaName, place.latLng!!.convertToString())
        //viewModel.confirmCoordinatesForCity(place.name!!,  place.latLng!!.convertToString())
    }

    private fun getAddressComponent(addressComponents: AddressComponents,
                                    componentName: String) : AddressComponent? {

        val components = addressComponents.asList()
        for(x in 0 until components.size) {
            if(components[x].types[0] == componentName) {
               return components[x]
            }
        }

        return null
    }

    private fun getUnsplashCall() {
        viewModel.getPhotoData("lagos")
    }

    private fun observeViewState() {

        // photo state observers
        viewModel.successPhotoLiveData.observe(this, Observer {
            Toast.makeText(context, it.size.toString(), Toast.LENGTH_LONG).show()
        })

        viewModel.errorPhotoLiveData.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        // venue state observers
        viewModel.successVenueLiveData.observe(this, Observer<List<Venue>> { venues ->
            Toast.makeText(activity, "Total places found: ${venues.size}", Toast.LENGTH_SHORT).show()
        })

        viewModel.errorVenueLiveData.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        // wikipedia state observer
        viewModel.coordConfirmationLiveData.observe(this, Observer { confirmed ->
            when(confirmed) {
                true -> Toast.makeText(activity, "Location confirmed. Load wiki page in chrome tab.", Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(activity, "Location cannot be confirmed. Use alternative confirmation", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.errorCoordConfirmationLiveData.observe(this, Observer { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        })
    }
}
