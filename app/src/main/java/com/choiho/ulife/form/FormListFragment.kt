package com.choiho.ulife.form


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_form_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class FormListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_form_list, container, false)

        if (GlobalVariables.studentPermissionID == "") {
            GlobalVariables.functions.makeToast("請先完成學生認證")
            GlobalVariables.functions.navigate(
                R.id.formListFragment,
                R.id.action_formListFragment_to_studentPermissionFragment
            )
        }
        else {
            Thread {
                GlobalVariables.taskCount++
                val formList = GlobalVariables.api.getFormList()
                if (activity!=null) requireActivity().runOnUiThread {
                    if (formList.isEmpty()) {
                        root.text_no_form.visibility = View.VISIBLE
                    }
                    else {
                        root.text_no_form.visibility = View.GONE
                        root.recycler_form_list.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(activity)
                            adapter = CardFormListAdapter(formList)
                        }
                    }
                }
                GlobalVariables.taskCount--
            }.start()
        }

        return root
    }


}
