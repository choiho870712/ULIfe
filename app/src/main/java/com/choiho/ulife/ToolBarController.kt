package com.choiho.ulife

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class ToolBarController {
//    private var isRunningAddFriend = false
    private var isRunningSubscribe = false
    private var isRunningEventRecord = false
    private var lockDeleteProposalItemButton = false

    fun setToolBarButtonOnClickListener() {
        GlobalVariables.activity.toolbar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.button_edit_person -> {
                    if (GlobalVariables.proposalUserInfo.isShop())
                        GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                            R.id.action_personShopInfoFragment_to_personEditFragment)
                    else
                        GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                            R.id.action_personInfoFragment_to_personEditFragment)
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
                        GlobalVariables.taskCount++

                        Thread {
                            if (GlobalVariables.api.subscribe(GlobalVariables.proposalUserInfo.ID, GlobalVariables.userInfo.ID)) {
                                GlobalVariables.functions.loginFromApi(GlobalVariables.userInfo.ID)
                                GlobalVariables.functions.makeToast("訂閱成功")
                                GlobalVariables.taskCount--
                                GlobalVariables.activity.runOnUiThread{
                                    openSubscribeButton(false)
                                    openUnSubscribeButton(true)
                                }
                            }
                            else {
                                GlobalVariables.functions.makeToast("訂閱失敗")
                                GlobalVariables.taskCount--
                            }
                            isRunningSubscribe = false
                        }.start()
                    }

                    true
                }
                R.id.button_unsubscribe -> {
                    Thread {
                        GlobalVariables.taskCount++

                        if (GlobalVariables.api.deleteSubscribe(
                                GlobalVariables.proposalUserInfo.ID,
                                GlobalVariables.userInfo.ID)
                        ) {
                            val id = GlobalVariables.userInfo.ID
                            GlobalVariables.functions.loginFromApi(id)
                            GlobalVariables.functions.makeToast("已取消訂閱")
                            GlobalVariables.activity.runOnUiThread{
                                openSubscribeButton(true)
                                openUnSubscribeButton(false)
                            }
                        }
                        else GlobalVariables.functions.makeToast("取消訂閱失敗")

                        GlobalVariables.taskCount--
                    }.start()

                    true
                }
                R.id.button_add_proposal -> {
                    GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                        R.id.homeEditFragment)

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

                            GlobalVariables.proposalUserInfo.copy(GlobalVariables.userInfo)
                            GlobalVariables.proposalUserScribeListData = GlobalVariables.subscribeList

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
                R.id.button_delete_proposal_item -> {
                    if (!lockDeleteProposalItemButton) {
                        Thread {
                            lockDeleteProposalItemButton = true
                            GlobalVariables.taskCount++
                            // call delete item api
                            val index = GlobalVariables.proposalItemIndex
                            val isSuccess = GlobalVariables.api.deleteFoodItem(
                                GlobalVariables.userInfo.permission[0],
                                GlobalVariables.userInfo.ID,
                                GlobalVariables.proposal!!.proposalItemList.size - index -1,
                                GlobalVariables.homeAreaChoose,
                                GlobalVariables.proposal!!.proposalItemList[index].imageUrlList[0]
                            )

                            if (isSuccess) {
                                GlobalVariables.functions.makeToast("刪除文章成功")
                                GlobalVariables.functions.resetProposalList()
                                GlobalVariables.activity.runOnUiThread {
                                    GlobalVariables.proposal!!.proposalItemList.removeAt(index)
                                    GlobalVariables.proposalItemAdapter.notifyDataSetChanged()
                                }

                            }
                            else GlobalVariables.functions.makeToast("刪除文章失敗")

                            GlobalVariables.taskCount--
                            lockDeleteProposalItemButton = false
                        }.start()
                    }
                    true
                }
                else -> false
            }
        }
    }

    fun openToolbarBackButton(enable: Boolean) {
        GlobalVariables.activity.runOnUiThread{
            if (!enable) GlobalVariables.activity.toolbar.setNavigationIcon(null)
        }
    }

    fun openToolbarMainButton(enable: Boolean) {
        GlobalVariables.activity.runOnUiThread {
            if (enable) GlobalVariables.activity.toolbar.button_toolbar_main.visibility = View.VISIBLE
            else GlobalVariables.activity.toolbar.button_toolbar_main.visibility = View.GONE
        }
    }

    fun openNavView(enable: Boolean) {
        GlobalVariables.activity.runOnUiThread {
            if (enable) GlobalVariables.activity.nav_view.visibility = View.VISIBLE
            else GlobalVariables.activity.nav_view.visibility = View.GONE
        }
    }

    fun openToolBar(enable: Boolean) {
        GlobalVariables.activity.runOnUiThread {
            if (enable) GlobalVariables.activity.toolbar.visibility = View.VISIBLE
            else GlobalVariables.activity.toolbar.visibility = View.GONE
        }
    }

    fun setMenuName() {
        GlobalVariables.activity.runOnUiThread {
            when (GlobalVariables.homeMenuChoose) {
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
    }

    fun openEditPersonButton(enable:Boolean) {
        val buttonEdit = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_edit_person)
        GlobalVariables.activity.runOnUiThread {
            buttonEdit.setEnabled(enable)
            buttonEdit.setVisible(enable)
        }
    }

    fun openLogoutButton(enable:Boolean) {
        val buttonLogout = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_logout)
        GlobalVariables.activity.runOnUiThread {
            buttonLogout.setEnabled(false)
            buttonLogout.setVisible(false)
        }
    }

    fun openAddFriendButton(enable:Boolean) {
        val buttonAddFriend = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_add_friend)
        GlobalVariables.activity.runOnUiThread {
            buttonAddFriend.setEnabled(enable)
            buttonAddFriend.setVisible(enable)
        }
    }

    fun openSubscribeButton(enable:Boolean) {
        val buttonSubscribe = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_subscribe)
        GlobalVariables.activity.runOnUiThread {
            buttonSubscribe.setEnabled(enable)
            buttonSubscribe.setVisible(enable)
        }
    }

    fun openUnSubscribeButton(enable:Boolean) {
        val buttonUnSubscribe = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_unsubscribe)
        GlobalVariables.activity.runOnUiThread {
            buttonUnSubscribe.setEnabled(enable)
            buttonUnSubscribe.setVisible(enable)
        }
    }

    fun openAddProposalButton(enable:Boolean) {
        val buttonAddProposal = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_add_proposal)
        GlobalVariables.activity.runOnUiThread {
            buttonAddProposal.setEnabled(enable)
            buttonAddProposal.setVisible(enable)
        }
    }

    fun openGoToShopInfoBottom(enable: Boolean) {
        val buttonGoToShopInfo = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_goToShopInfo)
        GlobalVariables.activity.runOnUiThread {
            buttonGoToShopInfo.setEnabled(enable)
            buttonGoToShopInfo.setVisible(enable)
        }
    }

    fun openEventRecordButton(enable: Boolean) {
        val buttonEventRecord = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_event_record)
        GlobalVariables.activity.runOnUiThread {
            buttonEventRecord.setEnabled(enable)
            buttonEventRecord.setVisible(enable)
        }
    }

    fun openDeleteItemButton(enable: Boolean) {
        val buttonDeleteItem = GlobalVariables.activity.toolbar.menu.findItem(R.id.button_delete_proposal_item)
        GlobalVariables.activity.runOnUiThread {
            buttonDeleteItem.setEnabled(enable)
            buttonDeleteItem.setVisible(enable)
        }
    }

    fun setTextColor(colorId:Int) {
        val color = GlobalVariables.activity.resources.getColor(colorId)
        GlobalVariables.activity.toolbar.setTitleTextColor(color)
        GlobalVariables.activity.toolbar.navigationIcon?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            }else{
                setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }
}