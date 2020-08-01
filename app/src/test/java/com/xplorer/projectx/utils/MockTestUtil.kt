package com.xplorer.projectx.utils

import com.xplorer.projectx.model.foursquare.*
import com.xplorer.projectx.model.unsplash.Photo
import com.xplorer.projectx.model.unsplash.PhotoResult
import com.xplorer.projectx.model.unsplash.Urls

class MockTestUtil {
  val wikiDataSummary: String
    get() {
      return "Lagos (, US also ; Yoruba: Èkó) is the most populous city in Nigeria and the African continent. Lagos is a major financial centre for all of Africa and is the economic hub of Lagos State. The megacity has the fourth-highest GDP in Africa and houses one of the largest and busiest seaports on the continent. It is one of the fastest growing cities in the world.Lagos initially emerged as a port city that originated on a collection of islands, which are contained in the present day Local Government Areas (LGAs) of Lagos Island, Eti-Osa, Amuwo-Odofin and Apapa. The islands are separated by creeks, fringing the southwest mouth of Lagos Lagoon, while being protected from the Atlantic Ocean by barrier islands and long sand spits such as Bar Beach, which stretch up to 100 km (62 mi) east and west of the mouth. Due to rapid urbanization, the city expanded to the west of the lagoon to include areas in the present day Lagos Mainland, Ajeromi-Ifelodun and Surulere. This led to the classification of Lagos into two main areas: the Island, which was the initial city of Lagos, before it expanded into the area known as the Mainland. This city area was governed directly by the Federal Government through the Lagos City Council, until the creation of Lagos State in 1967, which led to the splitting of Lagos city into the present day seven Local Government Areas (LGAs), and an addition of other towns (which now make up 13 LGAs) from the then Western Region to form the state.Lagos, the capital of Nigeria since its amalgamation in 1914, went on to become the capital of Lagos State after its creation. However, the state capital was later moved to Ikeja in 1976, and the federal capital moved to Abuja in 1991. Even though Lagos is still widely referred to as a city, the present day Lagos, also known as \"Metropolitan Lagos\", and officially as \"Lagos Metropolitan Area\" is an urban agglomeration or conurbation, consisting of 16 LGAs including Ikeja, the state capital of Lagos State. This conurbation makes up 37% of Lagos State's total land area, but houses about 85% of the state's total population.The exact population of Metropolitan Lagos is disputed. In the 2006 federal census data, the conurbation had a population of about 8 million people. However, the figure was disputed by the Lagos State Government, which later released its own population data, putting the population of Lagos Metropolitan Area at approximately 16 million. As of 2015, unofficial figures put the population of \"Greater Metropolitan Lagos\", which includes Lagos and its surrounding metro area, extending as far as into Ogun State, at approximately 21 million."
    }

  fun photoResult(): PhotoResult {
    return PhotoResult(486, 106, photoList(), photoList())
  }

  private fun photoList(): List<Photo> {
    val photoList = ArrayList<Photo>()
    val urls = Urls(
      "https://images.unsplash.com/photo-1536694185544-42908a7ded88?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjExMjA3Mn0",
      "https://images.unsplash.com/photo-1536694185544-42908a7ded88?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjExMjA3Mn0",
      "https://images.unsplash.com/photo-1536694185544-42908a7ded88?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjExMjA3Mn0",
      "https://images.unsplash.com/photo-1536694185544-42908a7ded88?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjExMjA3Mn0",
      "https://images.unsplash.com/photo-1536694185544-42908a7ded88?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjExMjA3Mn0"
    )
    val photo = Photo(
      "QCH7asxE9DM", "2018-09-11T15:31:43-04:00", "2020-07-07T01:04:03-04:00",
      5547, 3698, "#120C0C", "", "blue wall graffiti", urls, 40,
      "CmRaAAAAA6_LuVt45qCjvYY3ezueOyvLhIJta4D-VT04xo-AWpmwBnLDcOo4S101bnqsSdp8clh79XFy5LXgV5wyPEZ5Zqbw2MSGx2HsFC1dTeDzMgrjG0PtfhOCGU-xQ9-TmWvsEhDLbJg-pa57y6MBJATqXC4-GhRFy7VYeLULG-eG2zVSrqiem5-4xw"
    )

    val urls1 = Urls(
      "https://images.unsplash.com/photo-1533674119484-ebe7969f3683?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjExMjA3Mn0",
      "https://images.unsplash.com/photo-1533674119484-ebe7969f3683?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjExMjA3Mn0",
      "https://images.unsplash.com/photo-1533674119484-ebe7969f3683?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjExMjA3Mn0",
      "https://images.unsplash.com/photo-1533674119484-ebe7969f3683?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjExMjA3Mn0",
      "https://images.unsplash.com/photo-1533674119484-ebe7969f3683?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjExMjA3Mn0"
    )
    val photo1 = Photo(
      "X-TPrnF7wuc",
      "2018-08-07T16:38:26-04:00",
      "2020-07-07T01:42:11-04:00",
      4864,
      2736,
      "#12090C",
      "A young boy is seen training to become training on a football pitch. His teammates can be seen at the behind him waiting their turn.\r\n\r\nThis is what grassroots football looks like in the city of Lagos.",
      "man wearing orange t-shirt",
      urls1,
      56,
      "CmRaAAAAhy4CUqH3BQm0_H-5TO1mwLdfhzUbgu5X9M01cV9olbpcy5yr0ajUG_JgO_ikDj3xO94TdLShu5HyguqDpVRNbBvJ0KQZADIeQByGakMS6Zxfn28Jj3FoMJ19jEBdRhTtEhAQGe2rRrWwPuIDV9z_vPP9GhRy0hfeqFI0F5np7SUeyDU04ipL3w"
    )

    photoList.add(photo)
    photoList.add(photo1)
    return photoList
  }

  fun createVenueResponse(): FoursquareResponse {
    val venueLocation = VenueLocation("address", 19f, 19f)
    val venue = Venue("aabb", "Place of Interest", venueLocation)
    val venueItem = VenueItem(venue)

    val venueLocation2 = VenueLocation("address2", 56f, 56f)
    val venue2 = Venue("aabb", "Place of Interest2", venueLocation2)
    val venueItem2 = VenueItem(venue2)

    val venueItemList = ArrayList<VenueItem>()
    venueItemList.add(venueItem)
    venueItemList.add(venueItem2)

    val recommendedVenueGroup = VenueGroup("recommended", venueItemList)
    val venueGroups = ArrayList<VenueGroup>()
    venueGroups.add(recommendedVenueGroup)

    return FoursquareResponse(VenueResponse(venueGroups, 2))
  }
  fun venueList() : List<Venue>{
    val venueList = ArrayList<Venue>()
    val venueLocation2 = VenueLocation("address2", 56f, 56f)
    val venue2 = Venue("aabb", "Place of Interest2", venueLocation2)
    val venueLocation = VenueLocation("address", 19f, 19f)
    val venue = Venue("aabb", "Place of Interest", venueLocation)
    venueList.add(venue)
    venueList.add(venue2)
    return venueList
  }

}