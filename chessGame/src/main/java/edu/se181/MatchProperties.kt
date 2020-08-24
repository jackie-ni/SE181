package edu.se181

data class MatchProperties(
        val gameId: String = "",
        val name: String,
        val password: String = "",
        val private: Boolean = false,
        val players: Int = 0
)
