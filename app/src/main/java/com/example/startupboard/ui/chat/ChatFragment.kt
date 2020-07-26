package com.example.startupboard.ui.chat


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.GlobalVariables

import com.example.startupboard.R
import com.example.startupboard.ui.api.Chat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_chat, container, false)
        activity!!.toolbar.setNavigationIcon(null)

        val buttonFriendList = activity!!.toolbar.menu.findItem(R.id.button_friend_list)
        buttonFriendList.setEnabled(true)
        buttonFriendList.setVisible(true)

        val buttonAddFriend = activity!!.toolbar.menu.findItem(R.id.button_search_friend)
        buttonAddFriend.setEnabled(true)
        buttonAddFriend.setVisible(true)

        activity!!.toolbar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.button_friend_list -> {
                    activity!!.nav_host_fragment.findNavController().navigate(R.id.action_navigation_chat_to_chatFriendListFragment)
                    true
                }
                R.id.button_search_friend -> {
                    activity!!.nav_host_fragment.findNavController().navigate(R.id.action_navigation_chat_to_personSearchFriendFragment)
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_chat)
        Thread {

            val chatList: ArrayList<Chat> = ArrayList()
            chatList.add(
                Chat(
                    "chou",
                    "7/12",
                    "hi",
                    0
                )
            )

            activity!!.runOnUiThread(Runnable {
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(activity)
                    adapter = CardAdapter(chatList)
                }
            })

        }.start()

        GlobalVariables.uiController.setNavViewButton()

        return root
    }

    override fun onStop() {
        super.onStop()

        val buttonFriendList = activity!!.toolbar.menu.findItem(R.id.button_friend_list)
        buttonFriendList.setEnabled(false)
        buttonFriendList.setVisible(false)

        val buttonAddFriend = activity!!.toolbar.menu.findItem(R.id.button_search_friend)
        buttonAddFriend.setEnabled(false)
        buttonAddFriend.setVisible(false)
    }


}
