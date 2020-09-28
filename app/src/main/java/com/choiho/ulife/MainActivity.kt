package com.choiho.ulife

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import me.leolin.shortcutbadger.ShortcutBadger

class MainActivity : AppCompatActivity() {

    private lateinit var navController:NavController

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    private lateinit var appUpdateManager: AppUpdateManager
    private val MY_REQUEST_CODE = 8

    private fun checkUpdate() {
        // Returns an intent object that you use to check for an update.

        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Before starting an update, register a listener for updates.
        appUpdateManager.registerListener(listener!!)


        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        Log.d(">>>>>>", "Checking for updates")
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                Log.d(">>>>>>", "Update available")

                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MY_REQUEST_CODE)
            }
            else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                showSnackBarForCompleteUpdate()
            }
            else {
                Log.d(">>>>>>", "No Update available")
            }
        }
    }



    private val listener: InstallStateUpdatedListener? = InstallStateUpdatedListener { installState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            Log.d(">>>>>>", "An update has been downloaded")
            showSnackBarForCompleteUpdate()
        }
    }

    private fun showSnackBarForCompleteUpdate() {
        val snackbar: Snackbar = Snackbar.make(
            findViewById(R.id.main_layout),
            "New app is ready!",
            Snackbar.LENGTH_INDEFINITE
        )

        snackbar.setAction("Install", { view ->
            appUpdateManager.completeUpdate()
            appUpdateManager.unregisterListener(listener!!)
        })

        snackbar.setActionTextColor(resources.getColor(R.color.colorPrimaryDark))
        snackbar.show()
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->

                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        IMMEDIATE,
                        this,
                        MY_REQUEST_CODE
                    );
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MY_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(">>>>>>", "" + "Result Ok")
                    //  handle user's approval }
                }
                Activity.RESULT_CANCELED -> {
                    {
//if you want to request the update again just call checkUpdate()
                    }
                    Log.d(">>>>>>", "" + "Result Cancelled")
                    //  handle user's rejection  }
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    //if you want to request the update again just call checkUpdate()
                    Log.d(">>>>>>", "" + "Update Failure")
                    //  handle update failure
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setGlobalVariables()

        checkUpdate()

        val badgeCount = 1
        ShortcutBadger.applyCount(this, badgeCount)

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

        GlobalVariables.functions.refreshRandomChance()
    }

    private fun setUi() {
        navController = nav_host_fragment.findNavController()
        toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))
        toolbar.inflateMenu(R.menu.toolbar_menu)
        nav_view.setupWithNavController(navController)
    }

    private fun setButtons() {
         linkMainPageToolbarButton()
    }

    private fun linkMainPageToolbarButton() {
        toolbar.button_toolbar_main.setOnClickListener {
            GlobalVariables.functions.navigate(
                R.id.navigation_home,
                R.id.action_navigation_home_to_homeMenuFragment
            )
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
            while (true) {

                if (!GlobalVariables.functions.isNetWorkConnecting()) {
                    runOnUiThread {
                        text_login_status.text = "請確認網路"
                        layout_login_status.visibility = View.VISIBLE
                    }
                }
                else {
                    runOnUiThread {
                        layout_login_status.visibility = View.GONE
                    }
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

                Thread.sleep(500)
            }
        }.start()
    }
}
