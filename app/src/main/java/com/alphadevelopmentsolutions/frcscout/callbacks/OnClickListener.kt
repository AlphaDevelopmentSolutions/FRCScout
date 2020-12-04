package com.alphadevelopmentsolutions.frcscout.callbacks

import android.view.View
import java.io.Serializable

interface OnClickListener : View.OnClickListener, Serializable {
    override fun onClick(v: View)
}