package com.example.startupboard.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.R

class CardAdapter(val manager: FragmentManager, val myDataset: ArrayList<CardModel>)
    : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_chat, parent, false)
        card.setOnClickListener {
            val transaction = manager.beginTransaction()
            transaction.hide(manager.findFragmentByTag("chat_main")!!)
            transaction.add(R.id.container_chat_fragment, CardPageFragment(manager), "chat_card_page")
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.userName?.text = myDataset[position].userName
        holder.message?.text = myDataset[position].message
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val userName = card.findViewById<TextView>(R.id.text_userName_card_chat)
        val message = card.findViewById<TextView>(R.id.text_message_card_chat)
    }
}