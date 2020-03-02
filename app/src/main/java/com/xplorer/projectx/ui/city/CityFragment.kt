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

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentCityBinding
import com.xplorer.projectx.model.CityModel
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.model.latLong
import com.xplorer.projectx.model.poi.PlaceOfInterest
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.ui.PhotoClickListener
import com.xplorer.projectx.ui.adapter.CityPhotoRecyclerAdapter
import com.xplorer.projectx.ui.adapter.PlacesOfInterestAdapter
import com.xplorer.projectx.utils.AppPackageUtils
import com.xplorer.projectx.utils.Constants
import com.xplorer.projectx.utils.RecyclerViewUtils
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CityFragment : DaggerFragment(), OnMapReadyCallback, View.OnClickListener {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  lateinit var viewModelCity: CitySearchViewModel
  private lateinit var binding: FragmentCityBinding
  lateinit var place: CityModel
  private var areaName = "n/a"
  private lateinit var wikiArticleName: String
  private lateinit var navController: NavController
  private val MAP_BUNDLE_KEY = "MAP_BUNDLE_KEY"
  private lateinit var mapView: MapView
  private lateinit var toolbar: Toolbar

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(
      inflater,
      R.layout.fragment_city, container, false
    )
    viewModelCity = ViewModelProvider(this, viewModelFactory)
      .get(CitySearchViewModel::class.java)
    observeViewState()

    areaName = place.adminArea!!

    viewModelCity.getGooglePhotos(place.placeId)

    binding.cityAboutTitle.text = "About ${place.cityName}"
    binding.cityAboutLoadingBar.isVisible = true // make loading visible

    viewModelCity.confirmCoordinatesForCity(place.cityName, place.getLatLongString())
    displaceCityPhotos()

    buildPlacesOfInterest()

    mapView = binding.cityMap

    var mapBundle: Bundle? = null
    if (savedInstanceState != null) {
      mapBundle = savedInstanceState.getBundle(MAP_BUNDLE_KEY)
    }
    mapView.onCreate(mapBundle)
    mapView.getMapAsync(this)

    toolbar = binding.cityFragmentToolBar

    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    binding.cityFragmentToolBar!!.title = place.cityName

    // set more city info click listener
    binding.moreAboutCityButton.setOnClickListener(this)
    return binding.root
  }

  private fun buildPlacesOfInterest() {

    binding.morePlacesButton.setOnClickListener(this)

    // Build places of interest items
    val placesOfInterest = ArrayList<PlaceOfInterest>()

    // using dummy entries for now
    placesOfInterest.add(PlaceOfInterest("Restaurants"))
    placesOfInterest.add(PlaceOfInterest("Bars"))
    placesOfInterest.add(PlaceOfInterest("Lounges"))
    placesOfInterest.add(PlaceOfInterest("ATMs"))
    placesOfInterest.add(PlaceOfInterest("Fuel Stations"))
    placesOfInterest.add(PlaceOfInterest("Banks"))

    val placesOfInterestAdapter =
      PlacesOfInterestAdapter(placesOfInterest) { placeType ->
        Toast.makeText(context, "Place type clicked: $placeType", Toast.LENGTH_SHORT).show()
      }

    val spanCount = RecyclerViewUtils.getDynamicSpanCount(context,
      resources.getDimensionPixelSize(R.dimen.poi_item_size),
      resources.getDimensionPixelSize(R.dimen.poi_home_column_spacing))

    val gridLM = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)
    binding.poiRecyclerView.layoutManager = gridLM
    binding.poiRecyclerView.adapter = placesOfInterestAdapter
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    place = arguments!!.getParcelable("place")!!
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    navController = Navigation.findNavController(view)
  }

  private fun getAlternateConfirmation(place: CityModel) {

    if (areaName == "n/a") {
      Toast.makeText(
        activity,
        "Location cannot be confirmed. No area name could be confirmed",
        Toast.LENGTH_SHORT
      ).show()
      return
    }
    viewModelCity.altConfirmCoordinatesForCity(
      "${place.cityName}, $areaName",
      place.getLatLongString()
    )
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
          wikiArticleName = place.cityName
          viewModelCity.setCityInformationData(wikiArticleName)
        }
        false -> {
          getAlternateConfirmation(place)
        }
      }
    })

    viewModelCity.errorCoordConfirmationLiveData.observe(viewLifecycleOwner, Observer { error ->
      binding.cityAboutLoadingBar.isVisible = false // make loading visible
      binding.cityAboutText.apply {
        isVisible = true
        text = "Location cannot be confirmed. Use relevant posts about this city"
      }
    })

    // alternative location confirmation
    viewModelCity.altCoordConfirmationLiveData.observe(
      viewLifecycleOwner,
      Observer { altConfirmed ->
        when (altConfirmed) {
          true -> {
            wikiArticleName = "${place.cityName}, $areaName"
            viewModelCity.setCityInformationData(wikiArticleName)
          }
          false -> {

            binding.cityAboutLoadingBar.isVisible = false // make loading visible
            binding.cityAboutText.apply {
              isVisible = true
              text = "Location cannot be confirmed. Use relevant posts about this city"
            }
          }
        }
      })

    // set city information observers
    viewModelCity.cityInfoLiveData.observe(viewLifecycleOwner, Observer { cityInfo ->

      val shortenedSummary =
        "${cityInfo.citySummary.substring(0, Math.min(cityInfo.citySummary.length, 500))}..."

      binding.moreAboutCityButton.isEnabled = true // enable the more button
      binding.cityAboutLoadingBar.isVisible = false // make loading visible
      binding.cityAboutText.apply {
        isVisible = true
        text = shortenedSummary
      }
    })

    viewModelCity.successRelatedTitlesLiveData.observe(viewLifecycleOwner, Observer {
      Toast.makeText(
        context,
        "Total number of relevant posts for this city: ${it.size}",
        Toast.LENGTH_LONG
      ).show()
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
    val photoClickListener = object : PhotoClickListener {
      override fun showFullPhoto(photo: Photo) {
        val actionBottomSheetFragment = PhotoDetailFragment.getPhoto(photo)
        actionBottomSheetFragment.show(childFragmentManager, "")
      }
    }
    val cityPhotoRecyclerAdapter =
      CityPhotoRecyclerAdapter(photoList, context!!, photoClickListener)
    binding.cityPhotoRecyclerView.adapter = cityPhotoRecyclerAdapter
    viewModelCity.successPhotoLiveData.observe(viewLifecycleOwner, Observer {
      cityPhotoRecyclerAdapter.setList(it)
    })
    binding.cityPhotoTitle.text = place.cityName + "'s" + " photos"
    viewModelCity.errorPhotoLiveData.observe(viewLifecycleOwner, Observer {
      Toast.makeText(context, it, Toast.LENGTH_LONG).show()
    })
    binding.morePhotoText.setOnClickListener {
      val bundle = bundleOf(
        "place" to place
      )
      navController.navigate(R.id.cityPhotoFragment, bundle)
    }
  }

  override fun onMapReady(googleMap: GoogleMap) {
    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLong(), 12.0f))
    googleMap.addMarker(MarkerOptions().position(place.latLong()).title(place.cityName))
    googleMap.setOnMapClickListener {
      goToCityMap()
    }
  }

  override fun onClick(v: View?) {
    when (v!!.id) {
      R.id.moreAboutCityButton -> {
        if (AppPackageUtils.appInstalled(context!!, "com.android.chrome")) {
          launchWikiChromeTab()
        } else {
          launchWikiWebView()
        }
      }

      R.id.morePlacesButton -> {
        goToCityMap()
      }

      R.id.cityMap -> {
        goToCityMap()
      }
    }
  }

  private fun goToCityMap(placeOfInterest: String = "") {
    val extras = bundleOf(
      "place" to place,
      "poi" to placeOfInterest
    )

    val sharedExtras = FragmentNavigatorExtras(
      mapView to resources.getString(R.string.city_map_transition_name),
      toolbar to resources.getString(R.string.toolbar_transition_name)
    )

    navController.navigate(R.id.cityMapFragment, extras, null, sharedExtras)
  }

  private fun launchWikiWebView() {
    val wikiBundle = bundleOf(
      "wikilink" to Constants.WIKIPEDIA_INFO_URL + wikiArticleName,
      "title" to wikiArticleName
    )

    navController.navigate(R.id.cityDescription, wikiBundle)
  }

  private fun launchWikiChromeTab() {
    val wikiTabIntent = CustomTabsIntent.Builder().apply {

      setShowTitle(true)

      context?.let {
        setExitAnimations(
          it,
          android.R.anim.fade_in,
          android.R.anim.fade_out
        )
      }
    }.build()

    wikiTabIntent.launchUrl(
      context,
      Uri.parse(Constants.WIKIPEDIA_INFO_URL + wikiArticleName)
    )
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

//
//
