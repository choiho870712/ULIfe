package com.example.startupboard

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_make_user_info.*
import kotlinx.android.synthetic.main.fragment_make_user_info.view.*

class MakeUserInfoFragment : Fragment() {

    private var isRunningPickPicture = false
    private var isRunningMakeUserInfo = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_make_user_info, container, false)

        setButtons(root)

        return root
    }

    private fun setButtons(root:View) {
        linkMakeUserInfoChangeIconButton(root)
        linkMakeUserInfoSubmitButton(root)
    }

    private fun linkMakeUserInfoChangeIconButton(root:View) {
        root.button_changeIcon_make_user_info.setOnClickListener {
            if (!isRunningPickPicture) {
                isRunningPickPicture = true
                pickImageFromGallery()
                isRunningPickPicture = false
            }
        }
    }

    private fun linkMakeUserInfoSubmitButton(root:View) {
        root.button_submit_upload_user_info.setOnClickListener {
            if (!isRunningMakeUserInfo) {
                Thread {

                    isRunningMakeUserInfo = true

                    val userName = root.edit_username_make_user_info.text.toString()

                    if (GlobalVariables.api.uploadUserInfo(GlobalVariables.userInfo.ID, GlobalVariables.userInfo.iconString, userName)) {
                        activity!!.runOnUiThread(Runnable {
                            activity!!.nav_host_fragment.findNavController().navigate(R.id.action_makeUserInfoFragment_to_mobile_navigation)
                        })

                        GlobalVariables.userInfo.name = userName
                    }
                    else {
                        activity!!.runOnUiThread(Runnable {
                            Toast.makeText(activity, "上傳資料失敗", Toast.LENGTH_SHORT).show()
                        })
                    }

                    isRunningMakeUserInfo = false
                }.start()
            }
        }
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
            image_photoshot_make_user_info.setImageURI(data?.data)
            GlobalVariables.userInfo.iconString = GlobalVariables.api.convertImageToString64(
                image_photoshot_make_user_info.drawable.toBitmap())!!
        }
    }
}
