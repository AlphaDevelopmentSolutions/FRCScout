package com.alphadevelopmentsolutions.frcscout.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*

internal class ScoutCardsRecyclerViewAdapter(private val team: Team, private val scoutCards: ArrayList<ScoutCardInfo>, private val context: MainActivity) : RecyclerView.Adapter<ScoutCardsRecyclerViewAdapter.ViewHolder>()
{

    internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {

        var matchIdTextView: TextView
        var matchOptionsImageView: ImageView
        var viewMatchButton: Button

        init
        {

            matchIdTextView = view.findViewById(R.id.MatchIdTextView)
            matchOptionsImageView = view.findViewById(R.id.MatchOptionsImageView)
            viewMatchButton = view.findViewById(R.id.ViewMatchButton)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the event layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_match, viewGroup, false)

        return ScoutCardsRecyclerViewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ScoutCardsRecyclerViewAdapter.ViewHolder, position: Int)
    {

        val scoutCard = scoutCards[viewHolder.adapterPosition]

        //set scores
        viewHolder.matchIdTextView.text = scoutCard.matchId.toString()

        //Opens an option menu for various options on that score card
        viewHolder.matchOptionsImageView.setOnClickListener { } //TODO: options menu


        //Sends you to the scout card fragment
        viewHolder.viewMatchButton.setOnClickListener {
            //swap fragments
            context.changeFragment(ScoutCardFragment.newInstance(null!!, scoutCards[viewHolder.adapterPosition], team), true)
        }
    }

    override fun getItemCount(): Int
    {
        return scoutCards.size
    }
}
