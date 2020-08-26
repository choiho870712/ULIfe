package com.choiho.ulife.navigationUI.userInfo

import android.graphics.Bitmap
import com.choiho.ulife.GlobalVariables

data class UserInfo(
    var ID: String,
    var FMC_ID: String,
    var name: String,
    var auth: String,
    var content: String,
    var iconString: String,
    var hashtag: MutableList<String>,
    var latitude: Double,
    var longitude: Double,
    var permission: MutableList<String>,
    var subscribed: MutableList<String>
) {
    var isReady = false

    fun readFromApi(id:String) {
        if (!isReady) {
            Thread {
                copy(GlobalVariables.api.getUserInfo(id))
                isReady = true
            }.start()
        }
    }

    fun clear() {
        ID = ""
        FMC_ID = ""
        name = ""
        auth = ""
        content = ""
        iconString = ""
        hashtag = mutableListOf()
        latitude = 0.0
        longitude = 0.0
        permission = mutableListOf()
        subscribed = mutableListOf()
    }

    fun copy(userInfo: UserInfo) {
        isReady = false
        ID = userInfo.ID
        FMC_ID = userInfo.FMC_ID
        name = userInfo.name
        auth = userInfo.auth
        content = userInfo.content
        iconString = userInfo.iconString
        hashtag = userInfo.hashtag
        latitude = userInfo.latitude
        longitude = userInfo.longitude
        permission = userInfo.permission
        subscribed = userInfo.subscribed
        isReady = true
    }

    fun getHashTagString() : String {
        var hashTagString = ""
        for ( hashTagItem in hashtag )
            hashTagString += "$hashTagItem "
        return hashTagString
    }

    fun setHashTagFromString(hashTagString: String) {
        hashtag = mutableListOf(hashTagString)
    }

    fun getIconBitmap() :Bitmap? {
        if (iconString == "") return null
        else return GlobalVariables.imageHelper.convertString64ToImage(iconString)
    }

    fun writeDB(tag:String) {
        GlobalVariables.dbHelper.writeDB("{$tag}_ID", ID)
        GlobalVariables.dbHelper.writeDB("{$tag}_FMC_ID", FMC_ID)
        GlobalVariables.dbHelper.writeDB("{$tag}_name", name)
        GlobalVariables.dbHelper.writeDB("{$tag}_auth", auth)
        GlobalVariables.dbHelper.writeDB("{$tag}_content", content)
        GlobalVariables.dbHelper.writeDB("{$tag}_iconString", iconString)
        GlobalVariables.dbHelper.writeDB("{$tag}_hashtag", getHashTagString())
        GlobalVariables.dbHelper.writeDB("{$tag}_latitude", latitude.toString())
        GlobalVariables.dbHelper.writeDB("{$tag}_longitude", longitude.toString())
        for (i in 0 until(permission.size)) {
            GlobalVariables.dbHelper.writeDB("{$tag}_permission$i", permission[i])
        }
        for (i in 0 until(subscribed.size)) {
            GlobalVariables.dbHelper.writeDB("{$tag}_subscribed$i", subscribed[i])
        }
    }

    fun readDB(tag:String):Boolean {
        isReady = false
        ID = GlobalVariables.dbHelper.readDB("{$tag}_ID")
        if (ID != "") {
            FMC_ID = GlobalVariables.dbHelper.readDB("{$tag}_FMC_ID")
            name = GlobalVariables.dbHelper.readDB("{$tag}_name")
            auth = GlobalVariables.dbHelper.readDB("{$tag}_auth")
            content = GlobalVariables.dbHelper.readDB("{$tag}_content")
            iconString = GlobalVariables.dbHelper.readDB("{$tag}_iconString")
            setHashTagFromString(GlobalVariables.dbHelper.readDB("{$tag}_hashtag"))
            latitude = GlobalVariables.dbHelper.readDB("{$tag}_latitude").toDouble()
            longitude = GlobalVariables.dbHelper.readDB("{$tag}_longitude").toDouble()

            var count = 0
            permission.clear()
            var permissionItem = GlobalVariables.dbHelper.readDB("{$tag}_permission$count")
            while (permissionItem != "") {
                permission.add(permissionItem)
                count++
                permissionItem = GlobalVariables.dbHelper.readDB("{$tag}_permission$count")
            }

            count = 0
            subscribed.clear()
            var subscribedItem = GlobalVariables.dbHelper.readDB("{$tag}_subscribed$count")
            while (subscribedItem != "") {
                subscribed.add(subscribedItem)
                count++
                subscribedItem = GlobalVariables.dbHelper.readDB("{$tag}_subscribed$count")
            }

            isReady = true
            return true
        }
        return false
    }

    fun deleteDB(tag:String) {
        GlobalVariables.dbHelper.deleteDB("{$tag}_ID")
        GlobalVariables.dbHelper.deleteDB("{$tag}_FMC_ID")
        GlobalVariables.dbHelper.deleteDB("{$tag}_name")
        GlobalVariables.dbHelper.deleteDB("{$tag}_auth")
        GlobalVariables.dbHelper.deleteDB("{$tag}_content")
        GlobalVariables.dbHelper.deleteDB("{$tag}_iconString")
        GlobalVariables.dbHelper.deleteDB("{$tag}_hashtag")
        GlobalVariables.dbHelper.deleteDB("{$tag}_latitude")
        GlobalVariables.dbHelper.deleteDB("{$tag}_longitude")
        for (i in 0 until(permission.size)) {
            GlobalVariables.dbHelper.deleteDB("{$tag}_permission$i")
        }
        for (i in 0 until(subscribed.size)) {
            GlobalVariables.dbHelper.deleteDB("{$tag}_subscribed$i")
        }
        clear()
    }

    fun updateDB(tag:String) {
        GlobalVariables.dbHelper.updateDB("{$tag}_ID", ID)
        GlobalVariables.dbHelper.updateDB("{$tag}_FMC_ID", FMC_ID)
        GlobalVariables.dbHelper.updateDB("{$tag}_name", name)
        GlobalVariables.dbHelper.updateDB("{$tag}_auth", auth)
        GlobalVariables.dbHelper.updateDB("{$tag}_content", content)
        GlobalVariables.dbHelper.updateDB("{$tag}_iconString", iconString)
        GlobalVariables.dbHelper.updateDB("{$tag}_hashtag", getHashTagString())
        GlobalVariables.dbHelper.updateDB("{$tag}_latitude", latitude.toString())
        GlobalVariables.dbHelper.updateDB("{$tag}_longitude", longitude.toString())
        for (i in 0 until(permission.size)) {
            if (GlobalVariables.dbHelper.readDB("{$tag}_permission$i") != "")
                GlobalVariables.dbHelper.updateDB("{$tag}_permission$i", permission[i])
            else
                GlobalVariables.dbHelper.writeDB("{$tag}_permission$i", permission[i])
        }
        for (i in 0 until(subscribed.size)) {
            if (GlobalVariables.dbHelper.readDB("{$tag}_subscribed$i") != "")
                GlobalVariables.dbHelper.updateDB("{$tag}_subscribed$i", subscribed[i])
            else
                GlobalVariables.dbHelper.writeDB("{$tag}_subscribed$i", subscribed[i])
        }
    }

    fun isEmpty():Boolean {
        return ID == ""
    }

    fun isShop():Boolean {
        return latitude != 0.0
    }

    fun isMyAccount(): Boolean {
        return GlobalVariables.userInfo.ID == ID
    }

    fun isSubscribe(): Boolean {
        for (i in 0 until(GlobalVariables.userInfo.subscribed.size))
            if (GlobalVariables.userInfo.subscribed[i] == ID )
                return true
        return false
    }

    fun isSubscribeAvailable(): Boolean {
        return !isEmpty() && !isMyAccount() && !GlobalVariables.userInfo.isShop()
                && isShop() && !isSubscribe()
    }
}
