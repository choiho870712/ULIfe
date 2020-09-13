package com.choiho.ulife.navigationUI.notifications

import com.choiho.ulife.GlobalVariables

data class Notification(
    var pusher_id: String = "",
    var name: String = "",
    var iconString: String = "",
    var date: String = "",
    var content: String = "",
    var view: Int = 0,
    var create_time: Int
) {
    fun clear() {
        pusher_id = ""
        date = ""
        content = ""
        view = 0
    }

    fun writeDB(tag:String) {
        GlobalVariables.dbHelper.writeDB("{$tag}_pusher_id", pusher_id)
        GlobalVariables.dbHelper.writeDB("{$tag}_date", date)
        GlobalVariables.dbHelper.writeDB("{$tag}_content", content)
        GlobalVariables.dbHelper.writeDB("{$tag}_index", view.toString())

    }

    fun readDB(tag:String):Boolean {
        pusher_id = GlobalVariables.dbHelper.readDB("{$tag}_pusher_id")
        if (pusher_id != "") {
            date = GlobalVariables.dbHelper.readDB("{$tag}_date")
            content = GlobalVariables.dbHelper.readDB("{$tag}_content")
            view = GlobalVariables.dbHelper.readDB("{$tag}_index").toInt()
            return true
        }
        return false
    }

    fun deleteDB(tag:String) {
        GlobalVariables.dbHelper.deleteDB("{$tag}_pusher_id")
        GlobalVariables.dbHelper.deleteDB("{$tag}_date")
        GlobalVariables.dbHelper.deleteDB("{$tag}_content")
        GlobalVariables.dbHelper.deleteDB("{$tag}_index")
        clear()
    }

    fun updateDB(tag:String) {
        GlobalVariables.dbHelper.updateDB("{$tag}_pusher_id", pusher_id)
        GlobalVariables.dbHelper.updateDB("{$tag}_date", date)
        GlobalVariables.dbHelper.updateDB("{$tag}_content", content)
        GlobalVariables.dbHelper.updateDB("{$tag}_index", view.toString())
    }

    fun isEmpty():Boolean {
        return pusher_id == ""
    }
}