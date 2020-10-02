package com.choiho.ulife.form

data class FormItem(
    var question: String = "",
    var type: String = "",
    var answer: ArrayList<String> = arrayListOf()
)

data class FormListItem(
    var tilte: String = "",
    var end_time: String = "",
    var prefix: String = ""
)