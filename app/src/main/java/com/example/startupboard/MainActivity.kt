package com.example.startupboard

import android.os.Bundle
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.startupboard.GlobalVariables.Companion.globalUserInfo
import com.example.startupboard.GlobalVariables.Companion.globalApi
import com.example.startupboard.GlobalVariables.Companion.globalUserInfoSetted

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login()
    }

    private fun runMainPage() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    private fun login() {
        val loginPageLayout = findViewById<LinearLayout>(R.id.page_login)
        val mainPageLayout = findViewById<RelativeLayout>(R.id.container)
        val createAccountPageLayout = findViewById<LinearLayout>(R.id.page_create_account)
        val uploadUserInfoPageLayout = findViewById<LinearLayout>(R.id.page_upload_user_info)

        val submitButton = findViewById<Button>(R.id.button_login_submit)
        val createAccountButton = findViewById<Button>(R.id.button_create_new_account)
        val submitCreateAccountButton = findViewById<Button>(R.id.button_submit_create_new_account)
        val submitUploadUserInfo = findViewById<Button>(R.id.button_submit_upload_user_info)

        submitButton.setOnClickListener {
            Thread {
                val userNameView = findViewById<EditText>(R.id.edit_username_login_page)
                val passwordView = findViewById<EditText>(R.id.edit_password_login_page)
                val id = userNameView.text.toString()
                val password = passwordView.text.toString()

                if (globalApi.login(id, password, id)) {
                    runOnUiThread(Runnable {
                        loginPageLayout.visibility = LinearLayout.INVISIBLE
                        runMainPage()
                        mainPageLayout.visibility = RelativeLayout.VISIBLE
                    })

                    globalUserInfo = globalApi.getUserInfo(id)
                    globalUserInfo.ID = id
                    globalUserInfoSetted = true
                }
            }.start()
        }

        createAccountButton.setOnClickListener {
            loginPageLayout.visibility = LinearLayout.INVISIBLE
            createAccountPageLayout.visibility = LinearLayout.VISIBLE
        }

        submitCreateAccountButton.setOnClickListener {
            Thread {
                val userNameView = findViewById<EditText>(R.id.edit_username_create_account_page)
                val passwordView = findViewById<EditText>(R.id.edit_password_create_account_page)
                val vertifyPasswordView = findViewById<EditText>(R.id.edit_vertify_password_create_account_page)
                val id = userNameView.text.toString()
                val password = passwordView.text.toString()
                val vertifyPassword = vertifyPasswordView.text.toString()

                if (password == vertifyPassword)
                    if (globalApi.createUser(id, password, id)) {
                        runOnUiThread(Runnable {
                            createAccountPageLayout.visibility = LinearLayout.INVISIBLE
                            uploadUserInfoPageLayout.visibility = LinearLayout.VISIBLE
                        })

                        globalUserInfo.ID = id
                    }
            }.start()
        }

        submitUploadUserInfo.setOnClickListener {
            Thread {
                val userNameView = findViewById<EditText>(R.id.edit_username_upload_user_info)
                val userName = userNameView.text.toString()
                val iconString = "nil"

                if (globalApi.uploadUserInfo(globalUserInfo.ID, iconString, userName)) {
                    runOnUiThread(Runnable {
                        uploadUserInfoPageLayout.visibility = LinearLayout.INVISIBLE
                        loginPageLayout.visibility = LinearLayout.VISIBLE
                    })
                }
            }.start()
        }
    }
}
