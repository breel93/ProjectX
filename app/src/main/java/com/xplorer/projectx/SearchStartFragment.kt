package com.xplorer.projectx

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
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

/**
 * A simple [Fragment] subclass.
 */
class SearchStartFragment : Fragment() {

    lateinit var binding: FragmentSearchStartBinding
    private lateinit var placesClients: PlacesClient

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_start, container, false)
        getPlaceAutocomplete()
        return binding.root
    }

    private fun getPlaceAutocomplete() {
        if (!Places.isInitialized()) {
            Places.initialize(context!!, getString(R.string.google_api_key))
        }
        placesClients = Places.createClient(context!!)
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = childFragmentManager.
            findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
            }

            override fun onError(status: Status) {
            }
        })
    }
}
