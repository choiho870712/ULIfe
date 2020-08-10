package com.choiho.ulife.ui.api

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.net.ConnectivityManager
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.core.graphics.scale
import com.google.gson.Gson
import com.squareup.okhttp.*
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL


class Api {

    private val client = OkHttpClient()
    private val jsonType: MediaType = MediaType.parse("application/json; charset=utf-8")

    data class ApiUrl( val url:String, var isCalling:Boolean )
    private val urlDirectory = "https://6unoj2gvpj.execute-api.ap-southeast-1.amazonaws.com/Dev/"
    private val urlBuildRelationship = ApiUrl(urlDirectory+"friend/build-relationship", false)
    private val urlGetFriendList = ApiUrl(urlDirectory+"friend/get-friend-list", false)
    private val urlGetNotification = ApiUrl(urlDirectory+"notification/get-notification", false)
//    private val urlDeleteNotification = ApiUrl(urlDirectory+"notification/delete-notification", false)
    private val urlPostNotification = ApiUrl(urlDirectory+"notification/post-notification", false)
    private val urlClickInProposal = ApiUrl(urlDirectory+"proposal/click-in-proposal", false)
    private val urlGetProposal = ApiUrl(urlDirectory+"zhongli/food/get-item/by-defult", false)
    private val urlPostProposal = ApiUrl(urlDirectory+"event/post-event", false)
    private val urlUpdateProposal = ApiUrl(urlDirectory+"proposal/update-proposal", false)
    private val urlCreateUser = ApiUrl(urlDirectory+"user/create-user", false)
    private val urlGetUserInfo = ApiUrl(urlDirectory+"user/get-user-info", false)
    private val urlLogin = ApiUrl(urlDirectory+"user/login", false)
    private val urlUpdateFCMToken = ApiUrl(urlDirectory+"user/update-fcm-token", false)
    private val urlUpdateUserIcon = ApiUrl(urlDirectory+"user/update-user-icon", false)
    private val urlUpdateUserInfo = ApiUrl(urlDirectory+"user/update-user-info", false)
    private val urlUploadUserInfo = ApiUrl(urlDirectory+"user/upload-user-info", false)
    private val urlLoadImage = ApiUrl(urlDirectory+"load-img", false)

    private val urlGetFoodByClass = ApiUrl(urlDirectory+"zhongli/food/get-item/by-defult", false)
    private val urlGetFoodAll = ApiUrl(urlDirectory+"zhongli/food/get-item/main-page", false)
    private val urlSearchUserByName = ApiUrl(urlDirectory+"friend/search-user-name", false)

    private val urlSubscribe = ApiUrl(urlDirectory+"subscription/build-relationship", false)
    private val urlDeleteSubscribe = ApiUrl(urlDirectory+"subscription/delete-subscription", false)
    private val urlGetSubscribeList = ApiUrl(urlDirectory+"subscription/get-follower", false)

    private val urlLogout = ApiUrl(urlDirectory+"user/logout", false)

    private val urlPostFoodItem = ApiUrl(urlDirectory+"zhongli/food/post-item", false)
    private val urlGetFoodItem = ApiUrl(urlDirectory+"zhongli/food/get-item", false)

    private val urlServerStatusCode = ApiUrl(urlDirectory+"admin/server-statu-code", false)

    private val urlClickInNotification = ApiUrl(urlDirectory+"notification/click-in-notification", false)

    fun clickInNotification(id:String, date:String, content:String, view: Int, index: Int):Boolean {
        val json = "{\"id\": \"$id\", \"date\": \"$date\", \"content\": \"$content\", \"view\": $view, \"index\": $index}"
        return (getJsonMessage(callApi(json, urlClickInNotification)) == "No Error" )
    }

    fun getServerStatusCode(): Boolean {
        val json = "{}"
        return getJsonMessage(callApi(json, urlServerStatusCode)) == "No Error"
    }

