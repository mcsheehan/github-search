package net.marksheehan.githubsearch

import net.marksheehan.githubsearch.github.GithubApi
import org.junit.Test

class GithubRestApiTests{

    @Test
    fun ensure_more_than_1_result_returned(){
        val api = GithubApi.buildGithubRestApi()
        val a = api.searchForRepositories("tetris")
        val b = a.execute()
        val c = b.body()
    }
}