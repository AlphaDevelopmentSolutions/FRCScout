package com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.classes.Menu
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentMatchListBinding
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentRobotInfoBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment

class RobotInfoFragment(
    private val team: Team,
    override val TAG: FragmentTag = FragmentTag.MATCH_LIST
) : MasterFragment() {

    companion object {
        fun newInstance(team: Team) =
            RobotInfoFragment(team)
    }

    private lateinit var binding: FragmentRobotInfoBinding
    private lateinit var viewModel: RobotInfoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentRobotInfoBinding.inflate(inflater, container, false)

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

        viewModel = ViewModelProvider(this, RobotInfoViewModelFactory(activityContext, this, team)).get(RobotInfoViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}