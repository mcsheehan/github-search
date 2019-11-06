package net.marksheehan.githubsearch.ui.github

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.itemClicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.github_search_fragment.*
import net.marksheehan.githubsearch.R
import net.marksheehan.githubsearch.adapters.GithubItemAdapter
import net.marksheehan.githubsearch.datamodel.GithubRepository
import net.marksheehan.githubsearch.github.appendLanguageToQuery
import retrofit2.Response
import java.util.concurrent.TimeUnit

class GithubSearchFragment : Fragment(R.layout.github_search_fragment) {

    private var searchBoxTextChangeSubscriber: Disposable? = null
    private var restApiResult: Disposable? = null
    private var menuSubscription: Disposable? = null

    private lateinit var viewModel: GithubSearchViewModel

    override fun onDestroy() {
        super.onDestroy()
        searchBoxTextChangeSubscriber?.dispose()
        restApiResult?.dispose()
        menuSubscription?.dispose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(GithubSearchViewModel::class.java)

        setTextBoxInformation(0, 0)
        filter_button.setOnClickListener(createLanguageFilterMenu)

        recycler.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = GithubItemAdapter(viewModel.displayedItems)

        searchBoxTextChangeSubscriber = query_text_input.textChanges()
            .subscribeOn(AndroidSchedulers.mainThread())
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe(this::characterChanged)
    }

    fun characterChanged(characterSequence: CharSequence) {
        launchNewQuery()
    }

    val createLanguageFilterMenu : (View) -> Unit = {
        val languageFilterMenu = PopupMenu(this.context, it)

        viewModel.languageToSearchQueryMap.keys.forEach {
            languageFilterMenu.menu.add(it)
        }

        menuSubscription = languageFilterMenu.itemClicks()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(menuClicked)

        languageFilterMenu.show()
    }

    fun launchNewQuery() {
        val newQuery = appendLanguageToQuery(query_text_input.text.toString(),
            viewModel.currentLanguageSelected
        )

        val response = viewModel.searchRestApi(newQuery)

        restApiResult = response.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, viewModel.applicationErrorHandler)
    }

    val menuClicked : (MenuItem) -> Unit = {
        viewModel.currentLanguageSelected = viewModel.languageToSearchQueryMap[it.title.toString()]!!
        launchNewQuery()
    }

    private fun setTextBoxInformation(numberOfItems: Long, timeTakenMillis: Long) {
        query_information.setText("$numberOfItems hits in $timeTakenMillis ms")
    }

    val onSuccess : (Response<GithubRepository>) -> Unit = {
        githubResponse ->
        val timeTaken: Long =
            githubResponse.raw().receivedResponseAtMillis() - githubResponse.raw().sentRequestAtMillis()

        val numberOfItems: Long = githubResponse.body()?.total_count ?: 0

        if (githubResponse.code() == 403){
            Toast.makeText(this.context, getString(R.string.rate_limited), Toast.LENGTH_LONG).show()
        }

        setTextBoxInformation(numberOfItems, timeTaken)

        viewModel.displayedItems.clear()
        val items = githubResponse.body()?.items

        items?.let { it ->
            viewModel.displayedItems.addAll(it)
        }

        recycler.adapter!!.notifyDataSetChanged()
    }
}
