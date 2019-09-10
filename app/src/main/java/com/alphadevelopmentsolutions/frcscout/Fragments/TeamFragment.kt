package com.alphadevelopmentsolutions.frcscout.Fragments

import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.FontAwesomeIcon
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class TeamFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }
    
    private var headerConstraintLayout: ConstraintLayout? = null

    private var teamTabLayout: TabLayout? = null
    private var teamViewPager: ViewPager? = null

    private var teamNumberNameTextView: TextView? = null
    private var teamLocationTextView: TextView? = null

    private var teamLogoImageView: CircleImageView? = null

    private var facebookFontAwesomeBrandIcon: FontAwesomeIcon? = null
    private var twitterFontAwesomeBrandIcon: FontAwesomeIcon? = null
    private var instagramFontAwesomeBrandIcon: FontAwesomeIcon? = null
    private var youtubeFontAwesomeBrandIcon: FontAwesomeIcon? = null
    private var websiteFontAwesomeSolidIcon: FontAwesomeIcon? = null

    private var teamFloatingActionMenu: FloatingActionMenu? = null
    private var addRobotPhotoFloatingActionButton: FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team, container, false)
        context.isToolbarScrollable = false

        //gets rid of the shadow on the actionbar
        context.dropActionBar()
        context.lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })

        //assign the vars to the views on the page
        headerConstraintLayout = view.findViewById(R.id.HeaderConstraintLayout)

        teamNumberNameTextView = view.findViewById(R.id.TeamNumberNameTextView)
        teamLocationTextView = view.findViewById(R.id.TeamLocationTextView)

        teamTabLayout = view.findViewById(R.id.TeamTabLayout)
        teamViewPager = view.findViewById(R.id.TeamViewPager)

        teamLogoImageView = view.findViewById(R.id.TeamLogoImageView)

        facebookFontAwesomeBrandIcon = view.findViewById(R.id.FacebookFontAwesomeBrandIcon)
        twitterFontAwesomeBrandIcon = view.findViewById(R.id.TwitterFontAwesomeBrandIcon)
        instagramFontAwesomeBrandIcon = view.findViewById(R.id.InstagramFontAwesomeBrandIcon)
        youtubeFontAwesomeBrandIcon = view.findViewById(R.id.YoutubeFontAwesomeBrandIcon)
        websiteFontAwesomeSolidIcon = view.findViewById(R.id.WebsiteFontAwesomeSolidIcon)

        teamFloatingActionMenu = view.findViewById(R.id.TeamFloatingActionMenu)
        addRobotPhotoFloatingActionButton = view.findViewById(R.id.AddRobotPhotoFloatingActionButton)

        headerConstraintLayout!!.setBackgroundColor(context.primaryColor)
        teamTabLayout!!.setBackgroundColor(context.primaryColor)
        teamTabLayout!!.setSelectedTabIndicatorColor(context.primaryColorDark)
        teamFloatingActionMenu!!.menuButtonColorNormal = context.primaryColor
        teamFloatingActionMenu!!.menuButtonColorPressed = context.primaryColorDark
        addRobotPhotoFloatingActionButton!!.colorNormal = context.primaryColor
        addRobotPhotoFloatingActionButton!!.colorPressed = context.primaryColorDark

        loadingThread.join()

        addRobotPhotoFloatingActionButton!!.setOnClickListener { context.changeFragment(RobotMediaFragment.newInstance(null, team!!), true) }

        //update the app bar title to the team name
        context.setToolbarTitle(team!!.id.toString() + " - " + team!!.name)

        //load the photo if the file exists
        if (team!!.imageFileURI != "")
            Picasso.get()
                    .load(Uri.fromFile(File(team!!.imageFileURI)))
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.frc_logo)
                    .error(R.drawable.frc_logo)
                    .into(teamLogoImageView)
        else
            teamLogoImageView!!.setImageDrawable(context.getDrawable(R.drawable.frc_logo))

        //checks to see if the team has a valid URL for each social media, if not hide the icon
        if (team!!.facebookURL != "")
            facebookFontAwesomeBrandIcon!!.setURL(team!!.facebookURL, context)
        else
            facebookFontAwesomeBrandIcon!!.hide()

        if (team!!.twitterURL != "")
            twitterFontAwesomeBrandIcon!!.setURL(team!!.twitterURL, context)
        else
            twitterFontAwesomeBrandIcon!!.hide()

        if (team!!.instagramURL != "")
            instagramFontAwesomeBrandIcon!!.setURL(team!!.instagramURL, context)
        else
            instagramFontAwesomeBrandIcon!!.hide()

        if (team!!.youtubeURL != "")
            youtubeFontAwesomeBrandIcon!!.setURL(team!!.youtubeURL, context)
        else
            youtubeFontAwesomeBrandIcon!!.hide()

        if (team!!.websiteURL != "")
            websiteFontAwesomeSolidIcon!!.setURL(team!!.websiteURL, context)
        else
            websiteFontAwesomeSolidIcon!!.hide()

        teamNumberNameTextView!!.text = team!!.id.toString() + " - " + team!!.name
        teamLocationTextView!!.text = team!!.city + ", " + team!!.stateProvince + ", " + team!!.country

        val teamViewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)

        teamViewPagerAdapter.addFragment(MatchListFragment.newInstance(team), getString(R.string.scout_cards))
        teamViewPagerAdapter.addFragment(RobotInfoFragment.newInstance(team!!), getString(R.string.robot_info))
        teamViewPagerAdapter.addFragment(RobotMediaListFragment.newInstance(team!!), getString(R.string.robot_images))
        teamViewPagerAdapter.addFragment(QuickStatsFragment.newInstance(team!!), getString(R.string.quick_stats))

        teamViewPager!!.adapter = teamViewPagerAdapter
        teamViewPager!!.offscreenPageLimit = 5
        teamTabLayout!!.setupWithViewPager(teamViewPager)

        teamViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrolled(i: Int, v: Float, i1: Int)
            {

            }

            override fun onPageSelected(i: Int)
            {
                if (i == 3)
                    teamFloatingActionMenu!!.hideMenu(true)
                else if (teamFloatingActionMenu!!.isMenuHidden)
                    teamFloatingActionMenu!!.showMenu(true)


            }

            override fun onPageScrollStateChanged(i: Int)
            {

            }
        })

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
