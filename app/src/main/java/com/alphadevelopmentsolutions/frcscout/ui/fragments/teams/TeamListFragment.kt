package com.alphadevelopmentsolutions.frcscout.ui.fragments.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.api.Api
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import com.alphadevelopmentsolutions.frcscout.classes.Menu
import com.alphadevelopmentsolutions.frcscout.classes.SaveButtonWrapper
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentSettingsBinding
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentTeamListBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.dialogs.SelectDialogFragment
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment
import java.util.*

class TeamListFragment(override val TAG: FragmentTag = FragmentTag.TEAM_LIST) : MasterFragment() {
    private lateinit var binding: FragmentTeamListBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentTeamListBinding.inflate(inflater, container, false)

        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.DRAWER,
            getString(R.string.teams)
        )
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
                    navigate(TeamListFragmentDirections.actionTeamListFragmentDestinationToSettingsFragmentDestination())

                    isHandled = true
                }
            }


            isHandled
        }
}