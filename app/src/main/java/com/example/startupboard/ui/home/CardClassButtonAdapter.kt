package com.example.startupboard.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.GlobalVariables
import com.example.startupboard.GlobalVariables.Companion.activity
import com.example.startupboard.R
import com.example.startupboard.ui.api.Proposal
import kotlinx.android.synthetic.main.card_class_button.view.*
import kotlinx.android.synthetic.main.card_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class CardClassButtonAdapter(val myDataset: ArrayList<String>, val homePageView: View)
    : RecyclerView.Adapter<CardClassButtonAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_class_button, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.title.text = myDataset[position]

        holder.cardView.button_class.setOnClickListener {
            if (!GlobalVariables.isRefreshingHomePage) {
                GlobalVariables.isRefreshingHomePage = true
                homePageView.swip_recycler_home.isRefreshing = true
                GlobalVariables.homePageProposalCount = 0
                Thread {
                    GlobalVariables.homeProposalList = GlobalVariables.api.getFoodByClass(1, myDataset[position],GlobalVariables.homeAreaChoose)
                    GlobalVariables.homeAdapter = CardAdapter(GlobalVariables.homeProposalList)
                    GlobalVariables.homePageProposalCount = 10
                    GlobalVariables.homeLayoutManager = GridLayoutManager(activity, 2)

                    activity.runOnUiThread(Runnable {
                        homePageView.recycler_home.apply {
                            setHasFixedSize(true)
                            layoutManager = GlobalVariables.homeLayoutManager
                            adapter = GlobalVariables.homeAdapter
                        }
                        homePageView.swip_recycler_home.isRefreshing = false
                    })
                }.start()
                GlobalVariables.isRefreshingHomePage = false
            }
        }
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val title = card.button_class
    }

}