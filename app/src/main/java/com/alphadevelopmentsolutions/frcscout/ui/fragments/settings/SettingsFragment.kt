package com.alphadevelopmentsolutions.frcscout.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentSettingsBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.dialogs.SelectDialogFragment
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment

class SettingsFragment(override val TAG: FragmentTag = FragmentTag.SETTINGS) : MasterFragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.LOCKED_WITH_BACK,
            getString(R.string.settings)
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, SettingsViewModelFactory(activityContext.application, activityContext)).get(SettingsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}