package com.choiho.ulife.ui.chat


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_chat_friend_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class ChatFriendListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_chat_friend_list, container, false)

        root.recycler_friend_list_chat.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = CardFriendListAdapter(GlobalVariables.friendList)
        }

        return root
    }


}
