package com.choiho.ulife.login

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
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_make_user_info.*
import kotlinx.android.synthetic.main.fragment_make_user_info.view.*

class MakeUserInfoFragment : Fragment() {

    private lateinit var root:View
    private var lockPickPictureButton = false
    private var lockMakeUserInfoButton = false
    private var lockPassButton = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_make_user_info, container, false)
        setButtons()
        return root
    }

    private fun setButtons() {
        linkMakeUserInfoChangeIconButton()
        linkMakeUserInfoSubmitButton()
        linkMakeUserInfoPassButton()
    }

    private fun linkMakeUserInfoChangeIconButton() {
        root.button_changeIcon_make_user_info.setOnClickListener {
            if (!lockPickPictureButton) {
                lockPickPictureButton = true
                pickImageFromGallery()
                lockPickPictureButton = false
            }
        }
    }

    private fun linkMakeUserInfoSubmitButton() {
        root.button_submit_upload_user_info.setOnClickListener {
            if (!lockMakeUserInfoButton) {
                Thread {
                    lockMakeUserInfoButton = true
                    GlobalVariables.taskCount++
                    makeUserInfo()
                    lockMakeUserInfoButton = false
                    GlobalVariables.taskCount--
                }.start()
            }
        }
    }

    private fun linkMakeUserInfoPassButton() {
        root.button_pass_upload_user_info.setOnClickListener {
            if (!lockPassButton) {
                Thread {
                    lockPassButton = true
                    GlobalVariables.taskCount++
                    passMakeInfo()
                    lockPassButton = false
                    GlobalVariables.taskCount--
                }.start()
            }
        }
    }

    private fun passMakeInfo() {
        GlobalVariables.functions.loginFromApi(GlobalVariables.userInfo.ID)
        if (activity != null) requireActivity().runOnUiThread{
            GlobalVariables.functions.navigate(
                R.id.makeUserInfoFragment,
                R.id.action_makeUserInfoFragment_to_mobile_navigation
            )
        }
    }

    private fun makeUserInfo() {
        val userName = root.edit_username_make_user_info.text.toString()
        GlobalVariables.userInfo.iconString =
            GlobalVariables.imageHelper.getString(
                image_photoshot_make_user_info.drawable.toBitmap())!!

        if (userName.length < 2 || userName.length > 16)
            GlobalVariables.functions.makeToast("暱稱長度必須在 2~16 字元之間")
        else if (GlobalVariables.api.uploadUserInfo(
                GlobalVariables.userInfo.ID, GlobalVariables.userInfo.iconString, userName)) {

            if (activity != null) requireActivity().runOnUiThread{
                GlobalVariables.functions.navigate(
                    R.id.makeUserInfoFragment,
                    R.id.action_makeUserInfoFragment_to_mobile_navigation
                )
            }

            GlobalVariables.userInfo.name = userName
            GlobalVariables.userInfo.isReady = true
            GlobalVariables.functions.writeUserInfoToSQL()
            GlobalVariables.functions.loginFromApi(GlobalVariables.userInfo.ID)
        }
        else GlobalVariables.functions.makeToast("上傳資料失敗")
    }

    // the code of getting image /////////////////////////////////

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            IMAGE_PICK_CODE
        )
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        //Permission code
        private const val PERMISSION_CODE = 1001
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image_photoshot_make_user_info.setImageURI(data?.data)
        }
    }
}
