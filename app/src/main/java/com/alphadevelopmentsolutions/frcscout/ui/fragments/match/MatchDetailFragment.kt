package com.alphadevelopmentsolutions.frcscout.ui.fragments.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentMatchDetailBinding
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment
import com.alphadevelopmentsolutions.frcscout.ui.fragments.matchlist.MatchDatabaseView

class MatchDetailFragment(
    private val matchDatabaseView: MatchDatabaseView,
    private val allianceColor: AllianceColor,
    override val TAG: FragmentTag = FragmentTag.MATCH_DETAIL
) : MasterFragment() {

    companion object {
        fun newInstance(matchDatabaseView: MatchDatabaseView, allianceColor: AllianceColor) =
            MatchDetailFragment(matchDatabaseView, allianceColor)
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

        viewModel = ViewModelProvider(this, MatchDetailViewModelFactory(activityContext, navController, matchDatabaseView, allianceColor)).get(MatchDetailViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}