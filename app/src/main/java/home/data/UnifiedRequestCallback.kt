package home.data

import home.data.ListViewItem

data class UnifiedRequestCallback(
    val response: ArrayList<ListViewItem>?,
    val deviceId: String,
    val errorMessage: String = ""
)