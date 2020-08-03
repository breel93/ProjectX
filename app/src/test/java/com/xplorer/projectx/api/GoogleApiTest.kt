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
package com.xplorer.projectx.api

import com.xplorer.projectx.BuildConfig.GOOGLE_API_KEY
import com.xplorer.projectx.utils.ApiAbstract
import com.xplorer.projectx.utils.MockTestUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GoogleApiTest : ApiAbstract<GooglePhotoApi>() {
  private var googlePhotoApi: GooglePhotoApi? = null

  @Before
  fun init() {
    this.googlePhotoApi = createService(GooglePhotoApi::class.java)
  }
  @Test
  fun `test get city google photo result on success`() {
    // arrange
    val mockTestUtil = MockTestUtil()
    enqueueResponse("googleapi_response.json")

    // act
    val googlePhoto = googlePhotoApi!!.getGooglePhotos("lagos", GOOGLE_API_KEY).execute()

    // assert
    Assert.assertEquals(mockTestUtil.photoResult().photo[0].photo_reference, googlePhoto.body()!!.result.result[0].photo_reference)
    Assert.assertEquals(mockTestUtil.photoResult().photo[1].photo_reference, googlePhoto.body()!!.result.result[1].photo_reference)
  }
}
