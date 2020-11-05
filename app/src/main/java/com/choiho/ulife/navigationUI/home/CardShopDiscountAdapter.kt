package com.choiho.ulife.navigationUI.home

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.card_proposal_item.view.*
import kotlinx.android.synthetic.main.card_shop_discount.view.*
import kotlinx.android.synthetic.main.fragment_home_page1.view.*
import kotlinx.android.synthetic.main.fragment_person_shop_info.view.*
import java.lang.Exception
import java.text.SimpleDateFormat

class CardShopDiscountAdapter(val myDataset: MutableList<String>, val proposalView: View)
    : RecyclerView.Adapter<CardShopDiscountAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_shop_discount, parent, false)
        return CardHolder(
            card
        )
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.index = position
        holder.text.text = myDataset[position]

        holder.cardView.setOnClickListener {
            proposalView.image_use_discount_stamp.visibility = View.GONE
            proposalView.card_button_use_discount.visibility = View.VISIBLE
            proposalView.layout_shop_discount.visibility = View.VISIBLE
            proposalView.layout_shop_discount.animate().alpha(1.0f)
            proposalView.text_shop_discount_page.text = holder.text.text
        }
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val text = card.text_shop_discount
        var index = 0
    }

}