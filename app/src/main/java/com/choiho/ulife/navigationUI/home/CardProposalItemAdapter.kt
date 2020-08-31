package com.choiho.ulife.navigationUI.home

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.card_proposal_item.view.*
import kotlinx.android.synthetic.main.fragment_home_page1.view.*
import java.lang.Exception
import java.text.SimpleDateFormat

class CardProposalItemAdapter(val myDataset: MutableList<ProposalItem>, val proposalView: View)
    : RecyclerView.Adapter<CardProposalItemAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_proposal_item, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.index = position

        Thread {
            while (!myDataset[holder.index].isDoneImageLoadingOnlyOne())
                continue

            GlobalVariables.activity.runOnUiThread {
                holder.image.setImageBitmap(myDataset[holder.index].imageList[0])
            }
        }.start()

        holder.cardView.setOnClickListener {
            GlobalVariables.proposalItemIndex = holder.index
            // if this proposal is user's own proposal
            if (GlobalVariables.proposalUserInfo.isMyAccount()) {
                // enable delete item button
                GlobalVariables.toolBarController.openDeleteItemButton(true)
            }

            val parser = SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS")
            val formatter = SimpleDateFormat("MM/dd HH:mm")

            proposalView.text_content_home_page1.text = myDataset[holder.index].content
            proposalView.text_date_home_page1.text =
                formatter.format(parser.parse(myDataset[holder.index].date))
            proposalView.text_title_home_page1.text = myDataset[holder.index].title
            proposalView.text_tag_home_page1.text = myDataset[holder.index].getHashTagString()

            proposalView.text_date_home_page1.visibility = View.VISIBLE
            proposalView.text_title_home_page1.visibility = View.VISIBLE
            proposalView.text_tag_home_page1.visibility = View.VISIBLE

            proposalView.image_proposal.setImageBitmap(
                Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888))
            proposalView.image_proposal.visibility = View.VISIBLE

            Thread {
                GlobalVariables.taskCount++
                myDataset[holder.index].convertImageUrlToImageAll()
                while (!myDataset[holder.index].isDoneImageLoadingAll())
                    continue

                GlobalVariables.taskCount--

                try {
                    GlobalVariables.activity.runOnUiThread {
                        proposalView.image_proposal.setImageBitmap(
                            myDataset[holder.index].imageList[myDataset[holder.index].imageList.size-1])
                    }
                }
                catch (e:Exception) {

                }
            }.start()
        }
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val image = card.image_proposal_item
        var index = 0
    }

}