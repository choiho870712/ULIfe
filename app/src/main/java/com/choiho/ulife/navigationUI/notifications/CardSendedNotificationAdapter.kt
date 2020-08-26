package com.choiho.ulife.navigationUI.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_notification_send.view.*

class CardSendedNotificationAdapter(val myDataset: ArrayList<String>, val parentView:View)
    : RecyclerView.Adapter<CardSendedNotificationAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_sended_notification, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.message?.text = myDataset[position]
        holder.cardView.setOnClickListener {
            parentView.edit_send_notification.setText(holder.message?.text)
        }
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val message = card.findViewById<TextView>(R.id.text_sended_notification_card)
    }
}