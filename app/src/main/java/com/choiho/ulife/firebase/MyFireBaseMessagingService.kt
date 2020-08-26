package com.choiho.ulife.firebase

import android.util.Log
import com.choiho.ulife.GlobalVariables
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.squareup.okhttp.*
import java.io.IOException

class MyFireBaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            GlobalVariables.functions.makeToast("您有新的通知!!")

            Thread {
                if (!GlobalVariables.userInfo.isEmpty())
                    GlobalVariables.functions.updateNotificationList()
            }.start()
        }
    }

    private val gson = Gson()
    private val client = OkHttpClient()
    private val jsonType: MediaType = MediaType.parse("application/json; charset=utf-8")
    private val urlRequestSendingFCM = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "AAAA5zsYgfw:APA91bGdwSEuQcpkVANBwJ5Kl9SrF6ut6Dt4iEp3sHG1OkT92NCjU99h6KqAnR51vB6hWzL4FxFuvl_7NVRurivrzFiyC-wWDiP0KQa3TE47JaHbm6NqAVfam2EdVEGpgMg0G-hkK3tT"
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
                Log.d(">>>>>>>>>Fail>>>", e.message!!)
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response) {
                Log.d(">>>>>>Success>>>", response.body().string())
            }
        })
    }
}