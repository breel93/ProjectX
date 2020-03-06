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
import androidx.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng

import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentCityMapBinding
import com.xplorer.projectx.model.CityModel
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.model.latLong
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CityMapFragment : DaggerFragment(), OnMapReadyCallback {

  private lateinit var binding: FragmentCityMapBinding
  private lateinit var place: CityModel
  private lateinit var parent: AppCompatActivity
  private val MAP_BUNDLE_KEY = "MAP_BUNDLE_KEY_2"
  private lateinit var mapView: MapView
  private lateinit var toolbar: Toolbar
  private lateinit var viewMap: GoogleMap
  private lateinit var previousMarker: Marker

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var sharedCityMapViewModel: CityMapViewModel


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_map, container, false)
    mapView = binding.cityMap

    var mapBundle: Bundle? = null
    if (savedInstanceState != null) {
      mapBundle = savedInstanceState.getBundle(MAP_BUNDLE_KEY)
    }
    mapView.onCreate(mapBundle)
    mapView.getMapAsync(this)

    parent = activity as AppCompatActivity
    toolbar = binding.mapFragmentToolbar

    parent.setSupportActionBar(toolbar)
    parent.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    parent.supportActionBar!!.title = "Explore ${place.cityName}"


    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    place = arguments!!.getParcelable("place")!!
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)


    // Using the city map fragment as the lifecycle owner for the viewModel provider
    // To share its resources with the children fragment
    sharedCityMapViewModel =  ViewModelProvider(this, viewModelFactory).get(CityMapViewModel::class.java)

    sharedCityMapViewModel.setCurrentCoordinates(place.getLatLongString())

    sharedCityMapViewModel.successVenueLiveData.observe(viewLifecycleOwner, Observer<List<Venue>> { venues ->
      if(venues != null) {

//          val icon: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)
        for(venue in venues) {
          viewMap.addMarker(
            MarkerOptions()
              .position(LatLng(venue.venueLocation.lat.toDouble(), venue.venueLocation.lon.toDouble()))
              .title(venue.venueName)).tag = venue.venueName
        }
      } else {
        resetMap()
      }
    })

    sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
  }

  private fun resetMap() {
    viewMap.clear()
    viewMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLong(), 12.0f))
    viewMap.addMarker(MarkerOptions().position(place.latLong()).title(place.cityName))
  }

  override fun onMapReady(googleMap: GoogleMap) {
    viewMap = googleMap
    viewMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLong(), 12.0f))
    viewMap.addMarker(MarkerOptions().position(place.latLong()).title(place.cityName))
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onStop() {
    super.onStop()
    mapView.onStop()
  }

  override fun onStart() {
    super.onStart()
    mapView.onStart()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    var mapViewBundle = outState.getBundle(MAP_BUNDLE_KEY)
    if (mapViewBundle == null) {
      mapViewBundle = Bundle()
      outState.putBundle(MAP_BUNDLE_KEY, mapViewBundle)
    }

    mapView.onSaveInstanceState(mapViewBundle)
  }
}
