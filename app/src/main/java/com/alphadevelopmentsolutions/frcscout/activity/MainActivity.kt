package com.alphadevelopmentsolutions.frcscout.activity

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.alphadevelopmentsolutions.frcscout.api.Api
import com.alphadevelopmentsolutions.frcscout.api.ApiResponse
import com.alphadevelopmentsolutions.frcscout.classes.KeyStore
import com.alphadevelopmentsolutions.frcscout.classes.LoadingDialog
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.fragment.*
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.table.account.*
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Event
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Year
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.layout_dialog_download.view.*
import kotlinx.android.synthetic.main.layout_dialog_download.view.CancelButton
import kotlinx.android.synthetic.main.layout_dialog_download.view.ChecklistCheckBox
import kotlinx.android.synthetic.main.layout_dialog_download.view.RobotInfoCheckBox
import kotlinx.android.synthetic.main.layout_dialog_download.view.RobotMediaCheckBox
import kotlinx.android.synthetic.main.layout_dialog_download.view.ScoutCardInfoCheckBox
import kotlinx.android.synthetic.main.layout_dialog_upload.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.util.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        MasterFragment.OnFragmentInteractionListener
{
    private lateinit var context: MainActivity

    private lateinit var actionBarToggle: ActionBarDrawerToggle

    private lateinit var navHeader: View

    private lateinit var loadingDialog: LoadingDialog

    private var searchView: SearchView? = null

    val dp = fun(height: Int) = (height * context.resources.displayMetrics.density + 0.5f).toInt()

    private val ACTION_BAR_ELEVATION = 11f
    private var progressDialogProgress: Int = 0

    private lateinit var disposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        setSupportActionBar(MainToolbar)

        navHeader = NavigationView.getHeaderView(0)
        actionBarToggle = ActionBarDrawerToggle(context, MainDrawerLayout, MainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        MainDrawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        NavigationView.setNavigationItemSelectedListener(this)

        //Update app colors
        updateAppColors()

        disposable = CompositeDisposable()

        KeyStore.getInstance(this).selectedEventId?.let {

            //Update nav text
            disposable.add(VMProvider(this).eventViewModel.objWithId(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                            {
                                updateNavText(it)
                            },
                            {
                                AppLog.error(it)
                            }
                    )
            )
        }


        //Load the view for the fragments
        loadView(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    //region Overrides

    override fun onBackPressed()
    {
        if (MainDrawerLayout.isDrawerOpen(GravityCompat.START))
            MainDrawerLayout.closeDrawer(GravityCompat.START)

        else
        {
            //gets the highest fragment being shown
            val masterFragment = supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] as MasterFragment

            //master frag eats back press
            if(!masterFragment.onBackPressed())
                super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.nav_matches ->
                changeFragment(
                        MatchListFragment.newInstance(
                                null
                        ),
                        false,
                        false
                )

            R.id.nav_teams ->
                changeFragment(
                        TeamListFragment.newInstance(
                                null,
                                null
                        ),
                        false,
                        false
                )

            R.id.nav_checklist -> {
                KeyStore.getInstance(this).teamId?.let {
                    changeFragment(
                            ChecklistFragment.newInstance(
                                    it,
                                    null
                            ),
                            false,
                            false
                    )
                }
            }

            R.id.nav_events -> {

                KeyStore.getInstance(this).selectedYearId?.let {
                    changeFragment(
                            EventListFragment.newInstance(
                                    it
                            ),
                            false,
                            false
                    )
                }
            }
        }

        MainDrawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.main, menu)

        with(menu.findItem(R.id.SearchItem))
        {
            searchView = this.actionView as SearchView
            this.isVisible = isSearchViewVisible
            searchView?.setOnQueryTextListener(searchViewOnTextChangeListener)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.SyncItem ->
            {

                return true
            }

            R.id.SettingsItem ->
            {
                changeFragment(SettingsFragment.newInstance(SettingsFragment.Page.SETTINGS), true)

                return true
            }

            R.id.LogoutItem ->
            {
                VMProvider.getInstance(this).apply {
                    GlobalScope.launch(Dispatchers.IO) {
                        robotInfoKeyViewModel.clearData()
                        robotInfoViewModel.clearData()
                        scoutCardInfoKeyViewModel.clearData()
                        scoutCardInfoViewModel.clearData()
                        checklistItemViewModel.clearData()
                        checklistItemResultViewModel.clearData()
                        robotMediaViewModel.clearData()
                        teamViewModel.clearData()
                        userViewModel.clearData()
                        yearViewModel.clearData()
                    }
                }

                //Purge yearId media
                var mediaFolder = Constants.getFileDirectory(this, Constants.YEAR_MEDIA_DIRECTORY)
                if (mediaFolder.isDirectory)
                    for (child in mediaFolder.listFiles())
                        child.delete()

                //Purge robot media
                mediaFolder = Constants.getFileDirectory(this, Constants.ROBOT_MEDIA_DIRECTORY)
                if (mediaFolder.isDirectory)
                    for (child in mediaFolder.listFiles())
                        child.delete()

                KeyStore.getInstance(this).resetData()
                primaryColor = 0
                primaryColorDark = 0
                updateAppColors()
                changeFragment(ConfigViewPagerFragment.newInstance(), addToBackStack = false, animate = false)
            }
        }

        return false
    }

    override fun onFragmentInteraction(uri: Uri){}

    //endregion

    //region Layout Methods

    /**
     * Stores the primary color from the shared preference
     */
    var primaryColor: Int = 0
        get()
            {
                if(field == 0)
                {

                    KeyStore.getInstance(this).primaryColor.let {

                        field =
                                when {
                                    it != null && it != 0 -> it
                                    else -> ResourcesCompat.getColor(resources, R.color.primary, null)
                                }
                    }
                }

                return field
            }

    /**
     * Stores the primary dark color from the shared preference
     */
    var primaryColorDark: Int = 0
        get()
            {
                if(field == 0)
                {

                    KeyStore.getInstance(this).primaryColorDark.let {

                        field =
                                when {
                                    it != null && it != 0 -> it
                                    else -> ResourcesCompat.getColor(resources, R.color.primary_dark, null)
                                }
                    }
                }

                return field
            }

    /**
     * Used to set the background color of a button
     */
    var buttonBackground: ColorStateList? = null
        get()
        {
            if(field == null || field!!.defaultColor != primaryColorDark)
            {
                val states = arrayOf(
                        intArrayOf(android.R.attr.state_enabled)
                )

                val colors = intArrayOf(
                        primaryColor
                )

                field = ColorStateList(states, colors)
            }

            return field
        }

    /**
     * Used to set the ripple color of a button
     */
    var buttonRipple: ColorStateList? = null
        get()
        {
            if(field == null || field!!.defaultColor != primaryColorDark)
            {
                val states = arrayOf(
                        intArrayOf(android.R.attr.state_enabled),
                        intArrayOf(-android.R.attr.state_enabled),
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_pressed)
                )

                val colors = intArrayOf(
                        primaryColorDark,
                        primaryColorDark,
                        primaryColorDark,
                        primaryColorDark
                )

                field = ColorStateList(states, colors)
            }

            return field
        }

    /**
     * Used to set the background color of a edit text
     */
    var editTextBackground: ColorStateList? = null
        get()
        {
            if(field == null || field!!.defaultColor != primaryColorDark)
            {
                val states = arrayOf(
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf(-android.R.attr.state_focused)
                )

                val colors = intArrayOf(
                        primaryColor,
                        Color.GRAY

                )

                field = ColorStateList(states, colors)
            }

            return field
        }

    /**
     * Used to set the background for a checkbox
     */
    var checkboxBackground: ColorStateList? = null
        get()
        {
            if(field == null || field!!.defaultColor != primaryColor)
            {
                val states = arrayOf(
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(-android.R.attr.state_checked)
                )

                val colors = intArrayOf(
                        primaryColor,
                        Color.GRAY
                )

                field = ColorStateList(states, colors)
            }

            return field
        }

    /**
     * Used to set the ripple for a checkbox
     */
    var checkboxRipple: ColorStateList? = null
        get()
        {
            if(field == null || field!!.defaultColor != primaryColorDark)
            {
                val states = arrayOf(
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(-android.R.attr.state_checked)
                )

                val colors = intArrayOf(
                        Color.argb(30, Color.red(primaryColorDark), Color.green(primaryColorDark), Color.blue(primaryColorDark)),
                        Color.argb(30, Color.red(Color.GRAY), Color.green(Color.GRAY), Color.blue(Color.GRAY))
                )

                field = ColorStateList(states, colors)
            }

            return field
        }

    /**
     * Updates the apps colors to the specified ones in [KeyStore]
     */
    private fun updateAppColors()
    {
        window.statusBarColor = primaryColorDark
        MainToolbar.setBackgroundColor(primaryColor)
        navHeader.HeaderConstraintLayout.setBackgroundColor(primaryColor)

        val states: Array<IntArray> = arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_enabled))

        val colors: IntArray = intArrayOf(
                primaryColor,
                Color.DKGRAY)

        NavigationView.itemTextColor = ColorStateList(states, colors)
        NavigationView.itemIconTintList = ColorStateList(states, colors)
    }

    /**
     * Sets the title of the nav bar
     * @param title [String] to set the nav bar title to
     */
    fun setToolbarTitle(title: String)
    {
        supportActionBar!!.title = title
    }

    /**
     * Sets the title of the nav bar
     * @param titleId [Int] res id to set the nav bar title to
     */
    fun setToolbarTitle(titleId: Int)
    {
        supportActionBar!!.setTitle(titleId)
    }

    /**
     * Updates the nav bar text for teamId name and number
     */
    fun updateNavText(event: Event?)
    {
        navHeader.EventNameTextView.text = event?.toString() ?: ""
        navHeader.TeamNameTextView.text = let {
            KeyStore.getInstance(this).let {

                val teamNumber = it.teamNumber
                val teamName = it.teamName

                StringBuilder().apply {
                    if(teamNumber != null)
                        append(teamNumber)

                    if(teamNumber != null && teamName != null)
                        append(" - ")

                    if(teamName != null)
                        append(teamName)
                }.toString()
            }
        }
    }

    /**
     * Locks the drawer layoutl
     * @param setBackIcon [Boolean] to set the icon as the back arrow
     * @param backIconClickListener [View.OnClickListener] to preform when the back button is clicked
     */
    fun lockDrawerLayout(setBackIcon: Boolean = false, backIconClickListener: View.OnClickListener? = null)
    {
        MainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        if(setBackIcon)
        {
            MainToolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_white_24dp, null)
            MainToolbar.setNavigationOnClickListener(backIconClickListener)
        }
        else
        {
            MainToolbar.navigationIcon = null
            MainToolbar.setNavigationOnClickListener{
                MainDrawerLayout.openDrawer(GravityCompat.START)
            }
        }

    }

    /**
     * Unlocks the drawer layout
     */
    fun unlockDrawerLayout()
    {
        MainToolbar.navigationIcon = resources.getDrawable(R.drawable.ic_dehaze_white_24dp, null)
        MainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        MainToolbar.setNavigationOnClickListener {
            MainDrawerLayout.openDrawer(GravityCompat.START)
        }
    }

    /**
     * Displays the snackbar
     * @param message [String] to be displayed
     */
    fun showSnackbar(message: String)
    {
        hideKeyboard()
        Snackbar.make(MainFrame, message, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Hides the keyboard from view if it is showing
     */
    fun hideKeyboard()
    {
        //hide the keyboard before showing the snackbar
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    /**
     * Changes the current fragment to the specified one
     * @param fragment [MasterFragment] to change to
     * @param addToBackStack [Boolean] if [fragment] should be added to back stack, if false all frags pop and [fragment] is shown
     * @param animate [Boolean] if frag should animate or not
     * @param reverseAnimation [Boolean] if frag should reverse animation order
     */
    fun changeFragment(fragment: MasterFragment, addToBackStack: Boolean, animate: Boolean = true, reverseAnimation: Boolean = false)
    {
        with(supportFragmentManager.beginTransaction())
        {
            if(animate)
            {
                if(!reverseAnimation)
                    setCustomAnimations(R.anim.slide_in_from_right, R.anim.zoom_out, R.anim.zoom_in, R.anim.slide_out_to_right)
                else
                    setCustomAnimations(R.anim.zoom_in, R.anim.slide_out_to_right)

            }


            if (addToBackStack)
                addToBackStack(null) //add to the back stack

            else
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE) //pop all backstacks

            replace(R.id.MainFrame, fragment)

            commit()
        }




        if (MainDrawerLayout.isDrawerOpen(GravityCompat.START))
            MainDrawerLayout.closeDrawer(GravityCompat.START)

        hideKeyboard()
    }

    /**
     * All view loading logic is stored here
     * used for when the activity initially starts up
     * @param savedInstanceState [Bundle]
     */
    private fun loadView(savedInstanceState: Bundle?)
    {
        //if not previous saved state, eg not rotation
        if (savedInstanceState == null)
        {
            //default to the splash frag until changed
            changeFragment(SplashFragment(), false, false)

            //validate the app config to ensure all properties are filled
            if (KeyStore.getInstance(this).validateApiConfig())
            {
                //Event previously selected, switch to matchId list
                if (KeyStore.getInstance(this).selectedEventId != null)
                {
                    NavigationView.setCheckedItem(R.id.nav_matches)
                    changeFragment(
                            MatchListFragment.newInstance(
                                    null
                            ),
                            false,
                            false
                    )

                }

                //No eventId selected, default to event list
                else
                {
                    KeyStore.getInstance(this).selectedYearId?.let {
                        changeFragment(
                                EventListFragment.newInstance(
                                        it
                                ),
                                false,
                                false
                        )
                    }
                }

            }

            //Not yet configured, go to config frag
            else
                changeFragment(
                        ConfigViewPagerFragment(),
                        false,
                        false
                )

        }
    }

    /**
     * Drops the elevation of the actionbar so that specific pages don't have a shadow
     */
    fun dropActionBar()
    {
        MainAppBarLayout.elevation = 0f
    }

    /**
     * Elevates the action bar after dropping to preserve the shadow
     */
    fun elevateActionBar()
    {
        MainAppBarLayout.elevation = ACTION_BAR_ELEVATION
    }

    /**
     * Sets the menu item to be checked
     * @param menuItem [Int] to be marked as checked
     */
    fun setCheckedMenuItem(menuItem: Int)
    {
        NavigationView.menu.getItem(menuItem).isChecked = true
    }

    private var searchViewOnTextChangeListener: SearchView.OnQueryTextListener? = null

    /**
     * Sets the listener for text being changed for the search view on the toolbar
     * @param listener [SearchView.OnQueryTextListener] listener for [searchView]
     */
    fun setSearchViewOnTextChangeListener(listener: SearchView.OnQueryTextListener?)
    {
        searchView?.setOnQueryTextListener(listener)
        searchViewOnTextChangeListener = listener
    }

    /**
     * Gets / Sets the visibility of [searchView]
     */
    var isSearchViewVisible = false
    set(value)
    {
        if(field != value)
        {
            val item = MainToolbar.menu.findItem(R.id.SearchItem)

            if(item != null)
                item.isVisible = value

            if(!value)
                setSearchViewOnTextChangeListener(null)

            field = value
        }
    }

    /**
     * Allows or disallows the toolbar the ability to collapse when scrolling
     */
    var isToolbarScrollable = false
    set(value)
    {
        if(field != value)
        {
            if (value)
                (MainToolbar.layoutParams as AppBarLayout.LayoutParams).scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
            else
                (MainToolbar.layoutParams as AppBarLayout.LayoutParams).scrollFlags = 0
            field = value
        }
    }

    //endregion
}
