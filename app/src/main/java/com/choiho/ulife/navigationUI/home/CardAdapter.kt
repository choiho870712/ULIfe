package com.choiho.ulife.navigationUI.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.card_home.view.*

class CardAdapter(private val myDataSet: ArrayList<Proposal>)
    : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_home, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataSet.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.author.text = myDataSet[position].name
        holder.title.text = myDataSet[position].proposalItemList[0].title
        holder.tag.text = myDataSet[position].proposalItemList[0].getHashTagString()

        if (myDataSet[position].proposalItemList[0].isDoneImageLoadingOnlyOne())
            holder.image.setImageBitmap(myDataSet[position].proposalItemList[0].imageList[0])
        else {
            Thread {
                while (!myDataSet[position].proposalItemList[0].isDoneImageLoadingOnlyOne()) continue
                GlobalVariables.activity.runOnUiThread {
                    holder.image.setImageBitmap(myDataSet[position].proposalItemList[0].imageList[0])
                }
            }.start()
        }

        holder.cardView.setOnClickListener {
            GlobalVariables.proposal = myDataSet[position]

            Thread {
                if (GlobalVariables.proposalUserInfo.ID != myDataSet[position].poster_id)
                    GlobalVariables.proposalUserInfo.isReady = false
                GlobalVariables.proposalUserInfo.readFromApi(GlobalVariables.proposal!!.poster_id)
            }.start()

            Thread {
                GlobalVariables.proposalUserScribeListData =
                    GlobalVariables.api.getSubscribeList(myDataSet[position].poster_id)
            }.start()

            GlobalVariables.homeCurrentPosition = GlobalVariables.homeLayoutManager.findFirstVisibleItemPosition()

            for ( i in 0 until(myDataSet[position].proposalItemList.size))
                Thread {
                    myDataSet[position].proposalItemList[i].convertImageUrlToImageAll()
                }.start()

            GlobalVariables.functions.navigate(
                R.id.navigation_home,
                R.id.action_navigation_home_to_homePage1Fragment
            )
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