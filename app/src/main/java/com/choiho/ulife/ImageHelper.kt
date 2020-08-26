package com.choiho.ulife

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.net.URL

class ImageHelper {

    fun scaleImage(srcBmp: Bitmap): Bitmap {
        val image: Bitmap
        if (srcBmp.getWidth() > srcBmp.getHeight()){

            image = Bitmap.createBitmap(
                srcBmp,
                srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                0,
                srcBmp.getHeight(),
                srcBmp.getHeight()
            );

        }else if (srcBmp.getWidth() < srcBmp.getHeight()){

            image = Bitmap.createBitmap(
                srcBmp,
                0,
                srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                srcBmp.getWidth(),
                srcBmp.getWidth()
            );
        }
        else image = srcBmp

        return image.scale(800,800)
    }

    fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun getString(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.NO_WRAP)
    }

    fun convertUrlToImage(urlString: String) : Bitmap? {
        try {
            val imageString = GlobalVariables.dbHelper.readDB(urlString)
            var image:Bitmap? = null
            if (imageString == "") {
                image = BitmapFactory.decodeStream(URL(urlString).openConnection().getInputStream())
                GlobalVariables.dbHelper.writeDB(urlString, getString(image)!!)
            }
            else {
                image = convertString64ToImage(imageString)
            }

            return image
        }
        catch (e: Exception) {
            return Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
        }
    }
}