package com.choiho.ulife.discountTicket

data class DistountItem(
    var discount_code: String = "",
    var percentage: Int = 0,
    var content: String = "",
    var id: String = "",
    var name: String = ""
)
