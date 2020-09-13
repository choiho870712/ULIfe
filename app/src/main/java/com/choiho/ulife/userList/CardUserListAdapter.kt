package com.choiho.ulife.userList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.GlobalVariables.Companion.activity
import com.choiho.ulife.R
import com.choiho.ulife.navigationUI.userInfo.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_search_friend.view.*
import kotlinx.android.synthetic.main.fragment_chat_friend_list.view.*

class CardUserListAdapter(val myDataset: ArrayList<UserInfo>, val parentView: View)
    : RecyclerView.Adapter<CardUserListAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_search_friend, parent, false)
        return CardHolder(
            card
        )
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.name.text = myDataset[position].name
        holder.id = myDataset[position].ID
        val icon = myDataset[position].getIconBitmap()
        if (icon != null)
            holder.icon.setImageBitmap(icon)

        holder.cardView.card_friend_left.setOnClickListener {
            GlobalVariables.proposalUserInfo.copy(myDataset[position])

            if (myDataset[position].isShop()) {
                GlobalVariables.functions.navigate(
                    R.id.chatFriendListFragment,
                    R.id.action_chatFriendListFragment_to_personShopInfoFragment
                )
            }
            else {
                GlobalVariables.functions.navigate(
                    R.id.chatFriendListFragment,
                    R.id.action_chatFriendListFragment_to_personInfoFragment
                )
            }
        }


        holder.cardView.button_add_friend_card.visibility = View.GONE
        if (GlobalVariables.needUnsubscribeButton) {
            holder.cardView.button_add_friend_card.visibility = View.VISIBLE
            holder.cardView.button_add_friend_card.setOnClickListener {
                Thread {
                    GlobalVariables.taskCount++

                    if (GlobalVariables.api.deleteSubscribe(holder.id, GlobalVariables.userInfo.ID)) {
                        GlobalVariables.functions.deleteFromSubscribeList(holder.id)
                        GlobalVariables.functions.deleteFromNotificationList(holder.id)
                        activity.runOnUiThread {
//                            GlobalVariables.otherUserInfoList.removeAt(position)
                            this.notifyDataSetChanged()

                            if (GlobalVariables.otherUserInfoList.isEmpty())
                                parentView.text_no_subscribe_info.visibility = View.VISIBLE
                            else
                                parentView.text_no_subscribe_info.visibility = View.GONE
                        }
                        GlobalVariables.functions.makeToast("已退訂")
                        GlobalVariables.taskCount--
                    }
                    else {
                        GlobalVariables.functions.makeToast("退訂失敗")
                        GlobalVariables.taskCount--
                    }


                }.start()
            }
        }
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val name = card.text_username_card_search_friend
        val icon = card.image_card_search_friend
        var id = ""
    }

}