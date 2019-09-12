package com.alphadevelopmentsolutions.frcscout.Adapters

import android.graphics.Typeface
import android.support.design.button.MaterialButton
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.Fragments.ChecklistFragment
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamListFragment
import com.alphadevelopmentsolutions.frcscout.R
import java.lang.reflect.Type
import java.util.*

internal class MatchListRecyclerViewAdapter(private val event: Event, private val team: Team?, private val matches: ArrayList<Match>, private val context: MainActivity, private val fragmentOnClick: Type) : RecyclerView.Adapter<MatchListRecyclerViewAdapter.ViewHolder>()
{

    private val scoutCards: HashMap<Match, ArrayList<ScoutCardInfo>> = HashMap()

    internal class ViewHolder(view: View, context: MainActivity) : RecyclerView.ViewHolder(view)
    {

        var matchIdTextView: TextView

        var blueAllianceTeamOneIdTextView: TextView
        var blueAllianceTeamTwoIdTextView: TextView
        var blueAllianceTeamThreeIdTextView: TextView

        var redAllianceTeamOneIdTextView: TextView
        var redAllianceTeamTwoIdTextView: TextView
        var redAllianceTeamThreeIdTextView: TextView

        var blueAllianceScoreTextView: TextView
        var redAllianceScoreTextView: TextView

        var matchOptionsImageView: ImageView

        var viewMatchButton: Button
        var addCardButton: Button

