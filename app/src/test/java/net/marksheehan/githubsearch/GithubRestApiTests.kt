package net.marksheehan.githubsearch

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