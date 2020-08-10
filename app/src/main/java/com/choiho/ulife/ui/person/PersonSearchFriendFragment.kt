package com.choiho.ulife.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_person_search_friend.view.*

/**
 * A simple [Fragment] subclass.
 */
class PersonSearchFriendFragment : Fragment() {

    private lateinit var root: View
    private var isRunningSearchFriend = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_person_search_friend, container, false)
        setButton()
        return root
    }

    private fun setButton() {
        root.button_search_friend_person_submit.setOnClickListener {
            if (!isRunningSearchFriend) {
                isRunningSearchFriend = true
                Thread {
                    val name = root.edit_username_search_friend_person.text.toString()
                    val friendList = GlobalVariables.api.searchUserByName(name)

                    if (activity!= null) {
                        requireActivity().runOnUiThread(Runnable {
                            root.recycler_search_friend_person.apply {
                                setHasFixedSize(true)
                                layoutManager = LinearLayoutManager(activity)
                                adapter = CardSearchFriendAdapter(friendList)
                            }
                        })
                    }

                    isRunningSearchFriend = false
                }.start()
            }
        }
    }


}
