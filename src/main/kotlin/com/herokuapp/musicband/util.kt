package com.herokuapp.musicband

import kotlin.text.Regex.Companion.escapeReplacement

fun removeQuotesAndUnescape(uncleanJson: String): String {
    val noQuotes: String = uncleanJson.replace("^\"|\"$".toRegex(), "")
    return noQuotes.replace("""\"""", """"""")
}
