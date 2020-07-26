package com.example.startupboard.ui.notifications


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.startupboard.GlobalVariables

import com.example.startupboard.R
import kotlinx.android.synthetic.main.fragment_notification_send.view.*
import java.time.LocalDate
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationSendFragment : Fragment() {

    private var isRunningSendNotification = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_notification_send, container, false)

        root.button_send_notification.setOnClickListener {
            if (!isRunningSendNotification) {
                isRunningSendNotification = true

                var count = 0
                for (i in 0 until(GlobalVariables.subscribeList.size)) {
                    Thread{
                        val target_id = GlobalVariables.subscribeList[i].ID

                        val isSuccess = GlobalVariables.api.postNotification(
                            target_id,
                            GlobalVariables.userInfo.ID,
                            "shop",
                            root.edit_send_notification.text.toString(),
                            LocalDate.now().toString()
                        )

                        count++
                    }.start()
                }

                while(true) {
                    if (count == GlobalVariables.subscribeList.size) {
                        isRunningSendNotification = true
                        activity!!.runOnUiThread(Runnable {
                            Toast.makeText(activity, "送出訊息成功", Toast.LENGTH_SHORT).show()
                        })
                        break
                    }
                }
            }
        }

        return root
    }


}
