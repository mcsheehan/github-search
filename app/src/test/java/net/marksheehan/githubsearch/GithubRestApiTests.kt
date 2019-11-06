package net.marksheehan.githubsearch

import net.marksheehan.githubsearch.github.GithubApi
import net.marksheehan.githubsearch.github.GithubServiceInterface
import net.marksheehan.githubsearch.github.appendLanguageToQuery
import org.junit.Assert
import org.junit.Test

class GithubRestApiTests{

    var api : GithubServiceInterface = GithubApi.buildGithubRestApi()

    @Test
    fun ensureMoreThan1ResultReturned(){
        val observable  = api.searchForRepository("tetris")

        val response =  observable.blockingGet()
        val result = response.body()

        Assert.assertTrue(response.isSuccessful)
        Assert.assertNotNull(result)
        Assert.assertTrue(result!!.total_count > 0)
    }

    @Test
    fun emptyQueryProducesNotSuccessful(){
        val query = ""

        val observable  = api.searchForRepository(query)

        val response =  observable.blockingGet()
        Assert.assertFalse(response.isSuccessful)
    }

    @Test
    fun searchingMultipleLanguagesProducesResults(){
        val queryWithLanguage = appendLanguageToQuery("test", "kotlin")

        val observable  = api.searchForRepository(queryWithLanguage)

        val response =  observable.blockingGet()
        val result = response.body()

        Assert.assertTrue(response.isSuccessful)
        Assert.assertTrue(result!!.total_count > 0)
    }

    @Test
    fun queryWithoutSortingByStarsIsSuccessful(){
        val queryWithLanguage = appendLanguageToQuery("test", "kotlin")

        val observable  = api.searchForRepository(queryWithLanguage, "")

        val response =  observable.blockingGet()
        val result = response.body()

        Assert.assertTrue(response.isSuccessful)
        Assert.assertTrue(result!!.total_count > 0)
    }
}