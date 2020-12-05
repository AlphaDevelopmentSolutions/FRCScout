package com.alphadevelopmentsolutions.frcscout.ui.fragments.team

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import com.alphadevelopmentsolutions.frcscout.classes.ViewPagerFragment
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.dialogs.SelectDialogFragment
import com.alphadevelopmentsolutions.frcscout.ui.fragments.matches.MatchListFragment
import io.reactivex.Flowable

class TeamViewModel(
    application: Application,
    val childFragmentManager: FragmentManager,
    val team: Team
) : AndroidViewModel(application) {
    private val context = application

    val viewPagerAdapter =
        FragmentViewPagerAdapter(
            childFragmentManager
        )

    init {
        viewPagerAdapter.addFragment(
            ViewPagerFragment(
                context.getString(R.string.matches),
                MatchListFragment.newInstance(true, team)
            )
        )
    }
}