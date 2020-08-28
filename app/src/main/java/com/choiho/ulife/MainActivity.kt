package com.choiho.ulife

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    /*
    * TODO cost down "open app always call getAllFood api"
    * TODO cost down "open app always call getSubscribeList api"
    * TODO package pick image function
    * */

    private lateinit var navController:NavController

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setGlobalVariables()
        setUi()
        setButtons()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
    }

    // tool bar back button navigation
    override fun onSupportNavigateUp(): Boolean {
        return nav_host_fragment.findNavController().navigateUp()
    }

    private fun setGlobalVariables() {
        // reference this as context and activity in whole project
        // we use single activity + multiple fragment
        GlobalVariables.activity = this

        // create SQL database helper
        GlobalVariables.dbHelper = DBHelper(this)

        // create tool bar controller
        GlobalVariables.toolBarController.setToolBarButtonOnClickListener()

        // always reset proposal list when open the app
        // TODO cost down
        GlobalVariables.functions.resetProposalList()

        // always get fire base instance when open the app
        getFireBaseInstance()

        // initialize local date time zone
        AndroidThreeTen.init(GlobalVariables.activity)

        // check wifi every time
        createWifiConnectionChecker()

        // progress animation
        createProgressAnimationChecker()
    }

    private fun setUi() {
        navController = nav_host_fragment.findNavController()
        toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))
        toolbar.inflateMenu(R.menu.toolbar_menu)
        nav_view.setupWithNavController(navController)
    }

    private fun setButtons() {
        // linkMainPageToolbarButton()
    }

    private fun linkMainPageToolbarButton() {
        toolbar.button_toolbar_main.setOnClickListener {
            GlobalVariables.functions.navigate(
                R.id.action_navigation_home_to_homeMenuFragment)
        }
    }

    private fun getFireBaseInstance() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) return@OnCompleteListener
                GlobalVariables.FCM_token = task.result?.token.toString()
            })
    }

    private fun createWifiConnectionChecker() {
        Thread {
            var isReconnect = false
            while (true) {
                if (!GlobalVariables.functions.isNetWorkConnecting()) {
                    isReconnect = true
                    GlobalVariables.functions.popUpTo(R.id.login_navigation)
                    GlobalVariables.functions.makeToast("請確認 wifi 是否已開啟")
                }
                else if (isReconnect) {
                    // always reset proposal list when reconnect to wifi
                    // TODO cost down
                    GlobalVariables.functions.resetProposalList()

                    GlobalVariables.functions.popUpTo(R.id.login_navigation)
                    GlobalVariables.functions.makeToast("wifi 已開啟")
                    isReconnect = false
                }
                Thread.sleep(5000)
            }
        }.start()
    }

    private fun createProgressAnimationChecker() {
        Thread {
            while(true) {
                GlobalVariables.activity.runOnUiThread {
                    GlobalVariables.activity.progressBar.visibility =
                        if (GlobalVariables.taskCount > 0) View.VISIBLE else View.GONE
                }

                Thread.sleep(100)
            }
        }.start()
    }
}
