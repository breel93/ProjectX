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
package com.xplorer.projectx.repository.wikipedia

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.xplorer.projectx.api.WikipediaAPI
import com.xplorer.projectx.model.wikipedia.WikiGeoItem
import com.xplorer.projectx.model.wikipedia.WikiQueryTitleResponse
import com.xplorer.projectx.model.wikipedia.WikiQuery
import com.xplorer.projectx.model.wikipedia.WikiGeoQuery
import com.xplorer.projectx.model.wikipedia.WikiPageResult
import com.xplorer.projectx.model.wikipedia.WikiGeoSearchResponse
import com.xplorer.projectx.networkin_exp.Failure
import com.xplorer.projectx.networkin_exp.Result
import com.xplorer.projectx.networkin_exp.Success
import com.xplorer.projectx.networking.CoroutineContextProvider
import com.xplorer.projectx.networking.TestCoroutineContextProviderImpl
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import retrofit2.Call
import retrofit2.Response

class WikipediaRepositoryTest {

  private lateinit var wikipediaAPI: WikipediaAPI
  private lateinit var coroutineProvider: CoroutineContextProvider
  private lateinit var wikiRepo: WikipediaRepository
  private val onCompleteMock = mock<(Result<Boolean>) -> Unit> {}

  private val coordsResultCaptor = argumentCaptor<Result<Boolean>>()
  private val jsoupResultCaptor = argumentCaptor<Result<Boolean>>()
  private val wikiTitleResultCaptor = argumentCaptor<String>()
  private val coordinatesQuerytHtml = "<html><body><span class=\"geo\">%s</span></body></html>"

  @Before
  fun setUp() {
    wikipediaAPI = mock(WikipediaAPI::class.java)
    coroutineProvider = TestCoroutineContextProviderImpl()
    wikiRepo = WikipediaRepository(wikipediaAPI, coroutineProvider)
  }

  @Test
  fun `fetch wiki page document that has no coordinates available`() {

    // arrange

    val successResponse = String.format(coordinatesQuerytHtml, "").toResponseBody(null)

    val mockCall: Call<ResponseBody> = mock()
    whenever(mockCall.execute()).thenReturn(Response.success(successResponse))
    whenever(mockCall.clone()).thenReturn(mockCall)

    whenever(wikipediaAPI.findCoordinates(anyString())).thenReturn(mockCall)

    // act
    wikiRepo.confirmCityCoordinates("city", "19,19", onCompleteMock)

    // assert
    verify(onCompleteMock).invoke(jsoupResultCaptor.capture())
    val errorMsg = "No coordinates found for this location. Please try a different location."
    assertTrue((jsoupResultCaptor.firstValue as Failure).error.localizedMessage == errorMsg)
  }

  @Test
  fun `fetch wiki page document that has coordinates but too far from query city`() {

    val successResponse = String.format(coordinatesQuerytHtml, "23, 19").toResponseBody(null)

    val mockCall: Call<ResponseBody> = mock()
    whenever(mockCall.execute()).thenReturn(Response.success(successResponse))
    whenever(mockCall.clone()).thenReturn(mockCall)

    whenever(wikipediaAPI.findCoordinates(anyString())).thenReturn(mockCall)

    wikiRepo.confirmCityCoordinates("city", "19,19", onCompleteMock)
    verify(onCompleteMock).invoke(jsoupResultCaptor.capture())
    assertTrue(!(jsoupResultCaptor.firstValue as Success).data)
  }

  @Test
  fun `fetch wiki page document that has coordinates approximate to query city`() {

    val successResponse = String.format(coordinatesQuerytHtml, "19.01, 19.01").toResponseBody(null)

    val mockCall: Call<ResponseBody> = mock()
    whenever(mockCall.execute()).thenReturn(Response.success(successResponse))
    whenever(mockCall.clone()).thenReturn(mockCall)

    whenever(wikipediaAPI.findCoordinates(anyString())).thenReturn(mockCall)

    wikiRepo.confirmCityCoordinates("city", "19,19", onCompleteMock)
    verify(onCompleteMock).invoke(jsoupResultCaptor.capture())
    assertTrue((jsoupResultCaptor.firstValue as Success).data)
  }

