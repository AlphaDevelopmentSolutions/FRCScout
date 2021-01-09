package com.alphadevelopmentsolutions.frcscout.ui.fragments.medialist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.callbacks.OnConfirmationCallback
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.classes.ConfirmDialogFragment
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutCardRobotMediaBinding
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.extensions.runOnUiThread
import com.alphadevelopmentsolutions.frcscout.ui.fragments.team.TeamFragmentDirections
import com.google.android.flexbox.FlexboxLayoutManager

class RobotMediaListRecyclerViewAdapter(
    private val context: MainActivity,
    private val navController: NavController
) : RecyclerView.Adapter<RobotMediaListRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutCardRobotMediaBinding) : RecyclerView.ViewHolder(binding.root)

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private val robotMediaList: MutableList<RobotMedia> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutCardRobotMediaBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val robotMedia = robotMediaList[holder.adapterPosition]

        holder.binding.robotMedia = robotMedia
        holder.binding.handlers = this

        holder.binding.mediaImageView.layoutParams.let {
            if (it is FlexboxLayoutManager.LayoutParams)
                it.flexGrow = 1f
        }
    }

    override fun getItemCount(): Int {
        return robotMediaList.size
    }

    fun navigateToMedia(robotMedia: RobotMedia) {
        navController.navigate(TeamFragmentDirections.actionTeamFragmentDestinationToRobotMediaFragmentDestination(robotMedia))
    }

    fun deleteMedia(robotMedia: RobotMedia): Boolean {
        ConfirmDialogFragment.newInstance(
            object : OnConfirmationCallback {
                override fun onConfirm() {
                    launchIO {
                        Account.getInstance(context)?.let { account ->
                            robotMedia.markDeleted(account)
                            RepositoryProvider.getInstance(context).robotMediaRepository.delete(robotMedia)
                        }
                    }
                }

                override fun onCancel() {}

            },
            context.getString(R.string.delete_robot_media),
            context.getString(R.string.delete_robot_media_support),
            context.getString(R.string.delete)
        ).show(context)

        return true
    }

    fun setData(newList: List<RobotMedia>) {
        val diffCallback = RobotMediaListDiffCallback(robotMediaList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback, true)

        robotMediaList.clear()
        robotMediaList.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }
}