package com.example.startupboard.ui.notifications

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.GlobalVariables
import com.example.startupboard.R
import com.example.startupboard.ui.api.Notification
import com.example.startupboard.ui.api.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_notifications.view.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*

class CardAdapter(val myDataset: ArrayList<Notification>, val parentView: View)
    : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    private var isClickingCard = false
    private lateinit var userinfo:UserInfo
    private var isDoneGettingUserInfo =  false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_notifications, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.message?.text = myDataset[position].content

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

    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val message = card.findViewById<TextView>(R.id.text_message_card_notifications)
        val author = card.findViewById<TextView>(R.id.text_author_card_notifications)
        val icon = card.findViewById<ImageView>(R.id.image_card_notifications)
        var authorString = ""
        var iconBitmap:Bitmap? = null
    }

    private fun searchNotificationUserInfo(id:String): UserInfo {
        for (i in 0 until(GlobalVariables.notificationUserInfoList.size))
            if (GlobalVariables.notificationUserInfoList[i].ID == id)
                return GlobalVariables.notificationUserInfoList[i]

        return UserInfo("", "", "", "", "", "", mutableListOf(), 0.0, 0.0)
    }

    private fun setCardOnClickListener(holder: CardHolder) {
        holder.cardView.image_card_notifications.setOnClickListener {
            if (!isClickingCard) {
                isClickingCard = true

                GlobalVariables.proposalUserInfo = userinfo
                GlobalVariables.proposalUserInfoDoneLoading = true

                if (userinfo.isShop()) {
                    GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                        R.id.action_navigation_notifications_to_personShopInfoFragment)
                }
                else {
                    GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                        R.id.action_navigation_notifications_to_personInfoFragment)
                }

                isClickingCard = false
            }
        }

        holder.cardView.notification_card_left.setOnClickListener {
            if (!isClickingCard) {
                isClickingCard = true
                Thread {
                    while (true)
                        if (isDoneGettingUserInfo)
                            break

                    GlobalVariables.activity.runOnUiThread {
                        parentView.image_notifications.setImageBitmap(holder.iconBitmap)
                        parentView.text_message_notifications.text = holder.message.text
                        parentView.text_author_notifications.text = holder.author.text
                        parentView.scroll_notification.visibility = View.VISIBLE
                        parentView.recycler_notifications.visibility = View.GONE
                    }
                    isClickingCard = false
                }.start()
            }
        }
    }
}