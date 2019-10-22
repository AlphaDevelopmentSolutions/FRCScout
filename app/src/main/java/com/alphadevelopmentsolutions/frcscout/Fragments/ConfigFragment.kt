package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Api.Api
import com.alphadevelopmentsolutions.frcscout.Classes.LoadingDialog
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.google.android.gms.safetynet.SafetyNet
import kotlinx.android.synthetic.main.fragment_config.view.*

class ConfigFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private var configNumber: Int? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments != null)
        {
            configNumber = arguments!!.getInt(ARG_FRAGMENT_NUMBER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        //hide the actionbar while connecting
        context.supportActionBar!!.hide()
        context.lockDrawerLayout()

        var view: View? = null

        when(configNumber)
        {
            1 -> view = inflater.inflate(R.layout.layout_welcome, container, false)
            2 ->
            {
                view = inflater.inflate(R.layout.fragment_config, container, false)

                view.SignUpButton.setOnClickListener {
                    val url = "https://www.frcscout.app/create-account"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }

                view.ConnectButton!!.setOnClickListener {

                    when {
                        view.UsernameEditText.text.toString() == "" -> String.format(context.getString(R.string.not_empty), context.getString(R.string.username))
                        view.PasswordEditText.text.toString() == "" -> String.format(context.getString(R.string.not_empty), context.getString(R.string.password))
                        else -> login(view.UsernameEditText.text.toString(), view.PasswordEditText.text.toString())
                    }
                }
            }
        }


        return view
    }

    /**
     * Attempts to log into the web server
     * @param username [String] username to send to the web server
     * @param password [String] password to send to the web server
     */
    private fun login(username: String, password: String)
    {
        //reCAPTCHA check
        SafetyNet.getClient(context).verifyWithRecaptcha(Constants.RECAPTCHA_SITE_KEY)
                .addOnSuccessListener {

                    if(it.tokenResult.isNotEmpty())
                    {
                        context.keyStore.setPreference(Constants.SharedPrefKeys.API_CORE_USERNAME, username)
                        context.keyStore.setPreference(Constants.SharedPrefKeys.API_CORE_PASSWORD, password)
                        context.keyStore.setPreference(Constants.SharedPrefKeys.API_KEY_KEY, "TEMP")

                        val loadingDialog = LoadingDialog(context, LoadingDialog.Style.SPINNER)
                        loadingDialog.message = context.getString(R.string.logging_in)
                        loadingDialog.show()

                        Thread(Runnable {
                            //validate connection
                            if (context.isOnline)
                            {
                                //attempt to login to server
                                val login = Api.Account.Login(context, username, password, it.tokenResult)

                                //valid config
                                if (login.execute())
                                {
                                    with(context)
                                    {
                                        runOnUiThread {
                                            updateNavText(null)

                                            loadingDialog.dismiss()

                                            downloadApplicationData(false, false)
                                        }

                                    }
                                } else
                                {
                                    with(context)
                                    {
                                        runOnUiThread {
                                            loadingDialog.dismiss()
                                        }

                                        keyStore.resetData()
                                        showSnackbar(getString(R.string.invalid_login))
                                    }
                                }//invalid config
                            } else
                            {
                                with(context)
                                {
                                    runOnUiThread {
                                        loadingDialog.dismiss()
                                    }

                                    keyStore.resetData()
                                    showSnackbar(getString(R.string.invalid_url))
                                }
                            }//invalid connection
                        }).start()
                    }
                    else
                        context.showSnackbar(context.getString(R.string.recaptcha_error))

                }
                .addOnFailureListener { context.showSnackbar(context.getString(R.string.recaptcha_error)) }
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
