package com.alphadevelopmentsolutions.frcscout.Activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.AppBarLayout
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
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
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        MatchListFragment.OnFragmentInteractionListener,
        TeamListFragment.OnFragmentInteractionListener,
        TeamFragment.OnFragmentInteractionListener,
        ScoutCardFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        RobotInfoFragment.OnFragmentInteractionListener,
        ScoutCardListFragment.OnFragmentInteractionListener,
        ScoutCardPreGameFragment.OnFragmentInteractionListener,
        ScoutCardAutoFragment.OnFragmentInteractionListener,
        ScoutCardTeleopFragment.OnFragmentInteractionListener,
        ScoutCardEndGameFragment.OnFragmentInteractionListener,
        ScoutCardPostGameFragment.OnFragmentInteractionListener,
        RobotMediaFragment.OnFragmentInteractionListener,
        RobotMediaListFragment.OnFragmentInteractionListener,
        QuickStatsFragment.OnFragmentInteractionListener,
        EventListFragment.OnFragmentInteractionListener,
        ConfigFragment.OnFragmentInteractionListener,
        SplashFragment.OnFragmentInteractionListener,
        ChecklistFragment.OnFragmentInteractionListener,
        YearListFragment.OnFragmentInteractionListener
{
    private var context: MainActivity? = null

    private var database: Database? = null
    private var sharedPreferences: SharedPreferences? = null
    private var sharedPreferencesEditor: SharedPreferences.Editor? = null

    private var mainFrame: FrameLayout? = null
    private var toolbar: Toolbar? = null
    private var drawer: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var appBarLayout: AppBarLayout? = null
    private var teamNumberTextView: TextView? = null
    private var teamNameTextView: TextView? = null

    private var updateThread: Thread? = null

    private val ACTION_BAR_ELEVATION = 11
    private var progressDialogProgess: Int = 0

    private var isOnline: Boolean = false

    private var progressDialog: ProgressDialog? = null

    private var changeButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //assign ass base layout vars
        mainFrame = findViewById(R.id.MainFrame)
        appBarLayout = findViewById(R.id.AppBarLayout)
        toolbar = findViewById(R.id.toolbar)
        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        //set the action bar
        setSupportActionBar(toolbar)

        changeButton = findViewById(R.id.ChangeButton)
        val navHeader = navigationView!!.getHeaderView(0)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        teamNumberTextView = navHeader.findViewById(R.id.TeamNumberTextView)
        teamNameTextView = navHeader.findViewById(R.id.TeamNameTextView)

        //set context
        context = this

        //open the database as soon as the app starts
        database = Database(context!!)
        database!!.open()

        //add the listener for the drawer
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        //set the item selected listener
        navigationView!!.setNavigationItemSelectedListener(this)

        //change event button logic
        changeButton!!.setOnClickListener {
            val year = Year((getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int?)!!, getDatabase())

            //send to eventlist frag
            changeFragment(EventListFragment.newInstance(year), false)
        }

        //updates the nav text to the current team saved in shared pref
        updateNavText()

        //android >= marshmallow, permission needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //write permission not granted, request
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 5885)
            else
                loadView(savedInstanceState)//permission granted, continue
        } else
            loadView(savedInstanceState)//android < marshmallow, permission not needed

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
        setPreference(Constants.SharedPrefKeys.API_URL_KEY, "")
        setPreference(Constants.SharedPrefKeys.WEB_URL_KEY, "")
    }

    /**
     * Check all the shared pref settings to validate the app is setup with the required info
     * @return boolean if config is valid
     */
    private fun validateConfig(): Boolean
    {
        return getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "") != "" &&
                getPreference(Constants.SharedPrefKeys.WEB_URL_KEY, "") != "" &&
                getPreference(Constants.SharedPrefKeys.API_URL_KEY, "") != ""
    }

    //endregion

    //region Internet Methods

    /**
     * Downloads all app data from the server
     * @param downloadMedia whether or not the app should download robot media from server
     */
    fun downloadApplicationData(downloadMedia: Boolean)
    {
        //update all app data
        if (isOnline())
        {
            progressDialog = ProgressDialog(this, R.style.CustomProgressDialog)
            progressDialog!!.setTitle("Downloading data...")
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog!!.max = 100
            progressDialog!!.show()

            updateThread = Thread(Runnable {
                var mediaFolder: File

                progressDialogProgess = 0

                context!!.runOnUiThread {
                    progressDialog!!.setTitle("Downloading users...")
                    progressDialog!!.progress = progressDialogProgess
                }


                //update users
                val getUsers = Server.GetUsers(context!!)

                if (getUsers.execute())
                {
                    User.clearTable(getDatabase())
                    for (user in getUsers.users)
                    {
                        user.save(getDatabase())
                    }
                }

                progressDialogProgess = 5

                context!!.runOnUiThread {
                    progressDialog!!.setTitle("Downloading years...")
                    progressDialog!!.progress = progressDialogProgess
                }


                //Get the folder and purge all files
                mediaFolder = File(Constants.YEAR_MEDIA_DIRECTORY)
                if (mediaFolder.isDirectory)
                    for (child in mediaFolder.listFiles())
                        child.delete()

                //update years
                val getYears = Server.GetYears(context!!)
                if (getYears.execute())
                {
                    Year.clearTable(getDatabase())
                    RobotInfoKey.clearTable(getDatabase())
                    ScoutCardInfoKey.clearTable(getDatabase())

                    for (year in getYears.years)
                    {
                        if (year.save(getDatabase()) > 0)
                        {
                            //update robot info keys
                            val getRobotInfoKeys = Server.GetRobotInfoKeys(context!!, year)

                            if (getRobotInfoKeys.execute())
                            {
                                for (robotInfoKey in getRobotInfoKeys.robotInfoKeyList)
                                {
                                    robotInfoKey.yearId = year.serverId!!
                                    robotInfoKey.save(getDatabase())
                                }
                            }

                            //update scout card info keys
                            val getScoutCardInfoKeys = Server.GetScoutCardInfoKeys(context!!, year)

                            if (getScoutCardInfoKeys.execute())
                            {
                                for (scoutCardInfoKey in getScoutCardInfoKeys.scoutCardInfoKeys)
                                {
                                    scoutCardInfoKey.yearId = year.serverId!!
                                    scoutCardInfoKey.save(getDatabase())
                                }
                            }
                        }
                    }
                }

                //update events
                val getEvents = Server.GetEvents(context!!)
                if (getEvents.execute())
                {
                    Event.clearTable(getDatabase())
                    for (event in getEvents.events)
                        event.save(getDatabase())
                }

                progressDialogProgess = 10

                context!!.runOnUiThread {
                    progressDialog!!.setTitle("Downloading events...")
                    progressDialog!!.progress = progressDialogProgess
                }

                //clear the saved data
                EventTeamList.clearTable(getDatabase())
                Team.clearTable(getDatabase())
                ScoutCard.clearTable(getDatabase(), false)
                RobotInfo.clearTable(getDatabase(), true)
                Match.clearTable(getDatabase())
                ChecklistItem.clearTable(getDatabase())
                ChecklistItemResult.clearTable(getDatabase(), false)

                progressDialogProgess = 20
                context!!.runOnUiThread {
                    progressDialog!!.setTitle("Downloading checklist data...")
                    progressDialog!!.progress = progressDialogProgess
                }

                //download checklist data
                val getChecklistItems = Server.GetChecklistItems(context!!)
                if (getChecklistItems.execute())
                {
                    for (checklistItem in getChecklistItems.checklistItems)
                        checklistItem.save(getDatabase())
                }

                val getChecklistItemResults = Server.GetChecklistItemResults(context!!)
                if (getChecklistItemResults.execute())
                {
                    for (checklistItemResult in getChecklistItemResults.checklistItemResults)
                        checklistItemResult.save(getDatabase())
                }

                val remainingPercent = if (downloadMedia) 90 else 100 - progressDialogProgess //max amount of percentage we have before we can't add anymore to the progress dialog

                val events = Event.getObjects(null, null, getDatabase())

                //iterate through each event and get its data
                for (i in events!!.indices)
                {
                    val event = events[i]

                    context!!.runOnUiThread { progressDialog!!.setTitle("Downloading teams at " + event.blueAllianceId + "...") }

                    //update teams
                    val getTeamsAtEvent = Server.GetTeamsAtEvent(context!!, event)

                    //get teams at current event
                    if (getTeamsAtEvent.execute())
                    {
                        //get the teams from API
                        val teams = getTeamsAtEvent.teams

                        for (team in teams)
                        {
                            //check if the team exists in the database
                            //if not, save it
                            if (!team.load(getDatabase()))
                                team.save(getDatabase())

                            //save a new eventteamlist to the database
                            EventTeamList(-1, team.id!!, event.blueAllianceId!!).save(getDatabase())
                        }
                    }

                    context!!.runOnUiThread { progressDialog!!.setTitle("Downloading matches...") }

                    //update matches
                    val getMatches = Server.GetMatches(context!!, event)

                    //get teams at current event
                    if (getMatches.execute())
                    {
                        //get the teams from API
                        for (match in getMatches.matches)
                            match.save(getDatabase())

                    }

                    context!!.runOnUiThread { progressDialog!!.setTitle("Downloading scout card data...") }

                    //update scout cards
                    val getScoutCards = Server.GetScoutCards(context!!, event)

                    if (getScoutCards.execute())
                    {
                        for (scoutCard in getScoutCards.scoutCards)
                            scoutCard.save(getDatabase())
                    }

                    context!!.runOnUiThread { progressDialog!!.setTitle("Downloading robot info data...") }

                    //update robot info
                    val getRobotInfo = Server.GetRobotInfo(context!!, event)

                    if (getRobotInfo.execute())
                    {
                        for (robotInfo in getRobotInfo.robotInfoList)
                            robotInfo.save(getDatabase())
                    }

                    context!!.runOnUiThread { progressDialog!!.progress = remainingPercent / (events.size - i) + progressDialogProgess }
                }


                //download robot media from server
                if (downloadMedia)
                {
                    progressDialogProgess = 90

                    context!!.runOnUiThread {
                        progressDialog!!.setTitle("Download robot media...")
                        progressDialog!!.progress = progressDialogProgess
                    }

                    //Get the folder and purge all files
                    mediaFolder = File(Constants.ROBOT_MEDIA_DIRECTORY)
                    if (mediaFolder.isDirectory)
                        for (child in mediaFolder.listFiles())
                            child.delete()

                    RobotMedia.clearTable(getDatabase(), false)
                    var getRobotMedia: Server.GetRobotMedia

                    for (team in Team.getObjects(null, null, null, getDatabase())!!)
                    {
                        context!!.runOnUiThread { progressDialog!!.setTitle("Downloading teams " + team.id + " robot media...") }

                        getRobotMedia = Server.GetRobotMedia(context!!, team.id!!)

                        if (getRobotMedia.execute())
                        {
                            for (robotMedia in getRobotMedia.robotMedia)
                            {
                                robotMedia.save(getDatabase())

                                //save the image for the team
                                team.imageFileURI = robotMedia.fileUri
                                team.save(getDatabase())
                            }
                        }
                    }

                }

                runOnUiThread {
                    val year = Year((getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int?)!!, getDatabase())

                    //set the year when showing the event list frag
                    setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, year.serverId!!)

                    progressDialog!!.dismiss()
                    changeFragment(EventListFragment.newInstance(year), false)
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
    private fun uploadApplicationData()
    {
        progressDialog = ProgressDialog(context)

        val teams = Team.getObjects(null, null, null, getDatabase())
        val totalTeams = teams!!.size

        progressDialog!!.max = totalTeams
        progressDialog!!.progress = 0
        progressDialog!!.setTitle("Uploading data...")
        progressDialog!!.show()

        val uploadThread = Thread(Runnable {
            var success = true

            for (event in Event.getObjects(null, null, getDatabase())!!)
            {

                //upload team specific data
                for (team in teams)
                {

                    for (robotMedia in RobotMedia.getObjects(null, team, true, getDatabase())!!)
                    {
                        val submitRobotMedia = Server.SubmitRobotMedia(context!!, robotMedia)
                        if (submitRobotMedia.execute())
                        {
                            robotMedia.isDraft = false
                            robotMedia.save(getDatabase())
                        } else
                            success = false
                    }

                    for (robotInfo in RobotInfo.getObjects(null, null, team, null, null, true, getDatabase())!!)
                    {
                        val submitRobotInfo = Server.SubmitRobotInfo(context!!, robotInfo)
                        if (submitRobotInfo.execute())
                        {
                            robotInfo.isDraft = false
                            robotInfo.save(getDatabase())
                        } else
                            success = false
                    }
                }

                //Checklist item results
                for (checklistItem in ChecklistItem.getObjects(null, getDatabase())!!)
                {
                    for (checklistItemResult in checklistItem.getResults(null, true, getDatabase())!!)
                    {
                        val submitChecklistItemResult = Server.SubmitChecklistItemResult(context!!, checklistItemResult)
                        if (submitChecklistItemResult.execute())
                        {
                            checklistItemResult.isDraft = false
                            checklistItemResult.save(getDatabase())
                        } else
                            success = false

                    }
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
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        val id = item.itemId

        when (id)
        {
            R.id.nav_matches -> changeFragment(MatchListFragment.newInstance(null), false)

            R.id.nav_teams -> changeFragment(TeamListFragment.newInstance(null, null), false)

            R.id.nav_checklist -> changeFragment(ChecklistFragment.newInstance(Team((context!!.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1) as Int?)!!, database!!), null), false)
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 5885)
            } else
            {
                val intent = intent
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.main, menu)
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
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.upload_data)
                            .setMessage(R.string.upload_data_desc)
                            .setPositiveButton(R.string.yes) { _, _ -> uploadApplicationData() }
                            .setNegativeButton(R.string.cancel) { _, _ -> }
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show()
                } else
                {
                    showSnackbar(getString(R.string.no_internet))
                }

                return true
            }

            R.id.RefreshDataItem ->
            {

                val builder = AlertDialog.Builder(context!!)
                builder.setTitle(R.string.download_media)
                        .setMessage(R.string.download_media_desc)
                        .setPositiveButton(R.string.yes) { _, _ -> downloadApplicationData(true) }
                        .setNegativeButton(R.string.no) { _, _ -> downloadApplicationData(false) }
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()

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
     * Sets the title of the nav bar
     * @param title to set the nav bar title to
     */
    fun setTitle(title: String)
    {
        supportActionBar!!.title = title
    }

    /**
     * Sets the title of the nav bar
     * @param titleId to set the nav bar title to
     */
    override fun setTitle(titleId: Int)
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
    fun lockDrawerLayout()
    {
        toolbar!!.navigationIcon = null
        drawer!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    /**
     * Unlocks the drawer layout
     */
    fun unlockDrawerLayout()
    {
        toolbar!!.navigationIcon = resources.getDrawable(R.drawable.ic_dehaze_white_24dp, null)
        drawer!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

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
                changeFragment(ConfigFragment(), false)
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

    fun setChangeButtonOnClickListener(onClickListener: View.OnClickListener, buttonText: String, showMenuItems: Boolean)
    {
        //change event button logic
        changeButton!!.setOnClickListener(onClickListener)
        changeButton!!.text = buttonText

        if (!showMenuItems)
            navigationView!!.menu.clear()
        else
            navigationView!!.inflateMenu(R.menu.activity_main_drawer)


    }

    //endregion

}
