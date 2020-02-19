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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentCityPhotoBinding
import com.xplorer.projectx.model.CityModel
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.ui.PhotoClickListener
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
  private lateinit var viewModel: CityPhotoViewModel
  private lateinit var place: CityModel
  private lateinit var navController: NavController

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
    binding.cityFragmentToolBar!!.title = place.cityName + "'s" + " photos"
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    navController = Navigation.findNavController(view)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    place = arguments!!.getParcelable("place")!!
  }

  private fun displayPictures() {
    binding.photoList.hasFixedSize()
    val photoClickListener = object : PhotoClickListener {
      override fun showFullPhoto(photo: Photo) {
        val actionBottomSheetFragment = PhotoDetailFragment.getPhoto(photo)
        actionBottomSheetFragment.show(childFragmentManager, "")
      }
    }
    val cityPhotoPagedRecyclerAdapter = CityPhotoPagedRecyclerAdapter(context!!, photoClickListener)
    binding.photoList.adapter = cityPhotoPagedRecyclerAdapter
    viewModel.setSearchQuery(place.cityName)
    viewModel.setPlaceId(place.placeId)
    viewModel.refreshPhoto().observe(viewLifecycleOwner, Observer {
      cityPhotoPagedRecyclerAdapter.submitList(it)
    })
  }
}
