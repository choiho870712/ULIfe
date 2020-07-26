package com.example.startupboard.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.startupboard.GlobalVariables

import com.example.startupboard.R
import com.example.startupboard.ui.api.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_person_info.view.*
import kotlinx.android.synthetic.main.fragment_person_shop_info.view.*

/**
 * A simple [Fragment] subclass.
 */
class PersonInfoFragment : Fragment() {

    private var isRunningAddFriend = false
    private lateinit var friendStringList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_person_info, container, false)

        val myBundle = arguments
        if (myBundle != null) {

            val userinfo = myBundle.getParcelable<UserInfo>("userInfo")

            activity!!.toolbar.setTitle(userinfo!!.name)

            root.text_selfIntroduction_information_person_section1.text = userinfo.content
            root.text_tag_information_person_section1.text = userinfo.getHashTagString()
            val icon = userinfo.getIconBitmap()
            if (icon != null)
                root.image_photoshot_information_person_section1.setImageBitmap(icon)

            if (GlobalVariables.userInfo.ID == userinfo.ID) {
                val buttonEdit = activity!!.toolbar.menu.findItem(R.id.button_edit_person)
                buttonEdit.setEnabled(true)
                buttonEdit.setVisible(true)
                activity!!.toolbar.setOnMenuItemClickListener{
                    when (it.itemId) {
                        R.id.button_edit_person -> {
                            activity!!.nav_host_fragment.findNavController().navigate(R.id.action_personInfoFragment_to_personEditFragment)
                            true
                        }
                        else -> super.onOptionsItemSelected(it)
                    }
                }

                val friendCount = GlobalVariables.friendList.size
                val friendString = friendCount.toString() + "\n好友"
                root.text_friendCount_information_person_section1.text = friendString

                root.text_friendCount_information_person_section1.setOnClickListener {
                    activity!!.nav_host_fragment.findNavController().navigate(R.id.action_personInfoFragment_to_chatFriendListFragment)
                }
            }
            else {
                Thread {
                    friendStringList = GlobalVariables.api.getFriendList(userinfo.ID)
                    val friendStringShow = friendStringList.size.toString() + "\n好友"
                    activity!!.runOnUiThread(Runnable {
                        root.text_friendCount_information_person_section1.text = friendStringShow
                    })
                }.start()

                if (isAddFriendAvailable(userinfo.ID)) {
                    val buttonAddFriend = activity!!.toolbar.menu.findItem(R.id.button_add_friend)
                    buttonAddFriend.setEnabled(true)
                    buttonAddFriend.setVisible(true)

                    if (activity != null) {
                        activity!!.toolbar.setOnMenuItemClickListener{
                            when (it.itemId) {
                                R.id.button_add_friend -> {
                                    if (!isRunningAddFriend) {
                                        isRunningAddFriend = true
                                        Thread{
                                            if (GlobalVariables.api.buildRelationship(GlobalVariables.userInfo.ID, userinfo.ID)) {
                                                activity!!.runOnUiThread(Runnable {
                                                    Toast.makeText(activity, "加入好友成功", Toast.LENGTH_SHORT).show()
                                                })
                                            }
                                            else {
                                                activity!!.runOnUiThread(Runnable {
                                                    Toast.makeText(activity, "加入好友失敗", Toast.LENGTH_SHORT).show()
                                                })
                                            }
                                            isRunningAddFriend = false
                                        }.start()
                                    }

                                    true
                                }
                                else -> super.onOptionsItemSelected(it)
                            }
                        }
                    }
                }
            }
        }

        return root
    }

    override fun onStop() {
        super.onStop()
        val buttonEdit = activity!!.toolbar.menu.findItem(R.id.button_edit_person)
        buttonEdit.setEnabled(false)
        buttonEdit.setVisible(false)

        val buttonAddFriend = activity!!.toolbar.menu.findItem(R.id.button_add_friend)
        buttonAddFriend.setEnabled(false)
        buttonAddFriend.setVisible(false)
    }

    private fun isAddFriendAvailable(id:String): Boolean {
        if (GlobalVariables.userInfo.ID == id) return false
        for (i in 0 until(GlobalVariables.friendList.size)) {
            if (GlobalVariables.friendList[i].ID == id) return false
        }
        return true
    }
}
