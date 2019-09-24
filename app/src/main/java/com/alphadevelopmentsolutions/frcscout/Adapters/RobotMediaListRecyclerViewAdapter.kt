package com.alphadevelopmentsolutions.frcscout.Adapters

import android.graphics.Bitmap
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Fragments.RobotMediaFragment
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.layout_card_robot_media.view.*
import kotlinx.android.synthetic.main.layout_dialog_confirm.view.*
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
        viewHolder.view.RobotImageView.setImageBitmap(robotMediaBitmaps[viewHolder.adapterPosition])

        viewHolder.view.ViewRobotMediaButton.setOnClickListener { context.changeFragment(RobotMediaFragment.newInstance(robotMedia[viewHolder.adapterPosition], team), true) }
        viewHolder.view.DeleteRobotMediaButton.setOnClickListener {

            val confirmDialogBuilder = AlertDialog.Builder(context)
            var confirmDialog: AlertDialog? = null
            val layout = LayoutInflater.from(context).inflate(R.layout.layout_dialog_confirm, null)
            layout.ConfirmTitle.text = "Delete Robot Media?"
            layout.ConfirmSupport.text = "Are you sure you want to delete this robot media? This can't be undone."
            layout.CancelButton.setOnClickListener {
                confirmDialog!!.dismiss()
            }
            layout.ConfirmButton.setOnClickListener {
                confirmDialog!!.dismiss()

                if(robotMedia[viewHolder.adapterPosition].delete(context.database))
                {
                    robotMedia.remove(robotMedia[viewHolder.adapterPosition])
                    notifyItemRemoved(viewHolder.adapterPosition)
                }
            }

            confirmDialogBuilder.setView(layout)
            confirmDialog = confirmDialogBuilder.show()
        }
    }

    override fun getItemCount(): Int
    {
        return robotMedia.size
    }
}
