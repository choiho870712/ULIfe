package com.example.startupboard.ui.api

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import com.squareup.okhttp.*
import org.json.JSONArray
import java.io.IOException
import com.example.startupboard.GlobalVariables.Companion.globalApiRseponse
import com.example.startupboard.GlobalVariables.Companion.globalEmptyBitmap

class Api {
    data class ApiUrl( val url:String, var isCalling:Boolean )
    private val urlDirectory = "https://6unoj2gvpj.execute-api.ap-southeast-1.amazonaws.com/Dev/"
    private val urlBuildRelationship = ApiUrl(urlDirectory+"friend/build-relationship", false)
    private val urlGetFriendList = ApiUrl(urlDirectory+"friend/get-friend-list", false)
    private val urlGetNotification = ApiUrl(urlDirectory+"notification/get-notification", false)
    private val urlDeleteNotification = ApiUrl(urlDirectory+"notification/delete-notification", false)
    private val urlPostNotification = ApiUrl(urlDirectory+"notification/post-notification", false)
    private val urlClickInProposal = ApiUrl(urlDirectory+"proposal/click-in-proposal", false)
    private val urlGetProposal = ApiUrl(urlDirectory+"proposal/get-proposal", false)
    private val urlPostProposal = ApiUrl(urlDirectory+"proposal/post-proposal", false)
    private val urlUpdateProposal = ApiUrl(urlDirectory+"proposal/update-proposal", false)
    private val urlCreateUser = ApiUrl(urlDirectory+"user/create-user", false)
    private val urlGetUserInfo = ApiUrl(urlDirectory+"user/get-user-info", false)
    private val urlLogin = ApiUrl(urlDirectory+"user/login", false)
    private val urlUpdateFCMToken = ApiUrl(urlDirectory+"user/update-fcm-token", false)
    private val urlUpdateUserIcon = ApiUrl(urlDirectory+"user/update-user-icon", false)
    private val urlUpdateUserInfo = ApiUrl(urlDirectory+"user/update-user-info", false)
    private val urlUploadUserInfo = ApiUrl(urlDirectory+"user/upload-user-info", false)
    private val urlLoadImage = ApiUrl(urlDirectory+"load-img", false)

