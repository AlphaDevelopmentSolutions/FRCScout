package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.fragment_config_view_pager.view.*



class ConfigViewPagerFragment : MasterFragment()
{

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        //hide the actionbar while connecting
        context.supportActionBar!!.hide()
        context.lockDrawerLayout()

        val view = inflater.inflate(R.layout.fragment_config_view_pager, container, false)

        joinLoadingThread()

        //add all config frags to viewpager
        val viewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(ConfigFragment.newInstance(1), context.getString(R.string.welcome))
        viewPagerAdapter.addFragment(ConfigFragment.newInstance(2), context.getString(R.string.config_permission_header))
        viewPagerAdapter.addFragment(ConfigFragment.newInstance(3), context.getString(R.string.login))

        view.ViewPager.adapter = viewPagerAdapter
        view.ViewPagerDots.setViewPager(view.ViewPager)

        //add callback to check for if we are on the last page or not
        view.ViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(p0: Int)
            {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int)
            {
            }

            override fun onPageSelected(p0: Int)
            {
                if(p0 + 1 == view.ViewPager.adapter!!.count)
                    view.NextBackButton.text = getString(R.string.back)

                else
                    view.NextBackButton.text = getString(R.string.next)

            }

        })

        //change the page according to button text
        view.NextBackButton.setOnClickListener {

            if(view.NextBackButton.text == getString(R.string.next))
                view.ViewPager.currentItem = view.ViewPager.currentItem + 1

            else
                view.ViewPager.currentItem = view.ViewPager.currentItem - 1
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param index index of the config activity for the viewpager
         * @return A new instance of fragment ConfigFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): ConfigViewPagerFragment
        {
            val fragment = ConfigViewPagerFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
