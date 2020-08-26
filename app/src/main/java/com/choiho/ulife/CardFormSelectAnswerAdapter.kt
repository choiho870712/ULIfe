package com.choiho.ulife

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.card_form.view.*
import kotlinx.android.synthetic.main.card_form_select_answer.view.*
import kotlinx.android.synthetic.main.card_proposal_item.view.*
import kotlinx.android.synthetic.main.fragment_form.view.*
import kotlinx.android.synthetic.main.fragment_home_page1.view.*

class CardFormSelectAnswerAdapter(val myDataset:ArrayList<String>, val type: String)
    : RecyclerView.Adapter<CardFormSelectAnswerAdapter.CardHolder>() {

    val checkBoxList:ArrayList<CheckBox> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_form_select_answer, parent, false)
        checkBoxList.add(CheckBox(GlobalVariables.activity))
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        checkBoxList[position] = holder.selectAnswer
        checkBoxList[position].text = myDataset[position]

        checkBoxList[position].setOnClickListener {
            if (type == "Single" && checkBoxList[position].isChecked)
                for (i in 0 until(itemCount))
                    if (i != position)
                        checkBoxList[i].isChecked = false
        }
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val selectAnswer = card.checkbox_form_select_item
    }

}