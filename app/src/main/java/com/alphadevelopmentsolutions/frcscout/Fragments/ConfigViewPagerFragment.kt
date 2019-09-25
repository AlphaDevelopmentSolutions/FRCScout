package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter
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
        context.supportActionBar!!.hide()
        context.lockDrawerLayout()
        context.isToolbarScrollable = false

        val view = inflater.inflate(R.layout.fragment_config_view_pager, container, false)

        loadingThread.join()

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
                    view.NextBackButton.text = context.getString(R.string.back)

                else
                    view.NextBackButton.text = context.getString(R.string.next)

            }

        })

        //change the page according to button text
        view.NextBackButton.setOnClickListener {

            if(view.NextBackButton.text == context.getString(R.string.next))
                view.ViewPager.currentItem = view.ViewPager.currentItem + 1

            else
                view.ViewPager.currentItem = view.ViewPager.currentItem - 1
        }

        return view
    }


    override fun onDestroyView()
    {
        context.supportActionBar!!.show()
        context.unlockDrawerLayout()
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
