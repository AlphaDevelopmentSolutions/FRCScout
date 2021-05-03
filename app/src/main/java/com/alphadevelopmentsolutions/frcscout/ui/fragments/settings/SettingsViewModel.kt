package com.alphadevelopmentsolutions.frcscout.ui.fragments.settings

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Role
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.dialogs.SelectDialogFragment

class SettingsViewModel(
    private val context: MainActivity
) : ViewModel() {

    val year: ObservableField<String> = ObservableField()
    val event: ObservableField<String> = ObservableField()
    val userTeamAccount: ObservableField<String> = ObservableField()

    private var yearSelectDialogFragment: SelectDialogFragment? = null
    private var eventSelectDialogFragment: SelectDialogFragment? = null
    private var userTeamAccountSelectDialogFragment: SelectDialogFragment? = null

    fun onYearLayoutClicked() {
        yearSelectDialogFragment?.show(context)
    }

    fun onEventLayoutClicked() {
        eventSelectDialogFragment?.show(context)
    }

    fun onTeamAccountLayoutClicked() {
        userTeamAccountSelectDialogFragment?.show(context)
    }

    init {
        launchIO {
            setupYearSelectDialog()
        }

        KeyStore.getInstance(context).selectedYear?.let { selectedYear ->
            year.set(selectedYear.number.toString())

            launchIO {
                setupEventSelectDialog(selectedYear)
            }
        }

        KeyStore.getInstance(context).selectedEvent?.let { selectedEvent ->
            event.set(selectedEvent.toString())
        }

        Account.getInstance(context)?.let { account ->
            account.userTeamAccount?.let {
                userTeamAccount.set(it.toString())
            }

            launchIO {
                setupUserTeamAccountSelectDialog()
            }
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
                                KeyStore.getInstance(context).apply {
                                    selectedYear = selectedItem
                                    selectedEvent = null
                                }

                                year.set(selectedItem.number.toString())
                                event.set("")

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

                            event.set(selectedItem.toString())
                        }
                    }
                },
                eventList
            )
    }

    private suspend fun setupUserTeamAccountSelectDialog() {
        val userTeamAccountList = RepositoryProvider.getInstance(context).userTeamAccountRepository.getAllRawView()

        userTeamAccountSelectDialogFragment  =
            SelectDialogFragment.newInstance(
                object : OnItemSelectedListener {
                    override fun onItemSelected(selectedItem: Any) {
                        if (selectedItem is UserTeamAccountView) {
                            launchIO {
                                Account.getInstance(context)?.let { account ->

                                    val roleList = RepositoryProvider.getInstance(context).roleRepository.getListForUserTeamAccount(selectedItem.userTeamAccount)

                                    account.roleMatrix = Role.generateMatrix(roleList, selectedItem.teamAccount.id)
                                    account.userTeamAccount = selectedItem.userTeamAccount
                                    Account.setInstance(account, context)

                                    userTeamAccount.set(selectedItem.toString())
                                }
                            }
                        }
                    }
                },
                userTeamAccountList
            )
    }
}