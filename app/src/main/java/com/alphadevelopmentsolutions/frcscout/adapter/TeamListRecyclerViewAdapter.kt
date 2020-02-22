package com.alphadevelopmentsolutions.frcscout.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.fragment.ScoutCardInfoFragment
import com.alphadevelopmentsolutions.frcscout.fragment.TeamFragment
import com.alphadevelopmentsolutions.frcscout.R
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_card_team.view.*
import java.io.File

internal class TeamListRecyclerViewAdapter(private val match: Match?, private val teamList: ArrayList<Team>, private val context: MainActivity) : RecyclerView.Adapter<TeamListRecyclerViewAdapter.ViewHolder>()
{

    internal class ViewHolder(val view: View, context: MainActivity) : RecyclerView.ViewHolder(view)
    {
        init
        {
            view.ViewTeamButton.setTextColor(context.primaryColor)
            (view.ViewTeamButton as MaterialButton).rippleColor = context.buttonRipple
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the eventId layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_team, viewGroup, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val team = teamList[viewHolder.adapterPosition]

        with(viewHolder.view)
        {

            //Set the content on the card
            TeamNameTextView.text = team.toString()
            TeamLocationTextView.text = String.format("%s, %s, %s", team.city, team.stateProvince, team.country)

            //load the photo if the file exists
            if (team.avatarUri != "")
                Picasso.get()
                        .load(Uri.fromFile(File(team.avatarUri)))
                        .fit()
                        .centerCrop()
                        .into(viewHolder.view.TeamLogoImageView)

            //Sends you to the teamId fragment
            if (match == null)
                ViewTeamButton.setOnClickListener { this@TeamListRecyclerViewAdapter.context.changeFragment(TeamFragment.newInstance(teamList[viewHolder.adapterPosition]), true) }

            else
            {
                ViewTeamButton.text = context.getString(R.string.view_scout_card)
                ViewTeamButton.setOnClickListener { this@TeamListRecyclerViewAdapter.context.changeFragment(ScoutCardInfoFragment.newInstance(match, teamList[viewHolder.adapterPosition]), true) }
            }

            TeamNameTextView.setOnClickListener { this@TeamListRecyclerViewAdapter.context.changeFragment(TeamFragment.newInstance(teamList[viewHolder.adapterPosition]), true) }

        }
    }

    override fun getItemCount(): Int
    {
        return teamList.size
    }
}
