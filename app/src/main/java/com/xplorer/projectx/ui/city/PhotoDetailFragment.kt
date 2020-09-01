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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentPhotoDetailBinding
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class PhotoDetailFragment : BottomSheetDialogFragment() {
  private lateinit var binding: FragmentPhotoDetailBinding

  private val photo: Photo?
    get() = if (arguments!!.getParcelable<Photo>("photo") != null) {
      arguments!!.getParcelable("photo")
    } else {
      null
    }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_detail, container, false)
    showFullPhoto(this.photo!!)
    return binding.root
  }

  private fun showFullPhoto(photo: Photo) {
    val circularProgressDrawable = CircularProgressDrawable(requireContext())
    circularProgressDrawable.strokeWidth = 10f
    circularProgressDrawable.centerRadius = 40f
    circularProgressDrawable.setColorSchemeColors(R.color.colorAccent)
    circularProgressDrawable.start()

    val photoUrls: String = if (photo.photo_reference != null) {
      Constants.getPhotoWithReferenceURl(photo)
    } else {
      photo.urls.regular
    }
    Glide.with(requireContext())
      .load(photoUrls)
      .apply(
        RequestOptions()
          .placeholder(circularProgressDrawable)
          .error(R.drawable.placeholder)
      )
      .into(binding.fullPhoto)

    binding.closePhoto.setOnClickListener {
      dismiss()
    }
  }

  companion object {
    fun getPhoto(photo: Photo): PhotoDetailFragment {
      val fragment = PhotoDetailFragment()
      val args = Bundle()
      args.putParcelable("photo", photo)
      fragment.arguments = args
      return fragment
    }
  }
}
