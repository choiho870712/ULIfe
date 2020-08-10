package com.choiho.ulife

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.edit_username_login_page

class LoginFragment : Fragment() {

    private var isRunningLogin = false
    lateinit var root:View
    private var status = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_login, container, false)
        root.page_login.visibility = View.GONE

        Thread {
            if (!GlobalVariables.api.isNetWorkConnecting(requireContext())) {
                if (activity!= null) {
                    requireActivity().runOnUiThread {
                        root.page_login.visibility = View.GONE
                        root.text_login_status.visibility = View.GONE
                        GlobalVariables.uiController.openNavView(false)
                        GlobalVariables.uiController.openToolBar(false)
                    }
                }
            }
            else {
                status = GlobalVariables.api.getServerStatusCode()
                if (activity!= null) {
                    requireActivity().runOnUiThread {
                        if (!status) {
                            root.page_login.visibility = View.GONE
                            root.text_login_status.visibility = View.VISIBLE
                            GlobalVariables.uiController.openNavView(false)
                            GlobalVariables.uiController.openToolBar(false)
                        }
                        else if (GlobalVariables.functions.readDataFromSQL()) goToMainPage()
                        else createPage()
                    }
                }
            }

        }.start()

        return root
    }

    private fun createPage() {
        root.page_login.visibility = View.VISIBLE
        setUi()
        setButtons()
    }

    private fun setUi() {
        GlobalVariables.uiController.openNavView(false)
        GlobalVariables.uiController.openToolBar(false)
    }

    private fun setButtons() {
        linkLoginToCreateAccountButton()
        linkLoginSubmitButton()
    }

    private fun linkLoginToCreateAccountButton() {
        root.button_create_new_account.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_createAccountFragment)
        )
    }

    private fun linkLoginSubmitButton() {
        root.button_login_submit.setOnClickListener {
            if (!isRunningLogin) {
                Thread {
                    isRunningLogin = true
                    login()
                    isRunningLogin = false
                }.start()
            }
        }
    }

    private fun login() {
        val id = root.edit_username_login_page.text.toString()
        val password = root.edit_password_login_page.text.toString()

        if (GlobalVariables.api.login(id, password, GlobalVariables.FCM_token)) {
            goToMainPage()
            GlobalVariables.functions.readDataFromApi(id)
            GlobalVariables.functions.writeDataToSQL()
            GlobalVariables.api.updateFCMToken(GlobalVariables.userInfo.ID, GlobalVariables.FCM_token)
        }
        else {
            if (activity != null) {
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(activity, "登入失敗", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    private fun goToMainPage() {
        if (activity != null) {
            requireActivity().runOnUiThread(Runnable {
                requireActivity().nav_host_fragment.findNavController().navigate(
                    R.id.action_loginFragment_to_mobile_navigation)
            })
        }
    }
}
