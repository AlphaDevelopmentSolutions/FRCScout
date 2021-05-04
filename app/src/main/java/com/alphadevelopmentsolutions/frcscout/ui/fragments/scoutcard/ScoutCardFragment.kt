package com.alphadevelopmentsolutions.frcscout.ui.fragments.scoutcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.classes.Menu
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentMatchListBinding
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentRobotInfoBinding
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentScoutCardBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment

class ScoutCardFragment(override val TAG: FragmentTag = FragmentTag.SCOUT_CARD) : MasterFragment() {

    private val args: ScoutCardFragmentArgs by navArgs()

    private lateinit var binding: FragmentScoutCardBinding
    private lateinit var viewModel: ScoutCardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentScoutCardBinding.inflate(inflater, container, false)

        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.LOCKED_WITH_BACK,
            args.team.toString()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, ScoutCardViewModelFactory(activityContext, this, args.match, args.team)).get(ScoutCardViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}