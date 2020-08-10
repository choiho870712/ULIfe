package com.choiho.ulife

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...

        val TAG = ">>>>>>>>>>>>>>>>>>>>>>>>>>"

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")

            val message = it.body.toString()

            GlobalVariables.activity.runOnUiThread(Runnable {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            })

            Thread {
                if (!GlobalVariables.userInfo.isEmpty()) {
                    GlobalVariables.functions.updateNotificationList()

//                    Thread {
//                        while (!GlobalVariables.notificationFriendListIsReady)
//                            continue
//                        if (!GlobalVariables.notificationFriendList.isEmpty()) {
//                            GlobalVariables.activity.runOnUiThread(Runnable {
//                                GlobalVariables.notificationListFriendAdapter.notifyDataSetChanged()
//                                GlobalVariables.notificationListFriendLayoutManager.scrollToPosition(
//                                    GlobalVariables.notificationListFriendLayoutManager.itemCount-1
//                                )
//                            })
//                        }
//                    }.start()
                }
            }.start()
        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}