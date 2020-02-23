package com.alphadevelopmentsolutions.frcscout.adapter

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Event
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.fragment.*
import com.alphadevelopmentsolutions.frcscout.view.database.MatchDatabaseView
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_card_match.view.*
import java.lang.reflect.Type
import java.util.*

internal class MatchListRecyclerViewAdapter(
        event: Event,
        private val team: Team?,
        private val matches: MutableList<MatchDatabaseView>,
        private val context: MainActivity,
        private val fragmentOnClick: Type) : RecyclerView.Adapter<MatchListRecyclerViewAdapter.ViewHolder>()
{
    internal class ViewHolder(val view: View, context: MainActivity) : RecyclerView.ViewHolder(view)
    {
        init
        {
            view.ViewScoutCardButton.setTextColor(context.primaryColor)
            (view.ViewScoutCardButton as MaterialButton).rippleColor = context.buttonRipple

            view.AddScoutCardButton.setTextColor(context.primaryColor)
            (view.AddScoutCardButton as MaterialButton).rippleColor = context.buttonRipple

            view.ViewMatchButton.setTextColor(context.primaryColor)
            (view.ViewMatchButton as MaterialButton).rippleColor = context.buttonRipple

            view.ViewChecklistItemButton.setTextColor(context.primaryColor)
            (view.ViewChecklistItemButton as MaterialButton).rippleColor = context.buttonRipple
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the eventId layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_match, viewGroup, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val matchView = matches[viewHolder.adapterPosition]

        //set matchId numbers
        viewHolder.view.MatchIdTextView.text = matchView.toString()

        //set teams
        viewHolder.view.BlueAllianceTeamOneIdTextView.text = matchView.blueAllianceTeamOne.toString()
        viewHolder.view.BlueAllianceTeamTwoIdTextView.text = matchView.blueAllianceTeamTwo.toString()
        viewHolder.view.BlueAllianceTeamThreeIdTextView.text = matchView.blueAllianceTeamThree.toString()

        viewHolder.view.RedAllianceTeamOneIdTextView.text = matchView.redAllianceTeamOne.toString()
        viewHolder.view.RedAllianceTeamTwoIdTextView.text = matchView.redAllianceTeamTwo.toString()
        viewHolder.view.RedAllianceTeamThreeIdTextView.text = matchView.redAllianceTeamThree.toString()


        //set the click listeners for each teamId on the matchId
        //clicking their number will bring you to their teamId page
        if (team?.id != matchView.match.blueAllianceTeamOneId)
        {
            viewHolder.view.BlueAllianceTeamOneIdTextView.setOnClickListener {
                context.changeFragment(TeamFragment.newInstance(matchView.blueAllianceTeamOne), true)
            }
        }

        if (team?.id != matchView.match.blueAllianceTeamTwoId)
        {
            viewHolder.view.BlueAllianceTeamTwoIdTextView.setOnClickListener {
                context.changeFragment(TeamFragment.newInstance(matchView.blueAllianceTeamTwo), true)
            }
        }

        if (team?.id != matchView.match.blueAllianceTeamThreeId)
        {
            viewHolder.view.BlueAllianceTeamThreeIdTextView.setOnClickListener {
                context.changeFragment(TeamFragment.newInstance(matchView.blueAllianceTeamThree), true)
            }
        }

        if (team?.id != matchView.match.redAllianceTeamOneId)
        {
            viewHolder.view.RedAllianceTeamOneIdTextView.setOnClickListener {
                context.changeFragment(TeamFragment.newInstance(matchView.redAllianceTeamOne), true)
            }
        }

        if (team?.id != matchView.match.redAllianceTeamTwoId)
        {
            viewHolder.view.RedAllianceTeamTwoIdTextView.setOnClickListener {
                context.changeFragment(TeamFragment.newInstance(matchView.redAllianceTeamTwo), true)
            }
        }

        if (team?.id != matchView.match.redAllianceTeamThreeId)
        {
            viewHolder.view.RedAllianceTeamThreeIdTextView.setOnClickListener {
                context.changeFragment(TeamFragment.newInstance(matchView.redAllianceTeamThree), true)
            }
        }

        //set score
        matchView.match.blueAllianceScore?.let {
            viewHolder.view.BlueAllianceScoreTextView.text = it.toString()
        }

        matchView.match.redAllianceScore?.let {
            viewHolder.view.RedAllianceScoreTextView.text = it.toString()
        }

        //teamId specified, style according to specified teamId
        if (team != null)
        {

            var spannableString = SpannableString(team.id.toString())
            spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)

            var selectedTeamAllianceColor: AllianceColor? = null

            //add the underline when viewing matches for a specific teamId
            when
            {
                team.id == matchView.match.blueAllianceTeamOneId ->
                {

                    viewHolder.view.BlueAllianceTeamOneIdTextView.text = spannableString
                    selectedTeamAllianceColor = AllianceColor.BLUE
                }
                team.id == matchView.match.blueAllianceTeamTwoId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.BlueAllianceTeamTwoIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.BLUE
                }
                team.id == matchView.match.blueAllianceTeamThreeId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.BlueAllianceTeamThreeIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.BLUE
                }
                team.id == matchView.match.redAllianceTeamOneId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.RedAllianceTeamOneIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.RED
                }
                team.id == matchView.match.redAllianceTeamTwoId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.RedAllianceTeamTwoIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.RED
                }
                team.id == matchView.match.redAllianceTeamThreeId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.RedAllianceTeamThreeIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.RED
                }

                //underline the score of the selected teamId
            }

            //underline the score of the selected teamId
            if (selectedTeamAllianceColor == AllianceColor.BLUE)
            {
                matchView.match.blueAllianceScore?.let {
                    spannableString = SpannableString(it.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.BlueAllianceScoreTextView.text = spannableString
                }

            }
            else
            {
                matchView.match.redAllianceScore?.let {
                    spannableString = SpannableString(it.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.RedAllianceScoreTextView.text = spannableString
                }
            }
        }

        //set the bold text for the winning teamId
        with(matchView.match.matchStatus)
        {
            when
            {
                this === Match.Status.BLUE ->
                {
                    viewHolder.view.BlueAllianceTeamOneIdTextView.setTypeface(null, Typeface.BOLD)
                    viewHolder.view.BlueAllianceTeamTwoIdTextView.setTypeface(null, Typeface.BOLD)
                    viewHolder.view.BlueAllianceTeamThreeIdTextView.setTypeface(null, Typeface.BOLD)
                    viewHolder.view.BlueAllianceScoreTextView.setTypeface(null, Typeface.BOLD)

                    viewHolder.view.RedAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.RedAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.RedAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.RedAllianceScoreTextView.setTypeface(null, Typeface.NORMAL)
                }
                this === Match.Status.RED ->
                {
                    viewHolder.view.BlueAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.BlueAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.BlueAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.BlueAllianceScoreTextView.setTypeface(null, Typeface.NORMAL)

                    viewHolder.view.RedAllianceTeamOneIdTextView.setTypeface(null, Typeface.BOLD)
                    viewHolder.view.RedAllianceTeamTwoIdTextView.setTypeface(null, Typeface.BOLD)
                    viewHolder.view.RedAllianceTeamThreeIdTextView.setTypeface(null, Typeface.BOLD)
                    viewHolder.view.RedAllianceScoreTextView.setTypeface(null, Typeface.BOLD)
                }
                this === Match.Status.TIE ->
                {
                    viewHolder.view.BlueAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.BlueAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.BlueAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.BlueAllianceScoreTextView.setTypeface(null, Typeface.NORMAL)

                    viewHolder.view.RedAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.RedAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.RedAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL)
                    viewHolder.view.RedAllianceScoreTextView.setTypeface(null, Typeface.NORMAL)
                }
            }
        }

        //Opens an option menu for various options on that score card
