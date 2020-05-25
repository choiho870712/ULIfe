package com.example.startupboard.ui.api

data class Notification(
    var pusher_id: String = "",
    var date: String = "",
    var content: String = "",
    var index: Int = 0
)