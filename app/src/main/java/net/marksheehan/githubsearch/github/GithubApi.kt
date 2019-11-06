package net.marksheehan.githubsearch.github

import io.reactivex.Single
import net.marksheehan.githubsearch.datamodel.GithubRepository
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


fun appendLanguageToQuery(
    repositoryQuery: String,
    language: String
): String {
    val searchQuery = "${repositoryQuery}+language:${language}"
    return searchQuery
}

interface GithubServiceInterface {

    @GET("search/repositories")
    fun searchForRepository(
        @Query("q") repositorySearchTerm: String,
        @Query("sort") sort: String = "stars"
    ): Single<Response<GithubRepository>>
}

class GithubApi {
    companion object {
        fun buildGithubRestApi(): GithubServiceInterface {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(GithubServiceInterface::class.java)
        }
    }
}