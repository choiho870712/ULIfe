package com.choiho.ulife

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.ui.api.Notification
import com.choiho.ulife.ui.api.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class GlobalFunctions {

    fun readDataFromApi(id: String) {
        readUserInfoFromApi(id)
        Thread {
            while (true)
                if (GlobalVariables.UserInfoIsReady)
                    break

            readSubscribeListFromApi(id)
            if (!GlobalVariables.userInfo.isShop()) readNotificationListFromApi()
//                readFriendListFromApi(id)
        }.start()
    }

    private fun readUserInfoFromApi(id:String) {
        Thread {
            GlobalVariables.userInfo = GlobalVariables.api.getUserInfo(id)
            GlobalVariables.userInfo.ID = id
            GlobalVariables.userInfo.FMC_ID = GlobalVariables.FCM_token
            GlobalVariables.UserInfoIsReady = true
        }.start()
    }

    fun readNotificationListFromApi() {
        Thread {
            val shopNotificationList = GlobalVariables.api.getNotification(GlobalVariables.userInfo.subscribed)
            for(i in 0 until(shopNotificationList.size))
                shopNotificationList[i].type = "shop"

            GlobalVariables.notificationShopList.clear()
            GlobalVariables.notificationShopList.addAll(shopNotificationList)

            val idList = ArrayList<String>()
            for (i in 0 until(GlobalVariables.notificationShopList.size)) {
                val pusher_id = GlobalVariables.notificationShopList[i].pusher_id
                if (!idList.contains(pusher_id)) idList.add(pusher_id)
            }

            var countDown = idList.size
            for (i in 0 until(idList.size)) {
                Thread {
                    val pusher_id = idList[i]
                    val userInfo = GlobalVariables.api.getUserInfo(pusher_id)
                    userInfo.ID = pusher_id
                    GlobalVariables.notificationShopUserInfoList.add(userInfo)
                    countDown--
                }.start()
            }

            while (true)
                if (countDown == 0) {
                    GlobalVariables.notificationShopListIsReady = true
                    break
                }
        }.start()

//        Thread {
//            val friendNotificationList = GlobalVariables.api.getNotification(id, "friend")
//            for(i in 0 until(friendNotificationList.size))
//                friendNotificationList[i].type = "friend"
//
//            GlobalVariables.notificationFriendList.clear()
//            GlobalVariables.notificationFriendList.addAll(friendNotificationList)
//
//            val idList = ArrayList<String>()
//            for (i in 0 until(GlobalVariables.notificationFriendList.size)) {
//                val pusher_id = GlobalVariables.notificationFriendList[i].pusher_id
//                if (!idList.contains(pusher_id)) idList.add(pusher_id)
//            }
//
//            var countDown = idList.size
//            for (i in 0 until(idList.size)) {
//                Thread {
//                    val pusher_id = idList[i]
//                    val userInfo = GlobalVariables.api.getUserInfo(pusher_id)
//                    userInfo.ID = pusher_id
//                    GlobalVariables.notificationFriendUserInfoList.add(userInfo)
//                    countDown--
//                }.start()
//            }
//
//            while (true)
//                if (countDown == 0) {
//                    GlobalVariables.notificationFriendListIsReady = true
//                    break
//                }
//        }.start()
    }

    private fun readFriendListFromApi(id:String) {
//        Thread {
//            GlobalVariables.friendStringList = GlobalVariables.api.getFriendList(id)
//            GlobalVariables.friendList.clear()
//            var countDown = GlobalVariables.friendStringList.size
//            for ( i in 0 until(GlobalVariables.friendStringList.size) ) {
//                Thread {
//                    val friendInfo = GlobalVariables.api.getUserInfo(GlobalVariables.friendStringList[i])
//                    friendInfo.ID = GlobalVariables.friendStringList[i]
//                    GlobalVariables.friendList.add(friendInfo)
//                    countDown--
//                }.start()
//            }
//
//            while (true)
//                if (countDown == 0) {
//                    GlobalVariables.friendListIsReady = true
//                    break
//                }
//        }.start()
    }

    fun readSubscribeListFromApi(id:String) {
        Thread {
            if (GlobalVariables.userInfo.isShop()) {
                val subscribeList = GlobalVariables.api.getSubscribeList(id)
                GlobalVariables.subscribeList.clear()
                GlobalVariables.subscribeList.addAll(subscribeList)
                GlobalVariables.subscribeListIsReady = true
            }
            else {
                val subscribeStringList = GlobalVariables.userInfo.subscribed
                GlobalVariables.subscribeList.clear()
                for (i in 0 until(subscribeStringList.size)) {
                    Thread {
                        val subscriberId = subscribeStringList[i]
                        val subscriberUserInfo = GlobalVariables.api.getUserInfo(subscriberId)
                        subscriberUserInfo.ID = subscriberId
                        GlobalVariables.subscribeList.add(subscriberUserInfo)
                    }.start()
                }

                while (GlobalVariables.subscribeList.size != subscribeStringList.size)
                    continue

                GlobalVariables.subscribeListIsReady = true
            }
        }.start()
    }

    fun writeDataToSQL() {
        writeUserInfoToSQL()
        Thread {
            while(true)
                if (GlobalVariables.UserInfoIsReady)
                    break

            writeNotificationListToSQL()
            writeSubscribeListToSQL()
            if (GlobalVariables.userInfo.isShop()) writeFriendListToSQL()
        }.start()
    }

    private fun writeUserInfoToSQL() {
        Thread {
            while (true)
                if (GlobalVariables.UserInfoIsReady) {
                    GlobalVariables.userInfo.writeDB("userInfo")
                    break
                }
        }.start()
    }

    fun writeNotificationListToSQL() {
        Thread {
            while (true)
                if (GlobalVariables.notificationShopListIsReady) {
                    for (i in 0 until(GlobalVariables.notificationShopList.size))
                        GlobalVariables.notificationShopList[i].writeDB("notificationShop$i")
                    for (i in 0 until(GlobalVariables.notificationShopUserInfoList.size))
                        GlobalVariables.notificationShopUserInfoList[i].writeDB("notificationShopUserInfo$i")
                    break
                }
        }.start()

//        Thread {
//            while (true)
//                if (GlobalVariables.notificationFriendListIsReady) {
//                    for (i in 0 until(GlobalVariables.notificationFriendList.size))
//                        GlobalVariables.notificationFriendList[i].writeDB("notificationFriend$i")
//                    for (i in 0 until(GlobalVariables.notificationFriendUserInfoList.size))
//                        GlobalVariables.notificationFriendUserInfoList[i].writeDB("notificationFriendUserInfo$i")
//                    break
//                }
//        }.start()
    }

    private fun writeFriendListToSQL() {
//        Thread {
//            while (true)
//                if (GlobalVariables.friendListIsReady) {
//                    for (i in 0 until(GlobalVariables.friendList.size))
//                        GlobalVariables.friendList[i].writeDB("friendUserInfo$i")
//                    break
//                }
//        }.start()
    }

    private fun writeSubscribeListToSQL() {
        Thread {
            while (true)
                if (GlobalVariables.subscribeListIsReady) {
                    for (i in 0 until(GlobalVariables.subscribeList.size))
                        GlobalVariables.subscribeList[i].writeDB("subscribeUserInfo$i")
                    break
                }
        }.start()
    }

    fun readDataFromSQL():Boolean {
        if (readUserInfoFromSQL())
        {
//            readNotificationListFromSQL()
//            if (GlobalVariables.userInfo.isShop()) readSubscribeListFromSQL()
//            else readFriendListFromSQL()

            Thread {
                while (true)
                    if (GlobalVariables.UserInfoIsReady)
                        break

                readSendedNotificationFromSQL()
                readSubscribeListFromApi(GlobalVariables.userInfo.ID)
                updateDataToSQL()
            }.start()

            return true
        }
        return false
    }

    private fun readUserInfoFromSQL():Boolean {
        GlobalVariables.UserInfoIsReady = false
        GlobalVariables.userInfo.clear()
        GlobalVariables.UserInfoIsReady = GlobalVariables.userInfo.readDB("userInfo")
        return GlobalVariables.UserInfoIsReady
    }

    private fun readNotificationListFromSQL() {
        var notificationShopUserInfoIsReady = false
        var notificationShopIsReady = false
        GlobalVariables.notificationShopListIsReady = false

        Thread {
            try {
                var count = 0
                GlobalVariables.notificationShopUserInfoList.clear()
                while (true) {
                    val tempUserInfo = UserInfo("", "", "", "", "", "", mutableListOf(), 0.0, 0.0, mutableListOf(), mutableListOf())
                    if (tempUserInfo.readDB("notificationShopUserInfo$count")) {
                        GlobalVariables.notificationShopUserInfoList.add(tempUserInfo)
                        count++
                    }
                    else break
                }
                notificationShopUserInfoIsReady = true

                count = 0
                GlobalVariables.notificationShopList.clear()
                while (true) {
                    val tempNotification = Notification("", "", "", 0)
                    if (tempNotification.readDB("notificationShop$count")) {
                        GlobalVariables.notificationShopList.add(tempNotification)
                        count++
                    }
                    else break
                }
                notificationShopIsReady = true
            }
            catch (e: Exception) {
                readNotificationListFromApi()
            }
        }.start()

        Thread {
            while (true)
                if (notificationShopUserInfoIsReady && notificationShopIsReady) {
                    GlobalVariables.notificationShopListIsReady = true
                    break
                }
        }.start()

//        var notificationFriendUserInfoIsReady = false
//        var notificationFriendIsReady = false
//        GlobalVariables.notificationFriendListIsReady = false
//
//        Thread {
//            try {
//                var count = 0
//                GlobalVariables.notificationFriendUserInfoList.clear()
//                while (true) {
//                    val tempUserInfo = UserInfo("", "", "", "", "", "", mutableListOf(), 0.0, 0.0, mutableListOf())
//                    if (tempUserInfo.readDB("notificationFriendUserInfo$count")) {
//                        GlobalVariables.notificationFriendUserInfoList.add(tempUserInfo)
//                        count++
//                    }
//                    else break
//                }
//                notificationFriendUserInfoIsReady = true
//
//                count = 0
//                GlobalVariables.notificationFriendList.clear()
//                while (true) {
//                    val tempNotification = Notification("", "", "", 0)
//                    if (tempNotification.readDB("notificationFriend$count")) {
//                        GlobalVariables.notificationFriendList.add(tempNotification)
//                        count++
//                    }
//                    else break
//                }
//                notificationFriendIsReady = true
//            }
//            catch (e: Exception) {
//                readNotificationListFromApi(GlobalVariables.userInfo.ID)
//            }
//        }.start()
//
//        Thread {
//            while (true)
//                if (notificationFriendUserInfoIsReady && notificationFriendIsReady) {
//                    GlobalVariables.notificationFriendListIsReady = true
//                    break
//                }
//        }.start()
    }

    private fun readSubscribeListFromSQL() {
        GlobalVariables.subscribeListIsReady = false
        Thread {
            try {
                var count = 0
                GlobalVariables.subscribeList.clear()
                while (true) {
                    val tempUserInfo = UserInfo("", "", "", "", "", "", mutableListOf(), 0.0, 0.0, mutableListOf(), mutableListOf())
                    if (tempUserInfo.readDB("subscribeUserInfo$count")) {
                        GlobalVariables.subscribeList.add(tempUserInfo)
                        count++
                    }
                    else break
                }
                GlobalVariables.subscribeListIsReady = true
            }
            catch (e:Exception) {
                readSubscribeListFromApi(GlobalVariables.userInfo.ID)
            }
        }.start()
    }

    private fun readFriendListFromSQL() {
//        GlobalVariables.friendListIsReady = false
//        Thread {
//            try {
//                var count = 0
//                GlobalVariables.friendList.clear()
//                while (true) {
//                    val tempUserInfo = UserInfo("", "", "", "", "", "", mutableListOf(), 0.0, 0.0, mutableListOf())
//                    if (tempUserInfo.readDB("friendUserInfo$count")) {
//                        GlobalVariables.friendList.add(tempUserInfo)
//                        count++
//                    }
//                    else break
//                }
//                GlobalVariables.friendListIsReady = true
//            }
//            catch (e:Exception){
//                readFriendListFromApi(GlobalVariables.userInfo.ID)
//            }
//        }.start()
    }

    fun deleteDataFromSQL() {
        deleteUserInfoFromSQL()
        deleteNotificationListFromSQL()
        deleteSubscribeListFromSQL()
        deleteFriendListFromSQL()
    }

    private fun deleteUserInfoFromSQL() {
        GlobalVariables.UserInfoIsReady = false
        GlobalVariables.userInfo.deleteDB("userInfo")
    }

    fun deleteNotificationListFromSQL() {
        GlobalVariables.notificationShopListIsReady = false

        Thread {
            for (i in 0 until(GlobalVariables.notificationShopUserInfoList.size))
                GlobalVariables.notificationShopUserInfoList[i].deleteDB("notificationShopUserInfo$i")

            GlobalVariables.notificationShopUserInfoList.clear()
        }.start()

        Thread {
            for (i in 0 until(GlobalVariables.notificationShopList.size))
                GlobalVariables.notificationShopList[i].deleteDB("notificationShop$i")

            GlobalVariables.notificationShopList.clear()
        }.start()

//        GlobalVariables.notificationFriendListIsReady = false

//        Thread {
//            for (i in 0 until(GlobalVariables.notificationFriendUserInfoList.size))
//                GlobalVariables.notificationFriendUserInfoList[i].deleteDB("notificationFriendUserInfo$i")
//
//            GlobalVariables.notificationFriendUserInfoList.clear()
//        }.start()
//
//        Thread {
//            for (i in 0 until(GlobalVariables.notificationFriendList.size))
//                GlobalVariables.notificationFriendList[i].deleteDB("notificationFriend$i")
//
//            GlobalVariables.notificationFriendList.clear()
//        }.start()
    }

    private fun deleteSubscribeListFromSQL() {
        GlobalVariables.subscribeListIsReady = false

        Thread {
            for (i in 0 until(GlobalVariables.subscribeList.size))
                GlobalVariables.subscribeList[i].deleteDB("subscribeUserInfo$i")

            GlobalVariables.subscribeList.clear()
        }.start()
    }

    private fun deleteFriendListFromSQL() {
//        GlobalVariables.friendListIsReady = false
//
//        Thread {
//            for (i in 0 until(GlobalVariables.friendList.size))
//                GlobalVariables.friendList[i].deleteDB("friendUserInfo$i")
//
//            GlobalVariables.friendList.clear()
//        }.start()
    }

    fun updateDataToSQL() {
        if (GlobalVariables.userInfo.isEmpty()) return

        deleteNotificationListFromSQL()
        readNotificationListFromApi()
        writeNotificationListToSQL()

        if (GlobalVariables.userInfo.isShop()) {
            deleteSubscribeListFromSQL()
            readSubscribeListFromApi(GlobalVariables.userInfo.ID)
            writeSubscribeListToSQL()
        }
        else {
            deleteFriendListFromSQL()
            readFriendListFromApi(GlobalVariables.userInfo.ID)
            writeFriendListToSQL()
        }
    }

    fun logout() {
        val id = GlobalVariables.userInfo.ID
        deleteDataFromSQL()
        deleteSendedNotificationFromSQL()
        Thread {
            if (GlobalVariables.api.logout(id)) {
                GlobalVariables.activity.runOnUiThread(Runnable {
                    Toast.makeText(GlobalVariables.activity, "已成功登出", Toast.LENGTH_SHORT).show()
                })
            }
        }.start()
        GlobalVariables.activity.nav_host_fragment.findNavController().navigate(R.id.login_navigation)
    }

    fun addFriend(id:String) {
//        Thread {
//            GlobalVariables.friendListIsReady = false
//            val friendUserInfo = GlobalVariables.api.getUserInfo(id)
//            friendUserInfo.ID = id
//            val number = GlobalVariables.friendList.size
//            GlobalVariables.friendList.add(friendUserInfo)
//            friendUserInfo.writeDB("friendUserInfo$number")
//            GlobalVariables.friendListIsReady = true
//        }.start()
    }

    private fun readSendedNotificationFromSQL() {
        GlobalVariables.sendedNotificationListIsReady = false
        Thread {
            var count = 0
            GlobalVariables.sendedNotificationList.clear()
            while (true) {
                val message = GlobalVariables.dbHelper.readDB("sendedNotification$count")
                if (message != "") {
                    GlobalVariables.sendedNotificationList.add(message)
                    count++
                }
                else break
            }
            GlobalVariables.sendedNotificationListIsReady = true
        }.start()
    }

    private fun deleteSendedNotificationFromSQL() {
        GlobalVariables.sendedNotificationListIsReady = false

        Thread {
            for (i in 0 until(GlobalVariables.sendedNotificationList.size))
                GlobalVariables.dbHelper.deleteDB("sendedNotification$i")

            GlobalVariables.sendedNotificationList.clear()
        }.start()
    }

    fun addSendedNotification(message: String) {
        val number = GlobalVariables.sendedNotificationList.size
        GlobalVariables.dbHelper.writeDB("sendedNotification$number", message)
        GlobalVariables.sendedNotificationList.add(message)
    }

    fun updateNotificationList() {
//        Thread {
//            GlobalVariables.notificationFriendListIsReady = false
//            val friendNotificationList = GlobalVariables.api.getNotification(
//                GlobalVariables.userInfo.ID, "friend")
//
//            val newNotification = ArrayList<Notification>()
//
//            for(i in 0 until(friendNotificationList.size))
//                if (!searchNotificationFriend(friendNotificationList[i].index)) {
//                    friendNotificationList[i].type = "friend"
//                    newNotification.add(friendNotificationList[i])
//                }
//
//            GlobalVariables.activity.runOnUiThread {
//                GlobalVariables.notificationFriendList.addAll(newNotification)
//                for (i in 0 until(newNotification.size))
//                    Log.d(">>>>>>>>>>>>>>>>>>>>", newNotification[i].pusher_id)
//            }
//
//            GlobalVariables.notificationFriendListIsReady = true
//        }.start()

        GlobalVariables.notificationShopListIsReady = false

        Thread {
            val shopNotificationList = GlobalVariables.api.getNotification(GlobalVariables.userInfo.subscribed)

            val newNotification = ArrayList<Notification>()

            for(i in 0 until(shopNotificationList.size))
                if (!searchNotificationShop(shopNotificationList[i].date)) {
                    shopNotificationList[i].type = "shop"
                    newNotification.add(shopNotificationList[i])
                }

            GlobalVariables.activity.runOnUiThread {
                GlobalVariables.notificationShopList.addAll(newNotification)
            }

            GlobalVariables.notificationShopListIsReady = true
        }.start()

        Thread {
            while (!GlobalVariables.notificationShopListIsReady)
                continue

            if (!GlobalVariables.notificationShopList.isEmpty()) {
                if (GlobalVariables.notificationListShopAdapter!= null) {
                    GlobalVariables.activity.runOnUiThread(Runnable {
                        GlobalVariables.notificationListShopAdapter!!.notifyDataSetChanged()
                        GlobalVariables.notificationListShopLayoutManager.scrollToPosition(
                            GlobalVariables.notificationListShopLayoutManager.itemCount-1
                        )
                    })
                }
            }
        }.start()

    }

    private fun searchNotificationShop(date: String): Boolean {
        for(i in 0 until(GlobalVariables.notificationShopList.size))
            if (date == GlobalVariables.notificationShopList[i].date)
                return true

        return false

    }

    private fun searchNotificationFriend(index: Int): Boolean {
//        for(i in 0 until(GlobalVariables.notificationFriendList.size))
//            if (index == GlobalVariables.notificationFriendList[i].index)
//                return true

        return false

    }
}