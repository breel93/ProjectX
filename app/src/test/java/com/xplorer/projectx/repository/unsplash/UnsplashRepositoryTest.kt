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
package com.xplorer.projectx.repository.unsplash

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.xplorer.projectx.api.UnsplashApi
import com.xplorer.projectx.model.unsplash.PhotoResult
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.networking.CoroutineContextProvider
import com.xplorer.projectx.networking.TestCoroutineContextProviderImpl
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import com.xplorer.projectx.extentions.Result
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class UnsplashRepositoryTest {
  private lateinit var coroutineProvider: CoroutineContextProvider
  private lateinit var unsplashRepository: UnsplashRepository

  @Mock
  private lateinit var mockUnsplashApi: UnsplashApi

  private val photoResultCaptor = argumentCaptor<Result<PhotoResult>>()
  private val onCompleteListenerMock = mock<(Result<PhotoResult>) -> Unit> {}

  @Before
  fun setUp() {
    coroutineProvider = TestCoroutineContextProviderImpl()
    unsplashRepository = UnsplashRepository(mockUnsplashApi, coroutineProvider)
  }

  @Test
  fun testGetPhotoWithSuccessResult() {

    // arrange
    val photoResult = PhotoResult(1, 30, ArrayList(),ArrayList())
    val result = Success(photoResult)
    val mockCall = mock<Call<PhotoResult>> {
      on { execute() } doReturn Response.success(photoResult)
      on { clone() } doReturn it
    }

    `when`(
      mockUnsplashApi.getPhotos(
        anyString(),
        anyString(),
        anyInt(),
        anyInt()
      )
    ).thenReturn(mockCall)

    // act
    unsplashRepository.getPhotoData("query", 1, 10, onCompleteListenerMock)
    verify(onCompleteListenerMock).invoke(photoResultCaptor.capture())
    assertTrue((photoResultCaptor.firstValue as Success).data == result.data)
  }
}
