package com.choiho.ulife.discountTicket


import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_food_price.view.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 */
class FoodPriceFragment : Fragment() {

    private lateinit var root:View
    private lateinit var recyclerView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_food_price, container, false)

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        recyclerView = root.findViewById(R.id.recycler_food_price)

        if (GlobalVariables.isFoodPriceSelected) {
            val title = "【" + GlobalVariables.foodPriceSelect.name + "】"
            val triggerTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(GlobalVariables.foodPriceSelect.expiration_time.toLong()),
                ZoneId.systemDefault()
            )
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
            val formatter = SimpleDateFormat("MM/dd HH:mm")

            root.text_food_price_page2_name.text = title
            root.text_food_price_page2_content.text = GlobalVariables.foodPriceSelect.content
            root.text_food_price_page2_time.text = formatter.format(parser.parse(triggerTime.toString()))

            val icon = GlobalVariables.proposalUserInfo.getIconBitmap()
            if (icon != null)
                root.image_food_price_page2.setImageBitmap(icon)

            root.text_ticket_count_down.text = GlobalVariables.activity.text_count_down.text

            root.layout_food_price_page2.visibility = View.VISIBLE
            root.layout_food_price_page2.animate().alpha(1.0f)
        }

        Thread {

            if (!GlobalVariables.isFoodPriceSelected)
                GlobalVariables.taskCount++

            val foodPriceList = GlobalVariables.api.getDiscountTickets(
                GlobalVariables.userInfo.ID, GlobalVariables.homeAreaChoose)

            if (!GlobalVariables.isFoodPriceSelected)
                GlobalVariables.taskCount--

            if (activity != null) {
                requireActivity().runOnUiThread{

                    if (foodPriceList.isEmpty()) {
                        root.text_no_food_price.visibility = View.VISIBLE
                    }
                    else {
                        root.text_no_food_price.visibility = View.GONE

                        recyclerView.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(activity)
                            adapter = CardFoodPriceAdapter(foodPriceList, root)
                        }
                    }
                }
            }
        }.start()

        root.layout_food_price_page2.setOnClickListener {
            root.layout_food_price_page2.animate().alpha(0.0f)
            Thread {
                Thread.sleep(1000)
                if (activity!= null)requireActivity().runOnUiThread {
                    root.layout_food_price_page2.visibility = View.GONE
                }
            }.start()
            GlobalVariables.isFoodPriceSelected = false
            GlobalVariables.countDownTimer.cancel()
        }

        root.button_shop_info_food_price_page2.setOnClickListener {
            GlobalVariables.functions.navigate(
                R.id.foodPriceFragment,
                R.id.action_foodPriceFragment_to_personShopInfoFragment
            )
        }

        GlobalVariables.activity.text_count_down.doOnTextChanged { text, start, before, count ->
            root.text_ticket_count_down.text = text
            if (text.toString().toInt() == 0) {
                root.layout_food_price_page2.animate().alpha(0.0f)
                Thread {
                    Thread.sleep(1000)
                    if (activity!= null)requireActivity().runOnUiThread {
                        root.layout_food_price_page2.visibility = View.GONE
                    }
                }.start()
            }
        }

        return root
    }

    class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long, internal var tv: TextView) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            GlobalVariables.isFoodPriceSelected = false
        }
        override fun onTick(millisUntilFinished: Long) {
            tv.setText((millisUntilFinished/1000).toInt().toString())
        }
    }
}
