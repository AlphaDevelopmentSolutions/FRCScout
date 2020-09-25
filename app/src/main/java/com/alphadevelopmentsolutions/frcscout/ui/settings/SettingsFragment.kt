package com.alphadevelopmentsolutions.frcscout.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels

import com.alphadevelopmentsolutions.frcscout.databinding.FragmentSettingsBinding
import com.alphadevelopmentsolutions.frcscout.ui.MasterFragment

class SettingsFragment : MasterFragment()
{
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}
