package com.choiho.ulife

import android.graphics.Bitmap
import android.util.Log
import com.choiho.ulife.discountTicket.DistountItem
import com.choiho.ulife.discountTicket.DistountTicket
import com.choiho.ulife.form.FormItem
import com.choiho.ulife.navigationUI.notifications.Notification
import com.choiho.ulife.navigationUI.home.Proposal
import com.choiho.ulife.navigationUI.home.ProposalItem
import com.choiho.ulife.navigationUI.userInfo.UserInfo
import com.squareup.okhttp.*
import org.json.JSONArray
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.io.IOException

class Api {
    private val client = OkHttpClient()
    private val jsonType: MediaType = MediaType.parse("application/json; charset=utf-8")

    private val urlDirectory = "https://6unoj2gvpj.execute-api.ap-southeast-1.amazonaws.com/Dev/"
    private val urlGetNotification = urlDirectory + "notification/get-notification"
    private val urlPostNotification = urlDirectory + "notification/post-notification"
    private val urlCreateUser = urlDirectory + "user/create-user"
    private val urlGetUserInfo = urlDirectory + "user/get-user-info"
    private val urlLogin = urlDirectory + "user/login"
    private val urlUpdateUserInfo = urlDirectory + "user/update-user-info"
    private val urlUploadUserInfo = urlDirectory + "user/upload-user-info"
    private val urlGetFoodByClass = urlDirectory + "zhongli/food/get-item/by-defult"
    private val urlGetFoodAll = urlDirectory + "zhongli/food/get-item/main-page"
    private val urlDeleteFoodItem = urlDirectory + "zhongli/food/delete-item"
    private val urlSearchUserByName = urlDirectory + "friend/search-user-name"
    private val urlSubscribe = urlDirectory + "subscription/build-relationship"
    private val urlDeleteSubscribe = urlDirectory + "subscription/delete-subscription"
    private val urlGetSubscribeList = urlDirectory + "subscription/get-follower"
    private val urlPostFoodItem = urlDirectory + "zhongli/food/post-item"
    private val urlGetFoodItem = urlDirectory + "zhongli/food/get-item"
    private val urlServerStatusCode = urlDirectory + "admin/server-statu-code"
    private val urlClickInNotification = urlDirectory + "notification/click-in-notification"
    private val urlComplaint = urlDirectory + "complaint"
    private val urlStudentPermission = urlDirectory + "user/student-authentication"
    private val urlUpgradeUserAuthToStudent = urlDirectory + "user/upgrade-user-auth-to-student"
    private val urlGetDiscountItem = urlDirectory + "zhongli/discount/get-item"
    private val urlPostDiscountTicket = urlDirectory + "zhongli/discount/post-discount-ticket"
    private val urlGetDiscountTicket = urlDirectory + "zhongli/discount/get-discount-ticket"
    private val urlGetForm = urlDirectory + "questionnaire/get-questionnaire"
    private val urlPostForm = urlDirectory + "questionnaire/post-questionnaire"
    private val urlGetRanking = urlDirectory + "ranking/get-ranking"
    private val urlClickInRanking = urlDirectory + "ranking/click-in-ranking"

    fun clickInRanking(id:String, index:Int, time:Int): Boolean {
        val json = "{\"id\": \"$id\", \"index\": $index, \"time\": $time}"
        return getJsonMessage(callApi(json, urlClickInRanking)) == "No Error"
    }

    private fun getRakingJson(rawJsonString : String): ArrayList<Int> {
        val dataList = ArrayList<Int>()
        val jsonArray = JSONArray(rawJsonString)
        val jsonObject = jsonArray.getJSONObject(0)
        val jsonRankingArray = jsonObject.getJSONArray("ranking")
        for ( i in 0 until(jsonRankingArray.length()) )
            dataList.add(jsonRankingArray.getInt(i))

        return dataList
    }

    fun getRanking(id:String): ArrayList<Int> {
        val json = "{\"id\": \"$id\"}"
        return getRakingJson(callApi(json, urlGetRanking))
    }

    fun postForm(id:String, student_id:String, ans:ArrayList<String>): Int {
        var json = "{\"id\": \"$id\", \"student_id\": \"$student_id\""
        json += ", \"ans\": [ "
        var isFirst = true
        for (ansItem in ans) {
            if ( !isFirst ) json += ", "
            else isFirst = false

            if (ansItem.get(0) == '[')
                json += ansItem
            else
                json += "\"$ansItem\""
        }
        json += "]"
        json += "}"

        val msg = getJsonMessage(callApi(json, urlPostForm))
        if (msg == "No Error" )
            return 1
        else if (msg == "Questionnaire Already Completed")
            return 2
        else
            return 0
    }

