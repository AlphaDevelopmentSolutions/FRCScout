package com.alphadevelopmentsolutions.frcscout.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.callbacks.OnAnimationCompleteCallback
import com.alphadevelopmentsolutions.frcscout.callbacks.OnProgressCallback
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentDialogLoadingBinding

class LoadingDialogFragment : MasterDialogFragment() {

    /**
     * @see MasterDialogFragment.width
     */
    override val width by lazy { getDimension(R.dimen.dialog_loading_width) }

    /**
     * @see MasterDialogFragment.height
     */
    override val height by lazy { getDimension(R.dimen.dialog_loading_height) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loadingText = it.getString(ARG_LOADING_TEXT)
            it.getSerializable(ARG_CALLBACK)?.let { serializedCallback ->
                if (serializedCallback is OnAnimationCompleteCallback)
                    onAnimationCompleteCallback = serializedCallback
            }
        }

        isCancelable = false
    }

    /**
     * Text to display when the view is loading
     */
    private var loadingText: String? = null

    /**
     * Callbacks to run when either the animation completed successfully or failed
     */
    private var onAnimationCompleteCallback: OnAnimationCompleteCallback? = null
    private var localCallback =
        object : OnAnimationCompleteCallback {
            override fun onSuccess() {
                onAnimationCompleteCallback?.onSuccess()
                onFinish()
            }

            override fun onFail() {
                onAnimationCompleteCallback?.onFail()
                onFinish()
            }

            override fun onFinish() {
                onAnimationCompleteCallback?.onFinish()
                dismiss()
            }
        }

    /**
     * Holder for when [onViewCreated] gets called
     * Once [onViewCreated] gets called, this gets set to true and
     * the [success] field is then able to be fired
     */
    private var viewCreated = false

    /**
     * When this field is changed, it will fire the correct function to animate a
     * finish on the loading anim
     */
    private var success: Boolean? = null
        set(value) {

            if (viewCreated) {
                when (value) {
                    true -> successFinish()
                    false -> failureFinish()
                }
            }

            field = value
        }

    val onProgressCallback by lazy {
        object : OnProgressCallback {
            override fun onProgressChange(message: String?, progress: Float?) {
                message?.let {
                    loadingText = it
                    view?.loadingText = it
                }

                progress?.let {
                }
            }
        }
    }

    /**
     * Databinding view that holds all vars
     */
    private var view: FragmentDialogLoadingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = FragmentDialogLoadingBinding.inflate(inflater, container, false).apply {
            loadingText = this@LoadingDialogFragment.loadingText
        }

        return view?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewCreated = true

        success?.let {
            success = it
        }
    }

    /**
     * Sets the message and notifies the dialog that the loading has succeeded
     * @param message [String] message to show on success
     */
    fun success(message: String? = null) {
        this.loadingText = message
        success = true
    }

    /**
     * Sets the message and notifies the dialog that the loading has failed
     * @param message [String] message to show on fail
     */
    fun failure(message: String? = null) {
        this.loadingText = message
        success = false
    }

    /**
     * Starts the success animation for the loading dialog
     */
    private fun successFinish() {
        view?.loadingText = loadingText ?: getString(R.string.load_success)
        view?.Anim?.success(localCallback)
    }

    /**
     * Starts the fail animation for the loading dialog
     */
    private fun failureFinish() {
        view?.loadingText = loadingText ?: getString(R.string.load_failure)
        view?.Anim?.failure(localCallback)
    }

    override fun onDetach() {
        viewCreated = false
        success = null
        loadingText = null

        super.onDetach()
    }

    companion object {

        private const val ARG_LOADING_TEXT = "ARG_LOADING_TEXT"
        private const val ARG_CALLBACK = "ARG_CALLBACK"

        fun newInstance(
            loadingText: String? = null,
            onAnimationCompleteCallback: OnAnimationCompleteCallback? = null
        ) =
            LoadingDialogFragment().apply {
                arguments = Bundle().apply {
                    loadingText?.let {
                        putString(ARG_LOADING_TEXT, it)
                    }

                    onAnimationCompleteCallback?.let {
                        putSerializable(ARG_CALLBACK, it)
                    }
                }
            }
    }
}