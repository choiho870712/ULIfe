package com.choiho.ulife.ui.notifications


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_notification_send.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass.
 */
class NotificationSendFragment : Fragment() {

    private var isRunningSendNotification = false
    private lateinit var root: View
    private lateinit var myAdapter: CardSendedNotificationAdapter
    private lateinit var myLinearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_notification_send, container, false)
        myAdapter = CardSendedNotificationAdapter(GlobalVariables.sendedNotificationList, root)
        myLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)

        root.button_send_notification.setOnClickListener {
            if (!isRunningSendNotification) {
                isRunningSendNotification = true

                val message = root.edit_send_notification.text.toString()

                var count = 0

                Thread {
                    val subscribeList = GlobalVariables.api.getSubscribeList(GlobalVariables.userInfo.ID)

                    for (i in 0 until(subscribeList.size)) {
                        Thread{
                            Log.d(">>>>>>>>>>>>>>>>>>>>>>", subscribeList[i].ID)

                            GlobalVariables.api.requestSendingFCM(
                                subscribeList[i].FMC_ID,
                                GlobalVariables.userInfo.name,
                                message
                            )
                            count++
                        }.start()
                    }
                }.start()

                Thread {
                    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SSS")
                    GlobalVariables.api.postNotification(
                        GlobalVariables.userInfo.ID,
                        message,
                        dateTimeFormatter.format(LocalDateTime.now())
                    )
                }.start()

                while(true) {
                    if (count == GlobalVariables.subscribeList.size) {
                        isRunningSendNotification = false
                        GlobalVariables.functions.addSendedNotification(message)
                        if (activity != null) {
                            requireActivity().runOnUiThread(Runnable {
                                myAdapter.notifyDataSetChanged()
                                myLinearLayoutManager.scrollToPosition(
                                    myLinearLayoutManager.itemCount-1
                                )
                                Toast.makeText(activity, "送出訊息成功", Toast.LENGTH_SHORT).show()
                            })
                        }
                        break
                    }
                }
            }
        }

        Thread {
            while (true)
                if (GlobalVariables.sendedNotificationListIsReady)
                    break

            if (activity!= null) {
                requireActivity().runOnUiThread(Runnable {
                    root.recycler_sended_notification.apply {
                        setHasFixedSize(true)
                        layoutManager = myLinearLayoutManager
                        adapter = myAdapter
                    }
                    root.recycler_sended_notification.layoutManager!!.scrollToPosition(
                        root.recycler_sended_notification.layoutManager!!.itemCount-1
                    )
                })
            }

        }.start()

        return root
    }
}
