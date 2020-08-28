package com.choiho.ulife.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_create_account.view.*

class CreateAccountFragment : Fragment() {

    private var lockCreateAccountButton = false
    private lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_create_account, container, false)
        GlobalVariables.toolBarController.openToolBar(false)
        setButtons()
        return root
    }

    private fun setButtons() {
        linkCreateAccountSubmitButton()
        root.text_privacy_policy_chinese.setOnClickListener {
            GlobalVariables.functions.navigate(
                R.id.action_createAccountFragment_to_privacyPolicyChineseFragment
            )
        }
    }

    private fun linkCreateAccountSubmitButton() {
        root.button_submit_create_new_account.setOnClickListener {
            if (!lockCreateAccountButton) {
                Thread {
                    lockCreateAccountButton = true
                    GlobalVariables.taskCount++
                    createAccount()
                    lockCreateAccountButton = false
                    GlobalVariables.taskCount--
                }.start()
            }
        }
    }

    private fun createAccount() {
        val id = root.edit_username_create_account_page.text.toString()
        val password = root.edit_password_create_account_page.text.toString()
        val twicePassword = root.edit_vertify_password_create_account_page.text.toString()

        if (!root.checkbox_privacy_policy.isChecked)
            GlobalVariables.functions.makeToast("請詳閱隱私權條款")
        else if (!isLettersOrDigits(id))
            GlobalVariables.functions.makeToast("帳號必須為英文字母或數字")
        else if (password.length < 6 || password.length > 16)
            GlobalVariables.functions.makeToast("密碼長度必須在 6~20 之間")
        else if (password != twicePassword)
            GlobalVariables.functions.makeToast("密碼驗證失敗")
        else if (!GlobalVariables.api.createUser(id, password, GlobalVariables.FCM_token))
            GlobalVariables.functions.makeToast("註冊失敗")
        else {
            if (activity != null) requireActivity().runOnUiThread{
                GlobalVariables.functions.navigate(
                    R.id.action_createAccountFragment_to_makeUserInfoFragment
                )
            }

            GlobalVariables.userInfo.ID = id
        }
    }

    private fun isLettersOrDigits(chars: String): Boolean {
        for (c in chars) {
            if (c !in 'A'..'Z' && c !in 'a'..'z' && c !in '0'..'9') {
                return false
            }
        }
        return true
    }
}
