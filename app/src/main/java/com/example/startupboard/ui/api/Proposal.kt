package com.example.startupboard.ui.api

data class Proposal(
    val name: String = "",
    val id: Int = -1,
    val date: String = "",
    val view: Int = -1,
    val poster_id: String = "",
    val image: Int = -1,
    val content:String = "",
    val title:String = "",
    val hashtag:MutableList<String> = mutableListOf()
) {
    fun getHashTagString() : String {
        var hashTagString = ""
        for ( hashTagItem in hashtag )
            hashTagString += "$hashTagItem "
        return hashTagString
    }
}