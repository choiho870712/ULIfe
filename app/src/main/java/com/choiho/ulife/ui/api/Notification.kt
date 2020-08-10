package com.choiho.ulife.ui.api

import com.choiho.ulife.GlobalVariables

data class Notification(
    var pusher_id: String = "",
    var date: String = "",
    var content: String = "",
    var index: Int = 0,
    var type: String = ""
) {
    fun clear() {
        pusher_id = ""
        date = ""
        content = ""
        index = 0
        type = ""
    }

    fun writeDB(tag:String) {
        GlobalVariables.dbHelper.writeDB("{$tag}_pusher_id", pusher_id)
        GlobalVariables.dbHelper.writeDB("{$tag}_date", date)
        GlobalVariables.dbHelper.writeDB("{$tag}_content", content)
        GlobalVariables.dbHelper.writeDB("{$tag}_index", index.toString())
        GlobalVariables.dbHelper.writeDB("{$tag}_type", type)

    }

    fun readDB(tag:String):Boolean {
        pusher_id = GlobalVariables.dbHelper.readDB("{$tag}_pusher_id")
        if (pusher_id != "") {
            date = GlobalVariables.dbHelper.readDB("{$tag}_date")
            content = GlobalVariables.dbHelper.readDB("{$tag}_content")
            index = GlobalVariables.dbHelper.readDB("{$tag}_index").toInt()
            type = GlobalVariables.dbHelper.readDB("{$tag}_type")
            return true
        }
        return false
    }

    fun deleteDB(tag:String) {
        GlobalVariables.dbHelper.deleteDB("{$tag}_pusher_id")
        GlobalVariables.dbHelper.deleteDB("{$tag}_date")
        GlobalVariables.dbHelper.deleteDB("{$tag}_content")
        GlobalVariables.dbHelper.deleteDB("{$tag}_index")
        GlobalVariables.dbHelper.deleteDB("{$tag}_type")
        clear()
    }

    fun updateDB(tag:String) {
        GlobalVariables.dbHelper.updateDB("{$tag}_pusher_id", pusher_id)
        GlobalVariables.dbHelper.updateDB("{$tag}_date", date)
        GlobalVariables.dbHelper.updateDB("{$tag}_content", content)
        GlobalVariables.dbHelper.updateDB("{$tag}_index", index.toString())
        GlobalVariables.dbHelper.updateDB("{$tag}_type", type)
    }

    fun isEmpty():Boolean {
        return pusher_id == ""
    }
}