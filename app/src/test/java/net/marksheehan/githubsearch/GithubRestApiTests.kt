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
    fun searchingTargetLanguageProducesResultsWithTargetLanguage(){

        val targetLanguage = "Kotlin"
        val queryWithLanguage = appendLanguageToQuery("test", targetLanguage)

        val observable  = api.searchForRepository(queryWithLanguage)

        val response =  observable.blockingGet()
        val result = response.body()

        Assert.assertTrue(response.isSuccessful)
        for (item in result!!.items){
            Assert.assertTrue(item.language == targetLanguage )
        }
    }

    @Test
    fun searchingWithEmptyLanguageStringProducesMoreResults(){
        val queryWithNoLanguage = appendLanguageToQuery("farm", "")
        val queryWithLanguage = appendLanguageToQuery("farm", "Kotlin")

        val observableWithNoLanguage  = api.searchForRepository(queryWithNoLanguage)
        val responseWithNoLanguage =  observableWithNoLanguage.blockingGet()
        val resultWithNoLanguage = responseWithNoLanguage.body()

        val observableWithLanguage  = api.searchForRepository(queryWithLanguage)
        val responseWithLanguage =  observableWithLanguage.blockingGet()
        val resultWithLanguage = responseWithLanguage.body()

        Assert.assertTrue(responseWithLanguage.isSuccessful)
        Assert.assertTrue(responseWithNoLanguage.isSuccessful)
        Assert.assertTrue( resultWithNoLanguage!!.total_count > resultWithLanguage!!.total_count)
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