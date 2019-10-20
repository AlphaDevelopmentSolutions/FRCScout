package com.alphadevelopmentsolutions.frcscout.Classes

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.layout_dialog_loading_progress.view.*
import kotlinx.android.synthetic.main.layout_dialog_loading_spinner.view.*
import kotlinx.android.synthetic.main.layout_dialog_loading_spinner.view.MessageTextView

class LoadingDialog(context: MainActivity, style: Style) : AlertDialog.Builder(context)
{
    private var view: View = LayoutInflater.from(context).inflate(if(style == Style.SPINNER) R.layout.layout_dialog_loading_spinner else R.layout.layout_dialog_loading_progress, null)
    private lateinit var alertDialog: AlertDialog

    var message: String = context.getString(R.string.placeholder)
    set(value)
    {
        view.MessageTextView.text = value

        field = value
    }

    var progress: Int = 0
    set(value)
    {
        val animator = ObjectAnimator.ofInt(view.ProgressBar, "progress", field, value)
        animator.duration = 500
        animator.interpolator = DecelerateInterpolator()
        animator.start()

        field = value
    }

    var percentage: Int = 0
        set(value)
        {
            if(view.PercentageTextView.visibility != View.VISIBLE)
                view.PercentageTextView.visibility = View.VISIBLE

            if(value != field)
                view.PercentageTextView.text = String.format(context.getString(R.string.percentage), value)

            field = value
        }

    var showPercentage: Boolean = false
        set(value)
        {
            if(value)
            {
                if(view.PercentageTextView.visibility != View.VISIBLE)
                    view.PercentageTextView.visibility = View.VISIBLE
            }
            else
            {
                if(view.PercentageTextView.visibility != View.INVISIBLE)
                    view.PercentageTextView.visibility = View.INVISIBLE
            }

            field = value

        }

    init
    {
        if(style == Style.PROGRESS)
        {
            view.ProgressBar.max = 100
            view.ProgressBar.progressDrawable.setColorFilter(context.primaryColor, PorterDuff.Mode.SRC_IN)
        }
        else
            view.Spinner.indeterminateDrawable.setColorFilter(context.primaryColor, PorterDuff.Mode.SRC_IN)


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

    enum class Style
    {
        SPINNER,
        PROGRESS
    }
}