package com.choiho.ulife


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_account.view.*

class CreateAccountFragment : Fragment() {

    private var isRunningCreateAccount = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_create_account, container, false)
        GlobalVariables.uiController.openToolBar(false)
        setButtons(root)
        return root
    }

    private fun setButtons(root:View) {
        linkCreateAccountSubmitButton(root)

        root.text_privacy_policy_english.setOnClickListener {
            requireActivity().nav_host_fragment.findNavController().navigate(
                R.id.action_createAccountFragment_to_privacyPolicyEnglishFragment)
        }

        root.text_privacy_policy_chinese.setOnClickListener {
            requireActivity().nav_host_fragment.findNavController().navigate(
                R.id.action_createAccountFragment_to_privacyPolicyChineseFragment)
        }
    }

    private fun linkCreateAccountSubmitButton(root:View) {
        root.button_submit_create_new_account.setOnClickListener {
            if (!root.checkbox_privacy_policy.isChecked) {
                Toast.makeText(activity, "請詳閱隱私權條款", Toast.LENGTH_SHORT).show()
            }
            else if (!isRunningCreateAccount) {

                Thread {

                    isRunningCreateAccount = true

                    val id = root.edit_username_create_account_page.text.toString()
                    val password = root.edit_password_create_account_page.text.toString()
                    val vertifyPassword = root.edit_vertify_password_create_account_page.text.toString()

                    if (password == vertifyPassword) {
                        if (GlobalVariables.api.createUser(id, password, GlobalVariables.FCM_token)) {
                            if (activity != null) {
                                requireActivity().runOnUiThread(Runnable {
                                    requireActivity().nav_host_fragment.findNavController().navigate(
                                        R.id.action_createAccountFragment_to_makeUserInfoFragment)
                                })
                            }

                            GlobalVariables.userInfo.ID = id
                        }
                        else {
                            if (activity != null) {
                                requireActivity().runOnUiThread(Runnable {
                                    Toast.makeText(activity, "註冊失敗", Toast.LENGTH_SHORT).show()
                                })
                            }
                        }
                    }
                    else {
                        if (activity != null) {
                            requireActivity().runOnUiThread(Runnable {
                                Toast.makeText(activity, "密碼驗證失敗", Toast.LENGTH_SHORT).show()
                            })
                        }
                    }

                    isRunningCreateAccount = false
                }.start()
            }
        }
    }
}