  @Test
  fun `test alternative confirmation returns no redirect title`() {

    // arrange

    val errorMsg = "No alternative titles found. Please use list of articles fallback"
    val errorBody = "".toResponseBody()

    val mockCall: Call<WikiQueryTitleResponse> = mock()
    whenever(mockCall.execute()).thenReturn(Response.error(404, errorBody))
    whenever(mockCall.clone()).thenReturn(mockCall)

    whenever(wikipediaAPI.getWikiTitle(anyString())).thenReturn(mockCall)

    // act
    wikiRepo.getAlternateConfirmation("query", "19, 19", onCompleteMock)

    // assert
    verify(onCompleteMock).invoke(coordsResultCaptor.capture())
    assertTrue((coordsResultCaptor.firstValue as Failure).error.localizedMessage == errorMsg)
  }

  @Test
  fun `test alternative confirmation returns redirect title and call coordinate confirmation`() {

    // arrange
    val pageQuery = HashMap<String, WikiPageResult>()
    val title = WikiPageResult("Bamako")
    pageQuery["1"] = title

    val pageQueryResult = WikiQuery(pageQuery)
    val successBody = WikiQueryTitleResponse(pageQueryResult)

    // To mock the response when a call is made to the getWikiTitle function in the wikipedia repository
    val mockCall: Call<WikiQueryTitleResponse> = mock()
    whenever(mockCall.execute()).thenReturn(Response.success(successBody))
    whenever(mockCall.clone()).thenReturn(mockCall)
    whenever(wikipediaAPI.getWikiTitle(anyString())).thenReturn(mockCall)

    // To mock the response when a call is made to the findCoordinates function in case of a success
    val coordinatesMockCall: Call<ResponseBody> = mock()
    whenever(coordinatesMockCall.execute()).thenReturn(Response.success("".toResponseBody(null)))
    whenever(coordinatesMockCall.clone()).thenReturn(coordinatesMockCall)
    whenever(wikipediaAPI.findCoordinates(anyString())).thenReturn(coordinatesMockCall)

    // act
    wikiRepo.getAlternateConfirmation("query", "19, 19", onCompleteMock)

    // assert
    verify(wikipediaAPI).findCoordinates(wikiTitleResultCaptor.capture())
    assertTrue(wikiTitleResultCaptor.firstValue == "Bamako")
  }

  @Test
  fun `test get relevant posts returns empty list`() {

    // arrange

    val listOfGeoTerms = ArrayList<WikiGeoItem>()

    val geoQuery = WikiGeoQuery(listOfGeoTerms)
    val successBody = WikiGeoSearchResponse(geoQuery)

    // To mock the response when a call is made to the getWikiTitle function in the wikipedia repository
    val mockCall: Call<WikiGeoSearchResponse> = mock()
    whenever(mockCall.execute()).thenReturn(Response.success(successBody))
    whenever(mockCall.clone()).thenReturn(mockCall)

    whenever(wikipediaAPI.getNearbyWikiTitles(anyString(),
      anyInt(),
      anyInt()
    )).thenReturn(mockCall)

    val onRelevantPostCompleteMock: (Result<List<String>>) -> Unit = mock()
    val relevantTitleListCaptor = argumentCaptor<Result<List<String>>>()

    // act
    wikiRepo.getRelevantPosts("19, 19", onRelevantPostCompleteMock)

    // assert
    verify(onRelevantPostCompleteMock).invoke(relevantTitleListCaptor.capture())
    assertTrue((relevantTitleListCaptor.firstValue as Success).data.isEmpty())
  }

  @Test
  fun `test get relevant posts returns populated list`() {

    // arrange

    val listOfGeoTerms = ArrayList<WikiGeoItem>()
    listOfGeoTerms.add(WikiGeoItem("Mali"))
    listOfGeoTerms.add(WikiGeoItem("Bamako, Mali"))

    val geoQuery = WikiGeoQuery(listOfGeoTerms)
    val successBody = WikiGeoSearchResponse(geoQuery)

    // To mock the response when a call is made to the getWikiTitle function in the wikipedia repository
    val mockCall: Call<WikiGeoSearchResponse> = mock()
    whenever(mockCall.execute()).thenReturn(Response.success(successBody))
    whenever(mockCall.clone()).thenReturn(mockCall)

    whenever(wikipediaAPI.getNearbyWikiTitles(anyString(),
      anyInt(),
      anyInt()
    )).thenReturn(mockCall)

    val onRelevantPostCompleteMock: (Result<List<String>>) -> Unit = mock()
    val relevantTitleListCaptor = argumentCaptor<Result<List<String>>>()

    // act
    wikiRepo.getRelevantPosts("19, 19", onRelevantPostCompleteMock)

    // assert
    verify(onRelevantPostCompleteMock).invoke(relevantTitleListCaptor.capture())
    assertTrue((relevantTitleListCaptor.firstValue as Success).data.size == 2)
  }
}
