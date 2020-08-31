package com.choiho.ulife

import android.app.Application
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.firebase.MyFireBaseMessagingService
import com.choiho.ulife.navigationUI.notifications.Notification
import com.choiho.ulife.navigationUI.home.Proposal
import com.choiho.ulife.navigationUI.userInfo.UserInfo
import com.choiho.ulife.navigationUI.home.CardAdapter
import com.choiho.ulife.navigationUI.home.CardProposalItemAdapter
import org.threeten.bp.LocalDate

class GlobalVariables : Application() {
    companion object {
        lateinit var activity: FragmentActivity
        lateinit var dbHelper: DBHelper
        lateinit var FCM_token: String

        val toolBarController = ToolBarController()
        val imageHelper = ImageHelper()
        val api = Api()
        val functions = GlobalFunctions()
        val fireBase = MyFireBaseMessagingService()

        var userInfo = UserInfo("", "", "", "", "", "", mutableListOf(), 0.0, 0.0, mutableListOf(), mutableListOf())
        var studentPermissionID = ""
        var isDoneStudentForm = false

        var homePageView: View? = null
        var homeMenuChoose = "food"
        var homeAreaChoose = "Zhongli"
        lateinit var homeLayoutManager: GridLayoutManager
        lateinit var homeAdapter: RecyclerView.Adapter<CardAdapter.CardHolder>
        var homeProposalList:ArrayList<Proposal> = ArrayList()
        var lockRefreshHomeProposalList = false
        var lockRefreshHomePage = false
        var homeCurrentPosition = 0
        var homeProposalNumber = 1

        var proposal: Proposal? = null
        var proposalItemIndex = 0
        lateinit var proposalItemAdapter: RecyclerView.Adapter<CardProposalItemAdapter.CardHolder>
        var proposalUserInfo: UserInfo =
            UserInfo(
                "",
                "",
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
        lateinit var proposalUserScribeListData: ArrayList<UserInfo>

        var otherUserInfoList: ArrayList<UserInfo> = ArrayList()

        var needUnsubscribeButton = false
        var subscribeList: ArrayList<UserInfo> = ArrayList()
        var subscribeListIsReady = false

        lateinit var notificationListLayoutManager: LinearLayoutManager
        var notificationListAdapter:
                RecyclerView.Adapter<com.choiho.ulife.navigationUI.notifications.CardShopAdapter.CardHolder>? = null
        var notificationList: ArrayList<Notification> = ArrayList()
        var notificationListIsReady = false

        var myOldNotificationList: ArrayList<String> = ArrayList()
        var myOldNotificationListIsReady = false

        var taskCount = 0

        var random_price_chance = 3

        var report_shop_id = ""
    }
}