package com.example.startupboard.ui.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.R
import com.example.startupboard.ui.api.Proposal
import com.example.startupboard.GlobalVariables.Companion.globalApi

class CardAdapter(val activity: Activity, val manager: FragmentManager, val myDataset: ArrayList<Proposal>)
    : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_home, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.author?.text = myDataset[position].name
        holder.date?.text = myDataset[position].date
        holder.title?.text = myDataset[position].title
        holder.tag?.text = myDataset[position].getHashTagString()
        val viewCountStr = "觀看次數 : " + myDataset[position].view.toString()
        holder.viewCount?.text = viewCountStr
        Thread {
            globalApi.loadImage(activity, holder.image, myDataset[position].poster_id, 0, "U")
        }.start()

        holder.cardView.setOnClickListener {
            val transaction = manager.beginTransaction()
            transaction.hide(manager.findFragmentByTag("home_main")!!)
            transaction.add(R.id.container_home_fragment, CardPageFragment(manager, myDataset[position]), "home_card_page")
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val author = card.findViewById<TextView>(R.id.text_author_card_home)
        val date = card.findViewById<TextView>(R.id.text_date_card_home)
        val title = card.findViewById<TextView>(R.id.text_title_card_home)
        val tag = card.findViewById<TextView>(R.id.text_tag_card_home)
        val viewCount = card.findViewById<TextView>(R.id.text_viewCount_card_home)
        val image = card.findViewById<ImageView>(R.id.image_authorHead_card_home)
    }

}