    fun getFoodItem(id:String, type:String, area:String): Proposal {
        val json = "{\"id\": \"$id\", \"type\": \"$type\", \"area\": \"$area\"}"
        return getFoodAllJson(callApi(json, urlGetFoodItem))[0]
    }

    fun postFoodItem(id: String, title: String, content: String, date: String,
                     type: String, hashtag: MutableList<String>, image: String, area: String ): Boolean {
        var json = "{\"id\": \"$id\", \"title\": \"$title\", \"content\": \"$content\", \"date\": \"$date\"," +
                "\"type\": \"$type\", \"hashtag\": ["

        var isFirst = true
        for (i in 0 until(hashtag.size)) {
            if (!isFirst) {
                json += ","
                isFirst = false
            }
            json += "\"" + hashtag[i] + "\""
        }

        json += "], \"image\": [\"$image\"], \"area\": \"$area\"}"
        return (getJsonMessage(callApi(json, urlPostFoodItem)) == "No Error" )
    }

    fun logout(id:String): Boolean {
        val json = "{\"id\": \"$id\"}"
        return (getJsonMessage(callApi(json, urlLogout)) == "No Error" )
    }

    private val gson = Gson()
    private val urlRequestSendingFCM = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "AAAAZNF2XG8:APA91bFe7iSGOn2hpSFowHOa3V4h1znbeT9OZCx4UAyP8Xrf9An7jiwEszAMWSnwaCLxcuU8BSzGcVOi-O85fVLdRydtgLwoNZ2th7mvsmguF5FnZuZGelkjmBluKlA1xxCu68yQCiXB"
    fun requestSendingFCM(FCM_id: String, title: String, message: String) {
        val data = FcmData()
        data.to = FCM_id
        data.notification.title = title
        data.notification.body = message

        val body = RequestBody.create(jsonType, gson.toJson(data))
        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=$serverKey")
            .url(urlRequestSendingFCM)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request, e: IOException) {
                Log.d(">>>>>>>>>>>>>>>>>>>>Fail>>>", e.message!!)
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response) {
                Log.d(">>>>>>>>>>>>>>>>>>>>Success>>>", response.body().string())
            }
        })
    }

    fun getSubscribeListJson(rawJsonString : String) : ArrayList<UserInfo> {
        val dataList = ArrayList<UserInfo>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            if(jsonObject.has("message"))
                break

            dataList.add(UserInfo(
                jsonObject.getString("id"),
                jsonObject.getString("FCM_id"),
                "",
                "",
                "",
                "",
                mutableListOf(),
                0.0,
                0.0,
                mutableListOf(),
                mutableListOf()
            ))
        }
        return dataList
    }
    
    fun getSubscribeList(id:String) : ArrayList<UserInfo> {
        val json = "{\"id\": \"$id\"}"
        return getSubscribeListJson(callApi(json, urlGetSubscribeList))
    }
    
    fun deleteSubscribe(subscribed:String, follower: String) : Boolean {
        val json = "{\"subscribed\": \"$subscribed\",\"follower\": \"$follower\"}"
        return (getJsonMessage(callApi(json, urlDeleteSubscribe)) == "No Error" )
    }
    
    fun subscribe(subscribed:String, follower: String) : Boolean {
        val json = "{\"subscribed\": \"$subscribed\",\"follower\": \"$follower\"}"
        return (getJsonMessage(callApi(json, urlSubscribe)) == "No Error" )
    }

    fun isNetWorkConnecting(context: Context):Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }

    fun searchUserByNameJson(rawJsonString : String) : ArrayList<UserInfo> {
        val dataList = ArrayList<UserInfo>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            val iconUrl = jsonObject.getString("icon")
            var iconString = ""
            if (iconUrl.length > 10) {
                val icon = convertUrlToImage(iconUrl)
                if (icon != null)
                    iconString = convertImageToString64(icon)!!
            }

            dataList.add(UserInfo(
                jsonObject.getString("id"),
                "",
                jsonObject.getString("name"),
                "",
                "",
                iconString,
                mutableListOf(),
                0.0,
                0.0,
                mutableListOf(),
                mutableListOf()
            ))
        }
        return dataList
    }

    fun searchUserByName(name:String) : ArrayList<UserInfo> {
        val json = "{\"name\": \"$name\"}"
        return searchUserByNameJson(callApi(json, urlSearchUserByName))
    }

    fun getFoodByClass(serial:Int, type:String, area:String) : ArrayList<Proposal> {
        val json = "{\"serial\": $serial, \"type\": \"$type\", \"area\": \"$area\"}"
        return getFoodAllJson(callApi(json, urlGetFoodByClass))
    }

    fun getFoodAllJson(rawJsonString : String) : ArrayList<Proposal> {
        val dataList = ArrayList<Proposal>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            val itemList = ArrayList<ProposalItem>()
            val jsonItemArray = jsonObject.getJSONArray("item")
            for ( j in 0 until(jsonItemArray.length())) {
                val jsonItemObject = jsonItemArray.getJSONObject(j)

                val imageUrlList : MutableList<String> = mutableListOf()
                val jsonImageUrlArray = jsonItemObject.getJSONArray("image")
                for ( k in 0 until(jsonImageUrlArray.length()) )
                    imageUrlList.add(jsonImageUrlArray.getString(k))

                val hashtagList : MutableList<String> = mutableListOf()
                val jsonHashtagArray = jsonItemObject.getJSONArray("hashtag")
                for ( k in 0 until(jsonHashtagArray.length()) )
                    hashtagList.add(jsonHashtagArray.getString(k))

                itemList.add(ProposalItem(
                    jsonItemObject.getString("date"),
                    imageUrlList,
                    jsonItemObject.getInt("view"),
                    jsonItemObject.getString("title"),
                    jsonItemObject.getString("content"),
                    hashtagList,
                    mutableListOf()
                ))
            }

            dataList.add(Proposal(
                jsonObject.getString("name"),
                jsonObject.getString("id"),
                itemList
            ))
        }

        for ( i in 0 until(dataList.size)) {
            Thread {
                dataList[i].proposalItemList[0].convertImageUrlToImageOnlyOne()
            }.start()
        }
        return dataList
    }

    fun getFoodAll(serial:Int, area:String) : ArrayList<Proposal> {
        val json = "{\"serial\": $serial, \"area\": \"$area\"}"
//        callApi(json, urlGetFoodAll)
//        return getFoodAllJson(getResponse())
        return getFoodAllJson(callApi(json, urlGetFoodAll))
    }

    fun getJsonNotificationList(rawJsonString : String) : ArrayList<Notification> {
        val dataList = ArrayList<Notification>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            if (jsonObject.has("message"))
                break

            val pusher_id = jsonObject.getString("id")

            val jsonNotificationArray = jsonObject.getJSONArray("notification")
            for (j in 0 until(jsonNotificationArray.length())) {
                val jsonNotificationItem = jsonNotificationArray.getJSONObject(j)
                dataList.add(Notification(
                    pusher_id,
                    jsonNotificationItem.getString("date"),
                    jsonNotificationItem.getString("content"),
                    jsonNotificationItem.getInt("view")
                ))
            }
        }
        return dataList
    }

