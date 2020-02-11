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
package com.xplorer.projectx.ui.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.AddressComponents
import com.google.android.libraries.places.api.model.Place

import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentCityBinding
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.ui.adapter.CityPhotoRecyclerAdapter
import com.xplorer.projectx.utils.convertToString
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CityFragment : DaggerFragment(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModelCity: CitySearchViewModel
    private lateinit var binding: FragmentCityBinding
    lateinit var place: Place
    private var areaName = "n/a"

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_city, container, false)
        viewModelCity = ViewModelProvider(this, viewModelFactory)
            .get(CitySearchViewModel::class.java)
        observeViewState()

        areaName = getAreaNameForCity(place)

        if(areaName != "n/a") {
            viewModelCity.getPhotoData("${place.name!!}, $areaName")
        } else {
            viewModelCity.getPhotoData(place.name!!)
        }

        viewModelCity.confirmCoordinatesForCity(place.name!!, place.latLng!!.convertToString())
        displaceCityPhotos()

        val mapFragment = childFragmentManager.findFragmentById(R.id.cityMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        (activity as AppCompatActivity).setSupportActionBar(binding.cityFragmentToolBar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.cityFragmentToolBar!!.title = place.name!!
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        place = arguments!!.getParcelable("place")!!
    }

    private fun getFourSquareCall() {
        val coordinates = LatLng(6.5243793, 3.3792057) // coordinates for lagos, nigeria
        viewModelCity.getVenueData("restaurants", coordinates)
    }

    private fun getAlternateConfirmation(place: Place) {

        if(areaName == "n/a") {
            Toast.makeText(
                activity,
                "Location cannot be confirmed. No area name could be confirmed",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModelCity.altConfirmCoordinatesForCity("${place.name!!}, $areaName",
            place.latLng!!.convertToString())
    }

    private fun getAreaNameForCity(place: Place): String {
        val addressComponents = place.addressComponents
        val countryComponent = getAddressComponent(addressComponents!!, "country")
        if (countryComponent == null) {
            Toast.makeText(
                activity,
                "Location cannot be confirmed. No country available for this city",
                Toast.LENGTH_SHORT
            ).show()
            return "n/a"
        }

        if (countryComponent.shortName == "US" || countryComponent.shortName == "CA") {
            val stateComponent =
                getAddressComponent(addressComponents, "administrative_area_level_1")
            if (stateComponent != null) {
                return stateComponent.name
            }
        } else {
            return countryComponent.name
        }

        return "n/a"
    }

    private fun getAddressComponent(
      addressComponents: AddressComponents,
      componentName: String
    ): AddressComponent? {

        val components = addressComponents.asList()
        for (x in 0 until components.size) {
            if (components[x].types[0] == componentName) {
                return components[x]
            }
        }

        return null
    }

    private fun observeViewState() {

        // venue state observers
        viewModelCity.successVenueLiveData.observe(viewLifecycleOwner, Observer<List<Venue>> { venues ->
            Toast.makeText(activity, "Total places found: ${venues.size}", Toast.LENGTH_SHORT).show()
        })

        viewModelCity.errorVenueLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        // wikipedia state observer
        // main location confirmation
        viewModelCity.coordConfirmationLiveData.observe(viewLifecycleOwner, Observer { confirmed ->
            when (confirmed) {
                true -> {
                    Toast.makeText(activity, "Location confirmed. Load wiki page in chrome tab.", Toast.LENGTH_SHORT).show()
                    viewModelCity.setCityInformationData(place.name!!)
                }
                false -> {
                    Toast.makeText(activity, "Location cannot be confirmed. Using alternative confirmation", Toast.LENGTH_SHORT).show()
                    getAlternateConfirmation(place)
                }
            }
        })

        viewModelCity.errorCoordConfirmationLiveData.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        })

        // alternative location confirmation
        viewModelCity.altCoordConfirmationLiveData.observe(viewLifecycleOwner, Observer { altConfirmed ->
            when (altConfirmed) {
                true -> {
                    Toast.makeText(activity, "Alternative confirmation for Location worked. Load city data", Toast.LENGTH_SHORT).show()
                    viewModelCity.setCityInformationData("${place.name!!}, $areaName")
                }
                false -> {
                    Toast.makeText(activity, "Location cannot be confirmed. Use relevant posts", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // set city information observers
        viewModelCity.cityInfoLiveData.observe(viewLifecycleOwner, Observer { cityInfo ->
            val shortenedSummary = "${cityInfo.citySummary.substring(0, Math.min(cityInfo.citySummary.length, 500))}..."
            Toast.makeText(activity, "City summary: $shortenedSummary", Toast.LENGTH_LONG).show()

        })


        viewModelCity.successRelatedTitlesLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Total number of relevant posts for this city: ${it.size}", Toast.LENGTH_LONG).show()
        })

        viewModelCity.errorRelatedTitlesLiveData.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        })
    }

    private fun displaceCityPhotos() {
        val photoList = listOf<Photo>()
        binding.cityPhotoRecyclerView.hasFixedSize()
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.cityPhotoRecyclerView.layoutManager = layoutManager
        val cityPhotoRecyclerAdapter = CityPhotoRecyclerAdapter(photoList, context!!)
        binding.cityPhotoRecyclerView.adapter = cityPhotoRecyclerAdapter
        viewModelCity.successPhotoLiveData.observe(viewLifecycleOwner, Observer {
            cityPhotoRecyclerAdapter.setList(it)
        })
        binding.cityPhotoTitle.text = place.name + "'s" + " photos"
        viewModelCity.errorPhotoLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }




    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 12.0f))
        googleMap.addMarker(MarkerOptions().position(place.latLng!!).title(place.name))
    }
}
