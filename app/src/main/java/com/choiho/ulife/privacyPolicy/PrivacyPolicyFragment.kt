package com.choiho.ulife.privacyPolicy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R

/**
 * A simple [Fragment] subclass.
 */
class PrivacyPolicyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_privacy_policy, container, false)
        GlobalVariables.toolBarController.openToolBar(true)
        return root
    }


}
