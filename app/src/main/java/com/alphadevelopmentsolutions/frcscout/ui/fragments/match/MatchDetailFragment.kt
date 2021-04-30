package com.alphadevelopmentsolutions.frcscout.ui.fragments.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentMatchBinding
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentMatchDetailBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment
import com.google.android.material.tabs.TabLayoutMediator

class MatchDetailFragment(
    private val team1: Team,
    private val team2: Team,
    private val team3: Team,
    override val TAG: FragmentTag = FragmentTag.MATCH_DETAIL
) : MasterFragment() {

    companion object {
        fun newInstance(team1: Team, team2: Team, team3: Team) =
            MatchDetailFragment(team1, team2, team3)
    }

    private lateinit var binding: FragmentMatchDetailBinding
    private lateinit var viewModel: MatchDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentMatchDetailBinding.inflate(inflater, container, false)

        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.INVISIBLE ,
            ""
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, MatchDetailViewModelFactory(activityContext, navController, team1, team2, team3)).get(MatchDetailViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}