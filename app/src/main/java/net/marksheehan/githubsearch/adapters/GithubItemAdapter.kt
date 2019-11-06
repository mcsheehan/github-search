package net.marksheehan.githubsearch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.github_search_item.view.*
import net.marksheehan.githubsearch.datamodel.GithubItems
import net.marksheehan.githubsearch.R

class GithubItemAdapter(private val mItemList: List<GithubItems>) : RecyclerView.Adapter<GithubItemAdapter.GithubItemHolder>() {

    inner class GithubItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val onItemClicked = View.OnClickListener { view: View ->
            val currentItem = mItemList.get(adapterPosition)
        }

        init {
            itemView.setOnClickListener(onItemClicked)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.github_search_item, parent, false)
        return GithubItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: GithubItemHolder, position: Int) {
        val githubItems = mItemList[position]
        holder.itemView.stars.setText("${githubItems.stargazers_count}")
        holder.itemView.githubItemText.setText("${githubItems.name}")
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }
}
