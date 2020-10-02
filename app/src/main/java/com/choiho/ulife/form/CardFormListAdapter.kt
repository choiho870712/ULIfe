package com.choiho.ulife.form

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.card_form_list.view.*

class CardFormListAdapter(val myDataset:ArrayList<FormListItem>)
    : RecyclerView.Adapter<CardFormListAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_form_list, parent, false)
        return CardHolder(card)
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.title.text = myDataset[position].tilte
        holder.time.text = myDataset[position].end_time
        holder.prefix = myDataset[position].prefix

        val markFormDone = "markFormDone_" + holder.prefix
        if (GlobalVariables.dbHelper.readDB(markFormDone) != "") {
            holder.doneForm.text = "完成"
            val color = GlobalVariables.activity.resources.getColor(R.color.colorPrimaryDark)
            holder.doneForm.setTextColor(color)
        }
        else {
            holder.doneForm.text = "未填"
            val color = GlobalVariables.activity.resources.getColor(R.color.colorAccent)
            holder.doneForm.setTextColor(color)
        }

        holder.cardView.setOnClickListener {
            GlobalVariables.formPrefix = holder.prefix
            GlobalVariables.formTitle = holder.title.text.toString()
            GlobalVariables.functions.navigate(
                R.id.formListFragment,
                R.id.action_formListFragment_to_formFragment
            )
        }
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        val title = card.text_form_title
        val time = card.text_form_time
        var prefix:String = ""
        val doneForm = card.text_form_done
    }

}