package com.alphadevelopmentsolutions.frcscout.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.BuildConfig

import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentLicensesBinding
import kotlinx.android.synthetic.main.fragment_settings.view.*

class LicensesFragment : MasterFragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentLicensesBinding.inflate(inflater, container, false).root
    }
}
