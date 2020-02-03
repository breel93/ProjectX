package com.xplorer.projectx.ui.city


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.AddressComponents
import com.google.android.libraries.places.api.model.Place

import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentCityBinding
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.utils.convertToString
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CityFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModelCity: CitySearchViewModel
    private lateinit var binding: FragmentCityBinding

    lateinit var place: Place

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_city, container, false)
        viewModelCity = ViewModelProviders.of(this, viewModelFactory)
            .get(CitySearchViewModel::class.java)

        observeViewState()
//        getUnsplashCall()
//        getFourSquareCall()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        place = arguments!!.getParcelable("place")!!

        Toast.makeText(activity, place.name, Toast.LENGTH_SHORT).show()
    }

    private fun getFourSquareCall() {
        val coordinates = LatLng(6.5243793, 3.3792057) // coordinates for lagos, nigeria
        viewModelCity.getVenueData("restaurants", coordinates)
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

        viewModelCity.altConfirmCoordinatesForCity(place.name!!, areaName, place.latLng!!.convertToString())
        //viewModelCity.confirmCoordinatesForCity(place.name!!,  place.latLng!!.convertToString())
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
        viewModelCity.getPhotoData("lagos")
    }

    private fun observeViewState() {

        // photo state observers
        viewModelCity.successPhotoLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.size.toString(), Toast.LENGTH_LONG).show()
        })

        viewModelCity.errorPhotoLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        // venue state observers
        viewModelCity.successVenueLiveData.observe(viewLifecycleOwner, Observer<List<Venue>> { venues ->
            Toast.makeText(activity, "Total places found: ${venues.size}", Toast.LENGTH_SHORT).show()
        })

        viewModelCity.errorVenueLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        // wikipedia state observer
        viewModelCity.coordConfirmationLiveData.observe(viewLifecycleOwner, Observer { confirmed ->
            when(confirmed) {
                true -> Toast.makeText(activity, "Location confirmed. Load wiki page in chrome tab.", Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(activity, "Location cannot be confirmed. Use alternative confirmation", Toast.LENGTH_SHORT).show()
            }
        })

        viewModelCity.errorCoordConfirmationLiveData.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        })

        viewModelCity.successRelatedTitlesLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Total number of relevant posts for this city: ${it.size}", Toast.LENGTH_LONG).show()
        })

        viewModelCity.errorRelatedTitlesLiveData.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        })
    }

}
