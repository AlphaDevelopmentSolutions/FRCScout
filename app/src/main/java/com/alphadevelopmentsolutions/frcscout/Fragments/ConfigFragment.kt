package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.alphadevelopmentsolutions.frcscout.Api.Server
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ConfigFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ConfigFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfigFragment : MasterFragment()
{

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    private var webUrlEditText: EditText? = null

    private var connectButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments != null)
        {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_config, container, false)

        //hide the actionbar while connecting
        context.supportActionBar!!.hide()

        webUrlEditText = view.findViewById(R.id.WebUrlEditText)
        connectButton = view.findViewById(R.id.ConnectButton)

        connectButton!!.setOnClickListener {
            var url = webUrlEditText!!.text.toString()

            if (!url.contains("http"))
                url = "http://$url"

            context.setPreference(Constants.SharedPrefKeys.WEB_URL_KEY, url)
            context.setPreference(Constants.SharedPrefKeys.API_URL_KEY, "$url/api/api.php")
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
                            context.downloadApplicationData(false)
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

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri)
    {
        if (mListener != null)
        {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
        {
            mListener = context
        } else
        {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach()
    {
        context.supportActionBar!!.show()
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object
    {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConfigFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ConfigFragment
        {
            val fragment = ConfigFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
