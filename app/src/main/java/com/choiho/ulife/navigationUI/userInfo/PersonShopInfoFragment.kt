package com.choiho.ulife.navigationUI.userInfo


import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_person_shop_info.view.*

/**
 * A simple [Fragment] subclass.
 */
class PersonShopInfoFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var myMapView: MapView

    private var isRunningLoadSubList = false
    private lateinit var root: View

    private lateinit var rankList: ArrayList<Int>
    private var rankListIsReady = false
    private var selectRank = 0
    private var isScoredRank = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_person_shop_info, container, false)

        GlobalVariables.activity.nav_host_fragment
            .findNavController()
            .addOnDestinationChangedListener { _, _, _ ->
                GlobalVariables.toolBarController.openEditPersonButton(false)
                GlobalVariables.toolBarController.openLogoutButton(false)
                GlobalVariables.toolBarController.openSubscribeButton(false)
                GlobalVariables.toolBarController.openUnSubscribeButton(false)

                GlobalVariables.activity.nav_host_fragment
                    .findNavController()
                    .addOnDestinationChangedListener { _, _, _ ->

                    }
            }

        Thread {
            GlobalVariables.taskCount++
            while (!GlobalVariables.proposalUserInfo.isReady)
                Thread.sleep(500)

            if (activity != null) requireActivity().runOnUiThread {
                setUi()
                setButton()

                myMapView = root.findViewById(R.id.map_shopFragment) as MapView
                myMapView.getMapAsync(this)
                myMapView.onCreate(arguments)
            }

            GlobalVariables.taskCount--
        }.start()

        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ContextCompat.checkSelfPermission(
                GlobalVariables.activity, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            mMap.isMyLocationEnabled = true


            // Add a marker in Sydney and move the camera
            val sydney = LatLng(GlobalVariables.proposalUserInfo.latitude, GlobalVariables.proposalUserInfo.longitude)
            mMap.addMarker(MarkerOptions().position(sydney).title(GlobalVariables.proposalUserInfo.name)).showInfoWindow()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f))


            mMap.setOnMyLocationChangeListener { arg0 ->

                val myLocation = LatLng(arg0.latitude, arg0.longitude)
                GlobalVariables.api.getGoogleMapPath(sydney, myLocation)

                val options = PolylineOptions()
                options.color(Color.RED)
                options.width(5f)
            }


