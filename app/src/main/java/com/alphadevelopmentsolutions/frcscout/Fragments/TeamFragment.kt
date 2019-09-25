package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        teamViewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)

        loadFragmentsThread = Thread(Runnable {
            loadingThread.join()

            teamViewPagerAdapter.addFragment(MatchListFragment.newInstance(team!!), context.getString(R.string.matches))
            teamViewPagerAdapter.addFragment(RobotInfoFragment.newInstance(team!!), context.getString(R.string.info))
            teamViewPagerAdapter.addFragment(RobotMediaListFragment.newInstance(team!!), context.getString(R.string.media))
            teamViewPagerAdapter.addFragment(QuickStatsFragment.newInstance(team!!), context.getString(R.string.stats))

        })

        loadFragmentsThread.start()
    }

    private lateinit var teamViewPagerAdapter: FragmentViewPagerAdapter

    private lateinit var loadFragmentsThread: Thread

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team, container, false)
        context.currentZIndex++
        view.z = zIndex

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
                AddPhotoFab.backgroundTintList = ColorStateList.valueOf(primaryColor)
                AddPhotoFab.rippleColor = primaryColorDark
            }
        }

        loadFragmentsThread.join()

        view.AddPhotoFab.setOnClickListener { context.changeFragment(RobotMediaFragment.newInstance(null, team!!), true) }

        //update the app bar title to the team name
        context.setToolbarTitle(team!!.toString())

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
                    FacebookButton.setOnClickListener{ context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(facebookURL) }) }
                else
                    FacebookButton.visibility = View.GONE

                if (twitterURL != "")
                    TwitterButton.setOnClickListener{ context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(twitterURL) }) }
                else
                    TwitterButton.visibility = View.GONE

                if (instagramURL != "")
                    InstagramButton.setOnClickListener{ context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(instagramURL) }) }
                else
                    InstagramButton.visibility = View.GONE

                if (youtubeURL != "")
                    YoutubeButton.setOnClickListener{ context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(youtubeURL) }) }
                else
                    YoutubeButton.visibility = View.GONE

                if (websiteURL != "")
                    WebsiteButton.setOnClickListener{ context.startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(websiteURL) }) }
                else
                    WebsiteButton.visibility = View.GONE
            }

            TeamNumberNameTextView.text = "${team!!.id.toString()} - ${team!!.name}"
            TeamLocationTextView.text = "${team!!.city}, ${team!!.stateProvince}, ${team!!.country}"

            TeamViewPager.adapter = teamViewPagerAdapter
            TeamViewPager.offscreenPageLimit = 5
            TeamTabLayout.setupWithViewPager(TeamViewPager)

            val child = TeamTabLayout.getChildAt(0) as ViewGroup

            for(i in 0 until child.childCount)
            {
                with(child.getChildAt(i))
                {
                    layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
                        weight = 1f
                        width = LinearLayout.LayoutParams.MATCH_PARENT
                    }
                }
            }

            TeamViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
            {
                override fun onPageScrolled(i: Int, v: Float, i1: Int)
                {

                }

                override fun onPageSelected(i: Int)
                {
                    if (i == 2)
                        AddPhotoFab.show()
                    else if (AddPhotoFab.isShown)
                        AddPhotoFab.hide()
                }

                override fun onPageScrollStateChanged(i: Int)
                {

                }
            })

            if(TeamViewPager.currentItem != 2)
                AddPhotoFab.hide()
        }

        return view
    }

    override fun onDestroyView()
    {
        context.unlockDrawerLayout()
        super.onDestroyView()
    }

    override fun onDetach()
    {
        context.currentZIndex--
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
