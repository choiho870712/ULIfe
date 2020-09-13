package com.choiho.ulife.form

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.card_form_select_answer.view.*

class CardFormSelectAnswerAdapter(val myDataset:ArrayList<String>, val type: String)
    : RecyclerView.Adapter<CardFormSelectAnswerAdapter.CardHolder>() {

    val checkBoxList:ArrayList<CheckBox> = arrayListOf()
    val answerList:ArrayList<Boolean> = arrayListOf()

    fun init() {
        for (i in 0 until(itemCount)) {
            checkBoxList.add(CheckBox(GlobalVariables.activity))
            answerList.add(false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_form_select_answer, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        checkBoxList[position] = holder.selectAnswer
        checkBoxList[position].text = myDataset[position]

        fillAnswer()
        checkBoxList[position].setOnClickListener {
            if (type == "Single" && checkBoxList[position].isChecked)
                for (i in 0 until(itemCount))
                    if (i != position)
                        checkBoxList[i].isChecked = false

            for (i in 0 until(itemCount))
                answerList[i] = checkBoxList[i].isChecked
        }
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val selectAnswer = card.checkbox_form_select_item
    }

    fun fillAnswer() {
        for (i in 0 until(itemCount))
            checkBoxList[i].isChecked = answerList[i]
    }
}