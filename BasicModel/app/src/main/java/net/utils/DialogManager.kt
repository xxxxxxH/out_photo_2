package net.utils

import android.content.Context
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.ItemListener
import com.afollestad.materialdialogs.list.listItems

class DialogManager {
    companion object {
        private var instance: DialogManager? = null
            get() {
                field ?: run {
                    field = DialogManager()
                }
                return field
            }

        @Synchronized
        fun get(): DialogManager {
            return instance!!
        }
    }

    fun showDialog(
        context: Context,
        items: List<String>,
        type: String,
        listener: ItemListener
    ): MaterialDialog {
        return MaterialDialog(context, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            listItems(items = items, selection = listener)
        }
    }
}