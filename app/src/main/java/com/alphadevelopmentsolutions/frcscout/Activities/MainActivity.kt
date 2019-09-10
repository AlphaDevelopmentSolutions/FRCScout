package com.alphadevelopmentsolutions.frcscout.Activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.constraint.ConstraintLayout
import android.support.design.button.MaterialButton
import android.support.design.widget.AppBarLayout
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.alphadevelopmentsolutions.frcscout.Api.Server
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*
import com.alphadevelopmentsolutions.frcscout.Fragments.*
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.layout_download.view.*
import kotlinx.android.synthetic.main.layout_download.view.CancelButton
import kotlinx.android.synthetic.main.layout_download.view.ChecklistCheckBox
import kotlinx.android.synthetic.main.layout_download.view.RobotInfoCheckBox
import kotlinx.android.synthetic.main.layout_download.view.RobotMediaCheckBox
import kotlinx.android.synthetic.main.layout_download.view.ScoutCardInfoCheckBox
import kotlinx.android.synthetic.main.layout_upload.view.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        MasterFragment.OnFragmentInteractionListener
{
    private var context: MainActivity? = null

    private var database: Database? = null

    private var sharedPreferences: SharedPreferences? = null
    private var sharedPreferencesEditor: SharedPreferences.Editor? = null

    private var mainFrame: FrameLayout? = null
    private var toolbar: Toolbar? = null
    private var actionBarToggle: ActionBarDrawerToggle? = null
    private var drawer: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var appBarLayout: AppBarLayout? = null
    private var teamNumberTextView: TextView? = null
    private var teamNameTextView: TextView? = null
    private var headerConstraintLayout: ConstraintLayout? = null

    private var updateThread: Thread? = null

    private val ACTION_BAR_ELEVATION = 11
    private var progressDialogProgess: Int = 0

    private var isOnline: Boolean = false

    private var progressDialog: ProgressDialog? = null

    private var changeButton: Button? = null

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //assign as base layout vars
        mainFrame = findViewById(R.id.MainFrame)
        appBarLayout = findViewById(R.id.AppBarLayout)
        toolbar = findViewById(R.id.toolbar)
        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        //set context
        context = this

        //set the action bar
        setSupportActionBar(toolbar)

        changeButton = findViewById(R.id.ChangeButton)
        val navHeader = navigationView!!.getHeaderView(0)
        actionBarToggle = ActionBarDrawerToggle(context, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        teamNumberTextView = navHeader.findViewById(R.id.TeamNumberTextView)
        teamNameTextView = navHeader.findViewById(R.id.TeamNameTextView)
        headerConstraintLayout = navHeader.findViewById(R.id.HeaderConstraintLayout)

        updateAppColors()

        //open the database as soon as the app starts
        database = Database(context!!)
        database!!.open()

        //add the listener for the drawer
        drawer!!.addDrawerListener(actionBarToggle!!)
        actionBarToggle!!.syncState()

        //set the item selected listener
        navigationView!!.setNavigationItemSelectedListener(this)

        //updates the nav text to the current team saved in shared pref
        updateNavText()

        changeButton!!.setOnClickListener{
            clearApiConfig()
            changeFragment(ConfigViewPagerFragment.newInstance(), false)
        }

        loadView(savedInstanceState)//permission granted, continue

    }

    //region Data Management Methods

    /**
     * Returns the active database
     * @return database instance
     */
    fun getDatabase(): Database
    {
        if (database == null)
            database = Database(this)

        if (!database!!.isOpen)
            database!!.open()

        return database!!
    }


    /**
     * Sets or adds a preference into the shared preferences
     * @param key sharedpref key
     * @param value sharedpref value
     */
    fun setPreference(key: String, value: Any)
    {
        sharedPreferencesEditor = sharedPreferences!!.edit()

        if (value is String)
            sharedPreferencesEditor!!.putString(key, value)
        else if (value is Int)
            sharedPreferencesEditor!!.putInt(key, value)
        else if (value is Boolean)
            sharedPreferencesEditor!!.putBoolean(key, value)
        else if (value is Long)
            sharedPreferencesEditor!!.putLong(key, value)
        else if (value is Float)
            sharedPreferencesEditor!!.putFloat(key, value)

        sharedPreferencesEditor!!.apply()
    }

    /**
     * Sets or adds a preference into the shared preferences
     * @param key sharedpref key
     * @param defaultValue sharedpref defaultVal
     */
    fun getPreference(key: String, defaultValue: Any): Any?
    {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        return when (defaultValue)
        {
            is String -> sharedPreferences!!.getString(key, defaultValue)
            is Int -> sharedPreferences!!.getInt(key, defaultValue)
            is Boolean -> sharedPreferences!!.getBoolean(key, defaultValue)
            is Long -> sharedPreferences!!.getLong(key, defaultValue)
            is Float -> sharedPreferences!!.getFloat(key, defaultValue)
            else -> null
        }
    }

    /**
     * Clears all api configs from the phone
     */
    fun clearApiConfig()
    {
        setPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")
        setPreference(Constants.SharedPrefKeys.API_CORE_USERNAME, "")
        setPreference(Constants.SharedPrefKeys.API_CORE_PASSWORD, "")
    }

    /**
     * Check all the shared pref settings to validate the app is setup with the required info
     * @return boolean if config is valid
     */
    private fun validateConfig(): Boolean
    {
        return getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "") != "" &&
                getPreference(Constants.SharedPrefKeys.API_CORE_USERNAME, "") != "" &&
                getPreference(Constants.SharedPrefKeys.API_CORE_PASSWORD, "") != ""
    }

    //endregion

    //region Internet Methods

    /**
     * Downloads app data from the server
     * @param withFilters [Boolean] wether or not the app should apply download filters
     */
    fun downloadApplicationData(withFilters: Boolean = true)
    {
        //update app data
        if (isOnline())
        {
            val increaseFactor = 8
            progressDialog = ProgressDialog(this, R.style.CustomProgressDialog)
            progressDialog!!.setTitle("Downloading data...")
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog!!.max = 100
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()

            updateThread = Thread(Runnable {

                getDatabase().beginTransaction()

                progressDialogProgess = 0

                context!!.runOnUiThread {
                    progressDialog!!.setTitle("Downloading Users...")
                    progressDialog!!.progress = progressDialogProgess
                }

                /**
                 * SERVER CONFIG
                 */
                //Update Server Config
                val getServerConfig = Server.GetServerConfig(context!!)
                if(getServerConfig.execute())
                {
                    runOnUiThread{

                        var color = getPreference(Constants.SharedPrefKeys.PRIMARY_COLOR_KEY, "")

                        primaryColor =
                            if(color != "")
                                Color.parseColor("#$color")
                            else
                                ResourcesCompat.getColor(resources, R.color.primary, null)

                        color = getPreference(Constants.SharedPrefKeys.PRIMARY_COLOR_DARK_KEY, "")

                        primaryColorDark =
                                if(color != "")
                                    Color.parseColor("#$color")
                                else
                                    ResourcesCompat.getColor(resources, R.color.primary_dark, null)

                        updateAppColors()
                    }
                }

                context!!.runOnUiThread {
                    progressDialog!!.setTitle("Downloading Years...")
                    progressDialog!!.progress = progressDialogProgess
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
                val getYears = Server.GetYears(context!!)
                if (getYears.execute())
                {
                    Year.clearTable(getDatabase())

                    for (year in getYears.years)
                        year.save(getDatabase())
                }

                progressDialogProgess += increaseFactor
                context!!.runOnUiThread {
                    progressDialog!!.setTitle("Downloading Users...")
                    progressDialog!!.progress = progressDialogProgess
                }

                /**
                 * USERS
                 */
                val getUsers = Server.GetUsers(context!!)
                if (getUsers.execute())
                {
                    User.clearTable(getDatabase())

                    for (user in getUsers.users)
                        user.save(getDatabase())
                }

                /**
                 * EVENTS
                 */
                if(getPreference(Constants.SharedPrefKeys.DOWNLOAD_EVENTS_KEY, false) as Boolean)
                {

                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.setTitle("Downloading Events...")
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Update Events
                    val getEvents = Server.GetEvents(context!!)
                    if (getEvents.execute())
                    {
                        Event.clearTable(getDatabase())

                        for (event in getEvents.events)
                            event.save(getDatabase())
                    }

                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.setTitle("Downloading Event Metadata...")
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Update Event Team List
                    val getEventTeamList = Server.GetEventTeamList(context!!)
                    if (getEventTeamList.execute())
                    {
                        EventTeamList.clearTable(getDatabase())

                        for (eventTeamListItem in getEventTeamList.eventTeamList)
                            eventTeamListItem.save(getDatabase())
                    }
                }
                else
                    progressDialogProgess += increaseFactor * 2

                /**
                 * MATCHES
                 */
                if(getPreference(Constants.SharedPrefKeys.DOWNLOAD_MATCHES_KEY, false) as Boolean)
                {

                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.setTitle("Downloading Matches...")
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Update Matches
                    val getMatches = Server.GetMatches(context!!)
                    if (getMatches.execute())
                    {
                        Match.clearTable(getDatabase())

                        for (match in getMatches.matches)
                            match.save(getDatabase())
                    }
                }
                else
                    progressDialogProgess += increaseFactor

                /**
                 * TEAMS
                 */
                if(getPreference(Constants.SharedPrefKeys.DOWNLOAD_TEAMS_KEY, false) as Boolean)
                {
                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.setTitle("Downloading Teams...")
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Update Teams
                    val getTeams = Server.GetTeams(context!!)
                    if (getTeams.execute())
                    {
                        Team.clearTable(getDatabase())

                        for (team in getTeams.teams)
                            team.save(getDatabase())
                    }
                }
                else
                    progressDialogProgess += increaseFactor

                /**
                 * CHECKLISTS
                 */
                if(getPreference(Constants.SharedPrefKeys.DOWNLOAD_CHECKLISTS_KEY, false) as Boolean)
                {
                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.setTitle("Downloading Checklist...")
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Update Checklist Items
                    val getChecklistItems = Server.GetChecklistItems(context!!)
                    if (getChecklistItems.execute())
                    {
                        ChecklistItem.clearTable(getDatabase())

                        for (checklistItem in getChecklistItems.checklistItems)
                            checklistItem.save(getDatabase())
                    }

                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Update Checklist Items Results
                    val getChecklistItemResults = Server.GetChecklistItemResults(context!!)
                    if (getChecklistItemResults.execute())
                    {
                        ChecklistItemResult.clearTable(getDatabase())

                        for (checklistItemResult in getChecklistItemResults.checklistItemResults)
                            checklistItemResult.save(getDatabase())
                    }
                }
                else
                    progressDialogProgess += increaseFactor * 2

                /**
                 * ROBOT INFO
                 */
                if(getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_INFO_KEY, false) as Boolean)
                {
                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.setTitle("Downloading Robot Info...")
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Update Robot Info Keys
                    val getRobotInfoKeys = Server.GetRobotInfoKeys(context!!)
                    if (getRobotInfoKeys.execute())
                    {
                        RobotInfoKey.clearTable(getDatabase())

                        for (robotInfoKey in getRobotInfoKeys.robotInfoKeyList)
                            robotInfoKey.save(getDatabase())
                    }

                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Update Robot Info
                    val getRobotInfo = Server.GetRobotInfo(context!!)
                    if (getRobotInfo.execute())
                    {
                        RobotInfo.clearTable(getDatabase())

                        for (robotInfo in getRobotInfo.robotInfoList)
                            robotInfo.save(getDatabase())
                    }
                }
                else
                    progressDialogProgess += increaseFactor * 2

                /**
                 * SCOUT CARDS
                 */
                if(getPreference(Constants.SharedPrefKeys.DOWNLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean)
                {
                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.setTitle("Downloading Scout Cards...")
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Update Scout Card Info Keys
                    val getScoutCardInfoKeys = Server.GetScoutCardInfoKeys(context!!)
                    if (getScoutCardInfoKeys.execute())
                    {
                        ScoutCardInfoKey.clearTable(getDatabase())

                        for (scoutCardInfoKey in getScoutCardInfoKeys.scoutCardInfoKeys)
                            scoutCardInfoKey.save(getDatabase())
                    }

                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Update Scout Card Info
                    val getScoutCardInfo = Server.GetScoutCardInfo(context!!)
                    if (getScoutCardInfo.execute())
                    {
                        ScoutCardInfo.clearTable(getDatabase())

                        for (scoutCardInfo in getScoutCardInfo.scoutCardInfos)
                            scoutCardInfo.save(getDatabase())
                    }
                }
                else
                    progressDialogProgess += increaseFactor * 2

                /**
                 * ROBOT MEDIA
                 */
                if(getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_MEDIA_KEY, false) as Boolean)
                {
                    progressDialogProgess += increaseFactor
                    context!!.runOnUiThread {
                        progressDialog!!.setTitle("Downloading Robot Media...")
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Get the folder and purge all files
                    val mediaFolder = File(Constants.ROBOT_MEDIA_DIRECTORY)
                    if (mediaFolder.isDirectory)
                        for (child in mediaFolder.listFiles())
                            child.delete()

                    val getRobotMedia = Server.GetRobotMedia(context!!)
                    if (getRobotMedia.execute())
                    {
                        RobotMedia.clearTable(getDatabase(), true)

                        for (robotMedia in getRobotMedia.robotMedia)
                        {
                            if(robotMedia.save(getDatabase()) > 0)
                            {
                                val team = Team(robotMedia.teamId, getDatabase())
                                team.imageFileURI = robotMedia.fileUri
                                team.save(getDatabase())
                            }
                        }

                    }
                }

                getDatabase().finishTransaction()

                runOnUiThread {
                    val year = Year((getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int?)!!, getDatabase())

                    //set the year when showing the event list frag
                    setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, year.serverId!!)

                    progressDialog!!.dismiss()
                    context!!.recreate()
                }
            })

            updateThread!!.start()
        } else
        {
            showSnackbar(getString(R.string.no_internet))
        }
    }

    /**
     * Uploads all stored app data to server
     */
    private fun uploadApplicationData(withFilters: Boolean = false)
    {
        progressDialog = ProgressDialog(context)
        progressDialog!!.max = 100
        progressDialog!!.progress = 0
        progressDialog!!.setTitle("Uploading data...")
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()

        val increaseFactor = 25

        val uploadThread = Thread(Runnable {
            var success = true

            context!!.runOnUiThread {
                progressDialog!!.setTitle("Uploading Checklists...")
                progressDialog!!.progress = progressDialogProgess
            }

            /**
             * CHECKLIST ITEM RESULTS
             */
            if(getPreference(Constants.SharedPrefKeys.UPLOAD_CHECKLISTS_KEY, false) as Boolean)
            {
                for (checklistItemResult in ChecklistItemResult.getObjects(null, null, true, getDatabase())!!)
                {
                    val submitChecklistItemResult = Server.SubmitChecklistItemResult(context!!, checklistItemResult)
                    if (submitChecklistItemResult.execute())
                    {
                        checklistItemResult.isDraft = false
                        checklistItemResult.save(getDatabase())
                    }
                    else
                        success = false

                }
            }

            progressDialogProgess += increaseFactor
            context!!.runOnUiThread {
                progressDialog!!.setTitle("Uploading Robot Info...")
                progressDialog!!.progress = progressDialogProgess
            }

            /**
             * ROBOT INFO
             */
            if(getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_INFO_KEY, false) as Boolean)
            {
                for (robotInfo in RobotInfo.getObjects(null, null, null, null, null, true, getDatabase())!!)
                {
                    val submitRobotInfo = Server.SubmitRobotInfo(context!!, robotInfo)
                    if (submitRobotInfo.execute())
                    {
                        robotInfo.isDraft = false
                        robotInfo.save(getDatabase())
                    }
                    else
                        success = false
                }
            }

            progressDialogProgess += increaseFactor
            context!!.runOnUiThread {
                progressDialog!!.setTitle("Uploading Scout Cards...")
                progressDialog!!.progress = progressDialogProgess
            }

            /**
             * SCOUT CARD INFO
             */
            if(getPreference(Constants.SharedPrefKeys.UPLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean)
            {
                for (scoutCardInfo in ScoutCardInfo.getObjects(null, null, null, null, null, true, getDatabase())!!)
                {
                    val submitScoutCardInfo = Server.SubmitScoutCardInfo(context!!, scoutCardInfo)
                    if (submitScoutCardInfo.execute())
                    {
                        scoutCardInfo.isDraft = false
                        scoutCardInfo.save(getDatabase())
                    }
                    else
                        success = false
                }
            }

            progressDialogProgess += increaseFactor
            context!!.runOnUiThread {
                progressDialog!!.setTitle("Uploading Robot Media...")
                progressDialog!!.progress = progressDialogProgess
            }

            /**
             * ROBOT MEDIA
             */
            if(getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_MEDIA_KEY, false) as Boolean)
            {
                for (robotMedia in RobotMedia.getObjects(null, null, true, getDatabase())!!)
                {
                    val submitRobotMedia = Server.SubmitRobotMedia(context!!, robotMedia)
                    if (submitRobotMedia.execute())
                    {
                        robotMedia.isDraft = false
                        robotMedia.save(getDatabase())
                    }
                    else
                        success = false
                }
            }

            val finalSuccess = success
            runOnUiThread {
                progressDialog!!.hide()

                if (finalSuccess)
                    context!!.recreate()
            }
        })

        uploadThread.start()
    }


    /**
     * Attempts to ping the server with a hello request to verify connection state
     * @return boolean true if connection valid
     */
    fun isOnline(): Boolean
    {
        isOnline = false

        val isOnlineThread = Thread(Runnable {
            val hello = Server.Hello(context!!)

            if (hello.execute())
                isOnline = true
        })

        isOnlineThread.start()

        try
        {
            isOnlineThread.join()
        } catch (e: InterruptedException)
        {
            e.printStackTrace()
        }

        return isOnline
    }

    //endregion

    //region Overrides

    override fun onBackPressed()
    {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START)
        } else
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
            R.id.nav_matches -> changeFragment(MatchListFragment.newInstance(null), false)

            R.id.nav_teams -> changeFragment(TeamListFragment.newInstance(null, null), false)

            R.id.nav_checklist -> changeFragment(ChecklistFragment.newInstance(Team((getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1) as Int?)!!, database!!), null), false)

            R.id.nav_events -> changeFragment(EventListFragment.newInstance(Year(getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int, getDatabase())), false)
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.main, menu)

        val searchItem = menu.findItem(R.id.SearchItem)
        searchView = searchItem.actionView as SearchView
        searchItem.isVisible = isSearchViewVisible
        searchView!!.setOnQueryTextListener(searchViewOnTextChangeListener)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.UploadDataItem ->
            {
                if (isOnline())
                {
                    val uploadAppDataAlertDialogBuilder = AlertDialog.Builder(context!!)
                    var uploadAppDataDialog: AlertDialog? = null

                    val layout = LayoutInflater.from(context!!).inflate(R.layout.layout_upload, null)

                    with(layout)
                    {

                        ChecklistCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.UPLOAD_CHECKLISTS_KEY, false) as Boolean)
                        RobotInfoCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_INFO_KEY, false) as Boolean)
                        ScoutCardInfoCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.UPLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean)
                        RobotMediaCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_MEDIA_KEY, false) as Boolean)
                        
                        ChecklistCheckBox.setOnCheckedChangeListener { _, b ->
                            setPreference(Constants.SharedPrefKeys.UPLOAD_CHECKLISTS_KEY, b)
                        }

                        RobotInfoCheckBox.setOnCheckedChangeListener { _, b ->
                            setPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_INFO_KEY, b)
                        }

                        ScoutCardInfoCheckBox.setOnCheckedChangeListener { _, b ->
                            setPreference(Constants.SharedPrefKeys.UPLOAD_SCOUT_CARD_INFO_KEY, b)
                        }

                        RobotMediaCheckBox.setOnCheckedChangeListener { _, b ->
                            setPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_MEDIA_KEY, b)
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
                val downloadAppDataDialogBuilder = AlertDialog.Builder(context!!)
                var downloadAppDataDialog: AlertDialog? = null

                val layout = LayoutInflater.from(context!!).inflate(R.layout.layout_download, null)

                with(layout)
                {

                    EventsCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.DOWNLOAD_EVENTS_KEY, false) as Boolean)
                    MatchesCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.DOWNLOAD_MATCHES_KEY, false) as Boolean)
                    TeamsCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.DOWNLOAD_TEAMS_KEY, false) as Boolean)
                    ChecklistCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.DOWNLOAD_CHECKLISTS_KEY, false) as Boolean)
                    RobotInfoCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_INFO_KEY, false) as Boolean)
                    ScoutCardInfoCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.DOWNLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean)
                    RobotMediaCheckBox.isChecked = (getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_MEDIA_KEY, false) as Boolean)

                    EventsCheckBox.setOnCheckedChangeListener { _, b ->
                        setPreference(Constants.SharedPrefKeys.DOWNLOAD_EVENTS_KEY, b)
                    }

                    MatchesCheckBox.setOnCheckedChangeListener { _, b ->
                        setPreference(Constants.SharedPrefKeys.DOWNLOAD_MATCHES_KEY, b)
                    }

                    TeamsCheckBox.setOnCheckedChangeListener { _, b ->
                        setPreference(Constants.SharedPrefKeys.DOWNLOAD_TEAMS_KEY, b)
                    }

                    ChecklistCheckBox.setOnCheckedChangeListener { _, b ->
                        setPreference(Constants.SharedPrefKeys.DOWNLOAD_CHECKLISTS_KEY, b)
                    }

                    RobotInfoCheckBox.setOnCheckedChangeListener { _, b ->
                        setPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_INFO_KEY, b)
                    }

                    ScoutCardInfoCheckBox.setOnCheckedChangeListener { _, b ->
                        setPreference(Constants.SharedPrefKeys.DOWNLOAD_SCOUT_CARD_INFO_KEY, b)
                    }

                    RobotMediaCheckBox.setOnCheckedChangeListener { _, b ->
                        setPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_MEDIA_KEY, b)
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

    override fun onFragmentInteraction(uri: Uri)
    {

    }

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
                    val color = getPreference(Constants.SharedPrefKeys.PRIMARY_COLOR_KEY, "")

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
                    val color = getPreference(Constants.SharedPrefKeys.PRIMARY_COLOR_DARK_KEY, "")

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
     * Updates the apps colors to the specified ones in the shared preferences
     */
    fun updateAppColors()
    {
        this.window.statusBarColor = primaryColorDark
        toolbar!!.setBackgroundColor(primaryColor)
        headerConstraintLayout!!.setBackgroundColor(primaryColor)

        val states: Array<IntArray> = arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_enabled))

        val colors: IntArray = intArrayOf(
                primaryColor,
                Color.DKGRAY)

        navigationView!!.itemTextColor = ColorStateList(states, colors)
        navigationView!!.itemIconTintList = ColorStateList(states, colors)

        changeButton!!.setTextColor(primaryColor)
        (changeButton!! as MaterialButton).rippleColor = buttonRipple
    }

    /**
     * Sets the title of the nav bar
     * @param title to set the nav bar title to
     */
    fun setToolbarTitle(title: String)
    {
        supportActionBar!!.title = title
    }

    /**
     * Sets the title of the nav bar
     * @param titleId to set the nav bar title to
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
        teamNumberTextView!!.text = getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1).toString()
        teamNameTextView!!.text = getPreference(Constants.SharedPrefKeys.TEAM_NAME_KEY, "")!!.toString()
    }

    /**
     * Locks the drawer layout
     */
    fun lockDrawerLayout(setBackIcon: Boolean = false, backIconClickListener: View.OnClickListener? = null)
    {
        drawer!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        if(setBackIcon)
        {
            toolbar!!.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_white_24dp, null)
            toolbar!!.setNavigationOnClickListener(backIconClickListener)
        }
        else
            toolbar!!.navigationIcon = null
    }

    /**
     * Unlocks the drawer layout
     */
    fun unlockDrawerLayout()
    {
        toolbar!!.navigationIcon = resources.getDrawable(R.drawable.ic_dehaze_white_24dp, null)
        drawer!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar!!.setNavigationOnClickListener {
            drawer!!.openDrawer(GravityCompat.START)
        }

    }

    /**
     * Displays the snackbar
     * @param message to be displayed
     */
    fun showSnackbar(message: String)
    {
        hideKeyboard()
        Snackbar.make(mainFrame!!, message, Snackbar.LENGTH_SHORT).show()
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
     * @param fragment to change to
     * @param addToBackstack if frag should be added to backstack, if false all frags pop and specified one is shown
     */
    fun changeFragment(fragment: Fragment, addToBackstack: Boolean)
    {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.MainFrame, fragment)
        if (addToBackstack)
            fragmentTransaction.addToBackStack(null) //add to the backstack
        else
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE) //pop all backstacks
        fragmentTransaction.commit()

        if (drawer!!.isDrawerOpen(GravityCompat.START))
            drawer!!.closeDrawer(GravityCompat.START)

        hideKeyboard()

        //        elevateActionBar();
    }

    /**
     * All view loading logic is stored here
     * used for when the activity initially starts up
     * @param savedInstanceState
     */
    private fun loadView(savedInstanceState: Bundle?)
    {
        //if not previous saved state, eg not rotation
        if (savedInstanceState == null)
        {
            //default to the splash frag until changed
            changeFragment(SplashFragment(), false)



            //validate the app config to ensure all properties are filled
            if (validateConfig())
            {
                //android >= marshmallow, permission needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    //write permission not granted, request
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 5885)
                }

                //check any teams are on device and if the device is online
                //if no teams, update data
                if (Team.getObjects(null, null, null, getDatabase())!!.size == 0 && isOnline())
                    downloadApplicationData(false)

                //join back up with the update thread if it is not null
                if (updateThread != null)
                {
                    try
                    {
                        updateThread!!.join()
                    } catch (e: InterruptedException)
                    {
                        e.printStackTrace()
                    }

                }

                //event previously selected, switch to team list
                if ((getPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1) as Int?)!! > 0)
                {
                    navigationView!!.setCheckedItem(R.id.nav_matches)
                    changeFragment(MatchListFragment.newInstance(null), false)
                } else
                {
                    val year = Year((getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int?)!!, getDatabase())

                    //send to eventlist frag
                    changeFragment(EventListFragment.newInstance(year), false)
                }

            } else
            {
                changeFragment(ConfigViewPagerFragment(), false)
            }
        }
    }

    /**
     * Drops the elevation of the actionbar so that specific pages don't have a shadow
     */
    fun dropActionBar()
    {
        appBarLayout!!.elevation = 0f
    }

    /**
     * Elevates the action bar after dropping to preserve the shadow
     */
    fun elevateActionBar()
    {
        appBarLayout!!.elevation = ACTION_BAR_ELEVATION.toFloat()
    }

    /**
     * Sets the menu item to be checked
     */
    fun setCheckedMenuItem(menuItem: Int)
    {
        navigationView!!.menu.getItem(menuItem).isChecked = true
    }

    /**
     * Sets the listener for text being changed for the search view on the toolbar
     * @param listener [SearchView.OnQueryTextListener] listener for the searchview
     */
    fun setSearchViewOnTextChangeListener(listener: SearchView.OnQueryTextListener?)
    {
        searchView?.setOnQueryTextListener(listener)
        searchViewOnTextChangeListener = listener
    }

    private var searchViewOnTextChangeListener: SearchView.OnQueryTextListener? = null

    /**
     * Gets / Sets the visibility of the searchview
     */
    var isSearchViewVisible = false
    set(value)
    {
        if(field != value)
        {
            val item = toolbar!!.menu.findItem(R.id.SearchItem)

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
                (toolbar!!.layoutParams as AppBarLayout.LayoutParams).scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
            else
                (toolbar!!.layoutParams as AppBarLayout.LayoutParams).scrollFlags = 0
            field = value
        }
    }

    //endregion

}
