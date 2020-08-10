package com.choiho.ulife.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import com.choiho.ulife.ui.api.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_person_info.view.*

/**
 * A simple [Fragment] subclass.
 */
class PersonInfoFragment : Fragment() {

    private lateinit var root: View
    private var isRunningLoadSubList = false
    private var subscribeList: ArrayList<UserInfo> = ArrayList()
    private var subscribeListIsReady = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_person_info, container, false)

        setUi()
        setButton()

        return root
    }

    override fun onStop() {
        super.onStop()
        GlobalVariables.uiController.openEditPersonButton(false)
        GlobalVariables.uiController.openLogoutButton(false)
        GlobalVariables.uiController.openAddFriendButton(false)
    }

    private fun setUi() {
        if (activity != null) {
            setToolBarTitle(GlobalVariables.proposalUserInfo!!.name)
            setPageInfo()
            setFriendCount()
        }
    }

    private fun setToolBarTitle(title:String) {
        if (activity != null)
            requireActivity().toolbar.setTitle(title)
    }

    private fun setPageInfo() {
        root.text_selfIntroduction_information_person_section1.text = GlobalVariables.proposalUserInfo!!.content
        root.text_tag_information_person_section1.text = GlobalVariables.proposalUserInfo!!.getHashTagString()
        val icon = GlobalVariables.proposalUserInfo!!.getIconBitmap()
        if (icon != null)
            root.image_photoshot_information_person_section1.setImageBitmap(icon)


        Thread {
            if (GlobalVariables.proposalUserInfo.isMyAccount()){
                while (!GlobalVariables.subscribeListIsReady)
                    continue

                subscribeList = GlobalVariables.subscribeList
                subscribeListIsReady = true
            }
            else {
                val subscribeStringList = GlobalVariables.proposalUserInfo.subscribed
                for (i in 0 until(subscribeStringList.size)) {
                    Thread {
                        val id = subscribeStringList[i]
                        val subscriberUserInfo = GlobalVariables.api.getUserInfo(id)
                        subscriberUserInfo.ID = id
                        subscribeList.add(subscriberUserInfo)
                    }.start()
                }

                while (subscribeList.size != subscribeStringList.size)
                    continue

                subscribeListIsReady = true
            }

        }.start()

        Thread {
            while (!subscribeListIsReady)
                continue

            if (activity != null) {
                requireActivity().runOnUiThread {
                    root.text_followerCount_information_person_section1.text =
                        subscribeList.size.toString()
                }
            }
        }.start()
    }

    private fun setFriendCount() {
//        if (GlobalVariables.proposalUserInfo.isMyAccount()) {
//            val friendCount = GlobalVariables.friendList.size
//            val friendStringShow = friendCount.toString()
//            root.text_friendCount_information_person_section1.text = friendStringShow
//        }
//        else {
//            Thread {
//                val friendStringList = GlobalVariables.api.getFriendList(GlobalVariables.proposalUserInfo.ID)
//                val friendCount = friendStringList.size
//                val friendStringShow = friendCount.toString()
//                if (activity != null) {
//                    requireActivity().runOnUiThread(Runnable {
//                        root.text_friendCount_information_person_section1.text = friendStringShow
//                    })
//                }
//            }.start()
//        }
    }

    private fun setButton() {
        if (GlobalVariables.proposalUserInfo.isMyAccount()) {
            GlobalVariables.uiController.openEditPersonButton(true)
            GlobalVariables.uiController.openLogoutButton(true)

            root.text_friendCount_information_person_section1.setOnClickListener {
                if (activity != null) {
                    requireActivity().nav_host_fragment.findNavController().navigate(
                        R.id.action_personInfoFragment_to_chatFriendListFragment
                    )
                }
            }
        }
        else {
            if (GlobalVariables.proposalUserInfo.isAddFriendAvailable()) {
                GlobalVariables.uiController.openAddFriendButton(true)
            }
        }

        Thread {
            while (!subscribeListIsReady)
                continue

            if (activity != null) {
                requireActivity().runOnUiThread {
                    root.text_followerCount_information_person_section1.setOnClickListener {
                        if (!isRunningLoadSubList) {
                            isRunningLoadSubList = true
                            Thread {
                                GlobalVariables.friendList.clear()
                                for (i in 0 until(subscribeList).size) {
                                    Thread{
                                        val id = subscribeList[i].ID
                                        val userInfo = GlobalVariables.api.getUserInfo(id)
                                        userInfo.ID = id
                                        GlobalVariables.friendList.add(userInfo)
                                    }.start()
                                }

                                while (GlobalVariables.friendList.size != subscribeList.size)
                                    continue

                                GlobalVariables.needUnsubscribeButton = GlobalVariables.proposalUserInfo.isMyAccount()

                                if (activity!= null) {
                                    requireActivity().runOnUiThread(Runnable {
                                        GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                                            R.id.action_personInfoFragment_to_chatFriendListFragment)
                                    })
                                }

                                isRunningLoadSubList = false
                            }.start()
                        }
                    }
                }
            }
        }.start()
    }
}
