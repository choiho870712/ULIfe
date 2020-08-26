package com.choiho.ulife.navigationUI.userInfo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_person_add_friend.view.*
import kotlinx.android.synthetic.main.fragment_person_add_friend.view.edit_send_friend_notification
import java.time.LocalDate

/**
 * A simple [Fragment] subclass.
 */
class PersonAddFriendFragment : Fragment() {

    private var isRunningSendFriendNotification = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_person_add_friend, container, false)

        root.button_send_friend_notification.setOnClickListener {
            if (!isRunningSendFriendNotification) {
                isRunningSendFriendNotification = false
                Thread {
                    val message = root.edit_send_friend_notification.text.toString()

                    GlobalVariables.proposalUserInfo =
                        GlobalVariables.api.getUserInfo(GlobalVariables.proposalUserInfo!!.ID)

                    GlobalVariables.fireBase.requestSendingFCM(
                        GlobalVariables.proposalUserInfo!!.FMC_ID,
                        GlobalVariables.userInfo.name,
                        message
                    )

                    val isSuccess = GlobalVariables.api.postNotification(
                        GlobalVariables.proposalUserInfo!!.ID,
                        message,
                        LocalDate.now().toString()
                    )

                    if (isSuccess) {
                        if (activity != null) {
                            requireActivity().runOnUiThread(Runnable {
                                Toast.makeText(activity, "成功送出交友邀請", Toast.LENGTH_SHORT).show()
                            })
                        }
                    }

                    isRunningSendFriendNotification = true
                }.start()
            }
        }

        return root
    }


}
