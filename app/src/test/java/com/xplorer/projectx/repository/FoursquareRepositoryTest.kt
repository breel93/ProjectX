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
package com.xplorer.projectx.repository

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.xplorer.projectx.api.FoursquareAPI
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.model.foursquare.VenueResponse
import com.xplorer.projectx.model.foursquare.FoursquareResponse
import com.xplorer.projectx.model.foursquare.VenueGroup
import com.xplorer.projectx.model.foursquare.VenueItem
import com.xplorer.projectx.model.foursquare.VenueLocation
import com.xplorer.projectx.networking.TestCoroutineContextProviderImpl
import com.xplorer.projectx.repository.foursquare.FoursquareRepo
import com.xplorer.projectx.repository.foursquare.FoursquareRepository
import com.xplorer.projectx.utils.MockTestUtil
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import retrofit2.Call
import retrofit2.Response

class FoursquareRepositoryTest {

  private lateinit var foursquareRepository: FoursquareRepo
  private val coroutineProvider = TestCoroutineContextProviderImpl()

  private val venueResultCaptor = argumentCaptor<Result<List<Venue>>>()
  private lateinit var mockFoursquareAPI: FoursquareAPI
  private lateinit var onCompleteListenerMock: (Result<List<Venue>>) -> Unit

  @Before
  fun setup() {
    mockFoursquareAPI = mock(FoursquareAPI::class.java)
    foursquareRepository = FoursquareRepository(
      mockFoursquareAPI,
      coroutineProvider
    )
    onCompleteListenerMock = mock()
  }

  @Test
  fun `test get venue data returns list of venues on success`() {

    // arrange
    val mockTestUtil = MockTestUtil()
    val mockCall: Call<FoursquareResponse> = mock()
    whenever(mockCall.execute()).thenReturn(Response.success(mockTestUtil.createVenueResponse()))
    whenever(mockCall.clone()).thenReturn(mockCall)
    whenever(
      mockFoursquareAPI.getVenues(
        anyString(),
        anyString(),
        anyString(),
        anyString(),
        anyInt(),
        anyInt(),
        anyString()
      )
    ).thenReturn(mockCall)

    // act
    foursquareRepository.getVenueData(
      "Restaurants",
      "19,19",
      0,
      20,
      onCompleteListenerMock
    )

    // assert
    verify(onCompleteListenerMock).invoke(venueResultCaptor.capture())

    assertTrue((venueResultCaptor.firstValue as Success).data.size == 2)
  }

}
