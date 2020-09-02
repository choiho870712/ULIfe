package com.choiho.ulife.navigationUI.home


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home_edit.view.*
import kotlinx.android.synthetic.main.fragment_home_edit.view.edit_tag_edit_home
import org.threeten.bp.LocalDateTime
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 */
class HomeEditFragment : Fragment() {

    private lateinit var root:View
    private var isRunningSubmitAddProposal = false
    private lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_home_edit, container, false)

        root.text_author_edit_home.text = GlobalVariables.userInfo.name

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS")
        date = formatter.format(parser.parse(LocalDateTime.now().toString())!!)

        root.text_date_edit_home.text = date

        root.button_select_image_edit_home.setOnClickListener {
            pickImageFromGallery()
        }

        root.button_submit_edit_home.setOnClickListener {
            if (!isRunningSubmitAddProposal) {
                if (root.image_edit_home_proposalImage.drawable == null)
                    GlobalVariables.functions.makeToast("請上傳圖片")
                else if (root.edit_title_edit_home.text.toString() == "")
                    GlobalVariables.functions.makeToast("標題為必填")
                else if (root.edit_content_edit_home.text.toString() == "")
                    GlobalVariables.functions.makeToast("內文為必填")
                else {
                    // setup dialog builder
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setTitle("是否確定送出？")

                    builder.setPositiveButton("是", { dialogInterface, i ->
                        submitInfo()
                        isRunningSubmitAddProposal = false
                    })
                    builder.setNegativeButton("否", { dialogInterface, i ->
                        isRunningSubmitAddProposal = false
                    })

                    // create dialog and show it
                    requireActivity().runOnUiThread{
                        val dialog = builder.create()
                        dialog.show()
                    }

                }
            }
        }

        return root
    }

    private fun submitInfo() {
        isRunningSubmitAddProposal = true
        GlobalVariables.taskCount++

        Thread {
            val id = GlobalVariables.userInfo.ID
            val title = root.edit_title_edit_home.text.toString()
            val content = root.edit_content_edit_home.text.toString()
            val hashtag = root.edit_tag_edit_home.text.toString()
            val image = root.image_edit_home_proposalImage.drawable.toBitmap()
            val area = GlobalVariables.homeAreaChoose

//            var downCount = GlobalVariables.userInfo.permission.size
            for (i in 0 until(GlobalVariables.userInfo.permission.size)) {
                Thread {
                    GlobalVariables.api.postFoodItem(
                        id, title, content, date,
                        GlobalVariables.userInfo.permission[i],
                        mutableListOf(hashtag),
                        image,
                        area
                    )

//                    downCount--
                }.start()
            }

//            while (downCount > 0) continue
            Thread.sleep(5000)

            GlobalVariables.functions.makeToast("上傳文章成功")
            GlobalVariables.functions.resetProposalList()

            Thread {
                GlobalVariables.proposal = GlobalVariables.api.getFoodItem(
                    GlobalVariables.userInfo.ID,
                    GlobalVariables.userInfo.permission[0],
                    GlobalVariables.homeAreaChoose
                )

                GlobalVariables.proposalUserInfo.copy(GlobalVariables.userInfo)
                GlobalVariables.proposalUserScribeListData =
                    GlobalVariables.subscribeList

                GlobalVariables.functions.navigate(
                    R.id.action_homeEditFragment_to_homePage1Fragment)

                while (!GlobalVariables.proposal!!.proposalItemList[0].isDoneImageLoadingOnlyOne())
                    Thread.sleep(500)


                for (i in 0 until (GlobalVariables.proposal!!.proposalItemList.size)) {
                    Thread {
                        GlobalVariables.proposal!!.proposalItemList[i].convertImageUrlToImageAll()
                    }.start()
                }

                GlobalVariables.taskCount--
                isRunningSubmitAddProposal = false
            }.start()

            Thread{
                var count = 0
                val message = "發了一篇新的文章快來看看!!"
                Thread {
                    val subscribeList = GlobalVariables.api.getSubscribeList(
                        GlobalVariables.userInfo.ID)

                    for (i in 0 until(subscribeList.size)) {
                        Thread{
                            Log.d(">>>>>>>>>>>>>>>>>>>>>>", subscribeList[i].ID)

                            GlobalVariables.fireBase.requestSendingFCM(
                                subscribeList[i].FMC_ID,
                                GlobalVariables.userInfo.name,
                                message
                            )
                            count++
                        }.start()
                    }
                }.start()

                Thread {
                    GlobalVariables.api.postNotification(
                        GlobalVariables.userInfo.ID,
                        message,
                        date
                    )
                }.start()
            }.start()
        }.start()
    }



    // the code of getting image /////////////////////////////////

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            root.image_edit_home_proposalImage.setImageURI(data?.data)
        }
    }
}