//            Thread {
//                val result = URL(url).readText()
//                if (activity!= null) requireActivity().runOnUiThread {
//                    val parser: Parser = Parser()
//                    val stringBuilder: StringBuilder = StringBuilder(result)
//                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
//
//                    val routes = json.array<JsonObject>("routes")
//                    val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
//
//                    val polypts = points.map { it.obj("polyline")?.string("points")!!  }
//                    val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!)
//                    }
//
//                    //polyline
//                    options.add(sydney)
//                    for (point in polypts) options.add(point)
//                    options.add(myLocation)
//                    mMap!!.addPolyline(options)
//
//                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
//                }
//            }.start()

            myMapView.onResume()
        }
        else {
            // Add a marker in Sydney and move the camera
            val sydney = LatLng(GlobalVariables.proposalUserInfo.latitude, GlobalVariables.proposalUserInfo.longitude)
            mMap.addMarker(MarkerOptions().position(sydney).title(GlobalVariables.proposalUserInfo.name)).showInfoWindow()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f))
            myMapView.onResume()
        }
    }

    private fun getURL(from : LatLng, to : LatLng) : String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
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
        if (GlobalVariables.proposalUserInfo.isMyAccount()) {
            GlobalVariables.toolBarController.openEditPersonButton(true)

            val followerCount = GlobalVariables.subscribeList.size
            val followerStringShow = followerCount.toString()
            root.text_followerCount_shopFragment.text = followerStringShow

//            root.text_followerCount_shopFragment.setOnClickListener {
//                if (!isRunningLoadSubList) {
//                    isRunningLoadSubList = true
//                    Thread {
//                        GlobalVariables.otherUserInfoList = GlobalVariables.subscribeList
//                        GlobalVariables.needUnsubscribeButton = false
//
//                        if (activity != null) requireActivity().runOnUiThread{
//                            GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
//                                R.id.action_personShopInfoFragment_to_chatFriendListFragment)
//                        }
//
//                        isRunningLoadSubList = false
//                    }.start()
//                }
//            }
        }
        else {
            if (GlobalVariables.proposalUserInfo.isSubscribeAvailable()) {
                GlobalVariables.toolBarController.openSubscribeButton(true)
            }
            else
                GlobalVariables.toolBarController.openUnSubscribeButton(true)

            Thread {
                val subscribeStringList = GlobalVariables.api.getSubscribeList(GlobalVariables.proposalUserInfo.ID)
                val followerCount = subscribeStringList.size
                val followerStringShow = followerCount.toString()

                if (activity != null) {
                    requireActivity().runOnUiThread(Runnable {
                        root.text_followerCount_shopFragment.text = followerStringShow
                    })
                }
            }.start()
        }

        refreshScore()

        Thread {
            while (!rankListIsReady)
                Thread.sleep(500)


            if (activity!= null)requireActivity().runOnUiThread {
                root.text_score_shopFragment.setOnClickListener {
                    root.card_scoring.visibility = View.VISIBLE
                }

                root.button_cancel_scoring.setOnClickListener {
                    root.card_scoring.visibility = View.GONE
                }

                root.button_submit_scoring.setOnClickListener {
                    // setup dialog builder
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setTitle("是否確定送出評價？")

                    builder.setPositiveButton("是", { dialogInterface, i ->
                        GlobalVariables.taskCount++
                        Thread {
                            if (isScoredRank) {
                                GlobalVariables.functions.makeToast("您已評價過此商家")
                            }
                            else {
                                val isSuccess = GlobalVariables.api.clickInRanking(
                                    GlobalVariables.proposalUserInfo.ID,
                                    selectRank,
                                    rankList[selectRank]+1
                                )

                                if (isSuccess) {
                                    GlobalVariables.dbHelper.writeDB(
                                        "scoring"+GlobalVariables.proposalUserInfo.ID,
                                        (selectRank+1).toString()
                                    )
                                }

                                if (activity!= null) requireActivity().runOnUiThread {
                                    if (isSuccess) {
                                        root.card_scoring.visibility = View.GONE
                                        refreshScore()
                                        GlobalVariables.functions.makeToast("已送出評價")
                                    }
                                    else {
                                        GlobalVariables.functions.makeToast("送出評價失敗")
                                    }
                                }
                            }

                            GlobalVariables.taskCount--
                        }.start()
                    })
                    builder.setNegativeButton("否", { dialogInterface, i ->

                    })

                    // create dialog and show it
                    requireActivity().runOnUiThread{
                        val dialog = builder.create()
                        dialog.show()
                    }
                }

                if (!isScoredRank) {
                    root.button_score_star_1.setOnClickListener {
                        root.button_score_star_1.alpha = 1f
                        root.button_score_star_2.alpha = 0.1f
                        root.button_score_star_3.alpha = 0.1f
                        root.button_score_star_4.alpha = 0.1f
                        root.button_score_star_5.alpha = 0.1f
                        selectRank = 0
                    }

                    root.button_score_star_2.setOnClickListener {
                        root.button_score_star_1.alpha = 1f
                        root.button_score_star_2.alpha = 1f
                        root.button_score_star_3.alpha = 0.1f
                        root.button_score_star_4.alpha = 0.1f
                        root.button_score_star_5.alpha = 0.1f
                        selectRank = 1
                    }

                    root.button_score_star_3.setOnClickListener {
                        root.button_score_star_1.alpha = 1f
                        root.button_score_star_2.alpha = 1f
                        root.button_score_star_3.alpha = 1f
                        root.button_score_star_4.alpha = 0.1f
                        root.button_score_star_5.alpha = 0.1f
                        selectRank = 2
                    }

                    root.button_score_star_4.setOnClickListener {
                        root.button_score_star_1.alpha = 1f
                        root.button_score_star_2.alpha = 1f
                        root.button_score_star_3.alpha = 1f
                        root.button_score_star_4.alpha = 1f
                        root.button_score_star_5.alpha = 0.1f
                        selectRank = 3
                    }

                    root.button_score_star_5.setOnClickListener {
                        root.button_score_star_1.alpha = 1f
                        root.button_score_star_2.alpha = 1f
                        root.button_score_star_3.alpha = 1f
                        root.button_score_star_4.alpha = 1f
                        root.button_score_star_5.alpha = 1f
                        selectRank = 4
                    }
                }
            }
        }.start()
    }

    private fun refreshScore() {
        Thread {
            rankListIsReady = false
            rankList = GlobalVariables.api.getRanking(GlobalVariables.proposalUserInfo.ID)
            rankListIsReady = true
            var totalScore = 0.0
            var totalCount = 0.0
            for (i in 0 until(rankList.size)) {
                totalScore += rankList[i] * (i+1)
                totalCount += rankList[i]
            }
            val mean = if (totalCount == 0.0) 0.0 else totalScore/totalCount

            if (activity != null) requireActivity().runOnUiThread {
                root.text_score_shopFragment.text = String.format("%.1f", mean)
                root.text_score_star_1.text = rankList[0].toString()
                root.text_score_star_2.text = rankList[1].toString()
                root.text_score_star_3.text = rankList[2].toString()
                root.text_score_star_4.text = rankList[3].toString()
                root.text_score_star_5.text = rankList[4].toString()
            }
        }.start()

        val selectRankString = GlobalVariables.dbHelper.readDB(
            "scoring"+GlobalVariables.proposalUserInfo.ID)

        isScoredRank = selectRankString != ""

        if (isScoredRank)
            selectRank = GlobalVariables.dbHelper.readDB(
                "scoring"+GlobalVariables.proposalUserInfo.ID).toInt() - 1

        when (selectRank) {
            0 -> {
                root.button_score_star_1.alpha = 1f
                root.button_score_star_2.alpha = 0.1f
                root.button_score_star_3.alpha = 0.1f
                root.button_score_star_4.alpha = 0.1f
                root.button_score_star_5.alpha = 0.1f
            }
            1 -> {
                root.button_score_star_1.alpha = 1f
                root.button_score_star_2.alpha = 1f
                root.button_score_star_3.alpha = 0.1f
                root.button_score_star_4.alpha = 0.1f
                root.button_score_star_5.alpha = 0.1f
            }
            2 -> {
                root.button_score_star_1.alpha = 1f
                root.button_score_star_2.alpha = 1f
                root.button_score_star_3.alpha = 1f
                root.button_score_star_4.alpha = 0.1f
                root.button_score_star_5.alpha = 0.1f
            }
            3 -> {
                root.button_score_star_1.alpha = 1f
                root.button_score_star_2.alpha = 1f
                root.button_score_star_3.alpha = 1f
                root.button_score_star_4.alpha = 1f
                root.button_score_star_5.alpha = 0.1f
            }
            4 -> {
                root.button_score_star_1.alpha = 1f
                root.button_score_star_2.alpha = 1f
                root.button_score_star_3.alpha = 1f
                root.button_score_star_4.alpha = 1f
                root.button_score_star_5.alpha = 1f
            }
        }
    }
}
