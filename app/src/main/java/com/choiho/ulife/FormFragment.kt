package com.choiho.ulife


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.card_form.view.*
import kotlinx.android.synthetic.main.fragment_form.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * A simple [Fragment] subclass.
 */
class FormFragment : Fragment() {

    private lateinit var root:View
    private var lockSendFormButton = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_form, container, false)

        val questionList = GlobalVariables.api.getForm()

        val myAdapter = CardFormAdapter(questionList)
        root.recycler_form.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = myAdapter
        }

        root.button_send_form.setOnClickListener {
            if (!lockSendFormButton) {
                lockSendFormButton = true

                Thread {
                    GlobalVariables.taskCount++
                    val answerList:ArrayList<String> = arrayListOf()
                    for (i in 0 until(questionList.size)) {
                        if (questionList[i].type == "Shot") {
                            answerList.add(myAdapter.answerList[i])
                        }
                        else if (questionList[i].type == "Single") {
                            for (checkBox in myAdapter.selectAnswerAdapterList[i].checkBoxList)
                                if (checkBox.isChecked) {
                                    answerList.add(checkBox.text.toString())
                                    break
                                }
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
                    }

                    val isSuccess = GlobalVariables.api.postForm(
                        GlobalVariables.userInfo.ID,
                        GlobalVariables.studentPermissionID,
                        answerList
                    )

                    GlobalVariables.taskCount--

                    if (isSuccess) {
                        GlobalVariables.isDoneStudentForm = true
                        GlobalVariables.functions.navigate(R.id.action_formFragment_to_studentPermissionFragment)
                        GlobalVariables.dbHelper.writeDB("isDoneStudentForm", "true")
                        GlobalVariables.functions.makeToast("已送出問卷")
                    }
                    else {
                        GlobalVariables.isDoneStudentForm = false
                        GlobalVariables.functions.makeToast("送出問卷失敗")
                    }

                    lockSendFormButton = false
                }.start()
            }

        }

        return root
    }


}
