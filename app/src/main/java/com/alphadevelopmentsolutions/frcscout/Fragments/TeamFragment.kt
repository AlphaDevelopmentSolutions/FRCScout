package com.alphadevelopmentsolutions.frcscout.Fragments

import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_team.view.*
import java.io.File

class TeamFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team, container, false)
        
        with(context)
        {
            
            dropActionBar()
            lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })
            isToolbarScrollable = false
            
            with(view)
            {
                HeaderConstraintLayout.setBackgroundColor(primaryColor)
                TeamTabLayout.setBackgroundColor(primaryColor)
                TeamTabLayout.setSelectedTabIndicatorColor(primaryColorDark)
                TeamFloatingActionMenu.menuButtonColorNormal = primaryColor
                TeamFloatingActionMenu.menuButtonColorPressed = primaryColorDark
                AddRobotPhotoFloatingActionButton.colorNormal = primaryColor
                AddRobotPhotoFloatingActionButton.colorPressed = primaryColorDark
            }
        }
            

        loadingThread.join()

        view.AddRobotPhotoFloatingActionButton.setOnClickListener { context.changeFragment(RobotMediaFragment.newInstance(null, team!!), true) }

        //update the app bar title to the team name
        context.setToolbarTitle(team!!.id.toString() + " - " + team!!.name)

        with(view)
        {
            //load the photo if the file exists
            if (team!!.imageFileURI != "")
                Picasso.get()
                        .load(Uri.fromFile(File(team!!.imageFileURI)))
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.frc_logo)
                        .error(R.drawable.frc_logo)
                        .into(TeamLogoImageView)
            else
                TeamLogoImageView.setImageDrawable(context.getDrawable(R.drawable.frc_logo))

            //checks to see if the team has a valid URL for each social media, if not hide the icon
            with(team!!)
            {
                if (facebookURL != "")
                    FacebookFontAwesomeBrandIcon.setURL(facebookURL, context)
                else
                    FacebookFontAwesomeBrandIcon.hide()

                if (twitterURL != "")
                    TwitterFontAwesomeBrandIcon.setURL(twitterURL, context)
                else
                    TwitterFontAwesomeBrandIcon.hide()

                if (instagramURL != "")
                    InstagramFontAwesomeBrandIcon.setURL(instagramURL, context)
                else
                    InstagramFontAwesomeBrandIcon.hide()

                if (youtubeURL != "")
                    YoutubeFontAwesomeBrandIcon.setURL(youtubeURL, context)
                else
                    YoutubeFontAwesomeBrandIcon.hide()

                if (websiteURL != "")
                    WebsiteFontAwesomeSolidIcon.setURL(websiteURL, context)
                else
                    WebsiteFontAwesomeSolidIcon.hide()
            }

            TeamNumberNameTextView.text = "${team!!.id.toString()} - ${team!!.name}"
            TeamLocationTextView.text = "${team!!.city}, ${team!!.stateProvince}, ${team!!.country}"

            val teamViewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)

            teamViewPagerAdapter.addFragment(MatchListFragment.newInstance(team!!), getString(R.string.scout_cards))
            teamViewPagerAdapter.addFragment(RobotInfoFragment.newInstance(team!!), getString(R.string.robot_info))
            teamViewPagerAdapter.addFragment(RobotMediaListFragment.newInstance(team!!), getString(R.string.robot_images))
            teamViewPagerAdapter.addFragment(QuickStatsFragment.newInstance(team!!), getString(R.string.quick_stats))

            TeamViewPager.adapter = teamViewPagerAdapter
            TeamViewPager.offscreenPageLimit = 5
            TeamTabLayout.setupWithViewPager(TeamViewPager)

            TeamViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
            {
                override fun onPageScrolled(i: Int, v: Float, i1: Int)
                {

                }

                override fun onPageSelected(i: Int)
                {
                    if (i == 3)
                        TeamFloatingActionMenu.hideMenu(true)
                    else if (TeamFloatingActionMenu.isMenuHidden)
                        TeamFloatingActionMenu.showMenu(true)


                }

                override fun onPageScrollStateChanged(i: Int)
                {

                }
            })
        }

        return view
    }

    override fun onDetach()
    {
        context.unlockDrawerLayout()
        super.onDetach()
    }

    companion object
    {

        /**
         * Creates a new instance
         * @param team to show
         * @return A new instance of fragment [TeamFragment].
         */
        fun newInstance(team: Team): TeamFragment
        {
            val fragment = TeamFragment()
            val args = Bundle()
            args.putString(ARG_TEAM_JSON, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}
