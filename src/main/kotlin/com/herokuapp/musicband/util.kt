package com.herokuapp.musicband

fun removeQuotesAndUnescape(uncleanJson: String): String {
    val noQuotes: String = uncleanJson.replace("^\"|\"$".toRegex(), "")
    return noQuotes.replace("""\"""", """"""")
}
