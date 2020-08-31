package com.choiho.ulife.navigationUI.home


import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_home_page1.view.*
import kotlinx.android.synthetic.main.fragment_report.view.*

/**
 * A simple [Fragment] subclass.
 */
class HomePage1Fragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var myMapView: MapView

    private lateinit var root:View

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_home_page1, container, false)

        if (GlobalVariables.proposal != null) {

            Thread {
                GlobalVariables.taskCount++
                while (!GlobalVariables.proposalUserInfo.isReady) continue

                GlobalVariables.taskCount--


                if (activity != null) requireActivity().runOnUiThread {
                    GlobalVariables.toolBarController.openGoToShopInfoBottom(true)
                    if (GlobalVariables.proposalUserInfo.isSubscribeAvailable())
                        GlobalVariables.toolBarController.openSubscribeButton(true)
                    else if (GlobalVariables.proposalUserInfo.isMyAccount())
                        GlobalVariables.toolBarController.openAddProposalButton(true)
                    else
                        GlobalVariables.toolBarController.openUnSubscribeButton(true)

                    setInfo()

                    val icon = GlobalVariables.proposalUserInfo.getIconBitmap()
                    if (icon != null)
                        root.image_userIcon_home_page1.setImageBitmap(icon)

                    GlobalVariables.proposalItemAdapter = CardProposalItemAdapter(
                        GlobalVariables.proposal!!.proposalItemList,
                        root
                    )

                    if (activity != null) {
                        requireActivity().runOnUiThread(Runnable {
                            root.recycler_proposal_item.apply {
                                setHasFixedSize(true)
                                layoutManager = GridLayoutManager(activity, 3)
                                adapter = GlobalVariables.proposalItemAdapter
                            }

                            myMapView = root.findViewById(R.id.map_home_page1) as MapView
                            myMapView.getMapAsync(this)
                            myMapView.onCreate(arguments)
                        })
                    }

                    root.image_proposal.setOnClickListener {
                        if (root.image_proposal.visibility == View.VISIBLE) {
                            GlobalVariables.toolBarController.openDeleteItemButton(false)
                            root.image_proposal.visibility = View.GONE
                            setInfo()
                        }
                    }

                    root.button_complaint_shop_home_page1.setOnClickListener {
                        GlobalVariables.report_shop_id = GlobalVariables.proposalUserInfo.ID
                        GlobalVariables.functions.navigate(
                            R.id.action_homePage1Fragment_to_reportFragment)
                    }

                    root.map_home_page1.visibility = View.VISIBLE
                    root.button_complaint_shop_home_page1.visibility = View.VISIBLE
                }
            }.start()
        }

        return root
    }

    private fun setInfo() {
        root.text_author_home_page1.text = GlobalVariables.proposal!!.name
        root.text_content_home_page1.text = GlobalVariables.proposal!!.content

        root.text_title_home_page1.visibility = View.GONE
        root.text_date_home_page1.visibility = View.GONE
        root.text_tag_home_page1.visibility = View.GONE

    }

    override fun onStop() {
        super.onStop()
        GlobalVariables.toolBarController.openGoToShopInfoBottom(false)
        GlobalVariables.toolBarController.openDeleteItemButton(false)
        GlobalVariables.toolBarController.openSubscribeButton(false)
        GlobalVariables.toolBarController.openUnSubscribeButton(false)
        GlobalVariables.toolBarController.openAddProposalButton(false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ContextCompat.checkSelfPermission(
                GlobalVariables.activity, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
            mMap.isMyLocationEnabled = true

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(GlobalVariables.proposalUserInfo.latitude, GlobalVariables.proposalUserInfo.longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title(GlobalVariables.proposalUserInfo.name)).showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f))
        myMapView.onResume()
    }
}
