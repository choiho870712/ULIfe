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
import kotlinx.android.synthetic.main.fragment_notifications_shop.view.*

class CardShopAdapter(val myDataset: ArrayList<Notification>, val parentView: View)
    : RecyclerView.Adapter<CardShopAdapter.CardHolder>() {

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
        holder.viewCount.text = "觀看次數 " + myDataset[position].index.toString()
        holder.viewCountNumber = myDataset[position].index
        holder.pusher_id = myDataset[position].pusher_id

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
        if (!userinfo.isEmpty()) setCardOnClickListener(holder, position)

        holder.cardView.notification_card_add_friend.visibility = View.GONE
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val message = card.findViewById<TextView>(R.id.text_message_card_notifications)
        val author = card.findViewById<TextView>(R.id.text_author_card_notifications)
        val date = card.findViewById<TextView>(R.id.text_date_card_notifications)
        val icon = card.findViewById<ImageView>(R.id.image_card_notifications)
        val viewCount = card.findViewById<TextView>(R.id.text_viewCount_card_notifications)
        var authorString = ""
        var iconBitmap:Bitmap? = null
        var viewCountNumber = 0
        var pusher_id = ""
    }

    private fun searchNotificationUserInfo(id:String): UserInfo {
        for (i in 0 until(GlobalVariables.subscribeList.size))
            if (GlobalVariables.subscribeList[i].ID == id)
                return GlobalVariables.subscribeList[i]

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

    private fun setCardOnClickListener(holder: CardHolder, position: Int) {
        holder.cardView.image_card_notifications.setOnClickListener {
            if (!isClickingCard) {
                isClickingCard = true

                GlobalVariables.proposalUserInfo.copy(userinfo)

                GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                    R.id.action_notificationsShopFragment_to_personShopInfoFragment)

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
                        parentView.image_notifications_shop.setImageBitmap(holder.iconBitmap)
                        parentView.text_message_notifications_shop.text = holder.message.text
                        parentView.text_author_notifications_shop.text = holder.author.text
                        parentView.text_date_notifications_shop.text = holder.date.text
                        parentView.text_viewCount_notifications_shop.text = holder.viewCount.text
                        parentView.scroll_notification_shop.visibility = View.VISIBLE
                        parentView.recycler_notification_shop.visibility = View.GONE
                    }

                    val id = holder.pusher_id
                    val date = holder.date.text.toString()
                    val content = holder.message.text.toString()
                    val view = holder.viewCountNumber

                    isClickingCard = false

                    var count = 0
                    for (i in 0 until(position)) {
                        if (GlobalVariables.notificationList[i].pusher_id == id)
                            count++
                    }

                    GlobalVariables.api.clickInNotification(id, date, content, view, count)
                }.start()
            }
        }
    }
}