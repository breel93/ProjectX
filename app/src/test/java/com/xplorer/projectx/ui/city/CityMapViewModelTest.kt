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
package com.xplorer.projectx.ui.city

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.any
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.model.foursquare.Venue
import com.xplorer.projectx.repository.foursquare.FoursquareRepo
import com.xplorer.projectx.utils.MockTestUtil
import com.xplorer.projectx.utils.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock

class CityMapViewModelTest {
  private lateinit var foursquareRepo: FoursquareRepo
  private lateinit var viewModel: CityMapViewModel

  @get:Rule
  var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()
  @Before
  fun setUp() {
    foursquareRepo = mock(FoursquareRepo::class.java)
    viewModel = CityMapViewModel(foursquareRepo)
  }

  @Test
  fun `test livedata get success places`() {
    // arrange
    val mockTestUtil = MockTestUtil()
    val result = Success(mockTestUtil.venueList())
    whenever(foursquareRepo.getVenueData(anyString(), anyString(), anyInt(), anyInt(),
      any())).thenAnswer {
      (it.arguments[1] as (Result<List<Venue>>) -> Unit).invoke(result)
      viewModel.getVenueData("lagos", "")
      assertEquals(viewModel.successVenueLiveData.getOrAwaitValue().size, mockTestUtil.venueList().size)
      assertEquals(viewModel.successVenueLiveData.getOrAwaitValue()[1].venueName, mockTestUtil.venueList()[1].venueName)
    }
  }
}
