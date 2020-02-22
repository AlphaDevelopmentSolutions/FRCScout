package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.alphadevelopmentsolutions.frcscout.classes.LoadingDialog
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.KeyStore
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentConfigBinding
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import com.alphadevelopmentsolutions.frcscout.interfaces.IntentAction
import com.google.android.gms.safetynet.SafetyNet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigFragment : MasterFragment()
{
    private var configNumber: Int = 1
    private var loggingIn = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        arguments?.let {
            configNumber = it.getInt(ARG_FRAGMENT_NUMBER, 1)
        }
    }

    private lateinit var fragView: FragmentConfigBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        //hide the actionbar while connecting
        activityContext.supportActionBar?.hide()
        activityContext.lockDrawerLayout()

        when(configNumber)
        {
            1 -> return inflater.inflate(R.layout.layout_welcome, container, false)
            2 ->
            {
                fragView = DataBindingUtil.inflate(inflater, R.layout.fragment_config, container, false)

                fragView.handlers = this

                return fragView.root
            }
        }


        return inflater.inflate(R.layout.layout_welcome, container, false)
    }

    fun signup() = IntentAction.website(activityContext, "https://www.frcscout.app/create-account")

    /**
     * Attempts to log into the web server
     */
    fun login()
    {
        val username = fragView.UsernameEditText.text.toString()
        val password = fragView.PasswordEditText.text.toString()

        when {
            username.isNotBlank() -> activityContext.showSnackbar(String.format(activityContext.getString(R.string.not_empty), activityContext.getString(R.string.username)))
            password.isNotBlank() -> activityContext.showSnackbar(String.format(activityContext.getString(R.string.not_empty), activityContext.getString(R.string.password)))
            !loggingIn -> {

                loggingIn = true

                //reCAPTCHA check
                SafetyNet.getClient(activityContext).verifyWithRecaptcha(Constants.RECAPTCHA_SITE_KEY)
                        .addOnSuccessListener {

                            if (it.tokenResult.isNotEmpty()) {

                                val loadingDialog = LoadingDialog(activityContext, LoadingDialog.Style.SPINNER)
                                loadingDialog.message = activityContext.getString(R.string.logging_in)
                                loadingDialog.show()

                                //attempt to login to server
                                GlobalScope.launch(Dispatchers.IO) {

                                    val apiViewModel = VMProvider.getInstance(activityContext).apiViewModel

                                    if(apiViewModel.login(username, password)) {

                                        if(apiViewModel.fetchData()) {

                                            activityContext.runOnUiThread {
                                                activityContext.updateNavText(null)
                                                loadingDialog.dismiss()
                                            }
                                        }
                                    }
                                    else {
                                        activityContext.runOnUiThread {
                                            loadingDialog.dismiss()
                                            KeyStore.getInstance(activityContext).resetData()
                                            activityContext.showSnackbar(getString(R.string.invalid_login))
                                        }
                                    }

                                }
                            } else
                                activityContext.showSnackbar(activityContext.getString(R.string.recaptcha_error))

                            loggingIn = false

                        }
                        .addOnFailureListener {
                            activityContext.showSnackbar(activityContext.getString(R.string.recaptcha_error))

                            loggingIn = false

                            AppLog.error(it)
                        }
            }
        }
    }

    companion object
    {
        private const val ARG_FRAGMENT_NUMBER = "FRAGMENT_NUMBER"

        /**
         * Creates a new instance
         * @param index of the config activity for the viewpager
         * @return A new instance of fragment [ConfigFragment].
         */
        fun newInstance(index: Int): ConfigFragment
        {
            val fragment = ConfigFragment()
            val args = Bundle()
            args.putInt(ARG_FRAGMENT_NUMBER, index)
            fragment.arguments = args
            return fragment
        }
    }
}
