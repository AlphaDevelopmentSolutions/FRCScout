package com.alphadevelopmentsolutions.frcscout.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.CallSuper
import androidx.annotation.DimenRes
import androidx.fragment.app.DialogFragment
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity

abstract class MasterDialogFragment : DialogFragment() {

    /**
     * Holds whether the view has already been attached or not
     */
    private var isAttached: Boolean = false

    @CallSuper
    open fun show(context: MainActivity) {
        if (!isAttached)
            show(context.supportFragmentManager, null)
    }

    /**
     * Stores the height of the view when inflated
     */
    protected abstract val width: Int

    /**
     * Stores the width of the view when inflated
     */
    protected abstract val height: Int

    /**
     * Transparent color
     */
    private val transparent by lazy {
        ColorDrawable(Color.TRANSPARENT)
    }

    /**
     * Gets the dimension for the specific res id
     * @param resId [Int]
     * @return [Int] res id pixel size
     */
    protected fun getDimension(@DimenRes resId: Int): Int = resources.getDimensionPixelSize(resId)

    override fun onResume() {
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawable(transparent)

        super.onResume()
    }

    override fun onAttach(context: Context) {
        isAttached = true
        super.onAttach(context)
    }

    override fun onDetach() {
        isAttached = false
        super.onDetach()
    }
}