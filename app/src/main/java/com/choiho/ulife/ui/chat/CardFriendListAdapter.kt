package com.choiho.ulife.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.GlobalVariables.Companion.activity
import com.choiho.ulife.R
import com.choiho.ulife.ui.api.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
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
        holder.id = myDataset[position].ID
        val icon = myDataset[position].getIconBitmap()
        if (icon != null)
            holder.icon.setImageBitmap(icon)

        holder.cardView.card_friend_left.setOnClickListener {
            GlobalVariables.proposalUserInfo = myDataset[position]
            GlobalVariables.proposalUserInfoDoneLoading = true

            if (myDataset[position].isShop()) {
                activity.nav_host_fragment.findNavController().navigate(
                    R.id.action_chatFriendListFragment_to_personShopInfoFragment)
            }
            else {
                activity.nav_host_fragment.findNavController().navigate(
                    R.id.action_chatFriendListFragment_to_personInfoFragment)
            }
        }


        holder.cardView.button_add_friend_card.visibility = View.GONE
        if (GlobalVariables.needUnsubscribeButton) {
            holder.cardView.button_add_friend_card.visibility = View.VISIBLE
            holder.cardView.button_add_friend_card.setOnClickListener {
                Thread {
                    if (GlobalVariables.api.deleteSubscribe(holder.id, GlobalVariables.userInfo.ID)) {
                        val id = GlobalVariables.userInfo.ID
                        GlobalVariables.functions.deleteDataFromSQL()
                        GlobalVariables.functions.readDataFromApi(id)
                        GlobalVariables.functions.writeDataToSQL()
                        activity.runOnUiThread {
                            GlobalVariables.friendList.removeAt(position)
                            this.notifyDataSetChanged()
                        }
                    }

                }.start()
            }
        }
//        holder.cardView.button_add_friend_card.setBackgroundResource(R.drawable.ic_chat_white_24dp)
//        holder.cardView.button_add_friend_card.setOnClickListener {
//            Toast.makeText(activity, "開始聊天", Toast.LENGTH_SHORT).show()
//        }
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val name = card.text_username_card_search_friend
        val icon = card.image_card_search_friend
        var id = ""
    }

}