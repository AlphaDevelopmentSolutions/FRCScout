package com.alphadevelopmentsolutions.frcscout.Activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.design.widget.AppBarLayout
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alphadevelopmentsolutions.frcscout.Api.Server
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.KeyStore
import com.alphadevelopmentsolutions.frcscout.Classes.LoadingDialog
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*
import com.alphadevelopmentsolutions.frcscout.Fragments.*
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
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
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        MasterFragment.OnFragmentInteractionListener
{
    private lateinit var context: MainActivity
    var database: Database = Database(this)
    get()
    {
        if (!field.isOpen)
            field.open()

        return field
    }

    var keyStore = KeyStore()
    get()
    {
        if(field.context == null)
            field = KeyStore(this)

        return field
    }

    private lateinit var actionBarToggle: ActionBarDrawerToggle

    private lateinit var navHeader: View

    private lateinit var loadingDialog: LoadingDialog

    private var searchView: SearchView? = null

    public val dp = fun(height: Int): Int
    {
        return (height * context.resources.displayMetrics.density + 0.5f).toInt()
    }

    private val ACTION_BAR_ELEVATION = 11f
    private var progressDialogProgress: Int = 0
    var isOnline: Boolean = false
    get()
    {
        field = false

        with(
            Thread(Runnable {
                val hello = Server.Hello(context)

                if (hello.execute())
                    field = true
            })
        )
        {
            start()
            join()
        }

        return field
    }

    var currentZIndex = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        database = Database(context)

        ChangeButton.setOnClickListener{
            keyStore.resetData()
            changeFragment(ConfigViewPagerFragment.newInstance(), false, false)
        }

        database.open()

        setSupportActionBar(MainToolbar)

        navHeader = NavigationView.getHeaderView(0)
        actionBarToggle = ActionBarDrawerToggle(context, MainDrawerLayout, MainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        MainDrawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        NavigationView.setNavigationItemSelectedListener(this)

        //Update app colors
        updateAppColors()

        //Update nav text
        updateNavText()

        //Load the view for the fragments
        loadView(savedInstanceState)

    }

    //region Internet Methods

    /**
     * Downloads app data from the server
     * @param withFilters [Boolean] whether or not the app should apply download filters
     * @param refreshActivity [Boolean] whether or not the app recreate the activity on download complete
     * @return [Thread] download thread
     */
    fun downloadApplicationData(withFilters: Boolean = true, refreshActivity: Boolean = true): Thread?
    {
        if (isOnline)
        {
            val increaseFactor = 8

            loadingDialog = LoadingDialog(context, LoadingDialog.Style.PROGRESS)
            loadingDialog.message = "Downloading data..."
            loadingDialog.show()

            val updateThread = Thread(Runnable {

                database.beginTransaction()

                progressDialogProgress = 0

                context.runOnUiThread {
                    loadingDialog.message = "Downloading Users..."
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * SERVER CONFIG
                 */
                //Update Server Config
                val getServerConfig = Server.GetServerConfig(context)
                if(getServerConfig.execute())
                {
                    runOnUiThread{

                        var color = keyStore.getPreference(Constants.SharedPrefKeys.PRIMARY_COLOR_KEY, "")

                        primaryColor =
                            if(color != "")
                                Color.parseColor("#$color")
                            else
                                ResourcesCompat.getColor(resources, R.color.primary, null)

                        color = keyStore.getPreference(Constants.SharedPrefKeys.PRIMARY_COLOR_DARK_KEY, "")

                        primaryColorDark =
                                if(color != "")
                                    Color.parseColor("#$color")
                                else
                                    ResourcesCompat.getColor(resources, R.color.primary_dark, null)

                        updateAppColors()
                    }
                }

                context.runOnUiThread {
                    loadingDialog.message = "Downloading Years..."
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * YEARS
                 */
                //Purge Year Media
                val mediaFolder = File(Constants.YEAR_MEDIA_DIRECTORY)
                if (mediaFolder.isDirectory)
                    for (child in mediaFolder.listFiles())
                        child.delete()

                //Update Years
                val getYears = Server.GetYears(context)
                if (getYears.execute())
                {
                    Year.clearTable(database)

                    for (year in getYears.years)
                        year.save(database)
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.message = "Downloading Users..."
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * USERS
                 */
                val getUsers = Server.GetUsers(context)
                if (getUsers.execute())
                {
                    User.clearTable(database)

                    for (user in getUsers.users)
                        user.save(database)
                }

                /**
                 * EVENTS
                 */
                if(!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_EVENTS_KEY, false) as Boolean && withFilters))
                {

                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = "Downloading Events..."
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Events
                    val getEvents = Server.GetEvents(context)
                    if (getEvents.execute())
                    {
                        Event.clearTable(database)

                        for (event in getEvents.events)
                            event.save(database)
                    }

                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = "Downloading Event Metadata..."
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Event Team List
                    val getEventTeamList = Server.GetEventTeamList(context)
                    if (getEventTeamList.execute())
                    {
                        EventTeamList.clearTable(database)

                        for (eventTeamListItem in getEventTeamList.eventTeamList)
                            eventTeamListItem.save(database)
                    }
                }
                else
                    progressDialogProgress += increaseFactor * 2

                /**
                 * MATCHES
                 */
                if(!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_MATCHES_KEY, false) as Boolean && withFilters))
                {

                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = "Downloading Matches..."
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Matches
                    val getMatches = Server.GetMatches(context)
                    if (getMatches.execute())
                    {
                        Match.clearTable(database)

                        for (match in getMatches.matches)
                            match.save(database)
                    }
                }
                else
                    progressDialogProgress += increaseFactor

                /**
                 * TEAMS
                 */
                if(!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_TEAMS_KEY, false) as Boolean && withFilters))
                {
                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = "Downloading Teams..."
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Teams
                    val getTeams = Server.GetTeams(context)
                    if (getTeams.execute())
                    {
                        Team.clearTable(database)

                        for (team in getTeams.teams)
                            team.save(database)
                    }
                }
                else
                    progressDialogProgress += increaseFactor

                /**
                 * CHECKLISTS
                 */
                if(!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_CHECKLISTS_KEY, false) as Boolean && withFilters))
                {
                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = "Downloading Checklist..."
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Checklist Items
                    val getChecklistItems = Server.GetChecklistItems(context)
                    if (getChecklistItems.execute())
                    {
                        ChecklistItem.clearTable(database)

                        for (checklistItem in getChecklistItems.checklistItems)
                            checklistItem.save(database)
                    }

                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Checklist Items Results
                    val getChecklistItemResults = Server.GetChecklistItemResults(context)
                    if (getChecklistItemResults.execute())
                    {
                        ChecklistItemResult.clearTable(database)

                        for (checklistItemResult in getChecklistItemResults.checklistItemResults)
                            checklistItemResult.save(database)
                    }
                }
                else
                    progressDialogProgress += increaseFactor * 2

                /**
                 * ROBOT INFO
                 */
                if(keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_INFO_KEY, false) as Boolean)
                {
                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = "Downloading Robot Info..."
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Robot Info Keys
                    val getRobotInfoKeys = Server.GetRobotInfoKeys(context)
                    if (getRobotInfoKeys.execute())
                    {
                        RobotInfoKey.clearTable(database)

                        for (robotInfoKey in getRobotInfoKeys.robotInfoKeyList)
                            robotInfoKey.save(database)
                    }

                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Robot Info
                    val getRobotInfo = Server.GetRobotInfo(context)
                    if (getRobotInfo.execute())
                    {
                        RobotInfo.clearTable(database)

                        for (robotInfo in getRobotInfo.robotInfoList)
                            robotInfo.save(database)
                    }
                }
                else
                    progressDialogProgress += increaseFactor * 2

                /**
                 * SCOUT CARDS
                 */
                if(!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean && withFilters))
                {
                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = "Downloading Scout Cards..."
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Scout Card Info Keys
                    val getScoutCardInfoKeys = Server.GetScoutCardInfoKeys(context)
                    if (getScoutCardInfoKeys.execute())
                    {
                        ScoutCardInfoKey.clearTable(database)

                        for (scoutCardInfoKey in getScoutCardInfoKeys.scoutCardInfoKeys)
                            scoutCardInfoKey.save(database)
                    }

                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Scout Card Info
                    val getScoutCardInfo = Server.GetScoutCardInfo(context)
                    if (getScoutCardInfo.execute())
                    {
                        ScoutCardInfo.clearTable(database)

                        for (scoutCardInfo in getScoutCardInfo.scoutCardInfos)
                            scoutCardInfo.save(database)
                    }
                }
                else
                    progressDialogProgress += increaseFactor * 2

                /**
                 * ROBOT MEDIA
                 */
                if(keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_MEDIA_KEY, false) as Boolean && withFilters)
                {
                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = "Downloading Robot Media..."
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Get the folder and purge all files
                    val mediaFolder = File(Constants.ROBOT_MEDIA_DIRECTORY)
                    if (mediaFolder.isDirectory)
                        for (child in mediaFolder.listFiles())
                            child.delete()

                    val getRobotMedia = Server.GetRobotMedia(context)
                    if (getRobotMedia.execute())
                    {
                        RobotMedia.clearTable(database, true)

                        for (robotMedia in getRobotMedia.robotMedia)
                        {
                            if(robotMedia.save(database) > 0)
                            {
                                val team = Team(robotMedia.teamId, database)
                                team.imageFileURI = robotMedia.fileUri
                                team.save(database)
                            }
                        }

                    }
                }

                database.finishTransaction()

                runOnUiThread {
                    val year = Year((keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int?)!!, database)

                    //set the year when showing the event list frag
                    keyStore.setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, year.serverId!!)

                    loadingDialog.dismiss()

                    if(refreshActivity)
                        context.recreate()

                    else
                        changeFragment(EventListFragment.newInstance(Year((keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int?)!!, database)), false, false)
                }
            })

            updateThread.start()

            return updateThread
        } else
            showSnackbar(getString(R.string.no_internet))

        return null
    }

    /**
     * Uploads all stored app data to server
     * @param withFilters [Boolean] whether or not to apply filters to upload
     * @return [Thread] upload thread
     */
    private fun uploadApplicationData(withFilters: Boolean = false): Thread?
    {
        if(isOnline)
        {
            loadingDialog = LoadingDialog(context, LoadingDialog.Style.PROGRESS)
            loadingDialog.message = "Uploading data..."
            loadingDialog.show()

            val increaseFactor = 25

            val uploadThread = Thread(Runnable {
                var success = true

                context.runOnUiThread {
                    loadingDialog.message = "Uploading Checklists..."
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * CHECKLIST ITEM RESULTS
                 */
                if (!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_CHECKLISTS_KEY, false) as Boolean && withFilters))
                {
                    for (checklistItemResult in ChecklistItemResult.getObjects(null, null, true, database)!!)
                    {
                        val submitChecklistItemResult = Server.SubmitChecklistItemResult(context, checklistItemResult)
                        if (submitChecklistItemResult.execute())
                        {
                            checklistItemResult.isDraft = false
                            checklistItemResult.save(database)
                        }
                        else
                            success = false

                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.message = "Uploading Robot Info..."
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * ROBOT INFO
                 */
                if (!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_INFO_KEY, false) as Boolean && withFilters))
                {
                    for (robotInfo in RobotInfo.getObjects(null, null, null, null, null, true, database)!!)
                    {
                        val submitRobotInfo = Server.SubmitRobotInfo(context, robotInfo)
                        if (submitRobotInfo.execute())
                        {
                            robotInfo.isDraft = false
                            robotInfo.save(database)
                        }
                        else
                            success = false
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.message = "Uploading Scout Cards..."
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * SCOUT CARD INFO
                 */
                if (!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean && withFilters))
                {
                    for (scoutCardInfo in ScoutCardInfo.getObjects(null, null, null, null, null, true, database))
                    {
                        val submitScoutCardInfo = Server.SubmitScoutCardInfo(context, scoutCardInfo)
                        if (submitScoutCardInfo.execute())
                        {
                            scoutCardInfo.isDraft = false
                            scoutCardInfo.save(database)
                        }
                        else
                            success = false
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.message = "Uploading Robot Media..."
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * ROBOT MEDIA
                 */
                if (!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_MEDIA_KEY, false) as Boolean && withFilters))
                {
                    for (robotMedia in RobotMedia.getObjects(null, null, true, database)!!)
                    {
                        val submitRobotMedia = Server.SubmitRobotMedia(context, robotMedia)
                        if (submitRobotMedia.execute())
                        {
                            robotMedia.isDraft = false
                            robotMedia.save(database)
                        }
                        else
                            success = false
                    }
                }

                val finalSuccess = success
                runOnUiThread {
                    loadingDialog.dismiss()

                    if (finalSuccess)
                        context.recreate()
                }
            })

            uploadThread.start()

            return uploadThread
        }
        else
            showSnackbar(getString(R.string.no_internet))

        return null
    }

    //endregion

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
            R.id.nav_matches -> changeFragment(MatchListFragment.newInstance(null), false, false)

            R.id.nav_teams -> changeFragment(TeamListFragment.newInstance(null, null), false, false)

            R.id.nav_checklist -> changeFragment(ChecklistFragment.newInstance(Team((keyStore.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1) as Int), database), null), false, false)

            R.id.nav_events -> changeFragment(EventListFragment.newInstance(Year(keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int, database)), false, false)
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
            R.id.UploadDataItem ->
            {
                if (isOnline)
                {
                    val uploadAppDataAlertDialogBuilder = AlertDialog.Builder(context)
                    var uploadAppDataDialog: AlertDialog? = null

                    val layout = LayoutInflater.from(context).inflate(R.layout.layout_dialog_upload, null)

                    //Update the checkboxes and set check listeners
                    with(layout)
                    {

                        ChecklistCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_CHECKLISTS_KEY, false) as Boolean)
                        RobotInfoCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_INFO_KEY, false) as Boolean)
                        ScoutCardInfoCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean)
                        RobotMediaCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_MEDIA_KEY, false) as Boolean)
                        
                        ChecklistCheckBox.setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.UPLOAD_CHECKLISTS_KEY, b)
                        }

                        RobotInfoCheckBox.setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_INFO_KEY, b)
                        }

                        ScoutCardInfoCheckBox.setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.UPLOAD_SCOUT_CARD_INFO_KEY, b)
                        }

                        RobotMediaCheckBox.setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_MEDIA_KEY, b)
                        }

                        CancelButton.setOnClickListener {
                            uploadAppDataDialog!!.dismiss()
                        }

                        UploadButton.setOnClickListener {
                            uploadAppDataDialog!!.dismiss()
                            uploadApplicationData(true)
                        }
                    }

                    uploadAppDataAlertDialogBuilder.setView(layout)

                    uploadAppDataDialog = uploadAppDataAlertDialogBuilder.create()
                    uploadAppDataDialog.show()

                } else
                {
                    showSnackbar(getString(R.string.no_internet))
                }

                return true
            }

            R.id.RefreshDataItem ->
            {
                val downloadAppDataDialogBuilder = AlertDialog.Builder(context)
                var downloadAppDataDialog: AlertDialog? = null

                val layout = LayoutInflater.from(context).inflate(R.layout.layout_dialog_download, null)

                //Update checkboxes and set check listeners
                with(layout)
                {

                    EventsCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_EVENTS_KEY, false) as Boolean)
                    MatchesCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_MATCHES_KEY, false) as Boolean)
                    TeamsCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_TEAMS_KEY, false) as Boolean)
                    ChecklistCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_CHECKLISTS_KEY, false) as Boolean)
                    RobotInfoCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_INFO_KEY, false) as Boolean)
                    ScoutCardInfoCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean)
                    RobotMediaCheckBox.isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_MEDIA_KEY, false) as Boolean)

                    EventsCheckBox.setOnCheckedChangeListener { _, b ->
                        keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_EVENTS_KEY, b)
                    }

                    MatchesCheckBox.setOnCheckedChangeListener { _, b ->
                        keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_MATCHES_KEY, b)
                    }

                    TeamsCheckBox.setOnCheckedChangeListener { _, b ->
                        keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_TEAMS_KEY, b)
                    }

                    ChecklistCheckBox.setOnCheckedChangeListener { _, b ->
                        keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_CHECKLISTS_KEY, b)
                    }

                    RobotInfoCheckBox.setOnCheckedChangeListener { _, b ->
                        keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_INFO_KEY, b)
                    }

                    ScoutCardInfoCheckBox.setOnCheckedChangeListener { _, b ->
                        keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_SCOUT_CARD_INFO_KEY, b)
                    }

                    RobotMediaCheckBox.setOnCheckedChangeListener { _, b ->
                        keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_MEDIA_KEY, b)
                    }

                    CancelButton.setOnClickListener {
                        downloadAppDataDialog!!.dismiss()
                    }

                    DownloadButton.setOnClickListener {
                        downloadAppDataDialog!!.dismiss()
                        downloadApplicationData(true)
                    }
                }

                downloadAppDataDialogBuilder.setView(layout)

                downloadAppDataDialog = downloadAppDataDialogBuilder.create()
                downloadAppDataDialog.show()

                return true
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
                    val color = keyStore.getPreference(Constants.SharedPrefKeys.PRIMARY_COLOR_KEY, "")

                    field =
                    if(color != "")
                        Color.parseColor("#$color")
                    else
                        ResourcesCompat.getColor(resources, R.color.primary, null)
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
                    val color = keyStore.getPreference(Constants.SharedPrefKeys.PRIMARY_COLOR_DARK_KEY, "")

                    field =
                    if(color != "")
                        Color.parseColor("#$color")
                    else
                        ResourcesCompat.getColor(resources, R.color.primary_dark, null)
                }

                return field
            }

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

        ChangeButton.setTextColor(primaryColor)
        (ChangeButton as MaterialButton).rippleColor = buttonRipple
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
     * Updates the nav bar text for team name and number
     */
    fun updateNavText()
    {
        navHeader.TeamNumberTextView.text = keyStore.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1).toString()
        navHeader.TeamNameTextView.text = keyStore.getPreference(Constants.SharedPrefKeys.TEAM_NAME_KEY, "").toString()
    }

    /**
     * Locks the drawer layout
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
            if (keyStore.validateApiConfig())
            {
                //android >= marshmallow, permission needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    //write permission not granted, request
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 5885)
                }

                //Check if any events are on the device, if not download data
                if (Year.getObjects(null, database).size == 0)
                    downloadApplicationData(false)?.join()

                //Event previously selected, switch to match list
                if ((keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1) as Int) > 0)
                {
                    NavigationView.setCheckedItem(R.id.nav_matches)
                    changeFragment(MatchListFragment.newInstance(null), false, false)

                }

                //No event selected, default to yar list
                else
                {
                    val year = Year((keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int), database)
                    changeFragment(EventListFragment.newInstance(year), false, false)
                }

            }

            //Not yet configured, go to config frag
            else
                changeFragment(ConfigViewPagerFragment(), false, false)

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
