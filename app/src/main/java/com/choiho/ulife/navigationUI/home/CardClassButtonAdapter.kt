package com.choiho.ulife.navigationUI.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.GlobalVariables.Companion.activity
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.card_class_button.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class CardClassButtonAdapter(val myDataset: ArrayList<String>, val homePageView: View)
    : RecyclerView.Adapter<CardClassButtonAdapter.CardHolder>() {

    val buttonList:ArrayList<Button> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_class_button, parent, false)
        for (i in 0 until(itemCount))
            buttonList.add(Button(activity))
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.title.text = myDataset[position]
        buttonList[position] = holder.title

//        if ((GlobalVariables.homeClassChoose == "" && myDataset[position] == "全部")||
//            (GlobalVariables.homeClassChoose ==  myDataset[position])) {
//            buttonList[position].setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
//            buttonList[position].setTextColor(activity.resources.getColor(R.color.colorWhite))
//        }

        holder.cardView.button_class.setOnClickListener {
            if (!GlobalVariables.lockRefreshHomePage) {
                GlobalVariables.lockRefreshHomePage = true
                homePageView.swip_recycler_home.isRefreshing = true
                Thread {
                    GlobalVariables.homeProposalNumber = 1
                    GlobalVariables.isHomeProposalEnd = false

//                    activity.runOnUiThread(Runnable {
//                        for (i in 0 until(itemCount)) {
//                            buttonList[i].setBackgroundColor(activity.resources.getColor(R.color.colorWhite))
//                            buttonList[i].setTextColor(activity.resources.getColor(R.color.colorPrimaryDark))
//                        }
//
//                        if ((GlobalVariables.homeClassChoose == "" && myDataset[position] == "全部")||
//                            (GlobalVariables.homeClassChoose ==  myDataset[position])) {
//                            holder.title.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
//                            holder.title.setTextColor(activity.resources.getColor(R.color.colorWhite))
//                        }
//                    })

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