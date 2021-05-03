package com.alphadevelopmentsolutions.frcscout.ui.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity

class LoginViewModelFactory(
    val activity: MainActivity,
    val navController: NavController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        LoginViewModel(activity, navController) as T
}