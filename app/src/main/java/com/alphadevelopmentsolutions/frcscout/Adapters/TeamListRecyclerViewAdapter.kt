package com.alphadevelopmentsolutions.frcscout.Adapters

import android.net.Uri
import android.support.design.button.MaterialButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment
import com.alphadevelopmentsolutions.frcscout.R
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

internal class TeamListRecyclerViewAdapter(private val match: Match?, private val teamList: ArrayList<Team>, private val context: MainActivity) : RecyclerView.Adapter<TeamListRecyclerViewAdapter.ViewHolder>()
{

    internal class ViewHolder(view: View, context: MainActivity) : RecyclerView.ViewHolder(view)
    {
        var teamNameTextView: TextView
        var teamLocationTextView: TextView
        var teamLogoImageView: ImageView
        var viewTeamButton: Button

        init
        {

            teamNameTextView = view.findViewById(R.id.TeamNameTextView)
            teamLocationTextView = view.findViewById(R.id.TeamLocationTextView)
            teamLogoImageView = view.findViewById(R.id.TeamLogoImageView)
            viewTeamButton = view.findViewById(R.id.ViewTeamButton)
            viewTeamButton.setTextColor(context.primaryColor)
            (viewTeamButton as MaterialButton).rippleColor = context.buttonRipple
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the event layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_team, viewGroup, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val team = teamList[viewHolder.adapterPosition]
        //Set the content on the card
        viewHolder.teamNameTextView.text = team.toString()
        viewHolder.teamLocationTextView.text = String.format("%s, %s, %s", team.city, team.stateProvince, team.country)

        //load the photo if the file exists
        if (team.imageFileURI != "")
            Picasso.get()
                    .load(Uri.fromFile(File(team.imageFileURI)))
                    .fit()
                    .centerCrop()
                    .into(viewHolder.teamLogoImageView)
        else
            viewHolder.teamLogoImageView.setImageDrawable(context.getDrawable(R.drawable.frc_logo))


        //send over to the teams home page frag
        if (match == null)
        {
            //Sends you to the team fragment
            viewHolder.viewTeamButton.setOnClickListener { context.changeFragment(TeamFragment.newInstance(teamList[viewHolder.adapterPosition]), true) }
        } else
        {
            val scoutCards = team.getScoutCards(null, match, null, false, context.getDatabase())

            //scout card found, show that one
            if (scoutCards!!.size > 0)
            {
                viewHolder.viewTeamButton.text = context.getString(R.string.view_scout_card)
                //Sends you to the scout card fragment
                viewHolder.viewTeamButton.setOnClickListener { context.changeFragment(ScoutCardFragment.newInstance(match, scoutCards[scoutCards.size - 1], teamList[viewHolder.adapterPosition]), true) }
            } else
            {
                viewHolder.viewTeamButton.text = context.getString(R.string.add_scout_card)
                //Sends you to the scout card fragment
                viewHolder.viewTeamButton.setOnClickListener { context.changeFragment(ScoutCardFragment.newInstance(match, null, teamList[viewHolder.adapterPosition]), true) }
            }//no scout card found, add new one

            //Add a listener to the name of the team that when clicked will send you to the team page
            viewHolder.teamNameTextView.setOnClickListener { context.changeFragment(TeamFragment.newInstance(teamList[viewHolder.adapterPosition]), true) }

        }//find the most recent scout card if any, and send to the scout card page
    }

    override fun getItemCount(): Int
    {
        return teamList.size
    }
}
