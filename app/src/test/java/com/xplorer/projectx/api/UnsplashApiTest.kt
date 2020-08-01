package com.xplorer.projectx.api

import com.xplorer.projectx.BuildConfig
import com.xplorer.projectx.utils.ApiAbstract
import com.xplorer.projectx.utils.MockTestUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UnsplashApiTest : ApiAbstract<UnsplashApi>() {
  private var unsplashApi: UnsplashApi? = null

  @Before
  fun init(){
    this.unsplashApi = createService(UnsplashApi::class.java)
  }

  @Test
  fun `test get unsplash result on success`(){
    //arrange
    val mockTestUtil = MockTestUtil()
    enqueueResponse("unsplash_response.json")
    //act
    val unsplashResult = unsplashApi!!.getPhotos(BuildConfig.UNSPLASH_API_KEY, "lagos", 1, 5).execute()

    //assert
    Assert.assertEquals(mockTestUtil.photoResult().total, unsplashResult.body()!!.total)
    Assert.assertEquals(mockTestUtil.photoResult().photo[0].id, unsplashResult.body()!!.photo[0].id)
    Assert.assertEquals(mockTestUtil.photoResult().photo[1].id, unsplashResult.body()!!.photo[1].id)
    Assert.assertEquals(mockTestUtil.photoResult().photo[1].description, unsplashResult.body()!!.photo[1].description)
    Assert.assertEquals(mockTestUtil.photoResult().photo[0].alt_description, unsplashResult.body()!!.photo[0].alt_description)
  }
}