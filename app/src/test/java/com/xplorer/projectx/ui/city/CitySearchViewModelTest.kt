package com.xplorer.projectx.ui.city

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.xplorer.projectx.extentions.Failure
import com.xplorer.projectx.extentions.Result
import com.xplorer.projectx.extentions.Success
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.model.wikipedia.WikiCityInfo
import com.xplorer.projectx.repository.google_pictures.GooglePicturesRepo
import com.xplorer.projectx.repository.wikipedia.WikipediaRepo
import com.xplorer.projectx.utils.MockTestUtil
import com.xplorer.projectx.utils.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.mockito.Mockito.mock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.anyString
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CitySearchViewModelTest{
  private lateinit var viewModel: CitySearchViewModel
  private lateinit var googlePicturesRepo: GooglePicturesRepo
  private lateinit var wikipediaRepo: WikipediaRepo

  @get:Rule
  var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setUp(){
    googlePicturesRepo = mock(GooglePicturesRepo::class.java)
    wikipediaRepo = mock(WikipediaRepo::class.java)
    viewModel = CitySearchViewModel(googlePicturesRepo, wikipediaRepo)
  }

  @Test
  fun `test livedata get success list of photo`(){
    val mockTestUtil = MockTestUtil()
    val result = Success(mockTestUtil.photoResult().photo)
    whenever(googlePicturesRepo.getGooglePicturesData(anyString(), any())).thenAnswer{
      (it.arguments[1] as (Result<List<Photo>>) -> Unit).invoke(result)
    }
    viewModel.getGooglePhotos("lagos")
    assertEquals(viewModel.successPhotoLiveData.getOrAwaitValue()[0].photo_reference,
      mockTestUtil.photoResult().photo[0].photo_reference)
    assertEquals(viewModel.successPhotoLiveData.getOrAwaitValue()[1].description,
      mockTestUtil.photoResult().photo[1].description)
    assertEquals(viewModel.successPhotoLiveData.getOrAwaitValue().size,
      2)
  }

  @Test
  fun `test livedata get failure list of photo`(){
    val result = Failure(Throwable("Some Error"))
    whenever(googlePicturesRepo.getGooglePicturesData(anyString(), any())).thenAnswer{
      (it.arguments[1] as (Result<List<Photo>>) -> Unit).invoke(result)
    }
    viewModel.getGooglePhotos("lagos")
    assertEquals(viewModel.errorPhotoLiveData.getOrAwaitValue(), "Some Error")
  }

  @Test
  fun `test livedata get success wiki info`(){
    // arrange
    val mockTestUtil = MockTestUtil()
    val wikiResult = Success(WikiCityInfo("lagos", mockTestUtil.wikiDataSummary))
    whenever(wikipediaRepo.getCityInformation(anyString(), any())).thenAnswer{
      (it.arguments[1] as (Result<WikiCityInfo>) -> Unit).invoke(wikiResult)
    }
    viewModel.setCityInformationData("lagos")
    assertEquals(viewModel.cityInfoLiveData.getOrAwaitValue().cityName, "lagos")
    assertEquals(viewModel.cityInfoLiveData.getOrAwaitValue().citySummary, mockTestUtil.wikiDataSummary)
  }
  @Test
  fun `test livedata get failure wiki info`(){
    val result = Failure(Throwable("Some Error"))
    whenever(wikipediaRepo.getCityInformation(anyString(), any())).thenAnswer{
      (it.arguments[1] as (Result<WikiCityInfo>) -> Unit).invoke(result)
    }
    viewModel.setCityInformationData("lagos")
    assertEquals(viewModel.errorCityInfoLiveData.getOrAwaitValue(), "Some Error")
  }
}