package com.choiho.ulife


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_view_box.view.*
import org.threeten.bp.LocalDateTime
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 */
class ViewBoxFragment : Fragment() {

    private lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_box, container, false)

        root.button_send_complaint.setOnClickListener {
            Thread {
                GlobalVariables.taskCount++
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS")
                val isSuccess = GlobalVariables.api.complaint(
                    GlobalVariables.userInfo.ID,
                    GlobalVariables.userInfo.ID,
                    formatter.format(parser.parse(LocalDateTime.now().toString())),
                    "意見",
                    root.edit_send_complaint.text.toString()
                )

                GlobalVariables.taskCount--

                if (isSuccess)
                    GlobalVariables.functions.makeToast("我們已收到您的意見，謝謝您")
                else
                    GlobalVariables.functions.makeToast("送出失敗")
            }.start()
        }

        return root
    }


}
