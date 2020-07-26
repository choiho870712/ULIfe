package com.example.startupboard

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.startupboard.ui.UiController
import com.example.startupboard.ui.api.Api
import com.example.startupboard.ui.api.UserInfo
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_home_page2.view.*
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setGlobalVariables()
        setUi()
        setButtons()

    }

    override fun onSupportNavigateUp(): Boolean {
        return nav_host_fragment.findNavController().navigateUp()
    }

    private fun setGlobalVariables() {
        GlobalVariables.activity = this
        GlobalVariables.userInfo = UserInfo("","","","","", "", mutableListOf(), 0.0, 0.0)
        GlobalVariables.uiController = UiController()
        GlobalVariables.api = Api()
        GlobalVariables.homeMenuChoose = "food"
        GlobalVariables.homeAreaChoose = "Zhongli"

        Thread {
            while (true) {
                if (!GlobalVariables.api.isNetWorkConnecting(this)) {
                    runOnUiThread(Runnable {
                        Toast.makeText(this, "請確認 wifi 是否開啟", Toast.LENGTH_SHORT).show()
                    })
                }
                Thread.sleep(5000)
            }
        }.start()

        if (GlobalVariables.api.isNetWorkConnecting(this)) {
            Thread {
                GlobalVariables.homeProposalList = GlobalVariables.api.getFoodAll(1, GlobalVariables.homeAreaChoose)
                GlobalVariables.homePageProposalCount = 10
            }.start()
        }
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
            //nav_host_fragment.findNavController().navigate(R.id.action_navigation_home_to_homeMenuFragment)
        }
    }
}
