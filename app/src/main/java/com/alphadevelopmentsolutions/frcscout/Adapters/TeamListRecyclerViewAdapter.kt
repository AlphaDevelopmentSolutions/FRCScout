package com.alphadevelopmentsolutions.frcscout.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardInfoFragment
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment
import com.alphadevelopmentsolutions.frcscout.R
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_card_team.view.*
import java.io.File

internal class TeamListRecyclerViewAdapter(private val match: Match?, private val teamList: ArrayList<Team>, private val context: MainActivity) : RecyclerView.Adapter<TeamListRecyclerViewAdapter.ViewHolder>()
{
    private var teamHasScoutCardInfos: HashMap<Int, Boolean> = HashMap()

    init
    {
        if(match != null)
        {
            for(team in teamList)
                teamHasScoutCardInfos[team.id] = ScoutCardInfo.getObjects(null, match, team, null, null, false, context.database).size > 0
        }

    }

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
        //Inflate the event layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_team, viewGroup, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val team = teamList[viewHolder.adapterPosition]
        //Set the content on the card
        viewHolder.view.TeamNameTextView.text = team.toString()
        viewHolder.view.TeamLocationTextView.text = String.format("%s, %s, %s", team.city, team.stateProvince, team.country)

        //load the photo if the file exists
        if (team.imageFileURI != "")
            Picasso.get()
                    .load(Uri.fromFile(File(team.imageFileURI)))
                    .fit()
                    .centerCrop()
                    .into(viewHolder.view.TeamLogoImageView)
        else
            viewHolder.view.TeamLogoImageView.setImageDrawable(context.getDrawable(R.drawable.frc_logo))


        with(viewHolder.view.ViewTeamButton)
        {

            //Sends you to the team fragment
            if (match == null)
                setOnClickListener { this@TeamListRecyclerViewAdapter.context.changeFragment(TeamFragment.newInstance(teamList[viewHolder.adapterPosition]), true) }

            else
            {
                //scout card found, show that one
                if (teamHasScoutCardInfos[team.id] == true)
                {
                    text = context.getString(R.string.view_scout_card)
                    setOnClickListener { this@TeamListRecyclerViewAdapter.context.changeFragment(ScoutCardInfoFragment.newInstance(match, teamList[viewHolder.adapterPosition]), true) }
                }

                //no scout card found, add new one
                else
                {
                    text = context.getString(R.string.add_scout_card)
                    setOnClickListener { this@TeamListRecyclerViewAdapter.context.changeFragment(ScoutCardInfoFragment.newInstance(match, teamList[viewHolder.adapterPosition]), true) }
                }

                viewHolder.view.TeamNameTextView.setOnClickListener { this@TeamListRecyclerViewAdapter.context.changeFragment(TeamFragment.newInstance(teamList[viewHolder.adapterPosition]), true) }
            }
        }
    }

    override fun getItemCount(): Int
    {
        return teamList.size
    }
}
