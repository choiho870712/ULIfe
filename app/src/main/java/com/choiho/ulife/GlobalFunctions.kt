package com.choiho.ulife

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.navigationUI.notifications.Notification
import kotlinx.android.synthetic.main.activity_main.*

class GlobalFunctions {

    fun loginFromApi(id: String) {
        GlobalVariables.userInfo.isReady = false
        GlobalVariables.notificationListIsReady = false
        GlobalVariables.subscribeListIsReady = false

        GlobalVariables.userInfo.readFromApi(id)
        Thread {
            while (!GlobalVariables.userInfo.isReady)
                Thread.sleep(500)

            readSubscribeListFromApi(id)
            readNotificationListFromApi(GlobalVariables.userInfo.subscribed)

            writeUserInfoToSQL()
        }.start()
    }

    private fun readNotificationListFromApi(subscribeList: MutableList<String>) {
        GlobalVariables.notificationListIsReady = false
        Thread {
            GlobalVariables.notificationList.clear()
            GlobalVariables.notificationList.addAll(
                GlobalVariables.api.getNotificationList(subscribeList))
            GlobalVariables.notificationListIsReady = true
        }.start()
    }

    private fun readSubscribeListFromApi(id:String) {
        GlobalVariables.subscribeListIsReady = false
        Thread {
            if (GlobalVariables.userInfo.isShop()) {
                GlobalVariables.subscribeList.clear()
                GlobalVariables.subscribeList.addAll(GlobalVariables.api.getSubscribeList(id))
                GlobalVariables.subscribeListIsReady = true
            }
            else {
                GlobalVariables.subscribeList.clear()
                var countDown = GlobalVariables.userInfo.subscribed.size
                for (i in 0 until(GlobalVariables.userInfo.subscribed.size)) {
                    Thread {
                        GlobalVariables.subscribeList.add(
                            GlobalVariables.api.getUserInfo(GlobalVariables.userInfo.subscribed[i]))
                        countDown--
                    }.start()
                }

                while (countDown > 0)
                    Thread.sleep(500)

                GlobalVariables.subscribeListIsReady = true
            }
        }.start()
    }

    fun writeUserInfoToSQL() {
        Thread {
            while (!GlobalVariables.userInfo.isReady)
                Thread.sleep(500)

            GlobalVariables.userInfo.writeDB("userInfo")
        }.start()
    }

    fun loginFromSQL():Boolean {
        if (!readUserInfoFromSQL())return false

        // TODO cost down
        loginFromApi(GlobalVariables.userInfo.ID)

        return true
    }

    private fun readUserInfoFromSQL():Boolean {
        GlobalVariables.userInfo.isReady = false
        GlobalVariables.userInfo.clear()
        GlobalVariables.studentPermissionID =
            GlobalVariables.dbHelper.readDB("studentPermissionID")
        GlobalVariables.isDoneStudentForm =
            GlobalVariables.dbHelper.readDB("isDoneStudentForm") == "true"
        readMyOldNotificationFromSQL()
        return GlobalVariables.userInfo.readDB("userInfo")
    }

    private fun readMyOldNotificationFromSQL() {
        GlobalVariables.myOldNotificationListIsReady = false
        Thread {
            var count = 0
            GlobalVariables.myOldNotificationList.clear()
            while (true) {
                val message = GlobalVariables.dbHelper.readDB("myOldNotification$count")
                if (message != "") {
                    GlobalVariables.myOldNotificationList.add(message)
                    count++
                }
                else break
            }
            GlobalVariables.myOldNotificationListIsReady = true
        }.start()
    }

    fun addMyOldNotification(message: String) {
        val number = GlobalVariables.myOldNotificationList.size
        GlobalVariables.dbHelper.writeDB("myOldNotification$number", message)
        GlobalVariables.myOldNotificationList.add(message)
    }

    fun updateNotificationList() {
        GlobalVariables.notificationListIsReady = false
        Thread {
            val shopNotificationList = GlobalVariables.api.getNotificationList(GlobalVariables.userInfo.subscribed)

            val newNotification = ArrayList<Notification>()
            for(i in 0 until(shopNotificationList.size))
                if (!searchNotificationShop(shopNotificationList[i].date))
                    newNotification.add(shopNotificationList[i])

            GlobalVariables.activity.runOnUiThread {
                GlobalVariables.notificationList.addAll(newNotification)
            }

            GlobalVariables.notificationListIsReady = true
        }.start()

        Thread {
            while (!GlobalVariables.notificationListIsReady)
                Thread.sleep(500)

            if (GlobalVariables.notificationList.isNotEmpty()) {
                if (GlobalVariables.notificationListAdapter!= null) {
                    GlobalVariables.activity.runOnUiThread {
                        GlobalVariables.notificationListAdapter!!.notifyDataSetChanged()
                        GlobalVariables.notificationListLayoutManager.scrollToPosition(
                            GlobalVariables.notificationListLayoutManager.itemCount-1
                        )
                    }
                }
            }
        }.start()

    }

    private fun searchNotificationShop(date: String): Boolean {
        for(i in 0 until(GlobalVariables.notificationList.size))
            if (date == GlobalVariables.notificationList[i].date)
                return true

        return false
    }

    fun resetProposalList() {
        if (GlobalVariables.lockRefreshHomeProposalList) return
        Thread {
            GlobalVariables.homeProposalList.clear()
            if (isNetWorkConnecting()) {
                GlobalVariables.lockRefreshHomeProposalList = true
                GlobalVariables.homeProposalNumber = 1
                GlobalVariables.homeProposalList = GlobalVariables.api.getFoodAll(GlobalVariables.homeProposalNumber, GlobalVariables.homeAreaChoose)
                GlobalVariables.homeCurrentPosition = 0
                GlobalVariables.homeProposalNumber += 10
                GlobalVariables.lockRefreshHomeProposalList = false
            }
        }.start()
    }

    fun navigate(actionId: Int) {
        GlobalVariables.activity.runOnUiThread {
            GlobalVariables.activity.nav_host_fragment.findNavController().navigate(actionId)
        }
    }

    fun popUpTo(desId: Int) {
        GlobalVariables.activity.runOnUiThread {
            GlobalVariables.activity.nav_host_fragment.findNavController().popBackStack(desId, false)
        }
    }

    fun makeToast(message: String) {
        GlobalVariables.activity.runOnUiThread {
            Toast.makeText(GlobalVariables.activity, message, Toast.LENGTH_SHORT).show()
        }
    }

//    fun isNetWorkConnecting():Boolean {
//        val connectivityManager = GlobalVariables.activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
//        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
//    }

    fun isNetWorkConnecting(): Boolean {
        var result = false
        val connectivityManager =
            GlobalVariables.activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

}