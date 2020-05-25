package com.example.startupboard.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.R

class CardPageSection1Adapter(val manager: FragmentManager, val myDataset: ArrayList<CardPageSection1Model>)
    : RecyclerView.Adapter<CardPageSection1Adapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_page_home_section1, parent, false)
        card.setOnClickListener {
            val transaction = manager.beginTransaction()
            transaction.hide(manager.findFragmentByTag("home_card_page")!!)
            transaction.add(R.id.container_home_fragment, CardPageSection1CardPageFragment(manager))
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.message?.text = myDataset[position].message
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val message = card.findViewById<TextView>(R.id.text_card_page_home_section1)
    }
}