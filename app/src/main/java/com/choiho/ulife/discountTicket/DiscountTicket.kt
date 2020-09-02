package com.choiho.ulife.discountTicket

data class DistountTicket(
    var id: String = "",
    var name: String = "",
    var content: String = "",
    var discount_code: String = "",
    var create_time: Int = 0,
    var expiration_time: Int = 0
)
