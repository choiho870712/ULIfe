package com.example.startupboard.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.GlobalVariables
import com.example.startupboard.GlobalVariables.Companion.activity
import com.example.startupboard.R
import com.example.startupboard.ui.api.Proposal
import com.example.startupboard.ui.api.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_home.view.*
import kotlinx.android.synthetic.main.card_search_friend.view.*

class CardFriendListAdapter(val myDataset: ArrayList<UserInfo>)
    : RecyclerView.Adapter<CardFriendListAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_search_friend, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.name.text = myDataset[position].name
        val icon = myDataset[position].getIconBitmap()
        if (icon != null)
            holder.icon.setImageBitmap(icon)

        holder.cardView.card_friend_left.setOnClickListener {
            val myBundle = Bundle()
            myBundle.putParcelable("userInfo", myDataset[position])

            if (myDataset[position].latitude != 0.0) {
                activity.runOnUiThread(Runnable {
                    activity.nav_host_fragment.findNavController().navigate(
                        R.id.action_chatFriendListFragment_to_personShopInfoFragment, myBundle)
                })
            }
            else {
                activity.runOnUiThread(Runnable {
                    activity.nav_host_fragment.findNavController().navigate(
                        R.id.action_chatFriendListFragment_to_personInfoFragment, myBundle)
                })
            }
        }


        holder.cardView.button_add_friend_card.visibility = View.INVISIBLE
//        holder.cardView.button_add_friend_card.setBackgroundResource(R.drawable.ic_chat_white_24dp)
//        holder.cardView.button_add_friend_card.setOnClickListener {
//            Toast.makeText(activity, "開始聊天", Toast.LENGTH_SHORT).show()
//        }
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val name = card.text_username_card_search_friend
        val icon = card.image_card_search_friend
    }

}