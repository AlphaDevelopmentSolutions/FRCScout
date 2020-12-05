package com.alphadevelopmentsolutions.frcscout.ui.fragments.teams

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.dialogs.SelectDialogFragment
import io.reactivex.Flowable

class TeamListViewModel(
    application: Application,
    private val lifecycleOwner: LifecycleOwner,
    navController: NavController
) : AndroidViewModel(application) {

    private val context = application

    private var teamList: MutableList<Team> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)

            teamListRecyclerViewAdapter.notifyDataSetChanged()
        }

    val teamListRecyclerViewAdapter =
        TeamListRecyclerViewAdapter(
            context,
            teamList,
            navController
        )


    init {
        KeyStore.getInstance(context).selectedEvent?.let { event ->
            RepositoryProvider.getInstance(context).teamRepository.getAtEvent(event).observe<MutableList<Team>>(
                lifecycleOwner,
                {
                    teamList = it
                }
            )
        }
    }
}