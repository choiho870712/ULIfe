package com.choiho.ulife.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import com.choiho.ulife.ui.api.Notification
import com.choiho.ulife.ui.notifications.CardShopAdapter

/**
 * A simple [Fragment] subclass.
 */
class FoodPriceFragment : Fragment() {

    private lateinit var root:View
    private lateinit var recyclerView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_food_price, container, false)
        recyclerView = root.findViewById<RecyclerView>(R.id.recycler_food_price)

        Thread {
            // TODO call api
            val foodPriceList = arrayListOf<Notification>()

            if (activity != null) {
                requireActivity().runOnUiThread{
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
                        adapter = CardFoodPriceAdapter(foodPriceList)
                    }
                }
            }
        }.start()

        return root
    }


}
