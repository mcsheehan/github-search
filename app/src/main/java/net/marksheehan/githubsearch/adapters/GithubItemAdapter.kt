package net.marksheehan.githubsearch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.net.Uri
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.github_search_item.view.*
import net.marksheehan.githubsearch.datamodel.GithubItems
import net.marksheehan.githubsearch.R
import net.marksheehan.githubsearch.utilities.validateURL
import androidx.core.content.ContextCompat.startActivity



class GithubItemAdapter(private val mItemList: List<GithubItems>) : RecyclerView.Adapter<GithubItemAdapter.GithubItemHolder>() {

    inner class GithubItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private fun openLink(url: String){
            val intent = Intent(Intent.ACTION_VIEW)

            if (validateURL(url)) {
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.data = Uri.parse(url)
                val b = Bundle()
                b.putBoolean("new_window", true)
                startActivity(itemView.context, intent, b)
            }
        }

        val onItemClicked = View.OnClickListener {
            val currentItem = mItemList[adapterPosition]
            openLink(currentItem.html_url)
        }

        val onItemLongClicked = View.OnLongClickListener  {
            val currentItem = mItemList[adapterPosition]
            openLink(currentItem.homepage)
            true
        }

        init {
            itemView.setOnLongClickListener(onItemLongClicked)
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
        holder.itemView.gitHubItemDescription.setText("${githubItems.description} + ${githubItems.homepage}")
    }


    override fun getItemCount(): Int {
        return mItemList.size
    }
}
