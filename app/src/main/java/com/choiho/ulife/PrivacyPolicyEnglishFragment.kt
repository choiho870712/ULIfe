package com.choiho.ulife


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_privacy_policy_english.view.*

/**
 * A simple [Fragment] subclass.
 */
class PrivacyPolicyEnglishFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_privacy_policy_english, container, false)

        GlobalVariables.uiController.openToolBar(true)

        val webViewClient = WebViewClient()
        val webView = root.webview_privacy_policy
        webView.setWebViewClient(webViewClient)
        webView.loadUrl("https://www.privacypolicies.com/privacy/view/1e0b8ae26b777e70b89638d6910124a7")

        return root
    }


}