//        viewHolder.view.MatchOptionsImageView.setOnClickListener { } //TODO: options menu


        //logic for showing the view scout card button
        when (fragmentOnClick)
        {
            ScoutCardInfoFragment::class.java ->
            {
                viewHolder.view.ViewMatchButton.visibility = View.GONE
                viewHolder.view.ViewChecklistItemButton.visibility = View.GONE
                viewHolder.view.ViewScoutCardButton.visibility = View.GONE
                viewHolder.view.AddScoutCardButton.visibility = View.GONE

                //Sends you to the scout card fragment
                viewHolder.view.ViewScoutCardButton.setOnClickListener {
                    //show matchId
                    context.changeFragment(
                            ScoutCardInfoFragment.newInstance(matches[viewHolder.adapterPosition].match, team!!),
                            true
                    )
                }

                viewHolder.view.ViewMatchButton.visibility = View.VISIBLE

                //Sends you to the matchId overview fragment
                viewHolder.view.ViewMatchButton.setOnClickListener {
                    context.changeFragment(
                            MatchFragment.newInstance(
                                    matches[viewHolder.adapterPosition].match
                            ),
                            true
                    )
                }
            }
            ChecklistFragment::class.java ->
            {
                viewHolder.view.ViewChecklistItemButton.visibility = View.VISIBLE
                viewHolder.view.ViewMatchButton.visibility = View.GONE
                viewHolder.view.ViewScoutCardButton.visibility = View.GONE
                viewHolder.view.AddScoutCardButton.visibility = View.GONE

                //Sends you to the checklist fragment
                viewHolder.view.ViewChecklistItemButton.setOnClickListener {
                    context.changeFragment(
                            ChecklistFragment.newInstance(
                                    team!!,
                                    matches[viewHolder.adapterPosition].match
                            ),
                            true
                    )
                }
            }
            TeamListFragment::class.java ->
            {
                viewHolder.view.ViewMatchButton.visibility = View.VISIBLE
                viewHolder.view.ViewChecklistItemButton.visibility = View.GONE
                viewHolder.view.ViewScoutCardButton.visibility = View.GONE
                viewHolder.view.AddScoutCardButton.visibility = View.GONE

                //Sends you to the matchId overview fragment
                viewHolder.view.ViewMatchButton.setOnClickListener {
                    context.changeFragment(
                            MatchFragment.newInstance(
                                    matches[viewHolder.adapterPosition].match
                            ),
                            true
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int
    {
        return matches.size
    }
}
