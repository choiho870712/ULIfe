package com.example.startupboard.ui.api

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.example.startupboard.GlobalVariables
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    var ID: String,
    var FMC_ID: String,
    var name: String,
    var auth: String,
    var content: String,
    var iconString: String,
    var hashtag: MutableList<String>,
    var latitude: Double,
    var longitude: Double

) : Parcelable {

    fun getHashTagString() : String {
        var hashTagString = ""
        for ( hashTagItem in hashtag )
            hashTagString += "$hashTagItem "
        return hashTagString
    }

    fun getIconBitmap() :Bitmap? {
        if (iconString == "") return null
        else return GlobalVariables.api.convertString64ToImage(iconString)
    }
}

//data class UserInfo(
//    var ID: String?,
//    var FMC_ID: String?,
//    var name: String?,
//    var auth: String?,
//    var content: String?,
//    var iconString: String?,
//    var hashtag: String?,
//    var latitude: Double,
//    var longitude: Double
//
//) : Parcelable {
//
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readDouble(),
//        parcel.readDouble()
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(ID)
//        parcel.writeString(FMC_ID)
//        parcel.writeString(name)
//        parcel.writeString(auth)
//        parcel.writeString(content)
//        parcel.writeString(iconString)
//        parcel.writeString(hashtag)
//        parcel.writeDouble(latitude)
//        parcel.writeDouble(longitude)
//    }
//
//    fun getIconBitmap() :Bitmap? {
//        if (iconString == null || iconString == "") return null
//        else return GlobalVariables.api.convertString64ToImage(iconString!!)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<UserInfo> {
//        override fun createFromParcel(parcel: Parcel): UserInfo {
//            return UserInfo(parcel)
//        }
//
//        override fun newArray(size: Int): Array<UserInfo?> {
//            return arrayOfNulls(size)
//        }
//    }
//}