package com.choiho.ulife.discountTicket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.card_food_price.view.*
import kotlinx.android.synthetic.main.fragment_food_price.view.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.text.SimpleDateFormat

class CardFoodPriceAdapter(val myDataset: ArrayList<DistountTicket>, val parentView:View)
    : RecyclerView.Adapter<CardFoodPriceAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_food_price, parent, false)
        return CardHolder(
            card
        )
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val title = "【" + myDataset[position].name + "】"
        holder.name.text = title
        holder.content.text = myDataset[position].content

        val triggerTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(myDataset[position].expiration_time.toLong()),
            ZoneId.systemDefault()
        )

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val formatter = SimpleDateFormat("MM/dd HH:mm")
        holder.lastTime.text = formatter.format(parser.parse(triggerTime.toString()))

        holder.cardView.setOnClickListener {
            parentView.text_food_price_page2_name.text = holder.name.text
            parentView.text_food_price_page2_content.text = holder.content.text
            parentView.text_food_price_page2_time.text = holder.lastTime.text
            parentView.layout_food_price_page2.visibility = View.VISIBLE
            if (GlobalVariables.proposalUserInfo.ID != myDataset[position].id) {
                GlobalVariables.proposalUserInfo.ID = myDataset[position].id
                GlobalVariables.proposalUserInfo.isReady = false
            }
        }
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        var name = card.text_food_price_name
        val content = card.text_food_price_content
        val lastTime = card.text_food_price_time
    }
}