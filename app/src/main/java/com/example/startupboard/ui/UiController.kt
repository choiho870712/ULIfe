package com.example.startupboard.ui

import android.view.View
import com.example.startupboard.GlobalVariables
import com.example.startupboard.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class UiController {
    fun openToolbarBackButton(enable: Boolean) {
        if (!enable) GlobalVariables.activity.toolbar.setNavigationIcon(null)
    }

    fun openToolbarMainButton(enable: Boolean) {
        if (enable) GlobalVariables.activity.toolbar.button_toolbar_main.visibility = View.VISIBLE
        else GlobalVariables.activity.toolbar.button_toolbar_main.visibility = View.GONE
    }

    fun openNavView(enable: Boolean) {
        if (enable) GlobalVariables.activity.nav_view.visibility = View.VISIBLE
        else GlobalVariables.activity.nav_view.visibility = View.GONE
    }

    fun openToolBar(enable: Boolean) {
        if (enable) GlobalVariables.activity.toolbar.visibility = View.VISIBLE
        else GlobalVariables.activity.toolbar.visibility = View.GONE
    }

    fun setMenuName() {
        when(GlobalVariables.homeMenuChoose) {
            "food" -> {
                GlobalVariables.activity.toolbar.text_toolbar_main.text = "美食"
                GlobalVariables.activity.image_toolbar_main.setImageResource(R.drawable.chicken_m)
            }
            "game" -> {
                GlobalVariables.activity.toolbar.text_toolbar_main.text = "玩樂"
                GlobalVariables.activity.image_toolbar_main.setImageResource(R.drawable.disco_ball_m)
            }
            "event" -> {
                GlobalVariables.activity.toolbar.text_toolbar_main.text = "校園活動"
                GlobalVariables.activity.image_toolbar_main.setImageResource(R.drawable.price_m)

            }
            else -> {
                GlobalVariables.activity.toolbar.text_toolbar_main.text = "錯誤"
                GlobalVariables.activity.image_toolbar_main.setImageResource(R.drawable.chicken_m)
            }
        }
    }

    fun setNavViewButton() {
        val navView = GlobalVariables.activity.nav_view
//        when(navView.selectedItemId)
//        {
//            R.id.navigation_home ->
//            {
//                navView.menu.findItem(R.id.navigation_home).setEnabled(false)
//                navView.menu.findItem(R.id.navigation_chat).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_notifications).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_person).setEnabled(true)
//            }
//            R.id.navigation_chat ->
//            {
//                navView.menu.findItem(R.id.navigation_home).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_chat).setEnabled(false)
//                navView.menu.findItem(R.id.navigation_notifications).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_person).setEnabled(true)
//            }
//            R.id.navigation_notifications ->
//            {
//                navView.menu.findItem(R.id.navigation_home).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_chat).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_notifications).setEnabled(false)
//                navView.menu.findItem(R.id.navigation_person).setEnabled(true)
//            }
//            R.id.navigation_person ->
//            {
//                navView.menu.findItem(R.id.navigation_home).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_chat).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_notifications).setEnabled(true)
//                navView.menu.findItem(R.id.navigation_person).setEnabled(false)
//            }
//        }
    }
}