package com.example.startupboard.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.startupboard.R

class NotificationsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.container_notifications_fragment, CardFragment(childFragmentManager),
            "notifications_main")
        transaction.addToBackStack(null)
        transaction.commit()
        return root
    }
}