package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.R

class SplashFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        context.supportActionBar!!.hide()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }



    override fun onDetach()
    {
        context.supportActionBar!!.show()
        super.onDetach()
    }


    companion object
    {
        /**
         * Creates a new instance
         * @return A new instance of fragment [SplashFragment].
         */
        fun newInstance(): SplashFragment
        {
            val fragment = SplashFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
