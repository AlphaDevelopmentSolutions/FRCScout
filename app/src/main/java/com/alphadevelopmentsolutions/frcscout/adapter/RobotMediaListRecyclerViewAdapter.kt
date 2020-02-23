package com.alphadevelopmentsolutions.frcscout.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.classes.table.account.RobotMedia
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.fragment.RobotMediaFragment
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_card_robot_media.view.*
import kotlinx.android.synthetic.main.layout_dialog_confirm.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

internal class RobotMediaListRecyclerViewAdapter(private val teamId: UUID, private val robotMediaList: MutableList<RobotMedia>, private val context: MainActivity) : RecyclerView.Adapter<RobotMediaListRecyclerViewAdapter.ViewHolder>()
{
    private val robotMediaBitmaps = mutableListOf<Bitmap>()

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
        //Inflate the eventId layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_robot_media, viewGroup, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val robotMedia = robotMediaList[viewHolder.adapterPosition]

        viewHolder.view.RobotImageView.setImageBitmap(robotMediaBitmaps[viewHolder.adapterPosition])

        viewHolder.view.ViewRobotMediaButton.setOnClickListener {
            context.changeFragment(
                    RobotMediaFragment.newInstance(
                            robotMedia,
                            teamId
                    ),
                    true
            )
        }

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


                    GlobalScope.launch(Dispatchers.IO) {

                        robotMediaList[viewHolder.adapterPosition].let {
                            VMProvider.getInstance(context).robotMediaViewModel.delete(it)
                            robotMediaList.remove(it)
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
