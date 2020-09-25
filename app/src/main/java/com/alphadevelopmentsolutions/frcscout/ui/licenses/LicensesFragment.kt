package com.alphadevelopmentsolutions.frcscout.ui.licenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alphadevelopmentsolutions.frcscout.databinding.FragmentLicensesBinding
import com.alphadevelopmentsolutions.frcscout.ui.MasterFragment

class LicensesFragment : MasterFragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentLicensesBinding.inflate(inflater, container, false).root
    }
}
