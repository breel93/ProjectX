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
package com.xplorer.projectx.model.wikipedia

import com.google.gson.annotations.SerializedName
import com.xplorer.projectx.extentions.Mappable

data class WikiCitySummaryResponse(@SerializedName("query") val query: WikiQuery) :
  Mappable<WikiCityInfo> {

  override fun mapToData(): WikiCityInfo {

    val cityName = query.pageObject.entries.first().value.title
    val summary = query.pageObject.entries.first().value.summary

    return WikiCityInfo(cityName, summary!!)
  }
}
