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
package com.xplorer.projectx.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.xplorer.projectx.databinding.FragmentSearchStartBinding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.xplorer.projectx.R
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment

/**
 * A simple [Fragment] subclass.
 */
class SearchStartFragment : DaggerFragment() {

    private lateinit var binding: FragmentSearchStartBinding
    private lateinit var placesClients: PlacesClient


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

    private fun getPlaceAutocomplete() {
        if (!Places.isInitialized()) {
            Places.initialize(context!!, getString(R.string.google_api_key))
        }
        placesClients = Places.createClient(context!!)
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = childFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS))
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES)

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
            }

            override fun onError(status: Status) {
            }
        })
    }
}
