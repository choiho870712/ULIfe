package com.example.startupboard.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.startupboard.R

class ChatFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_chat, container, false)
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.container_chat_fragment, CardFragment(childFragmentManager), "chat_main")
        transaction.addToBackStack(null)
        transaction.commit()
        return root
    }
}