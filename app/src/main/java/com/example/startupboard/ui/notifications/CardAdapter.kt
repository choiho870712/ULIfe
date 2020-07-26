package com.example.startupboard.ui.notifications

import android.app.Activity
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

class CardAdapter(val myDataset: ArrayList<Notification>)
    : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    private var isClickingCard = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_notifications, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.content?.text = myDataset[position].content
        holder.date?.text = myDataset[position].content

        var userinfo:UserInfo? = null

        Thread {
            for (i in 0 until(GlobalVariables.notificationUserInfoList.size)) {
                if (GlobalVariables.notificationUserInfoList[i].ID == myDataset[position].pusher_id) {
                    userinfo = GlobalVariables.notificationUserInfoList[i]
                    val icon = userinfo!!.getIconBitmap()

                    GlobalVariables.activity.runOnUiThread(Runnable {
                        holder.content?.text = userinfo!!.name

                        if (icon != null) {
                            holder.icon.setImageBitmap(icon)
                        }
                    })
                    break
                }
            }
        }.start()

        holder.cardView.setOnClickListener {
            if (!isClickingCard) {
                isClickingCard = true

                Thread {

                    val myBundle = Bundle()
                    myBundle.putParcelable("userInfo", userinfo)

                    if (userinfo!!.latitude != 0.0) {
                        GlobalVariables.activity.runOnUiThread(Runnable {
                            GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                                R.id.action_navigation_notifications_to_personShopInfoFragment, myBundle)
                        })
                    }
                    else {
                        GlobalVariables.activity.runOnUiThread(Runnable {
                            GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                                R.id.action_navigation_notifications_to_personInfoFragment, myBundle)
                        })
                    }

                    isClickingCard = false
                }.start()
            }

        }
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val content = card.findViewById<TextView>(R.id.text_message_card_notifications)
        val date = card.findViewById<TextView>(R.id.text_date_card_notifications)
        val icon = card.findViewById<ImageView>(R.id.image_card_notifications)
    }
}