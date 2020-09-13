package com.choiho.ulife.form

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.card_form.view.*

class CardFormAdapter(val myDataset:ArrayList<FormItem>)
    : RecyclerView.Adapter<CardFormAdapter.CardHolder>() {

    val answerList:ArrayList<String> = arrayListOf()
    val selectAnswerAdapterList:ArrayList<CardFormSelectAnswerAdapter> = arrayListOf()

    fun init() {
        for (i in 0 until(itemCount)) {
            answerList.add("")
            selectAnswerAdapterList.add(
                CardFormSelectAnswerAdapter(
                    arrayListOf(),
                    "none"
                )
            )

            if (myDataset[i].type == "Single" || myDataset[i].type == "Multiple") {
                selectAnswerAdapterList[i] =
                    CardFormSelectAnswerAdapter(
                        myDataset[i].answer, myDataset[i].type
                    )

                selectAnswerAdapterList[i].init()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_form, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val questionString = (position+1).toString() + ". " + myDataset[position].question
        holder.question.text = questionString

        if (myDataset[position].type == "Single" || myDataset[position].type == "Multiple") {
            holder.cardView.recycler_answer_select_form.visibility = View.VISIBLE
            holder.answer.visibility = View.GONE

            holder.cardView.recycler_answer_select_form.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(GlobalVariables.activity)
                adapter = selectAnswerAdapterList[position]
            }
        }
        else {
            holder.cardView.recycler_answer_select_form.visibility = View.GONE
            holder.answer.visibility = View.VISIBLE

            holder.answer.setText(answerList[position])
            holder.answer.addTextChangedListener {
                if (holder.answer.text.isNotEmpty())
                    answerList[position] = holder.answer.text.toString()
            }
        }

    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val question = card.text_question_form
        val answer = card.edit_answer_form
    }

}