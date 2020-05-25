package com.example.startupboard

import android.app.Application
import android.graphics.Bitmap
import com.example.startupboard.ui.api.Api
import com.example.startupboard.ui.api.UserInfo

class GlobalVariables : Application() {
    companion object {
        var globalUserInfo : UserInfo = UserInfo(
            "","","","","",
            Bitmap.createBitmap(30, 30, Bitmap.Config.ARGB_8888),
            "",
            mutableListOf(), mutableListOf()
        )
        var globalUserInfoSetted : Boolean = false
        var globalApiRseponse : String = "able to call new api..."
        var globalApi : Api = Api()
        var globalEmptyBitmap : Bitmap = Bitmap.createBitmap(30, 30, Bitmap.Config.ARGB_8888)
    }
}