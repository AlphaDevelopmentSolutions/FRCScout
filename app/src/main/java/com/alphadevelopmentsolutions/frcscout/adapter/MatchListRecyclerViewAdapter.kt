package com.alphadevelopmentsolutions.frcscout.adapter

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.classes.table.Event
import com.alphadevelopmentsolutions.frcscout.classes.table.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.classes.table.Team
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.fragment.ChecklistFragment
import com.alphadevelopmentsolutions.frcscout.fragment.ScoutCardInfoFragment
import com.alphadevelopmentsolutions.frcscout.fragment.TeamFragment
import com.alphadevelopmentsolutions.frcscout.fragment.TeamListFragment
import com.alphadevelopmentsolutions.frcscout.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_card_match.view.*
import java.lang.reflect.Type
import java.util.*

internal class MatchListRecyclerViewAdapter(
        event: Event,
        private val team: Team?,
        private val matches: ArrayList<Match>,
        private val context: MainActivity,
        private val fragmentOnClick: Type) : RecyclerView.Adapter<MatchListRecyclerViewAdapter.ViewHolder>()
{
    private val scoutCards: HashMap<Match, ArrayList<ScoutCardInfo>> = HashMap()

    init
    {
        if(fragmentOnClick == ScoutCardInfoFragment::class.java)
        {
            for(match in matches)
            {
                scoutCards[match] = ScoutCardInfo.getObjects(event, match, team, null, null, false, context.database)
            }
        }


    }

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
        //Inflate the event layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_match, viewGroup, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val match = matches[viewHolder.adapterPosition]

        //set match numbers
        viewHolder.view.MatchIdTextView.text = match.toString()

        //set teams
        viewHolder.view.BlueAllianceTeamOneIdTextView.text = match.blueAllianceTeamOneId.toString()
        viewHolder.view.BlueAllianceTeamTwoIdTextView.text = match.blueAllianceTeamTwoId.toString()
        viewHolder.view.BlueAllianceTeamThreeIdTextView.text = match.blueAllianceTeamThreeId.toString()

        viewHolder.view.RedAllianceTeamOneIdTextView.text = match.redAllianceTeamOneId.toString()
        viewHolder.view.RedAllianceTeamTwoIdTextView.text = match.redAllianceTeamTwoId.toString()
        viewHolder.view.RedAllianceTeamThreeIdTextView.text = match.redAllianceTeamThreeId.toString()


        //set the click listeners for each team on the match
        //clicking their number will bring you to their team page
        if (team?.id != match.blueAllianceTeamOneId)
        {
            viewHolder.view.BlueAllianceTeamOneIdTextView.setOnClickListener {
                with(Team(match.blueAllianceTeamOneId))
                {
                    if(load(context.database))
                        context.changeFragment(TeamFragment.newInstance(this), true)
                    else
                        context.showSnackbar(String.format(context.getString(R.string.load_fail), context.getString(R.string.team)))
                }
            }
        }

        if (team?.id != match.blueAllianceTeamTwoId)
        {
            viewHolder.view.BlueAllianceTeamTwoIdTextView.setOnClickListener {
                with(Team(match.blueAllianceTeamTwoId))
                {
                    if(load(context.database))
                        context.changeFragment(TeamFragment.newInstance(this), true)
                    else
                        context.showSnackbar(String.format(context.getString(R.string.load_fail), context.getString(R.string.team)))
                }
            }
        }

        if (team?.id != match.blueAllianceTeamThreeId)
        {
            viewHolder.view.BlueAllianceTeamThreeIdTextView.setOnClickListener {
                with(Team(match.blueAllianceTeamThreeId))
                {
                    if(load(context.database))
                        context.changeFragment(TeamFragment.newInstance(this), true)
                    else
                        context.showSnackbar(String.format(context.getString(R.string.load_fail), context.getString(R.string.team)))
                }
            }        }

        if (team?.id != match.redAllianceTeamOneId)
        {
            viewHolder.view.RedAllianceTeamOneIdTextView.setOnClickListener {
                with(Team(match.redAllianceTeamOneId))
                {
                    if(load(context.database))
                        context.changeFragment(TeamFragment.newInstance(this), true)
                    else
                        context.showSnackbar(String.format(context.getString(R.string.load_fail), context.getString(R.string.team)))
                }
            }        }

        if (team?.id != match.redAllianceTeamTwoId)
        {
            viewHolder.view.RedAllianceTeamTwoIdTextView.setOnClickListener {
                with(Team(match.redAllianceTeamTwoId))
                {
                    if(load(context.database))
                        context.changeFragment(TeamFragment.newInstance(this), true)
                    else
                        context.showSnackbar(String.format(context.getString(R.string.load_fail), context.getString(R.string.team)))
                }
            }        }

        if (team?.id != match.redAllianceTeamThreeId)
        {
            viewHolder.view.RedAllianceTeamThreeIdTextView.setOnClickListener {
                with(Team(match.redAllianceTeamThreeId))
                {
                    if(load(context.database))
                        context.changeFragment(TeamFragment.newInstance(this), true)
                    else
                        context.showSnackbar(String.format(context.getString(R.string.load_fail), context.getString(R.string.team)))
                }
            }        }

        //set score
        viewHolder.view.BlueAllianceScoreTextView.text = match.blueAllianceScore.toString()
        viewHolder.view.RedAllianceScoreTextView.text = match.redAllianceScore.toString()

        //team specified, style according to specified team
        if (team != null)
        {

            var spannableString = SpannableString(team.id.toString())
            spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)

            var selectedTeamAllianceColor: AllianceColor? = null

            //add the underline when viewing matches for a specific team
            when
            {
                team.id == match.blueAllianceTeamOneId ->
                {

                    viewHolder.view.BlueAllianceTeamOneIdTextView.text = spannableString
                    selectedTeamAllianceColor = AllianceColor.BLUE
                }
                team.id == match.blueAllianceTeamTwoId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.BlueAllianceTeamTwoIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.BLUE
                }
                team.id == match.blueAllianceTeamThreeId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.BlueAllianceTeamThreeIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.BLUE
                }
                team.id == match.redAllianceTeamOneId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.RedAllianceTeamOneIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.RED
                }
                team.id == match.redAllianceTeamTwoId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.RedAllianceTeamTwoIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.RED
                }
                team.id == match.redAllianceTeamThreeId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.view.RedAllianceTeamThreeIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.RED
                }

                //underline the score of the selected team
            }

            //underline the score of the selected team
            if (selectedTeamAllianceColor == AllianceColor.BLUE)
            {
                spannableString = SpannableString(match.blueAllianceScore.toString())
                spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                viewHolder.view.BlueAllianceScoreTextView.text = spannableString
            } else
            {
                spannableString = SpannableString(match.redAllianceScore.toString())
                spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                viewHolder.view.RedAllianceScoreTextView.text = spannableString
            }
        }

        //set the bold text for the winning team
        with(match.matchStatus)
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

                //no card available, show the add card button
                if (scoutCards[match]!!.size < 1)
                {
                    viewHolder.view.AddScoutCardButton.visibility = View.VISIBLE

                    //Sends you to the scout card fragment
                    viewHolder.view.AddScoutCardButton.setOnClickListener {
                        //add new card
                        context.changeFragment(ScoutCardInfoFragment.newInstance(match, team!!), true)
                    }
                }

                //card available, show the view scout card button
                else
                {
                    viewHolder.view.ViewScoutCardButton.visibility = View.VISIBLE

                    //Sends you to the scout card fragment
                    viewHolder.view.ViewScoutCardButton.setOnClickListener {
                        //show match
                        context.changeFragment(ScoutCardInfoFragment.newInstance(match, team!!), true)
                    }
                }

                viewHolder.view.ViewMatchButton.visibility = View.VISIBLE

                //Sends you to the match overview fragment
                viewHolder.view.ViewMatchButton.setOnClickListener { context.changeFragment(TeamListFragment.newInstance(matches[viewHolder.adapterPosition], null), true) }
            }
            ChecklistFragment::class.java ->
            {
                viewHolder.view.ViewChecklistItemButton.visibility = View.VISIBLE
                viewHolder.view.ViewMatchButton.visibility = View.GONE
                viewHolder.view.ViewScoutCardButton.visibility = View.GONE
                viewHolder.view.AddScoutCardButton.visibility = View.GONE

                //Sends you to the checklist fragment
                viewHolder.view.ViewChecklistItemButton.setOnClickListener { context.changeFragment(ChecklistFragment.newInstance(team!!, matches[viewHolder.adapterPosition]), true) }
            }
            TeamListFragment::class.java ->
            {
                viewHolder.view.ViewMatchButton.visibility = View.VISIBLE
                viewHolder.view.ViewChecklistItemButton.visibility = View.GONE
                viewHolder.view.ViewScoutCardButton.visibility = View.GONE
                viewHolder.view.AddScoutCardButton.visibility = View.GONE

                //Sends you to the match overview fragment
                viewHolder.view.ViewMatchButton.setOnClickListener { context.changeFragment(TeamListFragment.newInstance(matches[viewHolder.adapterPosition], null), true) }
            }
        }
    }

    override fun getItemCount(): Int
    {
        return matches.size
    }
}
