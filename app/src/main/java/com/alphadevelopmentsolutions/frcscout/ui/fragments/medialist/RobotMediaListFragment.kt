package com.alphadevelopmentsolutions.frcscout.ui.fragments.medialist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentRobotMediaListBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment

class RobotMediaListFragment(
    private val team: Team? = null,
    override val TAG: FragmentTag = FragmentTag.MATCH_LIST
) : MasterFragment() {

    companion object {
        fun newInstance(team: Team) =
            RobotMediaListFragment(team)
    }

    private lateinit var binding: FragmentRobotMediaListBinding
    private lateinit var viewModel: RobotMediaListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentRobotMediaListBinding.inflate(inflater, container, false)

        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.INVISIBLE,
            getString(R.string.matches)
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, RobotMediaListViewModelFactory(activityContext, this, navController, team)).get(RobotMediaListViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    fun createMedia() {
        viewModel.createMedia()
    }
}