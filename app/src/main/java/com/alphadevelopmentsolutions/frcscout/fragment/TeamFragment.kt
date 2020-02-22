package com.alphadevelopmentsolutions.frcscout.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.alphadevelopmentsolutions.frcscout.adapter.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.extension.putUUID
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_team.view.*
import java.io.File

class TeamFragment : MasterFragment()
{

    override fun onBackPressed() = false

    private val teamViewPagerAdapter by lazy { FragmentViewPagerAdapter(childFragmentManager) }

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
                AddPhotoFab.backgroundTintList = ColorStateList.valueOf(primaryColor)
                AddPhotoFab.rippleColor = primaryColorDark
            }
        }

        teamId?.let { teamId ->

            VMProvider(this).teamViewModel.objWithId(teamId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { team ->


                                view.AddPhotoFab.setOnClickListener { context.changeFragment(RobotMediaFragment.newInstance(null, team), true) }

                                //update the app bar title to the teamId name
                                context.setToolbarTitle(teamId.toString())

                                with(view)
                                {
                                    //load the photo if the file exists
                                    if (team.imageFileURI != "")
                                        Picasso.get()
                                                .load(Uri.fromFile(File(team.imageFileURI)))
                                                .fit()
                                                .centerCrop()
                                                .into(TeamLogoImageView)

                                    //checks to see if the teamId has a valid URL for each social media, if not hide the icon
                                    with(team)
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

                                    TeamNumberNameTextView.text = "${team.id.toString()} - ${team.name}"
                                    TeamLocationTextView.text = "${team.city}, ${team.stateProvince}, ${team.country}"

                                    teamViewPagerAdapter.addFragment(MatchListFragment.newInstance(team), context.getString(R.string.matches))
                                    teamViewPagerAdapter.addFragment(RobotInfoFragment.newInstance(team), context.getString(R.string.info))
                                    teamViewPagerAdapter.addFragment(RobotMediaListFragment.newInstance(team), context.getString(R.string.media))
                                    teamViewPagerAdapter.addFragment(QuickStatsFragment.newInstance(team), context.getString(R.string.stats))

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
                            },
                            {
                                AppLog.error(it)
                            }
                    )

        }

        return view
    }

    override fun onDestroyView()
    {
        context.unlockDrawerLayout()
        super.onDestroyView()
    }

    companion object
    {

        /**
         * Creates a new instance
         * @param team to show
         * @return A new instance of fragment [TeamFragment].
         */
        fun newInstance(team: Team) = TeamFragment().apply {
            arguments = Bundle().apply {
                putUUID(ARG_TEAM_ID, team.id)
            }
        }
    }
}
