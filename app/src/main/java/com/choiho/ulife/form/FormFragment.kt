package com.choiho.ulife.form


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_form.view.*

/**
 * A simple [Fragment] subclass.
 */
class FormFragment : Fragment() {

    private lateinit var root:View
    private var lockSendFormButton = false
    private lateinit var questionList:ArrayList<FormItem>
    private var myAdapter = CardFormAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_form, container, false)

        if (GlobalVariables.isDoneStudentForm) {
            root.layout_form_done.visibility = View.VISIBLE
            root.layout_form_not_done.visibility = View.GONE
        }
        else {
            root.layout_form_done.visibility = View.GONE
            root.layout_form_not_done.visibility = View.VISIBLE
            createForm()
        }

        return root
    }

    private fun createForm() {
        questionList = GlobalVariables.api.getForm()

        myAdapter = CardFormAdapter(questionList)
        myAdapter.init()
        root.recycler_form.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = myAdapter
        }

        root.button_send_form.setOnClickListener {
            if (!lockSendFormButton) {
                // setup dialog builder
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle("是否確定送出？")

                builder.setPositiveButton("是", { dialogInterface, i ->
                    submitInfo()
                    lockSendFormButton = false
                })
                builder.setNegativeButton("否", { dialogInterface, i ->
                    lockSendFormButton = false
                })

                // create dialog and show it
                requireActivity().runOnUiThread{
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    private fun submitInfo() {
        lockSendFormButton = true

        Thread {
            GlobalVariables.taskCount++
            val answerList:ArrayList<String> = arrayListOf()
            for (i in 0 until(questionList.size)) {
                if (questionList[i].type == "Single") {
                    var answerString = ""
                    for (checkBox in myAdapter.selectAnswerAdapterList[i].checkBoxList)
                        if (checkBox.isChecked) {
                            answerString = checkBox.text.toString()
                            break
                        }

                    answerList.add(answerString)
                }
                else if (questionList[i].type == "Multiple") {
                    var answerString = "["
                    var isFirst = true
                    for (checkBox in myAdapter.selectAnswerAdapterList[i].checkBoxList)
                        if (checkBox.isChecked) {
                            if (!isFirst) answerString += ","
                            else isFirst = false
                            answerString += "\""
                            answerString += checkBox.text.toString()
                            answerString += "\""
                        }

                    answerString += "]"
                    answerList.add(answerString)
                }
                else {
                    answerList.add(myAdapter.answerList[i])
                }
            }

            var notFilledIndex = -1
            for (i in 0 until(answerList.size)) {
                if (questionList[i].type == "Multiple") {
                    if (answerList[i] == "[]") {
                        notFilledIndex = i
                        break
                    }
                }
                else {
                    if (answerList[i] == "") {
                        notFilledIndex = i
                        break
                    }
                }
            }
            notFilledIndex++

            if (notFilledIndex > 0) {
                GlobalVariables.functions.makeToast("第 $notFilledIndex 題為必填，請答題")
            }
            else {
                val isSuccess = GlobalVariables.api.postForm(
                    GlobalVariables.userInfo.ID,
                    GlobalVariables.studentPermissionID,
                    answerList
                )

                if (isSuccess > 0) {
                    GlobalVariables.isDoneStudentForm = true
                    GlobalVariables.dbHelper.writeDB("isDoneStudentForm", "true")
                    GlobalVariables.functions.makeToast("已送出問卷")

                    if (activity!=null) requireActivity().runOnUiThread {
                        root.layout_form_done.visibility = View.VISIBLE
                        root.layout_form_not_done.visibility = View.GONE
                    }
                }
                else {
                    GlobalVariables.isDoneStudentForm = false
                    GlobalVariables.functions.makeToast("送出問卷失敗")
                }
            }

            GlobalVariables.taskCount--
            lockSendFormButton = false
        }.start()
    }
}
