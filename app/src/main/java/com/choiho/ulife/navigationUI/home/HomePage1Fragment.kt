package com.choiho.ulife.navigationUI.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

/**
 * A simple [Fragment] subclass.
 */
class HomePage1Fragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var myMapView: MapView

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home_page1, container, false)

        if (GlobalVariables.proposal != null) {

            Thread {
                GlobalVariables.taskCount++
                while (!GlobalVariables.proposalUserInfo.isReady) continue

                if (activity != null) requireActivity().runOnUiThread {
                    GlobalVariables.toolBarController.openGoToShopInfoBottom(true)
                    if (GlobalVariables.proposalUserInfo.isSubscribeAvailable())
                        GlobalVariables.toolBarController.openSubscribeButton(true)

                    root.text_author_home_page1.text = GlobalVariables.proposal!!.name
                    val itemSelect = GlobalVariables.proposal!!.proposalItemList[0]

                    root.text_title_home_page1.text = itemSelect.title
                    root.text_date_home_page1.text = itemSelect.date
                    root.text_tag_home_page1.text = itemSelect.getHashTagString()
                    root.text_content_home_page1.text = itemSelect.content

                    val icon = GlobalVariables.proposalUserInfo.getIconBitmap()
                    if (icon != null)
                        root.image_userIcon_home_page1.setImageBitmap(icon)

                    if (activity != null) {
                        requireActivity().runOnUiThread(Runnable {
                            root.recycler_proposal_item.apply {
                                setHasFixedSize(true)
                                layoutManager = GridLayoutManager(activity, 3)
                                adapter = CardProposalItemAdapter(
                                    GlobalVariables.proposal!!.proposalItemList,
                                    root
                                )
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
                        }
                    }
                }

                GlobalVariables.taskCount--
            }.start()
        }

        return root
    }

    override fun onStop() {
        super.onStop()
        GlobalVariables.toolBarController.openGoToShopInfoBottom(false)
        GlobalVariables.toolBarController.openDeleteItemButton(false)
        GlobalVariables.toolBarController.openSubscribeButton(false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(GlobalVariables.proposalUserInfo.latitude, GlobalVariables.proposalUserInfo.longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f))
        myMapView.onResume()
    }
}
