package com.xplorer.projectx.api

import com.xplorer.projectx.model.wikipedia.WikiPageResult
import com.xplorer.projectx.model.wikipedia.WikiQuery
import com.xplorer.projectx.model.wikipedia.WikiQueryTitleResponse
import com.xplorer.projectx.utils.ApiAbstract
import com.xplorer.projectx.utils.MockTestUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WikipediaApiTest : ApiAbstract<WikipediaAPI>(){

  private var wikipediaAPI: WikipediaAPI? = null
  @Before
  fun initService(){
    this.wikipediaAPI = createService(WikipediaAPI::class.java)
  }

  @Test
  fun `test get city wikipedia result on success`(){
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