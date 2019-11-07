package net.marksheehan.githubsearch.ui.github

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.marksheehan.githubsearch.datamodel.GithubItems
import net.marksheehan.githubsearch.datamodel.GithubRepository
import net.marksheehan.githubsearch.github.GithubApi
import net.marksheehan.githubsearch.github.appendLanguageToQuery
import retrofit2.Response

class GithubSearchViewModel : ViewModel() {

    data class SearchStatistics(val timeTakenMillis: Long, val numberOfItems: Long)

    private var restApiResult: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        restApiResult?.dispose()
    }

    // This should be in a repository then injected in.
    private val githubRestApi = GithubApi.buildGithubRestApi()

    val languageToSearchQueryMap = mapOf(
        "Any" to "",
        "C++" to "c++",
        "python" to "python",
        "java" to "java",
        "kotlin" to "kotlin",
        "assembly" to "assembly",
        "shell" to "shell"
    )
    var currentLanguageSelected = ""

    private var mutableGithubItemList: MutableList<GithubItems> = mutableListOf()

    var observableGithubItemList: MutableLiveData<MutableList<GithubItems>> =
        MutableLiveData(mutableGithubItemList)

    val toastErrorMessages: MutableLiveData<String> = MutableLiveData()

    val searchStatistics: MutableLiveData<SearchStatistics> = MutableLiveData()

    val githubSuccessResponse: (Response<GithubRepository>) -> Unit = { githubResponse ->
        val timeTaken: Long =
            githubResponse.raw().receivedResponseAtMillis() - githubResponse.raw().sentRequestAtMillis()

        val numberOfItems: Long = githubResponse.body()?.total_count ?: 0

        if (githubResponse.code() == 403) {
            val errorMessage = "Github has rate limited you. Please wait a few seconds."
            toastErrorMessages.value = errorMessage
        }

        searchStatistics.value = SearchStatistics(timeTaken, numberOfItems)

        mutableGithubItemList.clear()

        val items = githubResponse.body()?.items

        items?.let { it ->
            mutableGithubItemList.addAll(it)
        }

        observableGithubItemList.value = mutableGithubItemList
    }

    fun scheduleNewGithubQuery(query: String) {
        val newQuery = appendLanguageToQuery(
            query,
            currentLanguageSelected
        )

        val response = searchRestApi(newQuery)

        restApiResult = response.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(githubSuccessResponse, applicationErrorHandler)
    }

    val applicationErrorHandler: (Throwable) -> Unit = {
        // Errors fall into two categories for the web api requests, operation interrupted and no internet.
        // These should be registered in a db / logged or the no internet error should display a message.
    }

    fun searchRestApi(searchTerm: String): Single<Response<GithubRepository>> {
        val observable = githubRestApi.searchForRepository(searchTerm)
        return observable
    }
}
