package com.xplorer.projectx.model.wikipedia

import com.google.gson.annotations.SerializedName
import com.xplorer.projectx.extentions.Mappable

data class WikiCitySummaryResponse(@SerializedName("query") val query: WikiQuery):
        Mappable<WikiCityInfo> {

  override fun mapToData(): WikiCityInfo {

    val cityName = query.pageObject.entries.first().value.title
    val summary = query.pageObject.entries.first().value.summary

    return WikiCityInfo(cityName, summary!!)
  }
}