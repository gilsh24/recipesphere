package com.example.recipesphere.extensions

fun listToCommaSeparatedString(list: List<String>?, defaultValue: String = "None"): String {
    val modifiedList = if (list != null && list.size > 5) list.take(4) else list
    return modifiedList?.joinToString(", ") ?: defaultValue
}