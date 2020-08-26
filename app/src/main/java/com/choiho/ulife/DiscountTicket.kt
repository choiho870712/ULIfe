package com.choiho.ulife

data class DistountTicket(
    var name: String = "",
    var content: String = "",
    var discount_code: String = "",
    var create_time: Int = 0,
    var expiration_time: Int = 0
)
