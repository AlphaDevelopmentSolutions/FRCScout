package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.alphadevelopmentsolutions.frcscout.adapter.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.fragment_config_view_pager.view.*

class ConfigViewPagerFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        //hide the actionbar while connecting
        activityContext.supportActionBar!!.hide()
        activityContext.lockDrawerLayout()
        activityContext.isToolbarScrollable = false

        val view = inflater.inflate(R.layout.fragment_config_view_pager, container, false)

        //add all config frags to viewpager
        val viewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(ConfigFragment.newInstance(1), activityContext.getString(R.string.welcome))
        viewPagerAdapter.addFragment(ConfigFragment.newInstance(2), activityContext.getString(R.string.login))

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
                    view.NextBackButton.text = activityContext.getString(R.string.back)

                else
                    view.NextBackButton.text = activityContext.getString(R.string.next)

            }

        })

        //change the page according to button text
        view.NextBackButton.setOnClickListener {

            if(view.NextBackButton.text == activityContext.getString(R.string.next))
                view.ViewPager.currentItem = view.ViewPager.currentItem + 1

            else
                view.ViewPager.currentItem = view.ViewPager.currentItem - 1
        }

        return view
    }


    override fun onDestroyView()
    {
        activityContext.supportActionBar!!.show()
        activityContext.unlockDrawerLayout()
        super.onDestroyView()
    }

    companion object
    {
        /**
         * Creates a new instance
         * @return A new instance of fragment [ConfigViewPagerFragment].
         */
        fun newInstance(): ConfigViewPagerFragment
        {
            val fragment = ConfigViewPagerFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
