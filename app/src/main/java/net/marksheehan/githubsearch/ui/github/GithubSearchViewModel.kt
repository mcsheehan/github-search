package net.marksheehan.githubsearch.ui.github

import androidx.lifecycle.ViewModel
import io.reactivex.Single
import net.marksheehan.githubsearch.datamodel.GithubItems
import net.marksheehan.githubsearch.github.GithubApi
import net.marksheehan.githubsearch.datamodel.GithubRepository
import retrofit2.Response

class GithubSearchViewModel : ViewModel() {

    private val githubRestApi = GithubApi.buildGithubRestApi()

    val languageToSearchQueryMap = mapOf(
        "Clear language filter" to "",
        "C++" to "c++",
        "python" to "python",
        "java" to "java",
        "kotlin" to "kotlin",
        "assembly" to "assembly",
        "shell" to "shell"
    )

    var currentLanguageSelected = ""

    var displayedItems : MutableList<GithubItems> = mutableListOf()


    val applicationErrorHandler: (Throwable) -> Unit = {
        // Errors fall into two categories for the web api requests, operation interrupted and no internet.
        // These should be registered in a db / logged or the no internet error should display a message.
    }

    fun searchRestApi(searchTerm: String): Single<Response<GithubRepository>> {
        val observable = githubRestApi.searchForRepository(searchTerm)
        return observable
    }
}
