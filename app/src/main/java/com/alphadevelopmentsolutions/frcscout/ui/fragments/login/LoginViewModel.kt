package com.alphadevelopmentsolutions.frcscout.ui.fragments.login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.dialogs.SelectDialogFragment

class LoginViewModel(
    private val context: MainActivity,
    private val navController: NavController
) : ViewModel() {

    var username: String = ""
    var password: String = ""

    fun login() {
        launchIO {
            val result =
                ApiViewModel.getInstance(context)
                    .login(
                        context,
                        username,
                        password,
                        null
                    )

            if (result)
                navController.navigate(
                    LoginFragmentDirections.actionLoginFragmentDestinationToTeamListFragmentDestination()
                )
        }
    }

    fun signup() {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW
            ).apply {
                data = Uri.parse("http://localhost:8080/signup")
            }
        )
    }
}