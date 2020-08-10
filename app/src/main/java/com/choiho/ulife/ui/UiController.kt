package com.choiho.ulife.ui

import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class UiController {
//    private var isRunningAddFriend = false
    private var isRunningSubscribe = false
    private var isRunningEventRecord = false

    fun setToolBarButtonOnClickListener() {
        GlobalVariables.activity.toolbar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.button_edit_person -> {
                    if (GlobalVariables.proposalUserInfo!!.isShop())
                        GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                            R.id.action_personShopInfoFragment_to_personEditFragment)
                    else
                        GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                            R.id.action_personInfoFragment_to_personEditFragment)
                    true
                }
                R.id.button_logout -> {
                    GlobalVariables.functions.logout()
                    true
                }
                R.id.button_add_friend -> {
                    GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                        R.id.action_personInfoFragment_to_personAddFriendFragment)

                    true
                }
                R.id.button_subscribe -> {
                    if (!isRunningSubscribe) {
                        isRunningSubscribe = true
                        if (GlobalVariables.api.subscribe(GlobalVariables.proposalUserInfo.ID, GlobalVariables.userInfo.ID)) {
                            GlobalVariables.functions.updateNotificationList()
                            GlobalVariables.activity.runOnUiThread(Runnable {
                                Toast.makeText(GlobalVariables.activity, "訂閱成功", Toast.LENGTH_SHORT).show()
                                openSubscribeButton(false)
                            })

                            val id = GlobalVariables.userInfo.ID
                            GlobalVariables.functions.deleteDataFromSQL()
                            GlobalVariables.functions.readDataFromApi(id)
                            GlobalVariables.functions.writeDataToSQL()
                        }
                        else {
                            GlobalVariables.activity.runOnUiThread(Runnable {
                                Toast.makeText(GlobalVariables.activity, "訂閱失敗", Toast.LENGTH_SHORT).show()
                            })
                        }
                        isRunningSubscribe = false
                    }

                    true
                }
                R.id.button_add_proposal -> {
                    GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                        R.id.action_navigation_home_to_homeEditFragment)

                    true
                }
                R.id.button_goToShopInfo -> {
                    GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                        R.id.action_homePage1Fragment_to_personShopInfoFragment)

                    true
                }
                R.id.button_event_record -> {
                    if (!isRunningEventRecord) {
                        isRunningEventRecord = true
                        Thread {
                            GlobalVariables.proposal = GlobalVariables.api.getFoodItem(
                                GlobalVariables.userInfo.ID,
                                GlobalVariables.userInfo.permission[0],
                                GlobalVariables.homeAreaChoose
                            )

                            GlobalVariables.proposalUserInfo = GlobalVariables.userInfo
                            GlobalVariables.proposalUserScribeList = GlobalVariables.subscribeList

                            while (!GlobalVariables.proposal!!.proposalItemList[0].isDoneImageLoadingOnlyOne())
                                ;

                            for (i in 0 until(GlobalVariables.proposal!!.proposalItemList.size)) {
                                Thread {
                                    GlobalVariables.proposal!!.proposalItemList[i].convertImageUrlToImageAll()
                                }.start()
                            }

                            GlobalVariables.activity.runOnUiThread {
                                GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                                    R.id.action_navigation_home_to_homePage1Fragment)
                            }

                            isRunningEventRecord = false
                        }.start()
                    }
                    true
                }
                else -> false
            }
        }
    }

    fun openToolbarBackButton(enable: Boolean) {
        if (!enable) GlobalVariables.activity.toolbar.setNavigationIcon(null)
    }

    fun openToolbarMainButton(enable: Boolean) {
        if (enable) GlobalVariables.activity.toolbar.button_toolbar_main.visibility = View.VISIBLE
        else GlobalVariables.activity.toolbar.button_toolbar_main.visibility = View.GONE
    }

    fun openNavView(enable: Boolean) {
        if (enable) GlobalVariables.activity.nav_view.visibility = View.VISIBLE
        else GlobalVariables.activity.nav_view.visibility = View.GONE
    }

    fun openToolBar(enable: Boolean) {
        if (enable) GlobalVariables.activity.toolbar.visibility = View.VISIBLE
        else GlobalVariables.activity.toolbar.visibility = View.GONE
    }

    fun setMenuName() {
        when(GlobalVariables.homeMenuChoose) {
            "food" -> {
                GlobalVariables.activity.toolbar.text_toolbar_main.text = "美食"
                GlobalVariables.activity.image_toolbar_main.setImageResource(R.drawable.chicken_m)
            }
            "game" -> {
                GlobalVariables.activity.toolbar.text_toolbar_main.text = "玩樂"
                GlobalVariables.activity.image_toolbar_main.setImageResource(R.drawable.disco_ball_m)
            }
            "event" -> {
                GlobalVariables.activity.toolbar.text_toolbar_main.text = "校園活動"
                GlobalVariables.activity.image_toolbar_main.setImageResource(R.drawable.price_m)

            }
            else -> {
                GlobalVariables.activity.toolbar.text_toolbar_main.text = "錯誤"
                GlobalVariables.activity.image_toolbar_main.setImageResource(R.drawable.chicken_m)
            }
        }
    }

    fun setNavViewButton() {
        val navView = GlobalVariables.activity.nav_view
//        when(navView.selectedItemId)
//        {
//            R.id.navigation_home ->
//            {
//                navView.menu.findItem(R.id.navigation_home).setEnabled(false)
//                navView.menu.findItem(R.id.navigation_chat).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_notifications).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_person).setEnabled(true)
//            }
//            R.id.navigation_chat ->
//            {
//                navView.menu.findItem(R.id.navigation_home).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_chat).setEnabled(false)
//                navView.menu.findItem(R.id.navigation_notifications).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_person).setEnabled(true)
//            }
//            R.id.navigation_notifications ->
//            {
//                navView.menu.findItem(R.id.navigation_home).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_chat).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_notifications).setEnabled(false)
//                navView.menu.findItem(R.id.navigation_person).setEnabled(true)
//            }
//            R.id.navigation_person ->
//            {
//                navView.menu.findItem(R.id.navigation_home).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_chat).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_notifications).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_person).setEnabled(false)
//            }
//        }
    }

    fun openEditPersonButton(enable:Boolean) {
        val buttonEdit = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_edit_person)
        buttonEdit.setEnabled(enable)
        buttonEdit.setVisible(enable)
    }

    fun openLogoutButton(enable:Boolean) {
        val buttonLogout = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_logout)
        buttonLogout.setEnabled(false)
        buttonLogout.setVisible(false)
    }

    fun openAddFriendButton(enable:Boolean) {
        val buttonAddFriend = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_add_friend)
        buttonAddFriend.setEnabled(enable)
        buttonAddFriend.setVisible(enable)
    }

    fun openSubscribeButton(enable:Boolean) {
        val buttonSubscribe = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_subscribe)
        buttonSubscribe.setEnabled(enable)
        buttonSubscribe.setVisible(enable)
    }

    fun openAddProposalButton(enable:Boolean) {
        val buttonAddProposal = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_add_proposal)
        buttonAddProposal.setEnabled(enable)
        buttonAddProposal.setVisible(enable)
    }

    fun openGoToShopInfoBottom(enable: Boolean) {
        val buttonGoToShopInfo = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_goToShopInfo)
        buttonGoToShopInfo.setEnabled(enable)
        buttonGoToShopInfo.setVisible(enable)
    }

    fun openEventRecordButton(enable: Boolean) {
        val buttonEventRecord = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_event_record)
        buttonEventRecord.setEnabled(enable)
        buttonEventRecord.setVisible(enable)
    }
}