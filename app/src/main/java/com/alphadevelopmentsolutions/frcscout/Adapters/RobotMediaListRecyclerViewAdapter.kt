package com.alphadevelopmentsolutions.frcscout.Adapters

import android.graphics.Bitmap
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
import kotlinx.android.synthetic.main.layout_card_robot_media.view.*
import java.util.*

internal class RobotMediaListRecyclerViewAdapter(private val team: Team, private val robotMedia: ArrayList<RobotMedia>, private val context: MainActivity) : RecyclerView.Adapter<RobotMediaListRecyclerViewAdapter.ViewHolder>()
{
    private val robotMediaBitmaps: ArrayList<Bitmap> = ArrayList()

    init
    {
        for(media in robotMedia)
        {
            robotMediaBitmaps.add(media.imageBitmap)
        }
    }

    internal class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the event layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_robot_media, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        //set scores
        viewHolder.view.RobotImageView.setImageBitmap(robotMediaBitmaps[viewHolder.adapterPosition])

        viewHolder.view.RobotImageView.setOnClickListener { context.changeFragment(RobotMediaFragment.newInstance(robotMedia[viewHolder.adapterPosition], team), true) }
    }

    override fun getItemCount(): Int
    {
        return robotMedia.size
    }
}
