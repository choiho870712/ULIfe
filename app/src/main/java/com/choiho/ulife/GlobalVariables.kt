package com.choiho.ulife

import android.app.Application
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.ui.UiController
import com.choiho.ulife.ui.api.Api
import com.choiho.ulife.ui.api.Notification
import com.choiho.ulife.ui.api.Proposal
import com.choiho.ulife.ui.api.UserInfo
import com.choiho.ulife.ui.home.CardAdapter

class GlobalVariables : Application() {
    companion object {
        lateinit var activity: FragmentActivity

        val uiController = UiController()
        val api = Api()
        val functions = GlobalFunctions()

        var homeMenuChoose = "food"
        var homeAreaChoose = "Zhongli"
        lateinit var homeLayoutManager: GridLayoutManager

        var homePageView: View? = null
        var homePageProposalCount = 0
        var isRefreshingHomePage = false

        var proposal: Proposal? = null
        var proposalUserInfo: UserInfo = UserInfo("","","","","", "", mutableListOf(), 0.0, 0.0, mutableListOf(), mutableListOf())
        var proposalUserInfoDoneLoading: Boolean = false
        lateinit var proposalUserScribeList: ArrayList<UserInfo>

        lateinit var homeAdapter: RecyclerView.Adapter<CardAdapter.CardHolder>
        lateinit var homeProposalList:ArrayList<Proposal>

        var userInfo = UserInfo("","","","","", "", mutableListOf(), 0.0, 0.0, mutableListOf(), mutableListOf())
        var UserInfoIsReady = false

        var friendStringList:ArrayList<String> = ArrayList()
        var friendList: ArrayList<UserInfo> = ArrayList()
        var friendListIsReady = false
        var needUnsubscribeButton = false

        var subscribeList: ArrayList<UserInfo> = ArrayList()
        var subscribeListIsReady = false

        lateinit var notificationListShopLayoutManager: LinearLayoutManager
        var notificationListShopAdapter:
                RecyclerView.Adapter<com.choiho.ulife.ui.notifications.CardShopAdapter.CardHolder>? = null
        var notificationShopList: ArrayList<Notification> = ArrayList()
        var notificationShopUserInfoList: ArrayList<UserInfo> = ArrayList()
        var notificationShopListIsReady = false

//        lateinit var notificationListFriendLayoutManager: LinearLayoutManager
//        lateinit var notificationListFriendAdapter: RecyclerView.Adapter<com.example.startupboard.ui.notifications.CardFriendAdapter.CardHolder>
//        var notificationFriendList: ArrayList<Notification> = ArrayList()
//        var notificationFriendUserInfoList: ArrayList<UserInfo> = ArrayList()
//        var notificationFriendListIsReady = false


        var sendedNotificationList: ArrayList<String> = ArrayList()
        var sendedNotificationListIsReady = false

        lateinit var dbHelper: DBHelper

        lateinit var FCM_token: String
    }
}