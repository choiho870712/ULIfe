package com.choiho.ulife.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.R
import com.choiho.ulife.ui.api.Chat

class CardAdapter(val myDataset: ArrayList<Chat>)
    : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_chat, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.username?.text = myDataset[position].pusher_id
        holder.message?.text = myDataset[position].content
//        Thread {
//            GlobalVariables.globalApi.loadImage(holder.image, myDataset[position].pusher_id, 0, "U")
//        }.start()
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val username = card.findViewById<TextView>(R.id.text_username_card_chat)
        val message = card.findViewById<TextView>(R.id.text_message_card_chat)
    }
}