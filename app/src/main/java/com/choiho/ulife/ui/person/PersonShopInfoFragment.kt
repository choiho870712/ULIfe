package com.choiho.ulife.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_person_shop_info.view.*

/**
 * A simple [Fragment] subclass.
 */
class PersonShopInfoFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var myMapView: MapView

    private lateinit var root: View
    private var isRunningLoadSubList = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_person_shop_info, container, false)

        Thread {
            while (true)
                if (GlobalVariables.proposalUserInfoDoneLoading)
                    break

            if (activity != null) {
                requireActivity().runOnUiThread {
                    setUi()
                    setButton()

                    myMapView = root.findViewById(R.id.map_shopFragment) as MapView
                    myMapView.getMapAsync(this)
                    myMapView.onCreate(arguments)
                }
            }
        }.start()

        return root
    }

    override fun onStop() {
        super.onStop()
        GlobalVariables.uiController.openEditPersonButton(false)
        GlobalVariables.uiController.openLogoutButton(false)
        GlobalVariables.uiController.openSubscribeButton(false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(GlobalVariables.proposalUserInfo.latitude, GlobalVariables.proposalUserInfo.longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18.0f))
        myMapView.onResume()
    }

    private fun setUi() {
        if (activity != null)
            requireActivity().toolbar.setTitle(GlobalVariables.proposalUserInfo.name)
        root.text_selfIntroduction_shopFragment.text = GlobalVariables.proposalUserInfo.content
        root.text_tag_shopFragment.text = GlobalVariables.proposalUserInfo.getHashTagString()
        val icon = GlobalVariables.proposalUserInfo.getIconBitmap()
        if (icon != null)
            root.image_userIcon_shopFragment.setImageBitmap(icon)
    }

    private fun setButton() {
        var subscribeList = GlobalVariables.subscribeList

        if (GlobalVariables.proposalUserInfo.isMyAccount()) {
            GlobalVariables.uiController.openEditPersonButton(true)
            GlobalVariables.uiController.openLogoutButton(true)

            val followerCount = subscribeList.size
            val followerStringShow = followerCount.toString()
            root.text_followerCount_shopFragment.text = followerStringShow
        }
        else {
            if (GlobalVariables.proposalUserInfo.isSubscribeAvailable()) {
                GlobalVariables.uiController.openSubscribeButton(true)
            }

            Thread {
                subscribeList = GlobalVariables.api.getSubscribeList(GlobalVariables.proposalUserInfo.ID)
                val followerCount = subscribeList.size
                val followerStringShow = followerCount.toString()

                if (activity != null) {
                    requireActivity().runOnUiThread(Runnable {
                        root.text_followerCount_shopFragment.text = followerStringShow
                    })
                }
            }.start()
        }

        root.text_followerCount_shopFragment.setOnClickListener {
            if (!isRunningLoadSubList) {
                isRunningLoadSubList = true
                Thread {
                    GlobalVariables.friendList.clear()
                    for (i in 0 until(subscribeList).size) {
                        Thread{
                            GlobalVariables.friendList.add(GlobalVariables.api.getUserInfo(subscribeList[i].ID))
                        }.start()
                    }

                    while (GlobalVariables.friendList.size != subscribeList.size)
                        continue

                    GlobalVariables.needUnsubscribeButton = false

                    if (activity != null) {
                        requireActivity().runOnUiThread(Runnable {
                            GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
                                R.id.action_personShopInfoFragment_to_chatFriendListFragment)
                        })
                    }

                    isRunningLoadSubList = false
                }.start()
            }
        }
    }
}
