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
package com.xplorer.projectx.ui.adapter.recent

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.xplorer.projectx.R
import com.xplorer.projectx.databinding.RecentCityItemBinding
import com.xplorer.projectx.model.CityModel

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
class RecentCitiesAdapter(
  private val context: Context,
  private val cityList: List<CityModel>,
  private val onItemSelectListener: (CityModel) -> Unit
) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val cityItemBinding = DataBindingUtil.inflate<RecentCityItemBinding>(
            LayoutInflater.from(context),
            R.layout.recent_city_item,
            parent,
            false)

        cityItemBinding.root.setOnClickListener {
            onItemSelectListener(cityList[position])
        }

        val currentPlace = cityList[position]
        val displayText = if (currentPlace.shortCountryName == "US" ||
            currentPlace.shortCountryName == "CA") {
            "${currentPlace.cityName}, ${currentPlace.shortAdminArea}"
        } else {
            "${currentPlace.cityName}, ${currentPlace.shortCountryName}"
        }

        cityItemBinding.cityNameText.text = displayText
        return cityItemBinding.root
    }

    fun setRecentCities(cities: List<CityModel>) {
        (cityList as ArrayList<CityModel>).clear()
        cityList.addAll(cities)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any {
        return cityList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return cityList.size
    }
}
