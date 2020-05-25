package com.example.startupboard.ui.api

import android.graphics.Bitmap

data class UserInfo(
    var ID: String,
    var FMC_ID: String,
    var name: String,
    var auth: String,
    var content: String,
    var icon: Bitmap,
    var iconString: String,
    var hashtag: MutableList<String>,
    var organization: MutableList<String>
) {
    fun getHashTagString() : String {
        var hashTagString = ""
        for ( hashTagItem in hashtag )
            hashTagString += "$hashTagItem "
        return hashTagString
    }

    fun getOrganizationString() : String {
        var organizationString = ""
        for ( organizationItem in organization )
            organizationString += "$organizationItem "
        return organizationString
    }
}