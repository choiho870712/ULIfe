package com.choiho.ulife

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidThreeTen.init(this)

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) return@OnCompleteListener
                GlobalVariables.FCM_token = task.result?.token.toString()
            })

        setGlobalVariables()
        setUi()
        setButtons()
    }

    override fun onSupportNavigateUp(): Boolean {
        return nav_host_fragment.findNavController().navigateUp()
    }

    private fun setGlobalVariables() {
        GlobalVariables.activity = this
        GlobalVariables.dbHelper = DBHelper(this)
        GlobalVariables.uiController.setToolBarButtonOnClickListener()

        Thread {
            var isReconnect = false
            while (true) {
                if (!GlobalVariables.api.isNetWorkConnecting(this)) {
                    isReconnect = true

                    runOnUiThread(Runnable {
                        GlobalVariables.activity.nav_host_fragment.findNavController().navigate(R.id.login_navigation)
                        Toast.makeText(this, "請確認 wifi 是否開啟", Toast.LENGTH_SHORT).show()
                    })
                }
                else {
                    if (isReconnect) {
                        runOnUiThread(Runnable {
                            GlobalVariables.activity.nav_host_fragment.findNavController().navigate(R.id.login_navigation)
                            Toast.makeText(this, "wifi 已開啟", Toast.LENGTH_SHORT).show()

                            GlobalVariables.homeProposalList = GlobalVariables.api.getFoodAll(1, GlobalVariables.homeAreaChoose)
                            GlobalVariables.homePageProposalCount = 10
                        })

                        isReconnect = false
                    }
                }
                Thread.sleep(5000)
            }
        }.start()

        Thread {
            if (GlobalVariables.api.isNetWorkConnecting(this)) {
                GlobalVariables.homeProposalList = GlobalVariables.api.getFoodAll(1, GlobalVariables.homeAreaChoose)
                GlobalVariables.homePageProposalCount = 10
            }
        }.start()
    }

    private fun setUi() {
        val navController = nav_host_fragment.findNavController()
        toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))
        toolbar.inflateMenu(R.menu.toolbar_menu)
        nav_view.setupWithNavController(navController)
    }

    private fun setButtons() {
        linkMainPageToolbarButton()
    }

    private fun linkMainPageToolbarButton() {
        toolbar.button_toolbar_main.setOnClickListener {
            nav_host_fragment.findNavController().navigate(R.id.action_navigation_home_to_homeMenuFragment)
        }
    }
}
