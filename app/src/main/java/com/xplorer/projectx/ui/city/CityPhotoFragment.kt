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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.model.Place

import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentCityPhotoBinding
import com.xplorer.projectx.ui.adapter.CityPhotoPagedRecyclerAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CityPhotoFragment : DaggerFragment() {
    internal lateinit var binding: FragmentCityPhotoBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: CityPhotoViewModel
    lateinit var place: Place

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_photo, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CityPhotoViewModel::class.java)
        displayPictures()
        (activity as AppCompatActivity).setSupportActionBar(binding.cityFragmentToolBar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.cityFragmentToolBar!!.title = place.name + "'s" + " photos"
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        place = arguments!!.getParcelable("place")!!
    }

    private fun displayPictures() {
        binding.photoList.hasFixedSize()
        ViewCompat.setNestedScrollingEnabled(binding.photoList, true)
        val cityPhotoPagedRecyclerAdapter = CityPhotoPagedRecyclerAdapter(context!!)
        binding.photoList.adapter = cityPhotoPagedRecyclerAdapter
        viewModel.setSearchQuery(place.name!!)
        viewModel.refreshPhoto().observe(viewLifecycleOwner, Observer {
            cityPhotoPagedRecyclerAdapter.submitList(it)
        })
    }
}
