package com.choiho.ulife.navigationUI.home

import android.graphics.Bitmap
import com.choiho.ulife.GlobalVariables

data class ProposalItem(
    var date: String = "",
    var imageUrlList: MutableList<String>,
    var viewCount: Int,
    var title: String,
    var content: String,
    var hashtag:MutableList<String>,

    var imageList: MutableList<Bitmap>
) {
    fun getHashTagString() : String {
        var hashTagString = ""
        for ( hashTagItem in hashtag )
            hashTagString += "$hashTagItem "
        return hashTagString
    }

    fun isDoneImageLoadingOnlyOne() : Boolean {
        return imageList.size > 0
    }

    fun convertImageUrlToImageOnlyOne() {
        if (imageUrlList.size > 0 && imageList.size == 0)
            imageList.add(GlobalVariables.imageHelper.convertUrlToImage(imageUrlList[0])!!)
    }

    fun isDoneImageLoadingAll() : Boolean {
        return imageList.size == imageUrlList.size
    }

    fun convertImageUrlToImageAll() {
        for ( i in imageList.size until(imageUrlList.size)) {
            imageList.add(GlobalVariables.imageHelper.convertUrlToImage(imageUrlList[i])!!)
        }
    }
}

data class Proposal(
    var name: String = "",
    var poster_id: String = "",
    var proposalItemList: MutableList<ProposalItem>
)