//    fun getJsonProposalList(rawJsonString : String) : ArrayList<Proposal> {
//        val dataList = ArrayList<Proposal>()
//        val jsonArray = JSONArray(rawJsonString)
//        for ( i in 0 until(jsonArray.length()) ) {
//            val jsonObject = jsonArray.getJSONObject(i)
//            val hashtag : MutableList<String> = mutableListOf()
//            val jsonHashtagArray = jsonObject.getJSONArray("hashtag")
//            for ( j in 0 until(jsonHashtagArray.length()) )
//                hashtag.add(jsonHashtagArray.getString(j))
//
//            val imageUrlList : MutableList<String> = mutableListOf()
//            val jsonImageUrlArray = jsonObject.getJSONArray("image")
//            for ( j in 0 until(jsonImageUrlArray.length()) )
//                imageUrlList.add(jsonImageUrlArray.getString(j))
//
//            var headImage = globalEmptyBitmap
//            if (jsonImageUrlArray.length() > 0)
//                headImage = convertUrlToImage(jsonImageUrlArray.getString(0))
//
//            dataList.add(Proposal(
//                jsonObject.getString("name"),
//                jsonObject.getInt("id"),
//                jsonObject.getString("date"),
//                jsonObject.getInt("view"),
//                jsonObject.getString("poster_id"),
//                imageUrlList,
//                mutableListOf(headImage),
//                jsonObject.getString("content"),
//                jsonObject.getString("title"),
//                hashtag,
//            ))
//        }
//        return dataList
//    }

    fun getJsonUserInfo(rawJsonString : String) : UserInfo {
        val jsonObject = JSONArray(rawJsonString).getJSONObject(0)

        val iconUrl = jsonObject.getString("icon")
        var iconString = ""
        if (iconUrl.length > 10) {
            val icon = convertUrlToImage(iconUrl)
            if (icon != null)
                iconString = convertImageToString64(icon)!!
        }

        val hashtag : MutableList<String> = mutableListOf()
        val jsonHashtagArray = jsonObject.getJSONArray("hashtag")
        for ( j in 0 until(jsonHashtagArray.length()) )
            hashtag.add(jsonHashtagArray.getString(j))

        val permission : MutableList<String> = mutableListOf()
        if (jsonObject.has("permission")) {
            val jsonPermissionArray = jsonObject.getJSONArray("permission")
            for ( j in 0 until(jsonPermissionArray.length()) )
                permission.add(jsonPermissionArray.getString(j))
        }

        var latitude = 0.0
        var longitude = 0.0
        if (jsonObject.has("latitude")) {
            latitude = jsonObject.getString("latitude").toDouble()
            longitude = jsonObject.getString("longitude").toDouble()
        }

        val subscribed : MutableList<String> = mutableListOf()
        if (jsonObject.has("subscribed")) {
            val jsonSubscribedArray = jsonObject.getJSONArray("subscribed")
            for ( j in 0 until(jsonSubscribedArray.length()) )
                subscribed.add(jsonSubscribedArray.getString(j))
        }

        return UserInfo(
            "",
            jsonObject.getString("FCM_id"),
            jsonObject.getString("name"),
            jsonObject.getString("auth"),
            jsonObject.getString("content"),
            iconString,
            hashtag,
            latitude,
            longitude,
            permission,
            subscribed
        )
    }

    fun getJsonMessage(rawJsonString:String) : String {
        if (rawJsonString[0].toString() == "[") {
            val jsonObject = JSONArray(rawJsonString).getJSONObject(0)
            if ( jsonObject.has("message") )
                return jsonObject.getString("message")
        }
        return ""
    }

    fun loadImage(activity: Activity, imageView: ImageView,
                       idString:String, idInt: Int, type: String) : Boolean {
        var json = ""
        if ( type == "U" ) json = "{\"id\": \"$idString\""
        else if ( type == "P") json = "{\"id\": $idInt"
        json += ",\"type\": \"$type\" }"
        return callApiImage(activity, imageView, json, urlLoadImage)
    }

    fun uploadUserInfo(id:String, icon: String, name: String) : Boolean {
        val json = "{\"id\": \"$id\",\"icon\": \"$icon\",\"name\": \"$name\" }"
        val a = callApi(json, urlUploadUserInfo)
        return (getJsonMessage(a)=="No Error")
    }

    fun updateUserInfo(id:String, name: String, icon: String, hashtag: List<String>, content: String) : Boolean {
        var json = "{\"id\": \"$id\",\"name\": \"$name\", \"icon\": \"$icon\""
        json += ", \"hashtag\": [ "
        var isFirst = true
        for (hashtagItem in hashtag) {
            if ( !isFirst ) json += ", "
            else isFirst = false
            json += "\"$hashtagItem\""
        }
        json += " ]"
        json += ", \"content\": \"$content\" }"
        return (getJsonMessage(callApi(json, urlUpdateUserInfo))=="No Error")
    }

    fun updateUserIcon(id:String, icon: String) : String {
        val json = "{\"id\": \"$id\",\"icon\": \"$icon\"}"
        return callApi(json, urlUpdateUserIcon)
    }

    fun updateFCMToken(id:String, FCM_id: String) : Boolean {
        val json = "{\"id\": \"$id\",\"FCM_id\": \"$FCM_id\"}"
        return (getJsonMessage(callApi(json, urlUpdateFCMToken)) == "No Error" )
    }

    fun login(id:String, password: String, FCM_id: String) : Boolean {
        val json = "{\"id\": \"$id\", \"password\": \"$password\", \"FCM_id\": \"$FCM_id\"}"
        return (getJsonMessage(callApi(json, urlLogin)) == "No Error" )
    }

    fun getUserInfo(id:String) : UserInfo {
        val json = "{\"id\": \"$id\"}"
        return getJsonUserInfo(callApi(json, urlGetUserInfo))
    }

    fun createUser(id:String, password:String, FCM_id:String) : Boolean {
        val json = "{\"id\": \"$id\",\"password\": \"$password\",\"FCM_id\": \"$FCM_id\"}"
        return (getJsonMessage(callApi(json, urlCreateUser)) == "No Error" )
    }

    fun updateProposal(id:Int, content: String, hashtag: List<String>,
                       title: String, image:List<String> ) : String {
        var json = "{\"id\": $id, \"content\": \"$content\""
        json += ", \"hashtag\": [ "
        var isFirst = true
        for (hashtagItem in hashtag) {
            if ( !isFirst ) json += ", "
            else isFirst = false
            json += "\"$hashtagItem\""
        }
        json += " ]"
        json += ", \"title\": \"$title\""
        json += ", \"image\": [ "
        isFirst = true
        for (imageItem in image) {
            if ( !isFirst ) json += ", "
            else isFirst = false
            json += "\"$imageItem\""
        }
        json += " ] }"
        return callApi(json, urlUpdateProposal)
    }

