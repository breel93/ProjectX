package com.xplorer.projectx.api

import com.xplorer.projectx.BuildConfig
import com.xplorer.projectx.BuildConfig.GOOGLE_API_KEY
import com.xplorer.projectx.utils.ApiAbstract
import com.xplorer.projectx.utils.MockTestUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GoogleApiTest: ApiAbstract<GooglePhotoApi>(){
  private var googlePhotoApi: GooglePhotoApi? = null

  @Before
  fun init(){
    this.googlePhotoApi = createService(GooglePhotoApi::class.java)
  }
  @Test
  fun `test get city google photo result on success`(){
    //arrange
    val mockTestUtil = MockTestUtil()
    enqueueResponse("googleapi_response.json")

    //act
    val googlePhoto = googlePhotoApi!!.getGooglePhotos("lagos", GOOGLE_API_KEY).execute()

    //assert
    Assert.assertEquals(mockTestUtil.photoResult().photo[0].photo_reference, googlePhoto.body()!!.result.result[0].photo_reference)
    Assert.assertEquals(mockTestUtil.photoResult().photo[1].photo_reference, googlePhoto.body()!!.result.result[1].photo_reference)

  }
}