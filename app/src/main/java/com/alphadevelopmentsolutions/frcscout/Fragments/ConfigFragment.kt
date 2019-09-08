package com.alphadevelopmentsolutions.frcscout.Fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Api.Server
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.fragment_config.view.*
import kotlinx.android.synthetic.main.layout_permission.view.*

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
                view = inflater.inflate(R.layout.layout_permission, container, false)

                view.GrantPermissionButton.setOnClickListener {
                    //write permission not granted, request
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 5885)

                    else
                        context.showSnackbar("Permission already granted.")
                }
            }
            3 ->
            {
                view = inflater.inflate(R.layout.fragment_config, container, false)

                view.SignUpButton.setOnClickListener {
                    val url = "https://frcscout.dev/create-account.php"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }

                view.ConnectButton!!.setOnClickListener {

                    if (view.UsernameEditText.text.toString() == "")
                        context.showSnackbar("Username must not be empty.")

                    else if (view.PasswordEditText.text.toString() == "")
                        context.showSnackbar("Password must not be empty.")

                    else
                    {

                        //android >= marshmallow, permission needed
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            //write permission not granted, request
                            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                            {
                                login(view.UsernameEditText.text.toString(), view.PasswordEditText.text.toString())
                            }
                            else
                                context.showSnackbar("You must grant storage permission before logging in.")
                        }
                        else
                            login(view.UsernameEditText.text.toString(), view.PasswordEditText.text.toString())
                    }
                }
            }
        }


        return view
    }

    private fun login(username: String, password: String)
    {
        context.setPreference(Constants.SharedPrefKeys.API_CORE_USERNAME, username)
        context.setPreference(Constants.SharedPrefKeys.API_CORE_PASSWORD, password)
        context.setPreference(Constants.SharedPrefKeys.API_KEY_KEY, "TEMP")

        Thread(Runnable {
            //validate connection
            if (context.isOnline())
            {
                //gather server configs
                val getServerConfig = Server.GetServerConfig(context)

                //valid config
                if (getServerConfig.execute())
                {
                    context.runOnUiThread {
                        context.updateNavText()
                        context.downloadApplicationData(true, false)
                    }
                } else
                {
                    context.clearApiConfig()
                    context.showSnackbar(getString(R.string.invalid_url))
                }//invalid config
            } else
            {
                context.clearApiConfig()
                context.showSnackbar(getString(R.string.invalid_url))
            }//invalid connection
        }).start()
    }



    override fun onDetach()
    {
        context.supportActionBar!!.show()
        context.unlockDrawerLayout()
        super.onDetach()
    }

    companion object
    {
        private const val ARG_FRAGMENT_NUMBER = "fragment_number"

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
