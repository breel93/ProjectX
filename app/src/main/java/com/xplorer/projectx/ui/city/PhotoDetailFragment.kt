package com.xplorer.projectx.ui.city


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FragmentPhotoDetailBinding
import com.xplorer.projectx.model.CityModel
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.utils.Constants

/**
 * A simple [Fragment] subclass.
 */
class PhotoDetailFragment : BottomSheetDialogFragment() {
  private lateinit var binding: FragmentPhotoDetailBinding

  private val photo: Photo?
    get() = if (arguments!!.getParcelable<Photo>("photo") != null) {
      arguments!!.getParcelable("photo")
    } else {
      null
    }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_detail, container, false)
    showFullPhoto(this.photo!!)
    return binding.root
  }


  private fun showFullPhoto(photo: Photo){
    val circularProgressDrawable = CircularProgressDrawable(context!!)
    circularProgressDrawable.strokeWidth = 10f
    circularProgressDrawable.centerRadius = 40f
    circularProgressDrawable.setColorSchemeColors(
      context!!.resources.getColor(R.color.colorAccent))
    circularProgressDrawable.start()

    val photoUrls : String = if(photo.photo_reference != null){
      Constants.getPhotoWithReferenceURl(photo)
    }else{
      photo.urls.regular
    }
    Glide.with(context!!)
      .load(photoUrls)
      .apply(
        RequestOptions()
          .placeholder(circularProgressDrawable)
          .error(R.drawable.placeholder))
      .into(binding.fullPhoto)

    binding.closePhoto.setOnClickListener{
      dismiss()
    }
  }


  companion object {
    fun getPhoto(photo:Photo): PhotoDetailFragment {
      val fragment = PhotoDetailFragment()
      val args = Bundle()
      args.putParcelable("photo", photo)
      fragment.arguments = args
      return fragment

    }
  }


}
