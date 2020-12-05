package com.alphadevelopmentsolutions.frcscout.ui.fragments.settings

import android.app.Application
import android.content.Context
import androidx.databinding.BaseObservable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.dialogs.SelectDialogFragment

class SettingsViewModel(
    private val context: MainActivity
) : ViewModel() {

    val year: MutableLiveData<String> = MutableLiveData()
    val event: MutableLiveData<String> = MutableLiveData()

    private var yearSelectDialogFragment: SelectDialogFragment? = null
    private var eventSelectDialogFragment: SelectDialogFragment? = null

    fun onYearLayoutClicked() {
        yearSelectDialogFragment?.show(context)
    }

    fun onEventLayoutClicked() {
        eventSelectDialogFragment?.show(context)
    }

    init {
        launchIO {
            setupYearSelectDialog()
        }

        KeyStore.getInstance(context).selectedYear?.let { selectedYear ->
            year.value = selectedYear.number.toString()

            launchIO {
                setupEventSelectDialog(selectedYear)
            }
        }

        KeyStore.getInstance(context).selectedEvent?.let { selectedEvent ->
            event.value = selectedEvent.toString()
        }
    }

    private suspend fun setupYearSelectDialog() {
        if (yearSelectDialogFragment == null) {
            val yearList = RepositoryProvider.getInstance(context).yearRepository.getAllRaw(false)

            yearSelectDialogFragment =
                SelectDialogFragment.newInstance(
                    object : OnItemSelectedListener {
                        override fun onItemSelected(selectedItem: Any) {
                            if (selectedItem is Year) {
                                KeyStore.getInstance(context).selectedYear = selectedItem

                                year.value = selectedItem.number.toString()

                                launchIO {
                                    setupEventSelectDialog(selectedItem)
                                }
                            }
                        }
                    },
                    yearList
                )
        }
    }

    private suspend fun setupEventSelectDialog(year: Year) {
        val eventList = RepositoryProvider.getInstance(context).eventRepository.getInYear(year)

        eventSelectDialogFragment =
            SelectDialogFragment.newInstance(
                object : OnItemSelectedListener {
                    override fun onItemSelected(selectedItem: Any) {
                        if (selectedItem is Event) {
                            KeyStore.getInstance(context).selectedEvent = selectedItem

                            event.value = selectedItem.toString()
                        }
                    }
                },
                eventList
            )
    }
}