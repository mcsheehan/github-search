package net.marksheehan.githubsearch.ui.github

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.itemClicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.github_search_fragment.*
import net.marksheehan.githubsearch.R
import net.marksheehan.githubsearch.adapters.GithubItemAdapter
import net.marksheehan.githubsearch.datamodel.GithubItems
import java.util.concurrent.TimeUnit

class GithubSearchFragment : Fragment(R.layout.github_search_fragment) {

    private var searchBoxTextChangeSubscriber: Disposable? = null
    private var menuSubscription: Disposable? = null

    private lateinit var viewModel: GithubSearchViewModel

    override fun onDestroy() {
        super.onDestroy()
        searchBoxTextChangeSubscriber?.dispose()
        menuSubscription?.dispose()
    }

    lateinit var githubItemAdapter: GithubItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(GithubSearchViewModel::class.java)

        query_information.setText("No query has been performed")

        filter_button.setOnClickListener(createLanguageFilterMenu)

        searchBoxTextChangeSubscriber = query_text_input.textChanges()
            .subscribeOn(AndroidSchedulers.mainThread())
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe(this::characterChanged)

        viewModel.toastErrorMessages.observe(this.viewLifecycleOwner, Observer<String>{
            Toast.makeText(this.context, it, Toast.LENGTH_LONG).show()
        })

        viewModel.searchStatistics.observe(this.viewLifecycleOwner, Observer<GithubSearchViewModel.SearchStatistics>{
            val queryString = "${it.numberOfItems} hits in ${it.timeTakenMillis} ms"
            query_information.setText(queryString)
        })

        github_repository_recycler.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        githubItemAdapter = GithubItemAdapter(viewModel.observableGithubItemList.value!!)
        github_repository_recycler.adapter = githubItemAdapter

        viewModel.observableGithubItemList.observe(this.viewLifecycleOwner, Observer<MutableList<GithubItems>>{
            githubItemAdapter.notifyDataSetChanged()
        })
    }

    fun characterChanged(characterSequence: CharSequence) {
        viewModel.scheduleNewGithubQuery(characterSequence.toString())
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

    val menuClicked : (MenuItem) -> Unit = { selectedMenuItem ->
        viewModel.currentLanguageSelected = viewModel.languageToSearchQueryMap[selectedMenuItem.title.toString()]!!
        viewModel.scheduleNewGithubQuery(query_text_input.text.toString())
    }
}
