package com.example.startupboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.startupboard.ui.UiController
import com.example.startupboard.ui.api.Api
import com.example.startupboard.ui.api.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {

    private var isRunningLogin = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        setUi()
        setButtons(root)
        resetGlobalVariables()
        return root
    }

    private fun resetGlobalVariables() {
        GlobalVariables.userInfo = UserInfo("","","","","", "", mutableListOf(), 0.0, 0.0)
        GlobalVariables.uiController = UiController()
        GlobalVariables.api = Api()
        GlobalVariables.homeMenuChoose = "food"
        GlobalVariables.homeAreaChoose = "Zhongli"

        if (GlobalVariables.api.isNetWorkConnecting(activity!!)) {
            Thread {
                GlobalVariables.homeProposalList = GlobalVariables.api.getFoodAll(1, GlobalVariables.homeAreaChoose)
                GlobalVariables.homePageProposalCount = 10
            }.start()
        }

        GlobalVariables.notificationList = ArrayList()
        GlobalVariables.notificationUserInfoList = ArrayList()
        GlobalVariables.friendList = ArrayList()
        GlobalVariables.friendStringList = ArrayList()
        GlobalVariables.subscribeList = ArrayList()
    }

    private fun setUi() {
        GlobalVariables.uiController.openNavView(false)
        GlobalVariables.uiController.openToolBar(false)
    }

    private fun setButtons(root:View) {
        linkLoginToCreateAccountButton(root)
        linkLoginSubmitButton(root)
    }

    private fun linkLoginToCreateAccountButton(root:View) {
        root.button_create_new_account.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_createAccountFragment)
        )
    }

    private fun linkLoginSubmitButton(root:View) {
        root.button_login_submit.setOnClickListener {
            if (!isRunningLogin) {
                Thread {
                    isRunningLogin = true

                    val id = root.edit_username_login_page.text.toString()
                    val password = root.edit_password_login_page.text.toString()

                    if (GlobalVariables.api.login(id, password, "android-not-sup")) {
                        GlobalVariables.userInfo = GlobalVariables.api.getUserInfo(id)
                        GlobalVariables.userInfo.ID = id

                        activity!!.runOnUiThread(Runnable {
                            activity!!.nav_host_fragment.findNavController().navigate(R.id.action_loginFragment_to_mobile_navigation)
                        })

                        val idList = ArrayList<String>()
                        GlobalVariables.notificationList = GlobalVariables.api.getNotification(GlobalVariables.userInfo.ID, "shop")
                        for (i in 0 until(GlobalVariables.notificationList.size)) {
                            if (!idList.contains(GlobalVariables.notificationList[i].pusher_id)) {
                                idList.add(GlobalVariables.notificationList[i].pusher_id)
                                val userInfo = GlobalVariables.api.getUserInfo(GlobalVariables.notificationList[i].pusher_id)
                                userInfo.ID = GlobalVariables.notificationList[i].pusher_id
                                GlobalVariables.notificationUserInfoList.add(userInfo)
                            }
                        }

                        if (GlobalVariables.userInfo.latitude == 0.0) {
                            GlobalVariables.friendStringList = GlobalVariables.api.getFriendList(GlobalVariables.userInfo.ID)
                            GlobalVariables.friendList = ArrayList()
                            for ( i in 0 until(GlobalVariables.friendStringList.size) ) {
                                val friendInfo = GlobalVariables.api.getUserInfo(GlobalVariables.friendStringList[i])
                                friendInfo.ID = GlobalVariables.friendStringList[i]
                                GlobalVariables.friendList.add(friendInfo)
                            }
                        }
                        else {
                            GlobalVariables.subscribeList = GlobalVariables.api.getSubscribeList(GlobalVariables.userInfo.ID)

                        }
                    }
                    else {
                        activity!!.runOnUiThread(Runnable {
                            Toast.makeText(activity, "登入失敗", Toast.LENGTH_SHORT).show()
                        })
                    }

                    isRunningLogin = false
                }.start()
            }
        }
    }
}
