package com.example.startupboard

import android.app.Application
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.ui.UiController
import com.example.startupboard.ui.api.Api
import com.example.startupboard.ui.api.Notification
import com.example.startupboard.ui.api.Proposal
import com.example.startupboard.ui.api.UserInfo
import com.example.startupboard.ui.home.CardAdapter

class GlobalVariables : Application() {
    companion object {
        lateinit var userInfo: UserInfo
        lateinit var activity: FragmentActivity
        lateinit var uiController: UiController
        lateinit var api: Api
        lateinit var homeMenuChoose: String
        lateinit var homeAreaChoose: String
        lateinit var homeLayoutManager: GridLayoutManager

        var homePageView: View? = null
        var homePageProposalCount = 0
        var isRefreshingHomePage = false

        var proposal: Proposal? = null
        var proposalUserInfo: UserInfo? = null
        var proposalUserInfoDoneLoading: Boolean = false
        lateinit var proposalUserScribeList: ArrayList<UserInfo>

        lateinit var homeAdapter: RecyclerView.Adapter<CardAdapter.CardHolder>
        lateinit var homeProposalList:ArrayList<Proposal>

        lateinit var friendStringList: ArrayList<String>
        lateinit var friendList: ArrayList<UserInfo>

        lateinit var subscribeList: ArrayList<UserInfo>

        lateinit var notificationList: ArrayList<Notification>
        lateinit var notificationUserInfoList: ArrayList<UserInfo>
    }
}