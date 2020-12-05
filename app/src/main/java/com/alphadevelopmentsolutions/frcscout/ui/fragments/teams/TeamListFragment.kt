package com.alphadevelopmentsolutions.frcscout.ui.fragments.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.api.Api
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
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

        onCancelListener =
            View.OnClickListener {
                launchIO {
                    ApiViewModel.getInstance(activityContext)
                        .sync(
                            activityContext,
                            null
                        )
                }
            }

        onSaveWrapper =
            SaveButtonWrapper(
                "Save"
            ) {
                navigate(TeamListFragmentDirections.actionTeamListFragmentDestinationToSettingsFragmentDestination())
            }

        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.EDIT,
            getString(R.string.teams)
        )
    }
}