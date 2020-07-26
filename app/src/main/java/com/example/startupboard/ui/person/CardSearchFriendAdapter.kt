package com.example.startupboard.ui.person

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

class CardSearchFriendAdapter(val myDataset: ArrayList<UserInfo>)
    : RecyclerView.Adapter<CardSearchFriendAdapter.CardHolder>() {

    private var isClickingCard = false
    lateinit var subscribeList:ArrayList<UserInfo>
    private var isDoneGettingSubscribeList = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_search_friend, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {

        Thread {
            subscribeList = GlobalVariables.api.getSubscribeList(myDataset[position].ID)
            isDoneGettingSubscribeList = true
        }.start()

        holder.name.text = myDataset[position].name
        val icon = myDataset[position].getIconBitmap()
        if (icon != null)
            holder.icon.setImageBitmap(icon)

        holder.cardView.card_friend_left.setOnClickListener {
            if (!isClickingCard) {
                isClickingCard = true

                Thread {

                    val friendInfo = GlobalVariables.api.getUserInfo(myDataset[position].ID)
                    friendInfo.ID = myDataset[position].ID

                    val myBundle = Bundle()
                    myBundle.putParcelable("userInfo", friendInfo)

                    if (friendInfo.latitude != 0.0) {

                        activity.runOnUiThread(Runnable {
                            activity.nav_host_fragment.findNavController().navigate(
                                R.id.action_personSearchFriendFragment_to_personShopInfoFragment, myBundle)
                        })
                    }
                    else {
                        activity.runOnUiThread(Runnable {
                            activity.nav_host_fragment.findNavController().navigate(
                                R.id.action_personSearchFriendFragment_to_personInfoFragment, myBundle)
                        })
                    }

                    isClickingCard = false
                }.start()
            }

        }

        Thread {
            if ((myDataset[position].latitude == 0.0 && isAddFriendAvailable(myDataset[position].ID))||
                myDataset[position].latitude != 0.0 && isSubscribeAvailable(myDataset[position].ID)) {
                holder.cardView.button_add_friend_card.setOnClickListener {
                    if (myDataset[position].latitude == 0.0 && isAddFriendAvailable(myDataset[position].ID)) {
                        if (GlobalVariables.api.buildRelationship(GlobalVariables.userInfo.ID, myDataset[position].ID)) {
                            activity.runOnUiThread(Runnable {
                                Toast.makeText(activity, "加入好友成功", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else {
                            activity.runOnUiThread(Runnable {
                                Toast.makeText(activity, "加入好友失敗", Toast.LENGTH_SHORT).show()
                            })
                        }
                    }
                    else if (myDataset[position].latitude != 0.0 && isSubscribeAvailable(myDataset[position].ID)) {
                        if (GlobalVariables.api.subscribe(GlobalVariables.userInfo.ID, myDataset[position].ID)) {
                            activity.runOnUiThread(Runnable {
                                Toast.makeText(activity, "訂閱成功", Toast.LENGTH_SHORT).show()
                            })
                        }
                        else {
                            activity.runOnUiThread(Runnable {
                                Toast.makeText(activity, "訂閱失敗", Toast.LENGTH_SHORT).show()
                            })
                        }
                    }
                }
            }
            else {
                activity.runOnUiThread(Runnable {
                    holder.cardView.button_add_friend_card.visibility = View.INVISIBLE
                })
            }
        }.start()
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val name = card.text_username_card_search_friend
        val icon = card.image_card_search_friend
    }

    private fun isSubscribeAvailable(id:String):Boolean {
        while (true) {
            if (isDoneGettingSubscribeList)
                break
        }
        if (GlobalVariables.userInfo.ID == id) return false
        for (i in 0 until(subscribeList.size)) {
            if (subscribeList[i].ID == GlobalVariables.userInfo.ID) return false
        }
        return true
    }

    private fun isAddFriendAvailable(id:String): Boolean {
        if (GlobalVariables.userInfo.ID == id) return false
        for (i in 0 until(GlobalVariables.friendList.size)) {
            if (GlobalVariables.friendList[i].ID == id) return false
        }
        return true
    }

}