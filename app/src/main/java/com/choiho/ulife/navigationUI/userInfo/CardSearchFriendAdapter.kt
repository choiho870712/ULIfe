package com.choiho.ulife.navigationUI.userInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.GlobalVariables.Companion.activity
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_search_friend.view.*

class CardSearchFriendAdapter(val myDataset: ArrayList<UserInfo>)
    : RecyclerView.Adapter<CardSearchFriendAdapter.CardHolder>() {

    private var isClickingCard = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_search_friend, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        setCardUi(holder, position)
        setCardButton(holder, position)
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val name = card.text_username_card_search_friend
        val icon = card.image_card_search_friend
    }

    private fun setCardUi(holder: CardHolder, position: Int) {
        holder.name.text = myDataset[position].name
        val icon = myDataset[position].getIconBitmap()
        if (icon != null)
            holder.icon.setImageBitmap(icon)
    }

    private fun setCardButton(holder: CardHolder, position: Int) {
        setCardOnClickListener(holder, position)
        setAddFriendButton(holder, position)
    }

    private fun setCardOnClickListener(holder: CardHolder, position: Int) {
        holder.cardView.card_friend_left.setOnClickListener {
            if (!isClickingCard) {
                isClickingCard = true

                Thread {
                    GlobalVariables.proposalUserInfo.readFromApi(myDataset[position].ID)

                    if (GlobalVariables.proposalUserInfo.isShop()) {

                        activity.runOnUiThread(Runnable {
                            activity.nav_host_fragment.findNavController().navigate(
                                R.id.action_personSearchFriendFragment_to_personShopInfoFragment)
                        })
                    }
                    else {
                        activity.runOnUiThread(Runnable {
                            activity.nav_host_fragment.findNavController().navigate(
                                R.id.action_personSearchFriendFragment_to_personInfoFragment)
                        })
                    }

                    isClickingCard = false
                }.start()
            }

        }
    }

    private fun setAddFriendButton(holder: CardHolder, position: Int) {
        holder.cardView.button_add_friend_card.visibility = View.INVISIBLE
        if (myDataset[position].isSubscribeAvailable()) {
            holder.cardView.button_add_friend_card.visibility = View.VISIBLE
            holder.cardView.button_add_friend_card.setOnClickListener {
                Thread {
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
                }.start()
            }
        }
    }
}