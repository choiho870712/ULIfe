package com.choiho.ulife.navigationUI.notifications

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import com.choiho.ulife.navigationUI.userInfo.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_notifications.view.*
import kotlinx.android.synthetic.main.fragment_notification_friend.view.*

class CardFriendAdapter(val myDataset: ArrayList<Notification>, val parentView: View)
    : RecyclerView.Adapter<CardFriendAdapter.CardHolder>() {

    private var isClickingCard = false
    private lateinit var userinfo: UserInfo
    private var isDoneGettingUserInfo =  false
    private var isRunningAddFriend = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_notifications, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.message?.text = myDataset[position].content
        holder.date.text = myDataset[position].date

        holder.cardView.notification_card_add_friend.visibility = View.VISIBLE

        userinfo = searchNotificationUserInfo(myDataset[position].pusher_id)
        userinfo.ID = myDataset[position].pusher_id
        if (!userinfo.isEmpty()) {
            holder.iconBitmap = userinfo.getIconBitmap()
            holder.authorString = userinfo.name

            holder.author?.text = holder.authorString
            if (holder.iconBitmap != null) holder.icon.setImageBitmap(holder.iconBitmap)
        }
        isDoneGettingUserInfo = true
        if (!userinfo.isEmpty()) setCardOnClickListener(holder)

        holder.cardView.button_accept_friend_notification.setOnClickListener {
//            if (!userinfo.isFriend()&& !isRunningAddFriend)
//            {
//                isRunningAddFriend = true
//                Thread{
//                    if (GlobalVariables.api.buildRelationship(GlobalVariables.userInfo.ID, userinfo.ID)) {
//                        GlobalVariables.activity.runOnUiThread(Runnable {
//                            Toast.makeText(GlobalVariables.activity, "加入好友成功", Toast.LENGTH_SHORT).show()
//                            GlobalVariables.api.deleteNotification(
//                                GlobalVariables.userInfo.ID, myDataset[position].index)
//
//                            holder.message?.text = "已成為好友"
//                            holder.cardView.notification_card_add_friend.visibility = View.GONE
//                            GlobalVariables.notificationFriendList.removeAt(position)
//                            GlobalVariables.notificationListFriendAdapter.notifyDataSetChanged()
//                        })
//                        GlobalVariables.functions.addFriend(userinfo.ID)
//                    }
//                    else {
//                        GlobalVariables.activity.runOnUiThread(Runnable {
//                            Toast.makeText(GlobalVariables.activity, "加入好友失敗", Toast.LENGTH_SHORT).show()
//                        })
//                    }
//                    isRunningAddFriend = false
//                }.start()
//            }
        }

        holder.cardView.button_delete_friend_notification.setOnClickListener {
//            GlobalVariables.api.deleteNotification(
//                GlobalVariables.userInfo.ID, myDataset[position].index)
//            holder.message?.text = "已拒絕"
//            holder.cardView.notification_card_add_friend.visibility = View.GONE
//            GlobalVariables.notificationFriendList.removeAt(position)
//            GlobalVariables.notificationListFriendAdapter.notifyDataSetChanged()
        }
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val message = card.findViewById<TextView>(R.id.text_message_card_notifications)
        val author = card.findViewById<TextView>(R.id.text_author_card_notifications)
        val date = card.findViewById<TextView>(R.id.text_date_card_notifications)
        val icon = card.findViewById<ImageView>(R.id.image_card_notifications)
        var authorString = ""
        var iconBitmap:Bitmap? = null
    }

    private fun searchNotificationUserInfo(id:String): UserInfo {
//        for (i in 0 until(GlobalVariables.notificationFriendUserInfoList.size))
//            if (GlobalVariables.notificationFriendUserInfoList[i].ID == id)
//                return GlobalVariables.notificationFriendUserInfoList[i]

        return UserInfo(
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
    }

    private fun setCardOnClickListener(holder: CardHolder) {
        holder.cardView.image_card_notifications.setOnClickListener {
            if (!isClickingCard) {
                isClickingCard = true

                GlobalVariables.proposalUserInfo.copy(userinfo)
                GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                    R.id.action_notificationFriendFragment_to_personInfoFragment)

                isClickingCard = false
            }
        }

        holder.cardView.notification_card_left.setOnClickListener {
            if (!isClickingCard) {
                isClickingCard = true
                Thread {
                    while (!isDoneGettingUserInfo)
                        Thread.sleep(500)

                    GlobalVariables.activity.runOnUiThread {
                        parentView.image_notifications_friend.setImageBitmap(holder.iconBitmap)
                        parentView.text_message_notifications_friend.text = holder.message.text
                        parentView.text_author_notifications_friend.text = holder.author.text
                        parentView.scroll_notification_friend.visibility = View.VISIBLE
                        parentView.recycler_notification_friend.visibility = View.GONE
                    }
                    isClickingCard = false
                }.start()
            }
        }
    }
}