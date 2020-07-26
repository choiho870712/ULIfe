package com.example.startupboard.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.GlobalVariables
import com.example.startupboard.R
import com.example.startupboard.ui.api.Proposal
import kotlinx.android.synthetic.main.card_home.view.*

class CardAdapter(val myDataset: ArrayList<Proposal>)
    : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_home, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.author.text = myDataset[position].name
        holder.title.text = myDataset[position].proposalItemList[0].title
        holder.tag.text = myDataset[position].proposalItemList[0].getHashTagString()

        Thread {
            while (!myDataset[position].proposalItemList[0].isDoneImageLoadingOnlyOne())
                ;

            GlobalVariables.activity.runOnUiThread {
                holder.image.setImageBitmap(myDataset[position].proposalItemList[0].imageList[0])
            }
        }.start()

        holder.cardView.setOnClickListener {
            if (!GlobalVariables.isRefreshingHomePage) {
                GlobalVariables.proposal = myDataset[position]

                // userinfo thread
                GlobalVariables.proposalUserInfoDoneLoading = false
                Thread {
                    GlobalVariables.proposalUserInfo = GlobalVariables.api.getUserInfo(GlobalVariables.proposal!!.poster_id)
                    GlobalVariables.proposalUserInfo!!.ID = GlobalVariables.proposal!!.poster_id
                    GlobalVariables.proposalUserInfoDoneLoading = true
                }.start()

                Thread {
                    GlobalVariables.proposalUserScribeList = GlobalVariables.api.getSubscribeList(myDataset[position].poster_id)
                }.start()

                // 9 image thread
                for ( i in 0 until(myDataset[position].proposalItemList.size)) {
                    Thread {
                        myDataset[position].proposalItemList[i].convertImageUrlToImageAll()
                    }.start()
                }

                holder.cardView.findNavController().navigate(R.id.action_navigation_home_to_homePage1Fragment)

            }
        }
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val author = card.text_author_card_home
        val title = card.text_title_card_home
        val tag = card.text_tag_card_home
        val image = card.image_food_card_home
    }

}