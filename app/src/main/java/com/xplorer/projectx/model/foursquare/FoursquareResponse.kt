package com.xplorer.projectx.model.foursquare

import com.google.gson.annotations.SerializedName
import com.xplorer.projectx.extentions.Mappable

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

data class FoursquareResponse(@SerializedName("response") val venueResponse: VenueResponse)
    : Mappable<List<Venue>> {

    override fun mapToData(): List<Venue> {
        val recommendedGroup = this.venueResponse.venueGroups[0] // recommended places group
        val venueList = ArrayList<Venue>()

        if(recommendedGroup.venueItemList.isNotEmpty()) {
            for(venueItem in recommendedGroup.venueItemList) {
                venueList.add(venueItem.venue)
            }
        }

        return venueList
    }
}