    private fun getFormJson(rawJsonString : String): ArrayList<FormItem> {
        val dataList = ArrayList<FormItem>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            if(jsonObject.has("message"))
                break

            val answerList : ArrayList<String> = arrayListOf()
            val jsonAnsArray = jsonObject.getJSONArray("ans")
            for ( j in 0 until(jsonAnsArray.length()) )
                answerList.add(jsonAnsArray.getString(j))

            dataList.add(
                FormItem(
                    jsonObject.getString("question"),
                    jsonObject.getString("type"),
                    answerList
                )
            )
        }
        return dataList
    }

    fun getForm(): ArrayList<FormItem> {
        val json = "{}"
        return getFormJson(callApi(json, urlGetForm))
    }

    private fun getDiscountTicketsJson(rawJsonString : String) :ArrayList<DistountTicket> {
        val dataList = ArrayList<DistountTicket>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            if(jsonObject.has("message"))
                break

            val timeInt = jsonObject.getInt("expiration_time")
            val triggerTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timeInt.toLong()),
                ZoneId.systemDefault()
            )
            if (triggerTime < LocalDateTime.now())
                continue

            dataList.add(
                DistountTicket(
                    jsonObject.getString("name"),
                    jsonObject.getString("content"),
                    jsonObject.getString("discount_code"),
                    jsonObject.getInt("create_time"),
                    timeInt
                )
            )
        }
        return dataList
    }

    fun getDiscountTickets(id:String, area: String): ArrayList<DistountTicket> {
        val json = "{\"id\": \"$id\", \"area\": \"$area\"}"
        return getDiscountTicketsJson(callApi(json, urlGetDiscountTicket))
    }

    fun postDiscountTicket(id: String, discount_code: String, area: String, shop_id:String, content:String): Boolean {
        val fixedContent = fixLineFeed(content)
        val json = "{\"id\": \"$id\", \"discount_code\": \"$discount_code\", \"area\": \"$area\", \"shop_id\": \"$shop_id\", \"content\": \"$fixedContent\"}"
        return getJsonMessage(callApi(json, urlPostDiscountTicket)) == "No Error"
    }

    private fun getDiscountItemsJson(rawJsonString : String) :ArrayList<DistountItem> {
        val dataList = ArrayList<DistountItem>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            if(jsonObject.has("message"))
                break

            dataList.add(
                DistountItem(
                    jsonObject.getString("discount_code"),
                    jsonObject.getInt("percentage"),
                    jsonObject.getString("content"),
                    jsonObject.getString("id"),
                    jsonObject.getString("name")
                )
            )
        }
        return dataList
    }

    fun getDiscountItems():ArrayList<DistountItem> {
        val json = "{}"
        return getDiscountItemsJson(callApi(json, urlGetDiscountItem))
    }

    fun upgradeUserAuthToStudent(student_id: String, school: String, id: String): Boolean {
        val json = "{\"student_id\": \"$student_id\", \"school\": \"$school\", \"id\": \"$id\"}"
        return getJsonMessage(callApi(json, urlUpgradeUserAuthToStudent)) == "No Error"
    }

    fun studentPermission(itouch_id: String, itouch_pw: String): Boolean {
        val json = "{\"itouch_id\": \"$itouch_id\", \"itouch_pw\": \"$itouch_pw\"}"
        return getJsonMessage(callApi(json, urlStudentPermission)) == "Login Success"
    }

    fun clickInNotification(id:String, date:String, content:String, view: Int, index: Int):Boolean {
        val fixedContent = fixLineFeed(content)
        val json = "{\"id\": \"$id\", \"date\": \"$date\", \"content\": \"$fixedContent\", \"view\": $view, \"index\": $index}"
        return getJsonMessage(callApi(json, urlClickInNotification)) == "No Error"
    }

    fun getServerStatusCode(): Boolean {
        val json = "{}"
        return getJsonMessage(callApi(json, urlServerStatusCode)) == "No Error"
    }

    fun complaint(complained_id: String, complainant: String, timestamp: String, type: String, content: String): Boolean {
        val fixedContent = fixLineFeed(content)
        val json = "{\"complained_id\": \"$complained_id\", \"complainant\": \"$complainant\", \"timestmp\": \"$timestamp\", \"type\": \"$type\", \"content\": \"$fixedContent\"}"
        return getJsonMessage(callApi(json, urlComplaint)) == "No Error"
    }

    fun deleteFoodItem(type: String, id: String, index: Int, area: String, image: String): Boolean {
        val json = "{\"type\": \"$type\", \"id\": \"$id\", \"index\": $index, \"area\": \"$area\", \"image\": [\"$image\"]}"
        return getJsonMessage(callApi(json, urlDeleteFoodItem)) == "No Error"
    }

    fun getFoodItem(id:String, type:String, area:String): Proposal {
        val json = "{\"id\": \"$id\", \"type\": \"$type\", \"area\": \"$area\"}"
        return getFoodAllJson(callApi(json, urlGetFoodItem))[0]
    }

    fun postFoodItem(id: String, title: String, content: String, date: String,
                     type: String, hashtag: MutableList<String>, image: Bitmap, area: String ): Boolean {
        val fixedContent = fixLineFeed(content)
        var json = "{\"id\": \"$id\", \"title\": \"$title\", \"content\": \"$fixedContent\", \"date\": \"$date\"," +
                "\"type\": \"$type\", \"hashtag\": ["

        var isFirst = true
        for (i in 0 until(hashtag.size)) {
            if (!isFirst) {
                json += ","
                isFirst = false
            }
            json += "\"" + hashtag[i] + "\""
        }
        json += "]"

        val imageSmallString = GlobalVariables.imageHelper.getString(
            GlobalVariables.imageHelper.scaleImage(image, 340))
        val imageLargeString = GlobalVariables.imageHelper.getString(
            GlobalVariables.imageHelper.scaleImage(image, 800))

        json += ", \"image\": [\"$imageSmallString\", \"$imageLargeString\"]"
        json += ", \"area\": \"$area\"}"

        return getJsonMessage(callApi(json, urlPostFoodItem)) == "No Error"
    }

    private fun getSubscribeListJson(rawJsonString : String) : ArrayList<UserInfo> {
        val dataList = ArrayList<UserInfo>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            if(jsonObject.has("message"))
                break

            dataList.add(
                UserInfo(
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
                )
            )
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

    private fun searchUserByNameJson(rawJsonString : String) : ArrayList<UserInfo> {
        val dataList = ArrayList<UserInfo>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            val iconUrl = jsonObject.getString("icon")
            var iconString = ""
            if (iconUrl.length > 10) {
                val icon = GlobalVariables.imageHelper.convertUrlToImage(iconUrl)
                if (icon != null)
                    iconString = GlobalVariables.imageHelper.getString(icon)!!
            }

            dataList.add(
                UserInfo(
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
                )
            )
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

    private fun getFoodAllJson(rawJsonString : String) : ArrayList<Proposal> {
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

                itemList.add(
                    ProposalItem(
                        jsonItemObject.getString("date"),
                        imageUrlList,
                        jsonItemObject.getInt("view"),
                        jsonItemObject.getString("title"),
                        jsonItemObject.getString("content"),
                        hashtagList,
                        mutableListOf()
                    )
                )
            }

            dataList.add(
                Proposal(
                    jsonObject.getString("name"),
                    jsonObject.getString("id"),
                    jsonObject.getString("content"),
                    itemList
                )
            )
        }

        for ( i in 0 until(dataList.size)) {
            Thread {
                dataList[i].proposalItemList[0].convertImageUrlToImageOnlyOne()
            }.start()
        }
        return dataList
    }

    fun getFoodAll(serial:Int, area:String) : ArrayList<Proposal> {
        try {
            val json = "{\"serial\": $serial, \"area\": \"$area\"}"
            return getFoodAllJson(callApi(json, urlGetFoodAll))
        }
        catch(e: Exception) {
            return arrayListOf()
        }
    }

    private fun getNotificationListJson(rawJsonString : String) : ArrayList<Notification> {
        val dataList = ArrayList<Notification>()
        val jsonArray = JSONArray(rawJsonString)
        for ( i in 0 until(jsonArray.length()) ) {
            val jsonObject = jsonArray.getJSONObject(i)

            if (jsonObject.has("message"))
                break

            val pusherId = jsonObject.getString("id")

            val jsonNotificationArray = jsonObject.getJSONArray("notification")
            for (j in 0 until(jsonNotificationArray.length())) {
                val jsonNotificationItem = jsonNotificationArray.getJSONObject(j)
                dataList.add(
                    Notification(
                        pusherId,
                        jsonNotificationItem.getString("date"),
                        jsonNotificationItem.getString("content"),
                        jsonNotificationItem.getInt("view")
                    )
                )
            }
        }
        return dataList
    }

    private fun getUserInfoJson(rawJsonString : String) : UserInfo {
        Log.d(">>>>>>>>>>>>>>", rawJsonString)
        val jsonObject = JSONArray(rawJsonString).getJSONObject(0)

        val iconUrl = jsonObject.getString("icon")
        var iconString = ""
        if (iconUrl.length > 10) {
            val icon = GlobalVariables.imageHelper.convertUrlToImage(iconUrl)
            if (icon != null)
                iconString = GlobalVariables.imageHelper.getString(icon)!!
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

        if (jsonObject.has("student_id")) {
            val studentPermissionID = jsonObject.getString("student_id")
            GlobalVariables.studentPermissionID = studentPermissionID
            GlobalVariables.dbHelper.writeDB("studentPermissionID", studentPermissionID)
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

    private fun getJsonMessage(rawJsonString:String) : String {
        if (rawJsonString[0].toString() == "[") {
            val jsonObject = JSONArray(rawJsonString).getJSONObject(0)
            if ( jsonObject.has("message") )
                return jsonObject.getString("message")
        }
        return ""
    }

    fun uploadUserInfo(id:String, icon: String, name: String) : Boolean {
        val bm = GlobalVariables.imageHelper.convertString64ToImage(icon)
        val scaled = GlobalVariables.imageHelper.scaleImage(bm, 340)
        val newIcon = GlobalVariables.imageHelper.getString(scaled)

        val json = "{\"id\": \"$id\",\"icon\": \"$newIcon\",\"name\": \"$name\" }"
        return (getJsonMessage(callApi(json, urlUploadUserInfo))=="No Error")
    }

    fun updateUserInfo(id:String, name: String, icon: String, hashtag: List<String>, content: String) : Boolean {
        val bm = GlobalVariables.imageHelper.convertString64ToImage(icon)
        val scaled = GlobalVariables.imageHelper.scaleImage(bm, 340)
        val newIcon = GlobalVariables.imageHelper.getString(scaled)

        var json = "{\"id\": \"$id\",\"name\": \"$name\", \"icon\": \"$newIcon\""
        json += ", \"hashtag\": [ "
        var isFirst = true
        for (hashtagItem in hashtag) {
            if ( !isFirst ) json += ", "
            else isFirst = false
            json += "\"$hashtagItem\""
        }
        json += " ]"
        val fixedContent = fixLineFeed(content)
        json += ", \"content\": \"$fixedContent\" }"

        return (getJsonMessage(callApi(json, urlUpdateUserInfo))=="No Error")
    }

    fun login(id:String, password: String, FCM_id: String) : Boolean {
        val json = "{\"id\": \"$id\", \"password\": \"$password\", \"FCM_id\": \"$FCM_id\"}"
        return (getJsonMessage(callApi(json, urlLogin)) == "No Error" )
    }

    fun getUserInfo(id:String) : UserInfo {
        val json = "{\"id\": \"$id\"}"
        val userInfo = getUserInfoJson(callApi(json, urlGetUserInfo))
        userInfo.ID = id
        return userInfo
    }

    fun createUser(id:String, password:String, FCM_id:String) : Boolean {
        val json = "{\"id\": \"$id\",\"password\": \"$password\",\"FCM_id\": \"$FCM_id\"}"
        return (getJsonMessage(callApi(json, urlCreateUser)) == "No Error" )
    }

    fun postNotification(id:String,content:String, date:String) : Boolean {
        val fixedContent = fixLineFeed(content)
        val json = "{\"id\": \"$id\", \"content\": \"$fixedContent\", \"date\": \"$date\"}"
        return getJsonMessage(callApi(json, urlPostNotification)) == "No Error"
    }

    fun getNotificationList(subscribed:MutableList<String>) : ArrayList<Notification> {
        var json = "{\"subscribed\": ["
        var isFirst = true
        for (i in 0 until(subscribed.size)) {
            if (!isFirst)
                json += ", "
            json += "\"" + subscribed[i] + "\""
            isFirst = false
        }
        json += "]}"
        return getNotificationListJson(callApi(json, urlGetNotification))
    }

    private fun fixLineFeed(json: String): String {
        var newJson = ""
        for (char in json) {
            if (char == '\n') newJson += "\\n"
            else newJson += char
        }
        return newJson
    }

    private fun callApi(json:String, apiUrl: String) : String {
        val request = Request.Builder()
            .url(apiUrl)
            .post(RequestBody.create(jsonType, json))
            .build()

        var responseStrng = ""
        var needToCallAgain = false
        var isSuccess = false

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request, e: IOException) {
                responseStrng = e.message!!
                needToCallAgain = true
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response) {
                responseStrng = response.body().string()
                isSuccess = true
            }
        })

        while (true) {
            if (needToCallAgain) {
                needToCallAgain = false
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(request: Request, e: IOException) {
                        responseStrng = e.message!!
                        needToCallAgain = true
                    }

                    @Throws(IOException::class)
                    override fun onResponse(response: Response) {
                        responseStrng = response.body().string()
                        isSuccess = true
                    }
                })
            }
            else if (isSuccess) {
                break
            }
        }

        return responseStrng
    }
}
