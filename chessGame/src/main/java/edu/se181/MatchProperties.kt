package edu.se181

data class MatchProperties(
        val matchName: String,
        val password: String? = null,
        val isPrivate: Boolean = false
)
