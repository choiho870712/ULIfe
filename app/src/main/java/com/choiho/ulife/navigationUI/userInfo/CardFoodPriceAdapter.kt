package com.choiho.ulife.navigationUI.userInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.DistountTicket
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import com.choiho.ulife.navigationUI.notifications.Notification
import kotlinx.android.synthetic.main.card_food_price.view.*
import org.threeten.bp.LocalDateTime
import java.text.SimpleDateFormat
import java.time.Instant

class CardFoodPriceAdapter(val myDataset: ArrayList<DistountTicket>)
    : RecyclerView.Adapter<CardFoodPriceAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_food_price, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.name.text = myDataset[position].name
        holder.content.text = myDataset[position].content


//        Instant.ofEpochMilli()
//
//        val begin = (LocalDate.parse("2019-11-04", dateFormatter).atOffset(ZoneOffset.UTC)
//            .toInstant()).atZone(ZoneId.of(timeZoneIdentifier))
//
//        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS")
//        formatter.format(parser.parse(LocalDateTime.now().toString()))
//
//        java.time.format.DateTimeFormatter.ISO_INSTANT
//            .format(java.time.Instant.ofEpochSecond(myDataset[position].expiration_time))
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        var name = card.text_food_price_name
        val content = card.text_food_price_content
    }
}