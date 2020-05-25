package com.example.startupboard.ui.notifications

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.GlobalVariables
import com.example.startupboard.R
import com.example.startupboard.ui.api.Notification

class CardAdapter(val activity: Activity, val manager: FragmentManager, val myDataset: ArrayList<Notification>)
    : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_notifications, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.content?.text = myDataset[position].content
        holder.date?.text = myDataset[position].date
        Thread {
            GlobalVariables.globalApi.loadImage(activity, holder.image, myDataset[position].pusher_id, 0, "U")
        }.start()
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val content = card.findViewById<TextView>(R.id.text_message_card_notifications)
        val date = card.findViewById<TextView>(R.id.text_date_card_notifications)
        val image = card.findViewById<ImageView>(R.id.image_authorHead_card_home)
    }
}