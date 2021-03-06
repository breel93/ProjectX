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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentCityMapBinding
import com.xplorer.projectx.model.CityModel
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.model.latLong
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class CityMapFragment : Fragment(), OnMapReadyCallback {

  private lateinit var binding: FragmentCityMapBinding
  private lateinit var place: CityModel
  private lateinit var parent: AppCompatActivity
  private val MAP_BUNDLE_KEY = "MAP_BUNDLE_KEY_2"
  private lateinit var mapView: MapView
  private lateinit var toolbar: Toolbar
  private lateinit var viewMap: GoogleMap
  private val markerList = ArrayList<Marker>()
  private var previousMarker: Marker? = null
  private var currentMarker: Marker? = null
  private lateinit var selectedIcon: BitmapDescriptor
  private lateinit var unselectedIcon: BitmapDescriptor

  private val sharedCityMapViewModel: CityMapViewModel by viewModels()

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

    unselectedIcon = bitmapDescriptorFromVector(context!!, R.drawable.marker_unselected)!!
    selectedIcon = bitmapDescriptorFromVector(context!!, R.drawable.marker_selected)!!

    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    place = arguments!!.getParcelable("place")!!
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    sharedCityMapViewModel.setCurrentCoordinates(place.getLatLongString())
    sharedCityMapViewModel.successVenueLiveData.observe(viewLifecycleOwner, Observer<List<Venue>> { venues ->
      if (venues != null && venues.isNotEmpty()) {
        try {
          for (venue in venues) {
            markerList.add(viewMap.addMarker(
              MarkerOptions()
                .icon(unselectedIcon)
                .position(LatLng(venue.venueLocation.lat.toDouble(), venue.venueLocation.lon.toDouble()))
                .title(venue.venueName)))
          }

          currentMarker = markerList[0]
          moveToMarker(0)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      } else {
        resetMap()
      }
    })

    sharedCityMapViewModel.currentMarkerPosition.observe(viewLifecycleOwner, Observer { position ->
      moveToMarker(position)
    })

    sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
  }

  private fun moveToMarker(position: Int) {
    previousMarker = currentMarker
    currentMarker = markerList[position]

    previousMarker?.setIcon(unselectedIcon)
    currentMarker?.setIcon(selectedIcon)

    currentMarker?.showInfoWindow()
    viewMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker?.position, 14f))
  }

  private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
      setBounds(0, 0, intrinsicWidth, intrinsicHeight)
      val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
      draw(Canvas(bitmap))
      BitmapDescriptorFactory.fromBitmap(bitmap)
    }
  }

  private fun resetMap() {
    viewMap.clear()
    markerList.clear()
    viewMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLong(), 12.0f))
    viewMap.addMarker(MarkerOptions().position(place.latLong()).title(place.cityName))
  }

  override fun onMapReady(googleMap: GoogleMap) {
    viewMap = googleMap
    viewMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLong(), 12.0f))
    viewMap.addMarker(MarkerOptions().position(place.latLong()).title(place.cityName))
    viewMap.setOnMarkerClickListener { clickedMarker ->
      if (clickedMarker.title != place.cityName) {
        sharedCityMapViewModel.selectPlaceOnPOIList(markerList.indexOf(clickedMarker))
      }
      true
    }
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
