package com.alphadevelopmentsolutions.frcscout.classes

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.callbacks.OnConfirmationCallback
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentDialogConfirmBinding

class ConfirmDialogFragment : MasterDialogFragment() {

    /**
     * @see MasterDialogFragment.width
     */
    override val width by lazy { getDimension(R.dimen.dialog_width) }

    /**
     * @see MasterDialogFragment.height
     */
    override val height by lazy { getDimension(R.dimen.dialog_height) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            if (title == null) {
                title = it.getString(ARG_TITLE)
            }

            if (description == null) {
                description = it.getString(ARG_DESCRIPTION)
            }

            if (cancelButtonText == null) {
                cancelButtonText = it.getString(ARG_CANCEL_BUTTON_TEXT)
            }

            if (confirmButtontext == null) {
                confirmButtontext = it.getString(ARG_CONFIRM_BUTTON_TEXT)
            }

            if (onConfirmationCallback == null) {
                it.getSerializable(ARG_CONFIRMATION_CALLBACK)?.let { item ->
                    if (item is OnConfirmationCallback)
                        onConfirmationCallback = item
                }
            }
        }
    }

    /**
     * Title of the [ConfirmDialogFragment]
     */
    var title: String? = null

    /**
     * Description or support text of the [ConfirmDialogFragment]
     */
    var description: String? = null

    /**
     * Cancel button text
     * */
    private var cancelButtonText: String? = null

    /**
     * Confirm button or support text
     */
    private var confirmButtontext: String? = null

    /**
     * Callback for either a confirm or cancel
     */
    var onConfirmationCallback: OnConfirmationCallback? = null

    /**
     * Prevents the [onDismiss] function from calling [OnConfirmationCallback.onCancel] repeatedly
     */
    private var callbackCalled = false

    private lateinit var config: Config

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentDialogConfirmBinding.inflate(inflater, container, false)

        view.config = config
        view.title = title
        view.description = description
        view.cancelButtonText = cancelButtonText
        view.confirmButtonText = confirmButtontext
        view.handlers = this

        return view.root
    }

    /**
     * Cancels the operation
     */
    fun cancel() {
        callbackCalled = true
        onConfirmationCallback?.onCancel()
        dismiss()
    }

    /**
     * Confirms the operation
     */
    fun confirm() {
        callbackCalled = true
        onConfirmationCallback?.onConfirm()
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (!callbackCalled)
            onConfirmationCallback?.onCancel()

        callbackCalled = false

        super.onDismiss(dialog)
    }

    override fun show(context: MainActivity) {

        config = context.config

        super.show(context)
    }

    companion object {

        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_DESCRIPTION = "ARG_DESCRIPTION"
        private const val ARG_CANCEL_BUTTON_TEXT = "ARG_CANCEL_BUTTON_TEXT"
        private const val ARG_CONFIRM_BUTTON_TEXT = "ARG_CONFIRM_BUTTON_TEXT"
        private const val ARG_CONFIRMATION_CALLBACK = "ARG_CONFIRMATION_CALLBACK"

        fun newInstance(
            onConfirmationCallback: OnConfirmationCallback,
            titleText: String? = null,
            descriptionText: String? = null,
            confirmButtonText: String? = null,
            cancelButtonText: String? = null
        ) =
            ConfirmDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, titleText)
                    putString(ARG_DESCRIPTION, descriptionText)
                    putString(ARG_CANCEL_BUTTON_TEXT, cancelButtonText)
                    putString(ARG_CONFIRM_BUTTON_TEXT, confirmButtonText)
                    putSerializable(ARG_CONFIRMATION_CALLBACK, onConfirmationCallback)
                }
            }
    }
}