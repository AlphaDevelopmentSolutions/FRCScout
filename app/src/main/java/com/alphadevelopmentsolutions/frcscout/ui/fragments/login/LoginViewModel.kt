package com.alphadevelopmentsolutions.frcscout.ui.fragments.login

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.api.ApiViewModel
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO

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