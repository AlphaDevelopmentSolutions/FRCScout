package com.alphadevelopmentsolutions.frcscout.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Fragments.RobotMediaFragment
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*

internal class RobotMediaListRecyclerViewAdapter(private val team: Team, private val robotMedia: ArrayList<RobotMedia>, private val context: MainActivity) : RecyclerView.Adapter<RobotMediaListRecyclerViewAdapter.ViewHolder>()
{

    internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var robotImageView: ImageView

        init
        {

            robotImageView = view.findViewById(R.id.RobotImageView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the event layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_robot_media, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        //set scores
        viewHolder.robotImageView.setImageBitmap(robotMedia[viewHolder.adapterPosition].imageBitmap)

        viewHolder.robotImageView.setOnClickListener { context.changeFragment(RobotMediaFragment.newInstance(robotMedia[viewHolder.adapterPosition], team), true) }
    }

    override fun getItemCount(): Int
    {
        return robotMedia.size
    }
}
