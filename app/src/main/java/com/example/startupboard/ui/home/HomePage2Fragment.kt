package com.example.startupboard.ui.home


import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.startupboard.BuildConfig
import com.example.startupboard.GlobalVariables

import com.example.startupboard.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home_page1.view.*
import kotlinx.android.synthetic.main.fragment_home_page2.*
import kotlinx.android.synthetic.main.fragment_home_page2.view.*
import kotlinx.android.synthetic.main.fragment_home_page2.view.map

/**
 * A simple [Fragment] subclass.
 */
class HomePage2Fragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var myMapView: MapView

    private var isRunningSubscribe = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home_page2, container, false)

        Thread {
            while (true)
                if (GlobalVariables.proposalUserInfoDoneLoading)
                    break

            if (isSubscribeAvailable(GlobalVariables.proposalUserInfo!!.ID)) {
                activity!!.runOnUiThread(Runnable {
                    val buttonAddFriend = activity!!.toolbar.menu.findItem(R.id.button_add_friend)
                    buttonAddFriend.setEnabled(true)
                    buttonAddFriend.setVisible(true)
                })

                if (activity != null) {
                    activity!!.toolbar.setOnMenuItemClickListener{
                        when (it.itemId) {
                            R.id.button_add_friend -> {
                                if (!isRunningSubscribe) {
                                    isRunningSubscribe = true
                                    if (GlobalVariables.api.subscribe(GlobalVariables.proposalUserInfo!!.ID, GlobalVariables.userInfo.ID)) {
                                        activity!!.runOnUiThread(Runnable {
                                            Toast.makeText(activity, "訂閱成功", Toast.LENGTH_SHORT).show()
                                        })
                                    }
                                    else {
                                        activity!!.runOnUiThread(Runnable {
                                            Toast.makeText(activity, "訂閱失敗", Toast.LENGTH_SHORT).show()
                                        })
                                    }
                                    isRunningSubscribe = false
                                }

                                true
                            }
                            else -> super.onOptionsItemSelected(it)
                        }
                    }
                }
            }
        }.start()

        Thread {
            while (true)
                if (GlobalVariables.proposalUserInfoDoneLoading)
                    break

            if (activity != null) {
                activity!!.runOnUiThread(Runnable {
                    activity!!.toolbar.setTitle(GlobalVariables.proposalUserInfo!!.name)
                    val icon = GlobalVariables.proposalUserInfo!!.getIconBitmap()
                    if (icon != null)
                        root.image_head_home_page2.setImageBitmap(icon)
                    root.text_selfIntroduction_home_page2.text = GlobalVariables.proposalUserInfo!!.content
                    root.text_tag_home_page2.text = GlobalVariables.proposalUserInfo!!.getHashTagString()

                    myMapView = root.findViewById(R.id.map) as MapView
                    myMapView.getMapAsync(this)
                    myMapView.onCreate(arguments)
                })
            }

            val followerString = GlobalVariables.proposalUserScribeList.size.toString() + "\n訂閱"
            activity!!.runOnUiThread(Runnable {
                root.text_followerCount_home_page2.text = followerString
            })
        }.start()

        root.button_toPage1_home_page2.setOnClickListener {
            activity!!.nav_host_fragment.findNavController().navigate(R.id.action_homePage2Fragment_to_homePage1Fragment)
        }

        return root
    }

    override fun onStop() {
        super.onStop()
        val buttonAddFriend = activity!!.toolbar.menu.findItem(R.id.button_add_friend)
        buttonAddFriend.setEnabled(false)
        buttonAddFriend.setVisible(false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(GlobalVariables.proposalUserInfo!!.latitude, GlobalVariables.proposalUserInfo!!.longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18.0f))
        myMapView.onResume()
    }

    private fun isSubscribeAvailable(id:String):Boolean {
        if (GlobalVariables.userInfo.ID == id) return false
        for (i in 0 until(GlobalVariables.proposalUserScribeList.size)) {
            if (GlobalVariables.proposalUserScribeList[i].ID == GlobalVariables.userInfo.ID) return false
        }
        return true
    }
}
