package com.alphadevelopmentsolutions.frcscout.Classes

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.layout_dialog_loading.view.*

class LoadingDialog(context: Context) : AlertDialog.Builder(context)
{
    private var view: View = LayoutInflater.from(context).inflate(R.layout.layout_dialog_loading, null)
    private lateinit var alertDialog: AlertDialog

    var messageText: String = context.getString(R.string.placeholder)
    set(value)
    {
        view.MessageTextView.text = value

        field = value
    }

    init
    {
        setCancelable(false)
    }

    override fun show(): AlertDialog
    {
        setView(view)
        alertDialog = create()
        alertDialog.show()

        return alertDialog
    }

    fun dismiss()
    {
        alertDialog.dismiss()
    }
}