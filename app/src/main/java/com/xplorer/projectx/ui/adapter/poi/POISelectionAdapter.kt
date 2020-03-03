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
package com.xplorer.projectx.ui.adapter.poi

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.PoiListItemBinding

class POISelectionAdapter(
  private val context: Context,
  private val poiList: List<String>,
  private val onItemSelectListener: (String) -> Unit
) : BaseAdapter() {

  @SuppressLint("ViewHolder")
  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

    val poiItemBinding = DataBindingUtil.inflate<PoiListItemBinding>(
      LayoutInflater.from(context),
      R.layout.poi_list_item,
      parent,
      false)

    poiItemBinding.root.setOnClickListener {
      onItemSelectListener(poiList[position])
    }

    val currentPoi = poiList[position]
    poiItemBinding.poiName.text = currentPoi
    return poiItemBinding.root
  }

  override fun getItem(position: Int): Any {
    return poiList[position]
  }

  override fun getItemId(p0: Int): Long {
    return 0
  }

  override fun getCount(): Int {
    return poiList.size
  }
}
