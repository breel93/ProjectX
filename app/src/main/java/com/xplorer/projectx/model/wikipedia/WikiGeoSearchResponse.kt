package com.xplorer.projectx.model.wikipedia

import com.google.gson.annotations.SerializedName
import com.xplorer.projectx.networkin_exp.Mappable

data class WikiGeoSearchResponse(@SerializedName("query") val geoSearchQuery: WikiGeoQuery)
    : Mappable<List<String>> {

    override fun mapToData(): List<String> {
        if(geoSearchQuery.geoSearchList.isEmpty()) {
            return emptyList()
        }

        val geoSearchTitles = ArrayList<String>()
        val geoSearchItems = geoSearchQuery.geoSearchList

        for(x in geoSearchItems.indices) {
            geoSearchTitles.add(geoSearchItems[x].title)
        }

        return geoSearchTitles
    }
}