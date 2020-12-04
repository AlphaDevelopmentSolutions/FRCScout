package com.alphadevelopmentsolutions.frcscout.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
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

    private var yearSelectDialogFragment: SelectDialogFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.yearLayout.setOnClickListener {
            yearSelectDialogFragment?.show(activityContext)
        }

        launchIO {
            setupYearSelectDialog()
        }

        KeyStore.getInstance(activityContext).selectedYear?.let { selectedYear ->
            binding.yearTextview.text = selectedYear.number.toString()
        }

        return onCreateView(
            inflater,
            container,
            binding.root,
            NavbarState.LOCKED_WITH_BACK,
            getString(R.string.settings)
        )
    }

    private suspend fun setupYearSelectDialog() {
        val yearList = RepositoryProvider.getInstance(activityContext).yearRepository.getAllRaw(false)

        yearSelectDialogFragment =
            SelectDialogFragment.newInstance(
                object : OnItemSelectedListener {
                    override fun onItemSelected(selectedItem: Any) {
                        if (selectedItem is Year) {
                            KeyStore.getInstance(activityContext).selectedYear = selectedItem

                            binding.yearTextview.text = selectedItem.number.toString()
                        }
                    }
                },
                yearList
            )
    }
}