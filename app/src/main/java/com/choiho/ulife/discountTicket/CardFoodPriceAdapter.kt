package com.choiho.ulife.discountTicket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_food_price.view.*
import kotlinx.android.synthetic.main.fragment_food_price.view.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.text.SimpleDateFormat

class CardFoodPriceAdapter(val myDataset: ArrayList<DistountTicket>, val parentView:View)
    : RecyclerView.Adapter<CardFoodPriceAdapter.CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_food_price, parent, false)
        return CardHolder(
            card
        )
    }

    override fun getItemCount(): Int = myDataset.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val title = "【" + myDataset[position].name + "】"
        holder.name.text = title
        holder.content.text = myDataset[position].content

        val triggerTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(myDataset[position].expiration_time.toLong()),
            ZoneId.systemDefault()
        )

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val formatter = SimpleDateFormat("MM/dd HH:mm")
        holder.lastTime.text = formatter.format(parser.parse(triggerTime.toString()))

        holder.cardView.setOnClickListener {
            // setup dialog builder
            val builder = android.app.AlertDialog.Builder(GlobalVariables.activity)
            builder.setTitle("是否使用優惠券？")

            builder.setPositiveButton("是", { dialogInterface, i ->
                GlobalVariables.foodPriceSelect = myDataset[position]
                GlobalVariables.isFoodPriceSelected = true

                parentView.text_food_price_page2_name.text = holder.name.text
                parentView.text_food_price_page2_content.text = holder.content.text
                parentView.text_food_price_page2_time.text = holder.lastTime.text
                if (GlobalVariables.proposalUserInfo.ID != myDataset[position].id) {
                    GlobalVariables.proposalUserInfo.ID = myDataset[position].id
                    GlobalVariables.proposalUserInfo.isReady = false
                }
                Thread {
                    GlobalVariables.taskCount++
                    GlobalVariables.proposalUserInfo.readFromApi(GlobalVariables.proposalUserInfo.ID)
                    while (!GlobalVariables.proposalUserInfo.isReady)
                        Thread.sleep(100)

                    GlobalVariables.activity.runOnUiThread {
                        val icon = GlobalVariables.proposalUserInfo.getIconBitmap()
                        if (icon != null)
                            parentView.image_food_price_page2.setImageBitmap(icon)

                        GlobalVariables.countDownTimer =
                            FoodPriceFragment.MyCountDownTimer(
                                60000,
                                1000,
                                GlobalVariables.activity.text_count_down
                            )

                        parentView.layout_food_price_page2.visibility = View.VISIBLE
                        parentView.layout_food_price_page2.animate().alpha(1.0f)
                        GlobalVariables.countDownTimer.start()
                        GlobalVariables.taskCount--
                    }
                }.start()
            })
            builder.setNegativeButton("否", { dialogInterface, i ->

            })

            // create dialog and show it
            GlobalVariables.activity.runOnUiThread{
                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    class CardHolder(card: View) : RecyclerView.ViewHolder(card) {
        val cardView = card
        var name = card.text_food_price_name
        val content = card.text_food_price_content
        val lastTime = card.text_food_price_time
    }
}