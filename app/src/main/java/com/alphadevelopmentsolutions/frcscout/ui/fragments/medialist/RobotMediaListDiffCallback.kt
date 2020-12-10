package com.alphadevelopmentsolutions.frcscout.ui.fragments.medialist

import androidx.recyclerview.widget.DiffUtil
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia

class RobotMediaListDiffCallback(
    private val oldList: List<RobotMedia>,
    private val newList: List<RobotMedia>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id.contentEquals(newList[newItemPosition].id)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.localFileUri == newItem.localFileUri
    }
}