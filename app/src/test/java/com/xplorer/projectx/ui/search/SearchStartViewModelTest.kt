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
class SearchStartViewModelTest{
  private lateinit var viewModel: SearchStartViewModel
  private lateinit var recentCitiesRepo: RecentCitiesRepo

  @get:Rule
  var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setUp(){
    recentCitiesRepo = mock(RecentCitiesRepo::class.java)
    viewModel = SearchStartViewModel(recentCitiesRepo)
  }

  @Test
  fun `test livedata get success list of recent cities`(){

  }
}