package home.interfaces

import androidx.recyclerview.widget.RecyclerView
import home.interfaces.RecyclerViewHelperInterface

interface RecyclerViewHelperInterfaceAdvanced : RecyclerViewHelperInterface {
    fun onItemHandleTouched(viewHolder: RecyclerView.ViewHolder)
}