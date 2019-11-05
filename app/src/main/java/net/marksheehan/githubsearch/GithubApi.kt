package net.marksheehan.githubsearch

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubServiceInterface {

    @GET("search/repositories")
    fun searchForRepositories(
        @Query("q") repositorySearchTerm: String,
        @Query("sort") sort: String = "stars"
    ): Call<GithubRepository>

    @GET("search/repositories")
    fun searchForRepositoriesReactive(
        @Query("q") repositorySearchTerm: String,
        @Query("sort") sort: String = "stars"
    ): Observable<GithubRepository>
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