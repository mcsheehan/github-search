package net.marksheehan.githubsearch.ui.github

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.github_search_fragment.*
import net.marksheehan.githubsearch.datamodel.GithubRepository
import net.marksheehan.githubsearch.R
import net.marksheehan.githubsearch.adapters.GithubItemAdapter
import net.marksheehan.githubsearch.github.appendLanguageToQuery
import retrofit2.Response
import java.util.concurrent.TimeUnit

class GithubSearchFragment : Fragment(R.layout.github_search_fragment) {

    private lateinit var searchBoxTextChangeSubscriber: Disposable
    private lateinit var restApiResult: Disposable

    private lateinit var viewModel: GithubSearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTextBoxInformation(0, 0)

        viewModel = ViewModelProviders.of(this).get(GithubSearchViewModel::class.java)

        searchBoxTextChangeSubscriber = query_text_input.textChanges()
            .subscribeOn(AndroidSchedulers.mainThread())
            .debounce(100, TimeUnit.MILLISECONDS)
            .subscribe(this::characterChanged)

        recycler.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

    fun setTextBoxInformation(numberOfItems: Long, timeTakenMillis: Long) {
        query_information.setText("$numberOfItems hits in $timeTakenMillis ms")
    }

    override fun onDestroy() {
        super.onDestroy()
        searchBoxTextChangeSubscriber.dispose()
        restApiResult.dispose()
    }

    val onSuccess : (Response<GithubRepository>) -> Unit = {
        githubResponse ->
        val timeTaken: Long =
            githubResponse.raw().receivedResponseAtMillis() - githubResponse.raw().sentRequestAtMillis()
        val numberOfItems: Long = githubResponse.body()?.total_count ?: 0

        setTextBoxInformation(numberOfItems, timeTaken)

        val items = githubResponse.body()?.items
        items?.let { it ->
            recycler.adapter = GithubItemAdapter(it)
        }
    }

    fun characterChanged(characterSequence: CharSequence) {
        val newQuery = appendLanguageToQuery(
            characterSequence.toString(),
            "python"
        )
        val response = viewModel.searchRestApi(newQuery)

        restApiResult = response.observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, viewModel.applicationErrorHandler)
    }
}
