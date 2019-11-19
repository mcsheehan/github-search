package net.marksheehan.githubsearch.datamodel

data class GithubItems(val id: Long,
                       val name: String,
                       val full_name : String,
                       val description : String,
                       val language: String,
                       val stargazers_count: Long,
                       val updated_at: String,
                       val pushed_at: String,
                       val homepage: String,
                       val url: String,
                       val html_url: String
                       )

class GithubRepository(
    val total_count : Long,
    val incomplete_results : Boolean,
    val items : List<GithubItems>){
}
