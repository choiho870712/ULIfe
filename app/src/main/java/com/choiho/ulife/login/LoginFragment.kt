package com.choiho.ulife.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.edit_username_login_page

class LoginFragment : Fragment() {

    private var lockLoginButton = false
    lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_login, container, false)

        root.page_login.visibility = View.GONE
        Thread {
            GlobalVariables.taskCount++
            while (!GlobalVariables.functions.isNetWorkConnecting()) {
                showStatusMaskPage()
                if (activity!= null) requireActivity().runOnUiThread {
                    root.text_login_status.text = "請確認網路"
                }
                Thread.sleep(5000)
            }

            if (activity!= null) requireActivity().runOnUiThread {
                root.text_login_status.visibility = View.GONE
            }

            if (!GlobalVariables.api.getServerStatusCode()) {
                showStatusMaskPage()
                if (activity!= null) requireActivity().runOnUiThread {
                    root.text_login_status.text = "系統維修中"
                }
            }
            else if (GlobalVariables.functions.loginFromSQL()) goToMainPage()
            else createPage()
            GlobalVariables.taskCount--
        }.start()

        return root
    }

    private fun showStatusMaskPage() {
        GlobalVariables.toolBarController.openNavView(false)
        GlobalVariables.toolBarController.openToolBar(false)

        if (activity!= null) requireActivity().runOnUiThread {
            root.text_login_status.visibility = View.VISIBLE
        }
    }

    private fun createPage() {
        setUi()
        setButtons()

        if (activity!= null) requireActivity().runOnUiThread {
            root.text_login_status.visibility = View.GONE
            root.page_login.visibility = View.VISIBLE
        }
    }

    private fun setUi() {
        GlobalVariables.toolBarController.openNavView(false)
        GlobalVariables.toolBarController.openToolBar(false)
    }

    private fun setButtons() {
        linkLoginToCreateAccountButton()
        linkLoginSubmitButton()
    }

    private fun linkLoginToCreateAccountButton() {
        if (activity!= null) requireActivity().runOnUiThread {
            root.button_create_new_account.setOnClickListener {
                GlobalVariables.functions.navigate(
                    R.id.action_loginFragment_to_createAccountFragment)
            }
        }
    }

    private fun linkLoginSubmitButton() {
        if (activity!= null) requireActivity().runOnUiThread {
            root.button_login_submit.setOnClickListener {
                if (!lockLoginButton) {
                    Thread {
                        lockLoginButton = true
                        GlobalVariables.taskCount++
                        login()
                        GlobalVariables.taskCount--
                        lockLoginButton = false
                    }.start()
                }
            }
        }
    }

    private fun login() {
        val id = root.edit_username_login_page.text.toString()
        val password = root.edit_password_login_page.text.toString()

        if (GlobalVariables.api.login(id, password, GlobalVariables.FCM_token)) {
            goToMainPage()
            GlobalVariables.functions.loginFromApi(id)
        }
        else GlobalVariables.functions.makeToast("登入失敗")
    }

    private fun goToMainPage() {
        if (activity != null) requireActivity().runOnUiThread {
            GlobalVariables.activity.nav_host_fragment.
                findNavController().setGraph(R.navigation.login_navigation)
            GlobalVariables.functions.navigate(
                R.id.action_loginFragment_to_mobile_navigation
            )
        }
    }
}
