package com.choiho.ulife.ui.person


import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import com.choiho.ulife.ui.api.Notification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_random_plate.view.*
import org.threeten.bp.LocalDateTime
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 */
class RandomPlateFragment : Fragment() {

    private lateinit var root: View
    private lateinit var buttonRandomFood: Button
    private lateinit var am:Animation
    private lateinit var iv: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_random_plate, container, false)

        am = RotateAnimation(0f, 360f*30, Animation.RELATIVE_TO_SELF, 0.495f, Animation.RELATIVE_TO_SELF, 0.447f)
        am.duration = 5000
        am.repeatCount = 0

        buttonRandomFood = root.button_random_food
        iv = root.image_random_food_plate_board
        buttonRandomFood.setOnClickListener {
            iv.startAnimation(am)
            var isDoneGettingPrice = false
            var rnds = 0

            Thread {
                // TODO call api get price list

                rnds = (0..10).random()
                isDoneGettingPrice = true

                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS")
                val date = formatter.format(parser.parse(LocalDateTime.now().toString()))

                // TODO call api store result
            }.start()

            Thread {
                Thread.sleep(am.duration)

                while (!isDoneGettingPrice)
                    Thread.sleep(500)

                val price = "白飯一碗$rnds"

                if (activity!= null) {
                    // setup dialog builder
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setTitle("恭喜您抽中！")
                    builder.setMessage(price)
                    builder.setPositiveButton("謝謝", { dialogInterface, i ->
                        requireActivity().runOnUiThread{
                            requireActivity().nav_host_fragment.findNavController().navigate(
                                R.id.action_randomPlateFragment_to_foodPriceFragment
                            )
                        }
                    })

                    // create dialog and show it
                    requireActivity().runOnUiThread{
                        val dialog = builder.create()
                        dialog.show()
                    }
                } // if
            }.start()
        }

        return root
    }


}
