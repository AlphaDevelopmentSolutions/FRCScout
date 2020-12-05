package com.alphadevelopmentsolutions.frcscout.ui.fragments.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.classes.Menu
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentMatchListBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment

class MatchListFragment(override val TAG: FragmentTag = FragmentTag.MATCH_LIST) : MasterFragment() {

    private lateinit var binding: FragmentMatchListBinding
    private lateinit var viewModel: MatchListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentMatchListBinding.inflate(inflater, container, false)

        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.DRAWER,
            getString(R.string.matches)
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, MatchListViewModelFactory(activityContext, this, navController)).get(MatchListViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun getMenu() =
        Menu(
            R.menu.main
        ) {
            var isHandled = false

            when (it.itemId) {
                R.id.sync_item -> {

                    launchIO {
                        ApiViewModel.getInstance(activityContext)
                            .sync(
                                activityContext,
                                null
                            )
                    }

                    isHandled = true
                }

                R.id.settings_item -> {
                    navigate(MatchListFragmentDirections.actionMatchListFragmentDestinationToSettingsFragmentDestination())

                    isHandled = true
                }
            }


            isHandled
        }
}