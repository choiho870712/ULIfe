package com.example.startupboard.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.startupboard.GlobalVariables

import com.example.startupboard.R
import com.example.startupboard.ui.api.UserInfo
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
    private lateinit var userinfo: UserInfo
    private lateinit var subscribeList: ArrayList<UserInfo>

    private var isRunningSubscribe = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_person_shop_info, container, false)

        val myBundle = arguments
        if (myBundle != null) {

            userinfo = myBundle.getParcelable<UserInfo>("userInfo")!!

            activity!!.toolbar.setTitle(userinfo.name)
            root.text_selfIntroduction_shopFragment.text = userinfo.content
            root.text_tag_shopFragment.text = userinfo.getHashTagString()
            val icon = userinfo.getIconBitmap()
            if (icon != null)
                root.image_userIcon_shopFragment.setImageBitmap(icon)

            if (GlobalVariables.userInfo.ID == userinfo.ID) {
                val buttonEdit = activity!!.toolbar.menu.findItem(R.id.button_edit_person)
                buttonEdit.setEnabled(true)
                buttonEdit.setVisible(true)
                activity!!.toolbar.setOnMenuItemClickListener{
                    when (it.itemId) {
                        R.id.button_edit_person -> {
                            activity!!.nav_host_fragment.findNavController().navigate(R.id.action_personShopInfoFragment_to_personEditFragment)
                            true
                        }
                        else -> super.onOptionsItemSelected(it)
                    }
                }

                val followerString = GlobalVariables.subscribeList.size.toString() + "\n訂閱"
                root.text_followerCount_shopFragment.text = followerString
            }
            else {
                Thread {
                    subscribeList = GlobalVariables.api.getSubscribeList(userinfo.ID)
                    val followerString = subscribeList.size.toString() + "\n訂閱"
                    activity!!.runOnUiThread(Runnable {
                        root.text_followerCount_shopFragment.text = followerString
                    })

                    if (isSubscribeAvailable(userinfo.ID)) {
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
                                            if (GlobalVariables.api.subscribe(userinfo.ID, GlobalVariables.userInfo.ID)) {
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
            }
        }

        myMapView = root.findViewById(R.id.map_shopFragment) as MapView
        myMapView.getMapAsync(this)
        myMapView.onCreate(arguments)

        return root
    }

    override fun onStop() {
        super.onStop()
        val buttonEdit = activity!!.toolbar.menu.findItem(R.id.button_edit_person)
        buttonEdit.setEnabled(false)
        buttonEdit.setVisible(false)

        val buttonAddFriend = activity!!.toolbar.menu.findItem(R.id.button_add_friend)
        buttonAddFriend.setEnabled(false)
        buttonAddFriend.setVisible(false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(userinfo.latitude, userinfo.longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18.0f))
        myMapView.onResume()
    }

    private fun isSubscribeAvailable(id:String):Boolean {
        if (GlobalVariables.userInfo.ID == id) return false
        for (i in 0 until(subscribeList.size)) {
            if (subscribeList[i].ID == GlobalVariables.userInfo.ID ) return false
        }
        return true
    }
}
