package com.alphadevelopmentsolutions.frcscout.Adapters

import android.graphics.Typeface
import android.support.design.button.MaterialButton
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.Fragments.ChecklistFragment
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardInfoFragment
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamListFragment
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.layout_card_match.view.*
import java.lang.reflect.Type
import java.util.*

internal class MatchListRecyclerViewAdapter(
        private val event: Event,
        private val team: Team?,
        private val matches: ArrayList<Match>,
        private val context: MainActivity,
        private val fragmentOnClick: Type) : RecyclerView.Adapter<MatchListRecyclerViewAdapter.ViewHolder>()
{
    private val scoutCards: HashMap<Match, ArrayList<ScoutCardInfo>> = HashMap()

    internal class ViewHolder(val view: View, context: MainActivity) : RecyclerView.ViewHolder(view)
    {
        init
        {
            view.ViewMatchButton.setTextColor(context.primaryColor)
            (view.ViewMatchButton as MaterialButton).rippleColor = context.buttonRipple

            view.AddCardButton.setTextColor(context.primaryColor)
            (view.AddCardButton as MaterialButton).rippleColor = context.buttonRipple
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
        if (team == null || team.id != Integer.parseInt(viewHolder.view.BlueAllianceTeamOneIdTextView.text.toString()))
        {
            (viewHolder.view.BlueAllianceTeamOneIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.view.BlueAllianceTeamOneIdTextView.text.toString()), context.database)), true) }
        }

        if (team == null || team.id != Integer.parseInt(viewHolder.view.BlueAllianceTeamTwoIdTextView.text.toString()))
        {
            (viewHolder.view.BlueAllianceTeamTwoIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.view.BlueAllianceTeamTwoIdTextView.text.toString()), context.database)), true) }
        }

        if (team == null || team.id != Integer.parseInt(viewHolder.view.BlueAllianceTeamThreeIdTextView.text.toString()))
        {
            (viewHolder.view.BlueAllianceTeamThreeIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.view.BlueAllianceTeamThreeIdTextView.text.toString()), context.database)), true) }
        }

        if (team == null || team.id != Integer.parseInt(viewHolder.view.RedAllianceTeamOneIdTextView.text.toString()))
        {
            (viewHolder.view.RedAllianceTeamOneIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.view.RedAllianceTeamOneIdTextView.text.toString()), context.database)), true) }
        }

        if (team == null || team.id != Integer.parseInt(viewHolder.view.RedAllianceTeamTwoIdTextView.text.toString()))
        {
            (viewHolder.view.RedAllianceTeamTwoIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.view.RedAllianceTeamTwoIdTextView.text.toString()), context.database)), true) }
        }

        if (team == null || team.id != Integer.parseInt(viewHolder.view.RedAllianceTeamThreeIdTextView.text.toString()))
        {
            (viewHolder.view.RedAllianceTeamThreeIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.view.RedAllianceTeamThreeIdTextView.text.toString()), context.database)), true) }
        }

        //set score
        viewHolder.view.BlueAllianceScoreTextView.text = match.blueAllianceScore.toString()
        viewHolder.view.RedAllianceScoreTextView.text = match.redAllianceScore.toString()

        var selectedTeamAllianceColor: AllianceColor? = null
        var spannableString: SpannableString

        //set the bold text for the winning team
        val matchStatus = match.matchStatus

        //team specified, style according to specified team
        if (team != null)
        {
            //add the underline when viewing matches for a specific team
            when
            {
                team.id == match.blueAllianceTeamOneId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
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

        when
        {
            matchStatus === Match.Status.BLUE ->
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
            matchStatus === Match.Status.RED ->
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
            matchStatus === Match.Status.TIE ->
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

        //Opens an option menu for various options on that score card
        viewHolder.view.MatchOptionsImageView.setOnClickListener { } //TODO: options menu


        //logic for showing the view scout card button
        if (fragmentOnClick == ScoutCardInfoFragment::class.java)
        {
            if(scoutCards[match] == null || scoutCards[match]!!.size < 1)
                scoutCards[match] = ScoutCardInfo.getObjects(event, match, team, null, null, false, context.database)

            //no card available, show the add card button
            if (scoutCards[match]!!.size < 1)
            {
                viewHolder.view.ViewMatchButton.visibility = View.GONE
                viewHolder.view.AddCardButton.visibility = View.VISIBLE

                //Sends you to the scout card fragment
                viewHolder.view.AddCardButton.setOnClickListener {
                    //add new card
                    context.changeFragment(ScoutCardInfoFragment.newInstance(match, team!!), true)
                }
            }

            //card available, show the view match button
            else
            {
                viewHolder.view.ViewMatchButton.visibility = View.VISIBLE
                viewHolder.view.AddCardButton.visibility = View.GONE

                //Sends you to the scout card fragment
                viewHolder.view.ViewMatchButton.setOnClickListener {
                    //show match
                    context.changeFragment(ScoutCardInfoFragment.newInstance(match, team!!), true)
                }
            }
        }

        else if (fragmentOnClick == ChecklistFragment::class.java)
        {
            viewHolder.view.ViewMatchButton.text = context.getString(R.string.view_checklist)
            //Sends you to the checklist fragment
            viewHolder.view.ViewMatchButton.setOnClickListener { context.changeFragment(ChecklistFragment.newInstance(team!!, matches[viewHolder.adapterPosition]), true) }
        }

        else if (fragmentOnClick == TeamListFragment::class.java)
        {
            viewHolder.view.ViewMatchButton.text = context.getString(R.string.view_match)
            //Sends you to the match overview fragment
            viewHolder.view.ViewMatchButton.setOnClickListener { context.changeFragment(TeamListFragment.newInstance(matches[viewHolder.adapterPosition], null), true) }
        }
    }

    override fun getItemCount(): Int
    {
        return matches.size
    }
}
