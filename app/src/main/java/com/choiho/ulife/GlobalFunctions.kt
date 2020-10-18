package com.choiho.ulife

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.navigationUI.notifications.Notification
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GlobalFunctions {

    private var lockLogin = false

    fun loginFromApi(id: String) {
        if (!lockLogin) {
            lockLogin = true

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

                lockLogin = false
            }.start()
        }
    }

    private fun readNotificationListFromApi(subscribeList: MutableList<String>) {
        GlobalVariables.notificationListIsReady = false
        Thread {
            GlobalVariables.notificationList.clear()
            GlobalVariables.notificationList.addAll(
                GlobalVariables.api.getNotificationList(subscribeList, GlobalVariables.homeAreaChoose))
            sortNotificationList()
            GlobalVariables.notificationListIsReady = true
        }.start()

        GlobalVariables.officialNotificationListIsReady = false
        Thread {
            GlobalVariables.officialNotificationList.clear()
            GlobalVariables.officialNotificationList.addAll(
                GlobalVariables.api.getNotificationList(arrayListOf("ULifeOffical"), "Offical"))
            GlobalVariables.officialNotificationListIsReady = true
        }.start()
    }

    private fun sortNotificationList() {
        val parser = SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS")
        var date1: Date?
        var date2: Date?
        var temp: Notification?
        val list = GlobalVariables.notificationList

        for (i in 0 until(list.size-1))
            for (j in 0 until(list.size-1-i)) {
                date1 = parser.parse(list[j].date)
                date2 = parser.parse(list[j+1].date)
                if (date1 > date2) {
                    temp = list[j]
                    list[j] = list[j+1]
                    list[j+1] = temp
                }
            }
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
                for (i in 0 until(GlobalVariables.userInfo.subscribed.size)) {
                    val sub = GlobalVariables.userInfo.subscribed[i]
                    Thread {
                        val myUserInfo = GlobalVariables.api.getUserInfo(sub)
                        while (!GlobalVariables.subscribeList.contains(myUserInfo)) {
                            GlobalVariables.subscribeList.add(myUserInfo)
                            Thread.sleep(500)
                        }
                    }.start()
                }

                while (GlobalVariables.userInfo.subscribed.size > GlobalVariables.subscribeList.size) {
                    Thread.sleep(500)
                }

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

        loginFromApi(GlobalVariables.userInfo.ID)

        return true
    }

    fun readUserInfoFromSQL():Boolean {
        GlobalVariables.userInfo.isReady = false
        GlobalVariables.userInfo.clear()
        GlobalVariables.studentPermissionID =
            GlobalVariables.dbHelper.readDB("studentPermissionID")
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

    fun deleteFromSubscribeList(subscribeID: String) {
        GlobalVariables.subscribeListIsReady = false
        Thread {
            GlobalVariables.userInfo.subscribed.remove(subscribeID)

            for (i in 0 until(GlobalVariables.subscribeList.size)) {
                if (GlobalVariables.subscribeList[i].ID == subscribeID) {
                    GlobalVariables.subscribeList.removeAt(i)
                    break
                }
            }

            GlobalVariables.subscribeListIsReady = true
        }.start()
    }

    fun updateToSubscribeList(newSubscribeID: String) {
        GlobalVariables.subscribeListIsReady = false
        Thread {
            GlobalVariables.userInfo.subscribed.add(newSubscribeID)
            GlobalVariables.subscribeList.add(
                GlobalVariables.api.getUserInfo(newSubscribeID))

            GlobalVariables.subscribeListIsReady = true
        }.start()
    }

    fun deleteFromNotificationList(subscribeID: String) {
        GlobalVariables.notificationListIsReady = false
        Thread {
            val size = GlobalVariables.notificationList.size
            for(i in 0 until(size)) {
                val index = size-i-1
                if (GlobalVariables.notificationList[index].pusher_id == subscribeID)
                    GlobalVariables.notificationList.removeAt(index)
            }
            GlobalVariables.notificationListIsReady = true
        }.start()
    }

    fun updateNotificationList() {
        GlobalVariables.notificationListIsReady = false
        Thread {
            val shopNotificationList = GlobalVariables.api.getNotificationList(
                GlobalVariables.userInfo.subscribed,
                GlobalVariables.homeAreaChoose
            )

            val newNotification = ArrayList<Notification>()
            for(i in 0 until(shopNotificationList.size))
                if (!searchNotificationShop(shopNotificationList[i].date))
                    newNotification.add(shopNotificationList[i])

            GlobalVariables.activity.runOnUiThread {
                GlobalVariables.notificationList.addAll(newNotification)
                GlobalVariables.notificationListIsReady = true
            }
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
        GlobalVariables.homeProposalListIsReady = false
        if (GlobalVariables.lockRefreshHomeProposalList) return
        GlobalVariables.homeProposalList.clear()
        if (isNetWorkConnecting()) {
            GlobalVariables.lockRefreshHomeProposalList = true
            GlobalVariables.homeProposalNumber = 1
            GlobalVariables.homeClassChoose = ""
            GlobalVariables.isHomeProposalEnd = false
            GlobalVariables.homeProposalList = GlobalVariables.api.getFoodAll(GlobalVariables.homeProposalNumber, GlobalVariables.homeAreaChoose)
            GlobalVariables.homeCurrentPosition = 0
            GlobalVariables.homeProposalNumber += 10
            GlobalVariables.homeProposalListIsReady = true
            GlobalVariables.lockRefreshHomeProposalList = false
        }
    }

    fun navigate(currentLocationId: Int, actionId: Int) {
        GlobalVariables.activity.runOnUiThread {
            if (GlobalVariables.activity.nav_host_fragment.findNavController().
                currentDestination?.id == currentLocationId)
                GlobalVariables.activity.nav_host_fragment.findNavController().navigate(actionId)
        }
    }

    fun makeToast(message: String) {
        GlobalVariables.activity.runOnUiThread {
            Toast.makeText(GlobalVariables.activity, message, Toast.LENGTH_LONG).show()
        }
    }

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

    fun refreshRandomChance() {
        val random_price_chance = GlobalVariables.dbHelper.readDB("random_price_chance")

        if (random_price_chance == "") {
            GlobalVariables.dbHelper.writeDB(
                "random_price_chance","3"
            )
            GlobalVariables.random_price_chance = 3
            GlobalVariables.dbHelper.writeDB(
                "random_price_chance_update_date",
                LocalDate.now().toString()
            )
            GlobalVariables.activity.nav_view.setBadgeText(
                R.id.randomPlateFragment, "NEW")
        }
        else {
            val lastUpdateDateString = GlobalVariables.dbHelper.readDB(
                "random_price_chance_update_date")

            val parser = SimpleDateFormat("yyyy-MM-dd")
            val lastUpdateDate = parser.parse(lastUpdateDateString)
            val localDate = parser.parse(LocalDate.now().toString())

            if (lastUpdateDate.day != localDate.day || lastUpdateDate.month != localDate.month) {
                GlobalVariables.random_price_chance = 3
                GlobalVariables.dbHelper.writeDB(
                    "random_price_chance","3"
                )
                GlobalVariables.dbHelper.writeDB(
                    "random_price_chance_update_date",
                    LocalDate.now().toString()
                )
                GlobalVariables.activity.nav_view.setBadgeText(
                    R.id.randomPlateFragment, "NEW")
            }
            else GlobalVariables.random_price_chance = random_price_chance.toInt()
        }
    }
}