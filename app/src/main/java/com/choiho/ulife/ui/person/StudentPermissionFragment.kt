package com.choiho.ulife.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_student_permission.view.*

/**
 * A simple [Fragment] subclass.
 */
class StudentPermissionFragment : Fragment() {

    private lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_student_permission, container, false)

        if (GlobalVariables.studentPermissionID == "") {
            root.layout_student_permission.visibility = View.VISIBLE
            root.layout_student_permission_done.visibility = View.GONE

            root.button_student_permission.setOnClickListener {
                Thread {
                    val studentID = root.edit_student_ID_permission.text.toString()
                    val studentPassword = root.edit_student_password_permission.text.toString()

                    // TODO call api
                    var isSuccess = true

                    GlobalVariables.dbHelper.writeDB("studentPermissionID", studentID)
                    GlobalVariables.studentPermissionID = studentID

                    if (isSuccess) {
                        if (activity != null) {
                            requireActivity().runOnUiThread {
                                Toast.makeText(activity, "已完成身份認證", Toast.LENGTH_SHORT).show()
                                root.text_student_permission_ID.text = GlobalVariables.studentPermissionID
                                root.layout_student_permission.visibility = View.GONE
                                root.layout_student_permission_done.visibility = View.VISIBLE
                            }
                        }
                    }


                }.start()
            }
        }
        else {
            root.text_student_permission_ID.text = GlobalVariables.studentPermissionID
            root.layout_student_permission.visibility = View.GONE
            root.layout_student_permission_done.visibility = View.VISIBLE
        }

        return root
    }


}
