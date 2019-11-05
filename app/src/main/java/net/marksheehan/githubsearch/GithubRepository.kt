package net.marksheehan.githubsearch

data class Items(val id: Long, val name: String, val full_name : String, val stargazers_count: Long)

class GithubRepository(
    val total_count : Long,
    val incomplete_results : Boolean,
    val items : List<Items>){
}
