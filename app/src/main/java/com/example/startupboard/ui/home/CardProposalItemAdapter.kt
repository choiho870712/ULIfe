package com.example.startupboard.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.GlobalVariables
import com.example.startupboard.R
import com.example.startupboard.ui.api.Proposal
import com.example.startupboard.ui.api.ProposalItem
import kotlinx.android.synthetic.main.card_home.view.*
import kotlinx.android.synthetic.main.card_proposal_item.view.*
import kotlinx.android.synthetic.main.fragment_home_page1.view.*

class CardProposalItemAdapter(val myDataset: ArrayList<ProposalItem>, val proposalView: View)
    : RecyclerView.Adapter<CardProposalItemAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_proposal_item, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        Thread {
            while (!myDataset[position].isDoneImageLoadingAll())
                ;

            GlobalVariables.activity.runOnUiThread {
                holder.image.setImageBitmap(myDataset[position].imageList[0])
            }
        }.start()

        holder.cardView.setOnClickListener {
            proposalView.text_author_home_page1.text = myDataset[position].title
            proposalView.text_content_home_page1.text = myDataset[position].content
            proposalView.text_date_home_page1.text = myDataset[position].date
            proposalView.text_title_home_page1.text = myDataset[position].title
            proposalView.text_tag_home_page1.text = myDataset[position].getHashTagString()
            proposalView.image_proposal.setImageBitmap(myDataset[position].imageList[0])
            proposalView.image_proposal.visibility = View.VISIBLE
        }
    }


    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val image = card.image_proposal_item
    }

}