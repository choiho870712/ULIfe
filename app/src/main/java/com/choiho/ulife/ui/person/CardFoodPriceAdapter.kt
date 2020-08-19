package com.choiho.ulife.ui.person

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import com.choiho.ulife.ui.api.Notification
import com.choiho.ulife.ui.api.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_food_price.view.*
import kotlinx.android.synthetic.main.card_notifications.view.*
import kotlinx.android.synthetic.main.fragment_notifications_shop.view.*

class CardFoodPriceAdapter(val myDataset: ArrayList<Notification>)
    : RecyclerView.Adapter<CardFoodPriceAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_food_price, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.message.text = myDataset[position].content
        holder.date.text = myDataset[position].date
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val message = card.text_food_price_message
        val date = card.text_food_price_date
    }
}