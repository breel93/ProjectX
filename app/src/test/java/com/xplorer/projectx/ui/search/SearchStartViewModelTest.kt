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
package com.xplorer.projectx.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xplorer.projectx.repository.recentCities.RecentCitiesRepo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchStartViewModelTest {
  private lateinit var viewModel: SearchStartViewModel
  private lateinit var recentCitiesRepo: RecentCitiesRepo

  @get:Rule
  var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setUp() {
    recentCitiesRepo = mock(RecentCitiesRepo::class.java)
    viewModel = SearchStartViewModel(recentCitiesRepo)
  }

  @Test
  fun `test livedata get success list of recent cities`() {
  }
}
