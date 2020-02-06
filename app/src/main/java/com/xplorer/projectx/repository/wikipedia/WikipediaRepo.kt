package com.xplorer.projectx.repository.wikipedia

import com.xplorer.projectx.networkin_exp.Result

interface WikipediaRepo {

  fun confirmCityCoordinates(cityName: String,
                             cityCoordinates: String,
                             onComplete: ((Result<Boolean>) -> Unit))

  fun getAlternateConfirmation(query: String,
                               cityCoordinates: String,
                               onComplete: (Result<Boolean>) -> Unit)

  fun getRelevantPosts(cityCoordinates: String,
                       onComplete: (Result<List<String>>) -> Unit)
}