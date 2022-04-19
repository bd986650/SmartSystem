package home.interfaces

import android.view.View
import home.data.ListViewItem

interface HomeRecyclerViewHelperInterface {
    fun onItemClicked(view: View, data: ListViewItem)
    fun onStateChanged(view: View, data: ListViewItem, state: Boolean)
}