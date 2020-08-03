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

import com.xplorer.projectx.model.wikipedia.WikiPageResult
import com.xplorer.projectx.model.wikipedia.WikiQuery
import com.xplorer.projectx.model.wikipedia.WikiQueryTitleResponse
import com.xplorer.projectx.utils.ApiAbstract
import com.xplorer.projectx.utils.MockTestUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WikipediaApiTest : ApiAbstract<WikipediaAPI>() {

  private var wikipediaAPI: WikipediaAPI? = null

  @Before
  fun initService() {
    this.wikipediaAPI = createService(WikipediaAPI::class.java)
  }

  @Test
  fun `test get city wikipedia result on success`() {
    // arrange
    val mockTestUtil = MockTestUtil()
    val pageQuery = HashMap<String, WikiPageResult>()
    val title = WikiPageResult("Lagos", mockTestUtil.wikiDataSummary)
    pageQuery["85232"] = title
    val pageQueryResult = WikiQuery(pageQuery)
    val successBody = WikiQueryTitleResponse(pageQueryResult)
    enqueueResponse("wikipedia_response.json")

    // act
    val wikiResult = wikipediaAPI!!.getWikiTitle("lagos").execute()

    // assert
    Assert.assertEquals(successBody.query, wikiResult.body()!!.query)
    Assert.assertEquals(successBody, wikiResult.body())
    Assert.assertEquals(successBody.query.pageObject, wikiResult.body()!!.query.pageObject)
  }
}
