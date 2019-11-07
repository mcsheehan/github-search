package net.marksheehan.githubsearch

import net.marksheehan.githubsearch.utilities.validateURL
import org.junit.Assert
import org.junit.Test


class UrlValidationTests{

    @Test
    fun httpAddressIsValidURL(){
        val url = "http://www.google.com"
        val isValid = validateURL(url)
        Assert.assertTrue(isValid)
    }

    @Test
    fun httpsAddressIsValidURL(){
        val url = "https://www.google.com"
        val isValid = validateURL(url)
        Assert.assertTrue(isValid)
    }

    @Test
    fun nullTextIsNotValidURL(){
        val url = "null"
        val isValid = validateURL(url)
        Assert.assertFalse(isValid)
    }

    @Test
    fun nullIsNotValidURL(){
        val url = null
        val isValid = validateURL(url)
        Assert.assertFalse(isValid)
    }

    @Test
    fun emptyStringIsNotValidURL(){
        val url = ""
        val isValid = validateURL(url)
        Assert.assertFalse(isValid)
    }

}