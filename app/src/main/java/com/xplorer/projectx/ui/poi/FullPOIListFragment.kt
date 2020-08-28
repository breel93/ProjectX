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
package com.xplorer.projectx.ui.poi

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.FullPoiListLayoutBinding
import com.xplorer.projectx.ui.adapter.poi.POISelectionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullPOIListFragment(private val poiListener: POISelectionListener) : BottomSheetDialogFragment() {

  private lateinit var binding: FullPoiListLayoutBinding
  private lateinit var poiAdapter: POISelectionAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate(inflater,
      R.layout.full_poi_list_layout,
      container,
      false)

    val placesOfInterest = resources.getStringArray(R.array.place_of_interest_list).toList()

    poiAdapter = POISelectionAdapter(context!!, placesOfInterest) { selectedPOI ->
      poiListener.onSelectPOI(selectedPOI)
      dismiss()
    }

    binding.poiListView.adapter = poiAdapter

    binding.exitPOIBtn.setOnClickListener {
      dismiss()
    }

    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val touchOutsideView = dialog!!
      .window!!
      .decorView.findViewById<View>(com.google.android.material.R.id.touch_outside)

    touchOutsideView.setOnClickListener(null)
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
    bottomSheetDialog.setOnShowListener {
      val bottomSheet = bottomSheetDialog
        .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

      bottomSheet?.let {
        BottomSheetBehavior.from(bottomSheet).apply {
          isHideable = true

          setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
              if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                state = BottomSheetBehavior.STATE_EXPANDED
              }
            }

            override fun onSlide(p0: View, p1: Float) {}
          })
        }
      }
    }

    return bottomSheetDialog
  }
}
