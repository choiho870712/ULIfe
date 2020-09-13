package com.choiho.ulife.discountTicket


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_food_price.view.*

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
        recyclerView = root.findViewById<RecyclerView>(R.id.recycler_food_price)

        Thread {
            GlobalVariables.taskCount++
            val foodPriceList = GlobalVariables.api.getDiscountTickets(
                GlobalVariables.userInfo.ID, GlobalVariables.homeAreaChoose)
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
            root.layout_food_price_page2.visibility = View.GONE
        }

        root.button_shop_info_food_price_page2.setOnClickListener {
            GlobalVariables.functions.navigate(
                R.id.foodPriceFragment,
                R.id.action_foodPriceFragment_to_personShopInfoFragment
            )

            Thread {
                GlobalVariables.proposalUserInfo.readFromApi(GlobalVariables.proposalUserInfo.ID)
            }.start()
        }

        return root
    }


}
