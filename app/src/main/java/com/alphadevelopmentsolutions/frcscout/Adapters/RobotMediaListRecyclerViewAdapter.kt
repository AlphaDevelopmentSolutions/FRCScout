package com.alphadevelopmentsolutions.frcscout.Adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Fragments.RobotMediaFragment
import com.alphadevelopmentsolutions.frcscout.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_card_robot_media.view.*
import kotlinx.android.synthetic.main.layout_dialog_confirm.view.*
import java.util.*

internal class RobotMediaListRecyclerViewAdapter(private val team: Team, private val robotMediaList: ArrayList<RobotMedia>, private val context: MainActivity) : RecyclerView.Adapter<RobotMediaListRecyclerViewAdapter.ViewHolder>()
{
    private val robotMediaBitmaps: ArrayList<Bitmap> = ArrayList()

    init
    {
        for(media in robotMediaList)
        {
            robotMediaBitmaps.add(media.imageBitmap)
        }
    }

    internal class ViewHolder(val view: View, context: MainActivity) : RecyclerView.ViewHolder(view)
    {
        init
        {
            view.ViewRobotMediaButton.setTextColor(context.primaryColor)
            (view.ViewRobotMediaButton as MaterialButton).rippleColor = context.buttonRipple

            view.DeleteRobotMediaButton.setTextColor(context.primaryColor)
            (view.DeleteRobotMediaButton as MaterialButton).rippleColor = context.buttonRipple
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the event layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_robot_media, viewGroup, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val robotMedia = robotMediaList[viewHolder.adapterPosition]

        viewHolder.view.RobotImageView.setImageBitmap(robotMediaBitmaps[viewHolder.adapterPosition])

        viewHolder.view.ViewRobotMediaButton.setOnClickListener { context.changeFragment(RobotMediaFragment.newInstance(robotMedia, team), true) }

        if(!robotMedia.isDraft)
            viewHolder.view.DeleteRobotMediaButton.visibility = View.GONE
        else
            viewHolder.view.DeleteRobotMediaButton.setOnClickListener {

                val confirmDialogBuilder = AlertDialog.Builder(context)
                var confirmDialog: AlertDialog? = null
                val layout = LayoutInflater.from(context).inflate(R.layout.layout_dialog_confirm, null)

                layout.ConfirmTitle.text = context.getString(R.string.delete_robot_media)

                layout.ConfirmSupport.text = context.getString(R.string.delete_robot_media_support)

                layout.CancelButton.setTextColor(context.primaryColor)
                (layout.CancelButton as MaterialButton).rippleColor = context.buttonRipple
                layout.CancelButton.setOnClickListener {
                    confirmDialog!!.dismiss()
                }

                layout.ConfirmButton.backgroundTintList = context.buttonBackground
                layout.ConfirmButton.setOnClickListener {
                    confirmDialog!!.dismiss()

                    with(robotMediaList[viewHolder.adapterPosition])
                    {
                        if(delete(context.database))
                        {
                            robotMediaList.remove(this)
                            notifyItemRemoved(viewHolder.adapterPosition)
                        }
                    }
                }

                confirmDialogBuilder.setView(layout)
                confirmDialog = confirmDialogBuilder.show()
            }
    }

    override fun getItemCount(): Int
    {
        return robotMediaList.size
    }
}
