package com.alphadevelopmentsolutions.frcscout.ui.fragments.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentRobotMediaBinding
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentRobotMediaListBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment

class RobotMediaFragment(override val TAG: FragmentTag = FragmentTag.MATCH_LIST) : MasterFragment() {

    private lateinit var binding: FragmentRobotMediaBinding
    private lateinit var viewModel: RobotMediaViewModel

    private val args: RobotMediaFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentRobotMediaBinding.inflate(inflater, container, false)

        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.LOCKED_WITH_BACK,
            getString(R.string.media)
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, RobotMediaViewModelFactory(activityContext, args.media, args.team)).get(RobotMediaViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}