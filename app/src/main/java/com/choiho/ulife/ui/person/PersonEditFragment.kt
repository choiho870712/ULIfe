package com.choiho.ulife.ui.person


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
import kotlinx.android.synthetic.main.fragment_person_edit.view.*

/**
 * A simple [Fragment] subclass.
 */
class PersonEditFragment : Fragment() {

    lateinit var root:View
    private var isRunningPickPicture = false
    private var isRunningUpdateUserInfo = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_person_edit, container, false)

        setUi()
        setButton()

        return root
    }

    private fun setUi() {
        root.text_username_edit_person.setText(GlobalVariables.userInfo.name)
        root.text_content_edit_person.setText(GlobalVariables.userInfo.content)
        root.text_tag_edit_person.setText(GlobalVariables.userInfo.getHashTagString())

        val icon = GlobalVariables.userInfo.getIconBitmap()
        if (icon != null)
            root.image_photoshot_edit_person.setImageBitmap(icon)
    }

    private fun setChangeImageButton() {
        root.button_changeIcon_edit_person.setOnClickListener {
            if (!isRunningPickPicture) {
                isRunningPickPicture = true
                pickImageFromGallery()
                isRunningPickPicture = false
            }
        }
    }

    private fun setSubmitButton() {
        root.button_update_user_info_submit.setOnClickListener {
            if (!isRunningUpdateUserInfo) {
                isRunningUpdateUserInfo = true

                val isSuccess = GlobalVariables.api.updateUserInfo(
                    GlobalVariables.userInfo.ID,
                    root.text_username_edit_person.text.toString(),
                    GlobalVariables.userInfo.iconString,
                    mutableListOf(root.text_tag_edit_person.text.toString()),
                    root.text_content_edit_person.text.toString()
                )

                if (isSuccess) {
                    GlobalVariables.userInfo.name = root.text_username_edit_person.text.toString()
                    GlobalVariables.userInfo.content = root.text_content_edit_person.text.toString()
                    GlobalVariables.userInfo.hashtag = mutableListOf(root.text_tag_edit_person.text.toString())

                    GlobalVariables.userInfo.updateDB("userInfo")

                    if (GlobalVariables.userInfo.isShop()) {
                        if (activity != null) {
                            requireActivity().nav_host_fragment.findNavController().navigate(
                                R.id.action_personEditFragment_to_personShopInfoFragment
                            )
                        }
                    }
                    else {
                        if (activity != null) {
                            requireActivity().nav_host_fragment.findNavController().navigate(
                                R.id.action_personEditFragment_to_personInfoFragment
                            )
                        }
                    }
                }
                else {
                    if (activity != null) {
                        requireActivity().runOnUiThread(Runnable {
                            Toast.makeText(activity, "上傳失敗", Toast.LENGTH_SHORT).show()
                        })
                    }
                }

                isRunningUpdateUserInfo = false
            }

        }
    }

    private fun setButton() {
        setChangeImageButton()
        setSubmitButton()
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
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
            root.image_photoshot_edit_person.setImageURI(data?.data)
            GlobalVariables.userInfo.iconString = GlobalVariables.api.convertImageToString64(
                root.image_photoshot_edit_person.drawable.toBitmap()
            )!!
        }
    }
}
