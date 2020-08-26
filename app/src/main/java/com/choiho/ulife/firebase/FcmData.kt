package com.choiho.ulife.firebase

data class FcmData(
    var to :String? = null,
    var notification: Notification = Notification()
) {
    data class Notification(
        var body: String? = null,
        var title: String? = null
    )
}