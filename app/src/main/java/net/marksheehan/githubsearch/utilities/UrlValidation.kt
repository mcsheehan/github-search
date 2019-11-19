package net.marksheehan.githubsearch.utilities

import okhttp3.HttpUrl


fun validateURL(url: String?): Boolean{

    val urlSafe: String = url ?: ""
    return HttpUrl.parse(urlSafe) != null

}
