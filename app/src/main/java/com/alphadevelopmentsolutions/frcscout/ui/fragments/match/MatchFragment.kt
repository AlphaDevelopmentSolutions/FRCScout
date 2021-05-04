package com.alphadevelopmentsolutions.frcscout.ui.fragments.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.classes.Menu
import com.alphadevelopmentsolutions.frcscout.classes.ViewPagerFragment
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentMatchBinding
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentMatchListBinding
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment
import com.alphadevelopmentsolutions.frcscout.ui.fragments.team.TeamFragment
import com.google.android.material.tabs.TabLayoutMediator

class MatchFragment(
    override val TAG: FragmentTag = FragmentTag.MATCH
) : MasterFragment() {

    private lateinit var binding: FragmentMatchBinding
    private val args: MatchFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentMatchBinding.inflate(inflater, container, false)

        activity?.let {
            val adapter = FragmentViewPagerAdapter(it).apply {
                addFragment(
                    ViewPagerFragment(
                        getString(R.string.blue_alliance),
                        MatchDetailFragment.newInstance(
                            args.matchDatabaseView,
                            AllianceColor.BLUE
                        )
                    )
                )

                addFragment(
                    ViewPagerFragment(
                        getString(R.string.red_alliance),
                        MatchDetailFragment.newInstance(
                            args.matchDatabaseView,
                            AllianceColor.RED
                        )
                    )
                )
            }

            binding.viewPager.adapter = adapter

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = adapter.getTitle(position)
            }.attach()
        }

        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.LOCKED_WITH_BACK ,
            getString(R.string.match)
        )
    }
}