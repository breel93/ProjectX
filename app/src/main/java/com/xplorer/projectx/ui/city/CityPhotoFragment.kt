package com.xplorer.projectx.ui.city


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xplorer.projectx.R
import dagger.android.support.DaggerFragment

/**
 * A simple [Fragment] subclass.
 */
class CityPhotoFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_photo, container, false)
    }


}
