package com.alphadevelopmentsolutions.frcscout.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.classes.Config
import com.alphadevelopmentsolutions.frcscout.databinding.ActivityMainBinding
import com.alphadevelopmentsolutions.frcscout.databinding.NavHeaderMainBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import com.alphadevelopmentsolutions.frcscout.ui.fragments.MasterFragment
import com.alphadevelopmentsolutions.frcscout.ui.fragments.matchlist.MatchListFragmentDirections
import com.alphadevelopmentsolutions.frcscout.ui.fragments.teamlist.TeamListFragmentDirections
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        MasterFragment.OnFragmentInteractionListener {
    /**
     * Not saving the bundle fixes an issue that crashes the app when loading dialog frag
     * pauses.
     * Issue primarily caused by [Context] not being able to be serialized
     */
    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {}

    var config: Config = Config()

    /**
     * [NavController] used to navigate throughout the [R.id.nav_graph]
     */
    protected val navController: NavController by lazy {
        findNavController(R.id.main_frame)
    }

    /**
     * [NavHostFragment] for the [navController]
     */
    private val host: NavHostFragment?
        get() =
            if (supportFragmentManager.findFragmentById(R.id.main_frame) is NavHostFragment)
                supportFragmentManager.findFragmentById(R.id.main_frame) as NavHostFragment
            else
                null

    /**
     * [InputMethodManager] primarily used in the [hideKeyboard] function to hide the keyboard
     * @see hideKeyboard
     */
    private val inputManager: InputMethodManager by lazy {
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    /**
     * The current [NavbarState] of the [MainDrawerLayout]
     * Setting this variable will preform an action against the [MainDrawerLayout]
     */
    var navbarState: NavbarState = NavbarState.DRAWER
        set(value) {
            when (value) {
                NavbarState.LOCKED -> binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                NavbarState.LOCKED_WITH_BACK -> binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                NavbarState.EDIT -> binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                NavbarState.DRAWER -> binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            field = value
        }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Account.initialize(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.navView.setNavigationItemSelectedListener(this)

        Account.getInstance(this)?.let { account ->
            val navHeader = binding.navView.getHeaderView(0)
            navHeader.findViewById<TextView>(R.id.user_full_name_text_view).text = account.toString()
            navHeader.findViewById<TextView>(R.id.username_text_view).text = account.username

        }
    }

    override fun onBackPressed() {
        when {
            // Close the drawer if open
            binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START) -> binding.mainDrawerLayout.closeDrawer(GravityCompat.START)

            // If the drawer is closed, call the KingFragment.onBackPressed method
            else ->
            {
                // If the king fragment was found & it returns true, act as if the kingFragment ate the
                // callback and ignore further commands
                if (getCurrentFragment()?.onBackPressed() != true) {
                    hideKeyboard()
                    super.onBackPressed()
                }
            }
        }
    }

    /**
     * Gets the currently displayed fragment
     */
    fun getCurrentFragment(): MasterFragment? {
        var masterFragment: MasterFragment? = null

        // Attempt to find the current king fragment that called the onBackPressed
        host?.let { host ->
            host.childFragmentManager.fragments.let rev@{
                it.forEach { fragment ->

                    if (fragment is MasterFragment) {
                        masterFragment = fragment
                        return@rev
                    }
                }
            }
        }

        return masterFragment
    }

    override fun onFragmentInteraction(uri: Uri) {}

    /**
     * Navigation handler for the [MainDrawerLayout] [MenuItem]
     * @param item [MenuItem] item being selected
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        binding.mainDrawerLayout.closeDrawer(GravityCompat.START)

        getCurrentFragment()?.TAG?.let { fragmentTag ->
            when (item.itemId) {
                R.id.nav_matches -> {
                    when (fragmentTag) {
                        FragmentTag.TEAM_LIST -> navController.navigate(TeamListFragmentDirections.actionTeamListFragmentDestinationToMatchListFragmentDestination())
                    }
                }

                R.id.nav_teams -> {
                    when (fragmentTag) {
                        FragmentTag.MATCH_LIST -> navController.navigate(MatchListFragmentDirections.actionMatchListFragmentDestinationToTeamListFragmentDestination())
                    }
                }
            }
        }

        return false
    }

    /**
     * Restarts the activity without the [Bundle] of data
     */
    fun recreateWithoutBundle() {
        intent.let {
            overridePendingTransition(0, 0)
            it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            overridePendingTransition(0, 0)
            startActivity(it)
        }
    }

    /**
     * Hides the keyboard activity view
     */
    private fun hideKeyboard() {
        inputManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    /**
     * Displays the [Snackbar]
     * @param message [String] to be displayed
     * @param length [Int] how long to show it for
     */
    fun showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        hideKeyboard()
        Snackbar.make(findViewById(R.id.main_frame), message, length).show()
    }

    /**
     * Displays the [Snackbar]
     * @param messageId [Int] [R.string] resource id
     * @param length [Int] how long to show it for
     */
    fun showSnackbar(@StringRes messageId: Int, length: Int = Snackbar.LENGTH_LONG) {
        showSnackbar(getString(messageId), length)
    }

    /**
     * Removes the highest top level fragment from view within the [navController] backstack
     */
    fun removeCurrentFragment() {
        hideKeyboard()
        navController.currentDestination?.id?.let { id ->
            navController.popBackStack(id, true)
        }
    }

    /**
     * Detaches and re-attaches a fragment to the [host]'s [FragmentManager]
     * Useful for handling orientation changes
     * @param fragment [KingFragment] fragment to restart
     */
    fun restartFragment(fragment: MasterFragment) {
        host
            ?.childFragmentManager
            ?.beginTransaction()
            ?.detach(fragment)
            ?.attach(fragment)
            ?.commit()
    }
}
