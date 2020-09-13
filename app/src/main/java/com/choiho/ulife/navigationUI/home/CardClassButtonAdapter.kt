package com.choiho.ulife.navigationUI.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.GlobalVariables.Companion.activity
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.card_class_button.view.*
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
            if (!GlobalVariables.lockRefreshHomePage) {
                GlobalVariables.lockRefreshHomePage = true
                homePageView.swip_recycler_home.isRefreshing = true
                Thread {
                    GlobalVariables.homeProposalNumber = 1
                    GlobalVariables.isHomeProposalEnd = false

                    if (myDataset[position] == "全部") {
                        GlobalVariables.functions.resetProposalList()
                    }
                    else {
                        GlobalVariables.homeClassChoose = myDataset[position]
                        GlobalVariables.homeProposalList = GlobalVariables.api.getFoodByClass(
                            GlobalVariables.homeProposalNumber,
                            GlobalVariables.homeClassChoose,
                            GlobalVariables.homeAreaChoose
                        )
                        GlobalVariables.homeProposalNumber += 10
                    }

                    GlobalVariables.homeAdapter = CardAdapter(GlobalVariables.homeProposalList)
                    GlobalVariables.homeLayoutManager = GridLayoutManager(activity, 2)

                    activity.runOnUiThread(Runnable {
                        homePageView.recycler_home.apply {
                            setHasFixedSize(true)
                            layoutManager = GlobalVariables.homeLayoutManager
                            adapter = GlobalVariables.homeAdapter
                        }
                        homePageView.swip_recycler_home.isRefreshing = false

                        if (GlobalVariables.homeProposalList.isEmpty()) {
                            homePageView.text_no_home_proposal.visibility = View.VISIBLE
                        }
                        else {
                            homePageView.text_no_home_proposal.visibility = View.GONE
                        }
                    })

                    GlobalVariables.lockRefreshHomePage = false
                }.start()
            }
        }
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val title = card.button_class
    }

}