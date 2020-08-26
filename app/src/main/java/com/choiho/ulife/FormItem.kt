package com.choiho.ulife

data class FormItem(
    var question: String = "",
    var type: String = "",
    var answer: ArrayList<String> = arrayListOf()
)
