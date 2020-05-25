package com.example.startupboard.ui.chat

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.R

class CardFragment(val manager: FragmentManager) : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_chat_container, container, false)
        modifyHeader()
        createRecyclerView(root)
        return root
    }

    private fun createRecyclerView(root:View) {
        val dataList = ArrayList<CardModel>()
        dataList.add(CardModel("user1", "message1"))
        dataList.add(CardModel("user2", "message2"))

        root.findViewById<RecyclerView>(R.id.recycler_chat).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = CardAdapter(manager, dataList)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if ( !hidden ) modifyHeader()
    }

    private fun modifyHeader() {
        val title : TextView = activity!!.findViewById(R.id.text_title_toolbar)
        val buttonRight : ImageButton = activity!!.findViewById(R.id.button_right_toolbar)
        val buttonLeft : ImageButton = activity!!.findViewById(R.id.button_left_toolbar)
        title.text = "聊天室"
        title.gravity = Gravity.CENTER
        buttonRight.setImageResource(R.drawable.ic_person_add_white_24dp)
        buttonRight.setOnClickListener {

        }
        buttonLeft.setImageResource(R.color.colorPrimary)
        buttonLeft.setOnClickListener {

        }
    }
}