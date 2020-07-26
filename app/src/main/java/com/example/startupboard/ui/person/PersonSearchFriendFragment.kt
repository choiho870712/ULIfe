package com.example.startupboard.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.startupboard.GlobalVariables

import com.example.startupboard.R
import com.example.startupboard.ui.api.UserInfo
import kotlinx.android.synthetic.main.fragment_person_search_friend.view.*

/**
 * A simple [Fragment] subclass.
 */
class PersonSearchFriendFragment : Fragment() {

    private lateinit var friendList:ArrayList<UserInfo>
    private var isRunningSearchFriend = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_person_search_friend, container, false)

        root.button_search_friend_person_submit.setOnClickListener {
            if (!isRunningSearchFriend) {
                isRunningSearchFriend = true
                Thread {
                    val name = root.edit_username_search_friend_person.text.toString()
                    friendList = GlobalVariables.api.searchUserByName(name)

                    activity!!.runOnUiThread(Runnable {
                        root.recycler_search_friend_person.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(activity)
                            adapter = CardSearchFriendAdapter(friendList)
                        }
                    })

                    isRunningSearchFriend = false
                }.start()
            }
        }

        return root
    }


}
