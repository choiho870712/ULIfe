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
import kotlinx.android.synthetic.main.fragment_notifications_official.view.*
import kotlinx.android.synthetic.main.fragment_notifications_shop.view.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.text.SimpleDateFormat

class CardOfficialAdapter(val myDataset: ArrayList<Notification>, val parentView: View)
    : RecyclerView.Adapter<CardOfficialAdapter.CardHolder>() {

    private var isClickingCard = false
    private lateinit var userinfo: UserInfo
    private var isDoneGettingUserInfo =  false
    private var isRunningAddFriend = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_notifications_official, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.message?.text = myDataset[position].content
        holder.viewCount.text = "觀看次數 " + myDataset[position].view.toString()
        holder.viewCountNumber = myDataset[position].view
        holder.pusher_id = myDataset[position].pusher_id
        holder.create_time = myDataset[position].create_time
        holder.authorString = myDataset[position].name
        holder.author?.text = holder.authorString

        val triggerTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(myDataset[position].create_time.toLong()),
            ZoneId.systemDefault()
        )

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val formatter = SimpleDateFormat("MM/dd HH:mm")
        holder.date.text = formatter.format(parser.parse(triggerTime.toString()))

//        userinfo = searchNotificationUserInfo(myDataset[position].pusher_id)
//        userinfo.ID = myDataset[position].pusher_id
//        if (!userinfo.isEmpty()) {
//            holder.authorString = myDataset[position].name
//
//            holder.author?.text = holder.authorString
//        }
//        isDoneGettingUserInfo = true
//        if (!userinfo.isEmpty()) setCardOnClickListener(holder, position)

        setCardOnClickListener(holder, position)
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val message = card.findViewById<TextView>(R.id.text_message_card_notifications)
        val author = card.findViewById<TextView>(R.id.text_author_card_notifications)
        val date = card.findViewById<TextView>(R.id.text_date_card_notifications)
        val viewCount = card.findViewById<TextView>(R.id.text_viewCount_card_notifications)
        var authorString = ""
        var viewCountNumber = 0
        var pusher_id = ""
        var create_time = 0
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
//        holder.cardView.image_card_notifications.setOnClickListener {
//            if (!isClickingCard) {
//                isClickingCard = true
//
//                GlobalVariables.proposalUserInfo.copy(userinfo)
//                GlobalVariables.functions.navigate(
//                    R.id.notificationOfficialFragment,
//                    R.id.action_notificationOfficialFragment_to_personShopInfoFragment
//                )
//
//                isClickingCard = false
//            }
//        }

        holder.cardView.notification_card_left.setOnClickListener {
            if (!isClickingCard) {
                isClickingCard = true
                Thread {
                    GlobalVariables.api.clickInNotification(
                        "ULifeOffical",
                        holder.create_time,
                        holder.viewCountNumber,
                        "Offical"
                    )
                }.start()

                parentView.text_message_notifications_official.text = holder.message.text
                parentView.text_author_notifications_official.text = holder.author.text
                parentView.text_date_notifications_official.text = holder.date.text
                parentView.scroll_notification_official.visibility = View.VISIBLE
                parentView.recycler_notification_official.visibility = View.GONE

                isClickingCard = false

            }
        }
    }
}