        init
        {

            matchIdTextView = view.findViewById(R.id.MatchIdTextView)

            blueAllianceTeamOneIdTextView = view.findViewById(R.id.BlueAllianceTeamOneIdTextView)
            blueAllianceTeamTwoIdTextView = view.findViewById(R.id.BlueAllianceTeamTwoIdTextView)
            blueAllianceTeamThreeIdTextView = view.findViewById(R.id.BlueAllianceTeamThreeIdTextView)

            redAllianceTeamOneIdTextView = view.findViewById(R.id.RedAllianceTeamOneIdTextView)
            redAllianceTeamTwoIdTextView = view.findViewById(R.id.RedAllianceTeamTwoIdTextView)
            redAllianceTeamThreeIdTextView = view.findViewById(R.id.RedAllianceTeamThreeIdTextView)

            blueAllianceScoreTextView = view.findViewById(R.id.BlueAllianceScoreTextView)
            redAllianceScoreTextView = view.findViewById(R.id.RedAllianceScoreTextView)

            matchOptionsImageView = view.findViewById(R.id.MatchOptionsImageView)

            viewMatchButton = view.findViewById(R.id.ViewMatchButton)
            viewMatchButton.setTextColor(context.primaryColor)
            (viewMatchButton as MaterialButton).rippleColor = context.buttonRipple

            addCardButton = view.findViewById(R.id.AddCardButton)
            addCardButton.setTextColor(context.primaryColor)
            (addCardButton as MaterialButton).rippleColor = context.buttonRipple
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
        viewHolder.matchIdTextView.text = match.toString()

        //set teams
        viewHolder.blueAllianceTeamOneIdTextView.text = match.blueAllianceTeamOneId.toString()
        viewHolder.blueAllianceTeamTwoIdTextView.text = match.blueAllianceTeamTwoId.toString()
        viewHolder.blueAllianceTeamThreeIdTextView.text = match.blueAllianceTeamThreeId.toString()

        viewHolder.redAllianceTeamOneIdTextView.text = match.redAllianceTeamOneId.toString()
        viewHolder.redAllianceTeamTwoIdTextView.text = match.redAllianceTeamTwoId.toString()
        viewHolder.redAllianceTeamThreeIdTextView.text = match.redAllianceTeamThreeId.toString()


        //set the click listeners for each team on the match
        //clicking their number will bring you to their team page
        if (team == null || team.id != Integer.parseInt(viewHolder.blueAllianceTeamOneIdTextView.text.toString()))
        {
            (viewHolder.blueAllianceTeamOneIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.blueAllianceTeamOneIdTextView.text.toString()), context.database)), true) }
        }

        if (team == null || team.id != Integer.parseInt(viewHolder.blueAllianceTeamTwoIdTextView.text.toString()))
        {
            (viewHolder.blueAllianceTeamTwoIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.blueAllianceTeamTwoIdTextView.text.toString()), context.database)), true) }
        }

        if (team == null || team.id != Integer.parseInt(viewHolder.blueAllianceTeamThreeIdTextView.text.toString()))
        {
            (viewHolder.blueAllianceTeamThreeIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.blueAllianceTeamThreeIdTextView.text.toString()), context.database)), true) }
        }

        if (team == null || team.id != Integer.parseInt(viewHolder.redAllianceTeamOneIdTextView.text.toString()))
        {
            (viewHolder.redAllianceTeamOneIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.redAllianceTeamOneIdTextView.text.toString()), context.database)), true) }
        }

        if (team == null || team.id != Integer.parseInt(viewHolder.redAllianceTeamTwoIdTextView.text.toString()))
        {
            (viewHolder.redAllianceTeamTwoIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.redAllianceTeamTwoIdTextView.text.toString()), context.database)), true) }
        }

        if (team == null || team.id != Integer.parseInt(viewHolder.redAllianceTeamThreeIdTextView.text.toString()))
        {
            (viewHolder.redAllianceTeamThreeIdTextView.parent as TableRow).setOnClickListener { context.changeFragment(TeamFragment.newInstance(Team(Integer.valueOf(viewHolder.redAllianceTeamThreeIdTextView.text.toString()), context.database)), true) }
        }

        //set score
        viewHolder.blueAllianceScoreTextView.text = match.blueAllianceScore.toString()
        viewHolder.redAllianceScoreTextView.text = match.redAllianceScore.toString()

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
                    viewHolder.blueAllianceTeamOneIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.BLUE
                }
                team.id == match.blueAllianceTeamTwoId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.blueAllianceTeamTwoIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.BLUE
                }
                team.id == match.blueAllianceTeamThreeId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.blueAllianceTeamThreeIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.BLUE
                }
                team.id == match.redAllianceTeamOneId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.redAllianceTeamOneIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.RED
                }
                team.id == match.redAllianceTeamTwoId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.redAllianceTeamTwoIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.RED
                }
                team.id == match.redAllianceTeamThreeId ->
                {
                    spannableString = SpannableString(team.id.toString())
                    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                    viewHolder.redAllianceTeamThreeIdTextView.text = spannableString

                    selectedTeamAllianceColor = AllianceColor.RED
                }

                //underline the score of the selected team
            }

            //underline the score of the selected team
            if (selectedTeamAllianceColor == AllianceColor.BLUE)
            {
                spannableString = SpannableString(match.blueAllianceScore.toString())
                spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                viewHolder.blueAllianceScoreTextView.text = spannableString
            } else
            {
                spannableString = SpannableString(match.redAllianceScore.toString())
                spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                viewHolder.redAllianceScoreTextView.text = spannableString
            }
        }

        when
        {
            matchStatus === Match.Status.BLUE ->
            {
                viewHolder.blueAllianceTeamOneIdTextView.setTypeface(null, Typeface.BOLD)
                viewHolder.blueAllianceTeamTwoIdTextView.setTypeface(null, Typeface.BOLD)
                viewHolder.blueAllianceTeamThreeIdTextView.setTypeface(null, Typeface.BOLD)
                viewHolder.blueAllianceScoreTextView.setTypeface(null, Typeface.BOLD)

                viewHolder.redAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.redAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.redAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.redAllianceScoreTextView.setTypeface(null, Typeface.NORMAL)
            }
            matchStatus === Match.Status.RED ->
            {
                viewHolder.blueAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.blueAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.blueAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.blueAllianceScoreTextView.setTypeface(null, Typeface.NORMAL)

                viewHolder.redAllianceTeamOneIdTextView.setTypeface(null, Typeface.BOLD)
                viewHolder.redAllianceTeamTwoIdTextView.setTypeface(null, Typeface.BOLD)
                viewHolder.redAllianceTeamThreeIdTextView.setTypeface(null, Typeface.BOLD)
                viewHolder.redAllianceScoreTextView.setTypeface(null, Typeface.BOLD)
            }
            matchStatus === Match.Status.TIE ->
            {
                viewHolder.blueAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.blueAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.blueAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.blueAllianceScoreTextView.setTypeface(null, Typeface.NORMAL)

                viewHolder.redAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.redAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.redAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL)
                viewHolder.redAllianceScoreTextView.setTypeface(null, Typeface.NORMAL)
            }
        }

        //Opens an option menu for various options on that score card
        viewHolder.matchOptionsImageView.setOnClickListener { } //TODO: options menu


        //logic for showing the view scout card button
        if (fragmentOnClick == ScoutCardFragment::class.java)
        {
            if(scoutCards[match] == null)
                scoutCards[match] = ScoutCardInfo.getObjects(event, match, team, null, null, false, context.database)

            //no card available, show the add card button
            if (scoutCards[match]!!.size < 1)
            {
                viewHolder.viewMatchButton.visibility = View.GONE
                viewHolder.addCardButton.visibility = View.VISIBLE

                //Sends you to the scout card fragment
                viewHolder.addCardButton.setOnClickListener {
                    //add new card
                    context.changeFragment(ScoutCardFragment.newInstance(match, team!!), true)
                }
            }

            //card available, show the view match button
            else
            {
                viewHolder.viewMatchButton.visibility = View.VISIBLE
                viewHolder.addCardButton.visibility = View.GONE

                //Sends you to the scout card fragment
                viewHolder.viewMatchButton.setOnClickListener {
                    //show match
                    context.changeFragment(ScoutCardFragment.newInstance(match, team!!), true)
                }
            }
        }

        else if (fragmentOnClick == ChecklistFragment::class.java)
        {
            viewHolder.viewMatchButton.text = context.getString(R.string.view_checklist)
            //Sends you to the checklist fragment
            viewHolder.viewMatchButton.setOnClickListener { context.changeFragment(ChecklistFragment.newInstance(team!!, matches[viewHolder.adapterPosition]), true) }
        }

        else if (fragmentOnClick == TeamListFragment::class.java)
        {
            viewHolder.viewMatchButton.text = context.getString(R.string.view_match)
            //Sends you to the match overview fragment
            viewHolder.viewMatchButton.setOnClickListener { context.changeFragment(TeamListFragment.newInstance(matches[viewHolder.adapterPosition], null), true) }
        }
    }

    override fun getItemCount(): Int
    {
        return matches.size
    }
}