//    fun postProposal(proposal: Proposal, type:String, area: String) : Boolean {
//        val poster_id = proposal.poster_id
//        val date = proposal.date
//        val title = proposal.title
//        val content = proposal.content
//        var json = "{\"poster_id\": \"$poster_id\", \"date\": \"$date\"" +
//                ", \"title\": \"$title\", \"content\": \"$content\", \"type\": \"$type\""
//        json += ", \"hashtag\": [ "
//        if (proposal.hashtag.isNotEmpty()) {
//            var isFirst = true
//            for (hashtagItem in proposal.hashtag) {
//                if ( !isFirst ) json += ", "
//                else isFirst = false
//                json += "\"$hashtagItem\""
//            }
//        }
//        json += " ]"
//        json += ", \"image\": [ "
//        if (proposal.imageList.isNotEmpty()) {
//            var isFirst = true
//            for (image in proposal.imageList) {
//                if ( !isFirst ) json += ", "
//                else isFirst = false
//                val imageString = convertImageToString64(image)
//                json += "\"$imageString\""
//            }
//        }
//        json += " ],  \"area\": \"$area\"}"
//        callApi(json, urlPostProposal)
//        return (getJsonMessage(getResponse()) == "No Error" )
//    }

//    fun getProposal(id:Int, type:String, area:String) : ArrayList<Proposal> {
//        val json = "{\"id\": $id, \"type\": \"$type\", \"area\": \"$area\"}"
//        callApi(json, urlGetProposal)
//        return getJsonProposalList(getResponse())
//    }

    fun clickInProposal(id:Int, target:String) : String {
        val json = "{\"id\": $id, \"target\": \"$target\"}"
        return callApi(json, urlClickInProposal)
    }

    fun postNotification(id:String,content:String, date:String) : Boolean {
        val json = "{\"id\": \"$id\", \"content\": \"$content\", \"date\": \"$date\"}"
        return getJsonMessage(callApi(json, urlPostNotification)) == "No Error"
    }

    fun deleteNotification(id:String, index:Int) : String {
//        val json = "{\"id\": \"$id\", \"index\": $index}"
//        return callApi(json, urlDeleteNotification)
        return ""
    }

    fun getNotification(subscribed:MutableList<String>) : ArrayList<Notification> {
        var json = "{\"subscribed\": ["
        var isFirst = true
        for (i in 0 until(subscribed.size)) {
            if (!isFirst)
                json += ", "
            json += "\"" + subscribed[i] + "\""
            isFirst = false
        }
        json += "]}"
        return getJsonNotificationList(callApi(json, urlGetNotification))
    }

    fun getJsoFriendList(rawJsonString : String) : ArrayList<String> {
        val dataList = ArrayList<String>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)
            if (jsonObject.has("id"))
                dataList.add(jsonObject.getString("id"))
        }
        return dataList
    }

    fun getFriendList(id:String) : ArrayList<String> {
        val json = "{\"id\": \"$id\"}"
        return getJsoFriendList(callApi(json, urlGetFriendList))
    }

    fun buildRelationship(master_id:String, slave_id: String) : Boolean {
        val json = "{\"master_id\": \"$master_id\",\"slave_id\": \"$slave_id\"}"
        return (getJsonMessage(callApi(json, urlBuildRelationship)) == "No Error" )
    }

    private fun callApi(json:String, apiUrl: ApiUrl) : String {
        val request = Request.Builder()
            .url(apiUrl.url)
            .post(RequestBody.create(jsonType, json))
            .build()

        var responseStrng = ""

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request, e: IOException) {
                responseStrng = e.message!!
                // if Unable... means wifi disconnect....
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response) {
                responseStrng = response.body().string()
                apiUrl.isCalling = false
            }
        })

        while (true)
            if (responseStrng != "")
                return responseStrng
    }

    fun scaleImage(srcBmp: Bitmap): Bitmap {
        val image:Bitmap
        if (srcBmp.getWidth() > srcBmp.getHeight()){

            image = Bitmap.createBitmap(
                srcBmp,
                srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                0,
                srcBmp.getHeight(),
                srcBmp.getHeight()
            );

        }else if (srcBmp.getWidth() < srcBmp.getHeight()){

            image = Bitmap.createBitmap(
                srcBmp,
                0,
                srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                srcBmp.getWidth(),
                srcBmp.getWidth()
            );
        }
        else image = srcBmp

        return image.scale(800,800)
    }

    fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun convertImageToString64(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 30, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.NO_WRAP)
    }

    fun convertUrlToImage(urlString: String) : Bitmap? {
        return BitmapFactory.decodeStream(URL(urlString).openConnection().getInputStream())
    }

    private fun callApiImage(activity: Activity, imageView: ImageView, json:String, apiUrl: ApiUrl) : Boolean {
        val request = Request.Builder()
            .url(apiUrl.url)
            .post(RequestBody.create(jsonType, json))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(response: Response) {
                val responseStr = response.body().string()
                val jsonArrayImage = JSONArray(responseStr)
                val jsonObjectImage = jsonArrayImage.getJSONObject(0)
                val imageStr = jsonObjectImage.getString("image")
                if ( imageStr.length > 30 )
                    activity.runOnUiThread {
                        imageView.setImageBitmap(convertString64ToImage(imageStr.substring(30)))
                    }
            }
        })

        return true
    }
}