    fun getJsonNotificationList(rawJsonString : String) : ArrayList<Notification> {
        val dataList = ArrayList<Notification>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            dataList.add(Notification(
                jsonObject.getString("pusher_id"),
                jsonObject.getString("date"),
                jsonObject.getString("content"),
                jsonObject.getInt("index")
            ))
        }
        return dataList
    }

    fun getJsonProposalList(rawJsonString : String) : ArrayList<Proposal> {
        val dataList = ArrayList<Proposal>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)
            val hashtag : MutableList<String> = mutableListOf()
            val jsonHashtagArray = jsonObject.getJSONArray("hashtag")
            for ( j in 0 until(jsonHashtagArray.length()) )
                hashtag.add(jsonHashtagArray.getString(j))

            dataList.add(Proposal(
                jsonObject.getString("name"),
                jsonObject.getInt("id"),
                jsonObject.getString("date"),
                jsonObject.getInt("view"),
                jsonObject.getString("poster_id"),
                jsonObject.getInt("image"),
                jsonObject.getString("content"),
                jsonObject.getString("title"),
                hashtag
            ))
        }
        return dataList
    }

    fun getJsonUserInfo(rawJsonString : String) : UserInfo {
        val jsonObject = JSONArray(rawJsonString).getJSONObject(0)

        val iconString = jsonObject.getString("icon")
        lateinit var icon : Bitmap
        if (iconString.length > 30)
            icon = convertString64ToImage(iconString.substring(30))
        else icon = globalEmptyBitmap

        val hashtag : MutableList<String> = mutableListOf()
        val jsonHashtagArray = jsonObject.getJSONArray("hashtag")
        for ( j in 0 until(jsonHashtagArray.length()) )
            hashtag.add(jsonHashtagArray.getString(j))

        val organization : MutableList<String> = mutableListOf()
        val jsonOrganizationArray = jsonObject.getJSONArray("organization")
        for ( j in 0 until(jsonOrganizationArray.length()) )
            organization.add(jsonOrganizationArray.getString(j))

        return UserInfo(
            "",
            jsonObject.getString("FCM_id"),
            jsonObject.getString("name"),
            jsonObject.getString("auth"),
            jsonObject.getString("content"),
            icon,
            iconString,
            hashtag,
            organization
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

    fun getResponse() : String {
        while( globalApiRseponse == "calling api..." ||
               globalApiRseponse == "able to call new api..." )
            Thread.sleep(100)
        val response = globalApiRseponse
        globalApiRseponse = "able to call new api..."
        return response
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
        callApi(json, urlUploadUserInfo)
        return (getJsonMessage(getResponse())=="No Error")
    }

    fun updateUserInfo(id:String, icon: String, content: String, name: String,
                       hashtag: List<String>, organization: List<String>) : Boolean {
        var json = "{\"id\": \"$id\",\"icon\": \"$icon\", \"content\": \"$content\",\"name\": \"$name\""
        json += ", \"hashtag\": [ "
        var isFirst = true
        for (hashtagItem in hashtag) {
            if ( !isFirst ) json += ", "
            else isFirst = false
            json += "\"$hashtagItem\""
        }
        json += " ]"
        json += ", \"organization\": [ "
        isFirst = true
        for (organizationItem in organization) {
            if ( !isFirst ) json += ", "
            else isFirst = false
            json += "\"$organizationItem\""
        }
        json += " ] }"
        callApi(json, urlUpdateUserInfo)
        return (getJsonMessage(getResponse())=="No Error")
    }

    fun updateUserIcon(id:String, icon: String) : String {
        val json = "{\"id\": \"$id\",\"icon\": \"$icon\"}"
        callApi(json, urlUpdateUserIcon)
        return getResponse()
    }

    fun updateFCMToken(id:String, FCM_id: String) : String {
        val json = "{\"id\": \"$id\",\"FCM_id\": \"$FCM_id\"}"
        callApi(json, urlUpdateFCMToken)
        return getResponse()
    }

    fun login(id:String, password: String, FCM_id: String) : Boolean {
        val json = "{\"id\": \"$id\", \"password\": \"$password\", \"FCM_id\": \"$FCM_id\"}"
        callApi(json, urlLogin)
        return (getJsonMessage(getResponse()) == "No Error" )
    }

    fun getUserInfo(id:String) : UserInfo {
        val json = "{\"id\": \"$id\"}"
        callApi(json, urlGetUserInfo)
        return getJsonUserInfo(getResponse())
    }

    fun createUser(id:String, password:String, FCM_id:String) : Boolean {
        val json = "{\"id\": \"$id\",\"password\": \"$password\",\"FCM_id\": \"$FCM_id\"}"
        callApi(json, urlCreateUser)
        val message = getResponse()
        return (getJsonMessage(message) == "No Error" )
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
        callApi(json, urlUpdateProposal)
        return getResponse()
    }

    fun postProposal(proposal: Proposal) : Boolean {
        val poster_id = proposal.poster_id
        val date = proposal.date
        val title = proposal.title
        val content = proposal.content
        var json = "{\"poster_id\": \"$poster_id\", \"date\": \"$date\"" +
                ", \"title\": \"$title\", \"content\": \"$content\""
        json += ", \"hashtag\": [ "
        if (proposal.hashtag.isNotEmpty()) {
            var isFirst = true
            for (hashtagItem in proposal.hashtag) {
                if ( !isFirst ) json += ", "
                else isFirst = false
                json += "\"$hashtagItem\""
            }
        }
        else {
            json += "\"nil\""
        }
        json += " ]"
        json += ", \"image\": [ "
        json += "\"nil\""
        json += " ] }"
        callApi(json, urlPostProposal)
        return (getJsonMessage(getResponse()) == "No Error" )
    }

    fun getProposal(id:Int) : ArrayList<Proposal> {
        val json = "{\"id\": $id}"
        callApi(json, urlGetProposal)
        return getJsonProposalList(getResponse())
    }

    fun clickInProposal(id:Int, target:String) : String {
        val json = "{\"id\": $id, \"target\": \"$target\"}"
        callApi(json, urlClickInProposal)
        return getResponse()
    }

    fun postNotification(id:String, pusher_id:String, type:String,content:String, date:String) : String {
        val json = "{\"id\": \"$id\", \"pusher_id\": \"$pusher_id\", \"type\": \"$type\", " +
                "\"content\": \"$content\", \"date\": \"$date\"}"
        callApi(json, urlPostNotification)
        return getResponse()
    }

    fun deleteNotification(id:String, index:Int) : String {
        val json = "{\"id\": \"$id\", \"index\": $index}"
        callApi(json, urlDeleteNotification)
        return getResponse()
    }

    fun getNotification(id:String, type:String) : ArrayList<Notification> {
        val json = "{\"id\": \"$id\", \"type\": \"$type\"}"
        callApi(json, urlGetNotification)
        return getJsonNotificationList(getResponse())
    }

    fun getFriendList(id:String) : String {
        val json = "{\"id\": \"$id\"}"
        callApi(json, urlGetFriendList)
        return getResponse()
    }

    fun buildRelationship(master_id:String, slave_id: String) : String {
        val json = "{\"master_id\": \"$master_id\",\"slave_id\": \"$slave_id\"}"
        callApi(json, urlBuildRelationship)
        return getResponse()
    }

    private val client = OkHttpClient()
    private val jsonType: MediaType = MediaType.parse("application/json; charset=utf-8")

    private fun callApi(json:String, apiUrl: ApiUrl) {
        while (globalApiRseponse != "able to call new api...")
            Thread.sleep(100)

        globalApiRseponse = "calling api..."
        val request = Request.Builder()
            .url(apiUrl.url)
            .post(RequestBody.create(jsonType, json))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request, e: IOException) {
                globalApiRseponse = e.message!!
                // if Unable... means wifi disconnect....
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response) {
                globalApiRseponse = response.body().string()
                apiUrl.isCalling = false
            }
        })
    }

    fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
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
