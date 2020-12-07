package com.alphadevelopmentsolutions.frcscout.ui.fragments.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.api.Api
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import com.alphadevelopmentsolutions.frcscout.classes.Menu
import com.alphadevelopmentsolutions.frcscout.classes.SaveButtonWrapper
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentSettingsBinding
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentTeamBinding
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentTeamListBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.dialogs.SelectDialogFragment
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment
import com.alphadevelopmentsolutions.frcscout.ui.fragments.login.LoginViewModel
import com.alphadevelopmentsolutions.frcscout.ui.fragments.login.LoginViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class TeamFragment(override val TAG: FragmentTag = FragmentTag.TEAM_LIST) : MasterFragment() {

    private lateinit var binding: FragmentTeamBinding
    private lateinit var viewModel: TeamViewModel

    private val args: TeamFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentTeamBinding.inflate(inflater, container, false)

        binding.addPhotoFab.hide()
        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.LOCKED_WITH_BACK,
            getString(R.string.team)
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, TeamViewModelFactory(activityContext, childFragmentManager, this, args.team)).get(TeamViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}