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
import com.alphadevelopmentsolutions.frcscout.classes.Database
import com.alphadevelopmentsolutions.frcscout.classes.KeyStore
import com.alphadevelopmentsolutions.frcscout.classes.LoadingDialog
import com.alphadevelopmentsolutions.frcscout.classes.table.*
import com.alphadevelopmentsolutions.frcscout.ui.*
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
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
import java.util.*
import kotlin.math.round

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

    val dp = fun(height: Int): Int
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
                val hello = Api.Connect.Hello(context)

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

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        database = Database(context)
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
        updateNavText(Event((keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1) as Int)).apply { load(database) })

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
        loadingDialog = LoadingDialog(context, LoadingDialog.Style.PROGRESS)
        loadingDialog.message = getString(R.string.connecting_to_server)
        loadingDialog.show()

        if (isOnline)
        {
            loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.data))

            val getEvents = Api.Get.Events(context)
            var getEventsSuccess: Boolean? = null
            val getEventTeamList = Api.Get.EventTeamList(context)
            var getEventTeamListSuccess: Boolean? = null

            val getMatches = Api.Get.Matches(context)
            var getMatchesSuccess: Boolean? = null

            val getTeams = Api.Get.Teams(context)
            var getTeamsSuccess: Boolean? = null

            val getChecklistItems = Api.Get.ChecklistItems(context)
            var getChecklistItemsSuccess: Boolean? = null
            val getChecklistItemResults = Api.Get.ChecklistItemResults(context)
            var getChecklistItemResultsSuccess: Boolean? = null

            val getRobotInfoKeys = Api.Get.RobotInfoKeys(context)
            var getRobotInfoKeysSuccess: Boolean? = null
            val getRobotInfo = Api.Get.RobotInfo(context)
            var getRobotInfoSuccess: Boolean? = null

            val getScoutCardInfoKeys = Api.Get.ScoutCardInfoKeys(context)
            var getScoutCardInfoKeysSuccess: Boolean? = null
            val getScoutCardInfo = Api.Get.ScoutCardInfo(context)
            var getScoutCardInfoSuccess: Boolean? = null
            
            val increaseFactor = 4

            val updateThread = Thread(Runnable {

                database.beginTransaction()

                progressDialogProgress = 0

                context.runOnUiThread {
                    loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.app_config))
                    loadingDialog.progress = progressDialogProgress
                }

                //region Downloading

                /**
                 * SERVER CONFIG
                 */
                //Update Server Config
                val getServerConfig = Api.Get.ServerConfig(context)
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
                    loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.years))
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * YEARS
                 */
                //Purge Year Media
                val mediaFolder = Constants.getFileDirectory(this, Constants.YEAR_MEDIA_DIRECTORY)
                if (mediaFolder.isDirectory)
                    for (child in mediaFolder.listFiles())
                        child.delete()

                //Update Years
                val getYears = Api.Get.Years(context)
                if (getYears.execute())
                {
                    Year.clearTable(database)

                    for (year in getYears.years)
                        year.save(database)
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.users))
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * USERS
                 */
                val getUsers = Api.Get.Users(context)
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
                        loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.events))
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Events
                    getEventsSuccess = getEvents.execute()
                    
                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.event_metadata))
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Event Team List
                    getEventTeamListSuccess = getEventTeamList.execute()
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
                        loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.matches))
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Matches
                    getMatchesSuccess = getMatches.execute()
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
                        loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.teams))
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Teams
                    getTeamsSuccess = getTeams.execute()
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
                        loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.checklist))
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Checklist Items
                    getChecklistItemsSuccess = getChecklistItems.execute()

                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Checklist Items Results
                    getChecklistItemResultsSuccess = getChecklistItemResults.execute()
                }
                else
                    progressDialogProgress += increaseFactor * 2

                /**
                 * ROBOT INFO
                 */
                if(!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_INFO_KEY, false) as Boolean && withFilters))
                {
                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.robot_info))
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Robot Info Keys
                    getRobotInfoKeysSuccess = getRobotInfoKeys.execute()

                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Robot Info
                    getRobotInfoSuccess = getRobotInfo.execute()
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
                        loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.scout_cards))
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Scout Card Info Keys
                    getScoutCardInfoKeysSuccess = getScoutCardInfoKeys.execute()
                    
                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.progress = progressDialogProgress
                    }

                    //Update Scout Card Info
                    getScoutCardInfoSuccess = getScoutCardInfo.execute()
                }
                else
                    progressDialogProgress += increaseFactor * 2

                //endregion

                //region Importing

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.progress = progressDialogProgress
                }

                if (getEventsSuccess == true)
                {
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.importing_data), getString(R.string.events))
                        loadingDialog.percentage = 0
                    }

                    Event.clearTable(database)

                    val objs = getEvents.events
                    val objsSize = objs.size

                    for(i in 0 until objsSize)
                    {
                        context.runOnUiThread {
                            loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                        }

                        objs[i].save(database)
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.progress = progressDialogProgress
                }

                if (getEventTeamListSuccess == true)
                {
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.importing_data), getString(R.string.event_metadata))
                        loadingDialog.percentage = 0
                    }

                    EventTeamList.clearTable(database)

                    val objs = getEventTeamList.eventTeamList
                    val objsSize = objs.size

                    for(i in 0 until objsSize)
                    {
                        context.runOnUiThread {
                            loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                        }

                        objs[i].save(database)
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.progress = progressDialogProgress
                }

                if (getMatchesSuccess == true)
                {
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.importing_data), getString(R.string.matches))
                        loadingDialog.percentage = 0
                    }

                    Match.clearTable(database)

                    val objs = getMatches.matches
                    val objsSize = objs.size

                    for(i in 0 until objsSize)
                    {
                        context.runOnUiThread {
                            loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                        }

                        objs[i].save(database)
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.progress = progressDialogProgress
                }

                if (getTeamsSuccess == true)
                {
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.importing_data), getString(R.string.teams))
                        loadingDialog.percentage = 0
                    }

                    Team.clearTable(database)

                    val objs = getTeams.teams
                    val objsSize = objs.size

                    for(i in 0 until objsSize)
                    {
                        context.runOnUiThread {
                            loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                        }

                        objs[i].save(database)
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.progress = progressDialogProgress
                }

                if (getChecklistItemsSuccess == true)
                {
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.importing_data), getString(R.string.checklist))
                        loadingDialog.percentage = 0
                    }

                    ChecklistItem.clearTable(database)

                    val objs = getChecklistItems.checklistItems
                    val objsSize = objs.size

                    for(i in 0 until objsSize)
                    {
                        context.runOnUiThread {
                            loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                        }

                        objs[i].save(database)
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.progress = progressDialogProgress
                }

                if (getChecklistItemResultsSuccess == true)
                {
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.importing_data), getString(R.string.checklist_results))
                        loadingDialog.percentage = 0
                    }

                    ChecklistItemResult.clearTable(database)

                    val objs = getChecklistItemResults.checklistItemResults
                    val objsSize = objs.size

                    for(i in 0 until objsSize)
                    {
                        context.runOnUiThread {
                            loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                        }

                        objs[i].save(database)
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.progress = progressDialogProgress
                }

                if (getRobotInfoKeysSuccess == true)
                {
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.importing_data), getString(R.string.robot_info_metadata))
                        loadingDialog.percentage = 0
                    }

                    RobotInfoKey.clearTable(database)

                    val objs = getRobotInfoKeys.robotInfoKeyList
                    val objsSize = objs.size

                    for(i in 0 until objsSize)
                    {
                        context.runOnUiThread {
                            loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                        }

                        objs[i].save(database)
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.progress = progressDialogProgress
                }

                if (getRobotInfoSuccess == true)
                {
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.importing_data), getString(R.string.robot_info))
                        loadingDialog.percentage = 0
                    }

                    RobotInfo.clearTable(database)

                    val objs = getRobotInfo.robotInfoList
                    val objsSize = objs.size

                    for(i in 0 until objsSize)
                    {
                        context.runOnUiThread {
                            loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                        }

                        objs[i].save(database)
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.progress = progressDialogProgress
                }

                if (getScoutCardInfoKeysSuccess == true)
                {
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.importing_data), getString(R.string.scout_card_metadata))
                        loadingDialog.percentage = 0
                    }

                    ScoutCardInfoKey.clearTable(database)

                    val objs = getScoutCardInfoKeys.scoutCardInfoKeys
                    val objsSize = objs.size

                    for(i in 0 until objsSize)
                    {
                        context.runOnUiThread {
                            loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                        }

                        objs[i].save(database)
                    }
                }

                progressDialogProgress += increaseFactor
                context.runOnUiThread {
                    loadingDialog.progress = progressDialogProgress
                }

                if (getScoutCardInfoSuccess == true)
                {
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.importing_data), getString(R.string.scout_cards))
                        loadingDialog.percentage = 0
                    }

                    ScoutCardInfo.clearTable(database)

                    val objs = getScoutCardInfo.scoutCardInfos
                    val objsSize = objs.size

                    for(i in 0 until objsSize)
                    {
                        context.runOnUiThread {
                            loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                        }

                        objs[i].save(database)
                    }
                }

                //endregion

                /**
                 * ROBOT MEDIA
                 * This has to be ran after teams have been imported
                 */
                if(keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_MEDIA_KEY, false) as Boolean && withFilters)
                {
                    progressDialogProgress += increaseFactor
                    context.runOnUiThread {
                        loadingDialog.message = String.format(context.getString(R.string.downloading_data), getString(R.string.robot_media))
                        loadingDialog.progress = progressDialogProgress
                        loadingDialog.percentage = 0
                        loadingDialog.showPercentage = false
                    }

                    //Get the folder and purge all files
                    val mediaFolder = Constants.getFileDirectory(this, Constants.ROBOT_MEDIA_DIRECTORY)
                    if (mediaFolder.isDirectory)
                        for (child in mediaFolder.listFiles())
                            child.delete()

                    val getRobotMedia = Api.Get.RobotMedia(context)
                    if (getRobotMedia.execute())
                    {

                        RobotMedia.clearTable(database, true)

                        val objs = getRobotMedia.robotMedia
                        val objsSize = objs.size

                        for(i in 0 until objsSize)
                        {
                            context.runOnUiThread {
                                loadingDialog.percentage = round((i.toDouble() / objsSize.toDouble()) * 100.00).toInt()
                            }

                            with(objs[i])
                            {
                                if (save(database) > 0)
                                {
                                    val team = Team(teamId).apply { load(database) }
                                    team.imageFileURI = fileUri
                                    team.save(database)
                                }
                            }
                        }
                    }
                }

                database.finishTransaction()

                runOnUiThread {
                    val year = Year(-1, (keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int?)!!).apply { load(database) }

                    //set the year when showing the event list frag
                    keyStore.setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, year.serverId)

                    loadingDialog.dismiss()

                    if(refreshActivity)
                        context.recreate()

                    else
                        changeFragment(EventListFragment.newInstance(Year(-1, (keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int?)!!).apply { load(database) }), false, false)
                }
            })

            updateThread.start()

            return updateThread
        } else
        {
            loadingDialog.dismiss()
            showSnackbar(getString(R.string.no_internet))
        }


        return null
    }

    /**
     * Uploads all stored app data to server
     * @param withFilters [Boolean] whether or not to apply filters to upload
     * @return [Thread] upload thread
     */
    private fun uploadApplicationData(withFilters: Boolean = false): Thread?
    {
        loadingDialog = LoadingDialog(context, LoadingDialog.Style.PROGRESS)
        loadingDialog.message = getString(R.string.connecting_to_server)
        loadingDialog.show()

        if(isOnline)
        {
            loadingDialog.message = getString(R.string.uploading_data)

            val increaseFactor = 25
            progressDialogProgress = 0

            val uploadThread = Thread(Runnable {
                var success = true

                context.runOnUiThread {
                    loadingDialog.message = String.format(context.getString(R.string.uploading_data), getString(R.string.checklist))
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * CHECKLIST ITEM RESULTS
                 */
                if (!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_CHECKLISTS_KEY, false) as Boolean && withFilters))
                {
                    for (checklistItemResult in ChecklistItemResult.getObjects(null, null, true, database)!!)
                    {
                        val submitChecklistItemResult = Api.Set.ChecklistItemResult(context, checklistItemResult)
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
                    loadingDialog.message = String.format(context.getString(R.string.uploading_data), getString(R.string.robot_info))
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * ROBOT INFO
                 */
                if (!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_INFO_KEY, false) as Boolean && withFilters))
                {
                    for (robotInfo in RobotInfo.getObjects(null, null, null, null, null, true, database))
                    {
                        val submitRobotInfo = Api.Set.RobotInfo(context, robotInfo)
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
                    loadingDialog.message = String.format(context.getString(R.string.uploading_data), getString(R.string.scout_cards))
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * SCOUT CARD INFO
                 */
                if (!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean && withFilters))
                {
                    for (scoutCardInfo in ScoutCardInfo.getObjects(null, null, null, null, null, true, database))
                    {
                        val submitScoutCardInfo = Api.Set.ScoutCardInfo(context, scoutCardInfo)
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
                    loadingDialog.message = String.format(context.getString(R.string.uploading_data), getString(R.string.robot_media))
                    loadingDialog.progress = progressDialogProgress
                }

                /**
                 * ROBOT MEDIA
                 */
                if (!withFilters || (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_MEDIA_KEY, false) as Boolean && withFilters))
                {
                    for (robotMedia in RobotMedia.getObjects(null, null, null, null, true, database))
                    {
                        val submitRobotMedia = Api.Set.RobotMedia(context, robotMedia)
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
        {
            loadingDialog.dismiss()
            showSnackbar(getString(R.string.no_internet))
        }


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

            R.id.nav_checklist -> changeFragment(ChecklistFragment.newInstance(Team((keyStore.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1) as Int)).apply { load(database) }, null), false, false)

            R.id.nav_events -> changeFragment(EventListFragment.newInstance(Year(-1, keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int).apply { load(database) }), false, false)
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
                        ChecklistCheckBox.apply {
                            isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_CHECKLISTS_KEY, false) as Boolean)
                            buttonTintList = checkboxBackground
                            (background as RippleDrawable).setColor(checkboxRipple)
                            setOnCheckedChangeListener { _, b ->
                                keyStore.setPreference(Constants.SharedPrefKeys.UPLOAD_CHECKLISTS_KEY, b)
                            }
                        }

                        RobotInfoCheckBox.apply {
                            isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_INFO_KEY, false) as Boolean)
                            buttonTintList = checkboxBackground
                            (background as RippleDrawable).setColor(checkboxRipple)
                            setOnCheckedChangeListener { _, b ->
                                keyStore.setPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_INFO_KEY, b)
                            }
                        }

                        ScoutCardInfoCheckBox.apply {
                            isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean)
                            buttonTintList = checkboxBackground
                            (background as RippleDrawable).setColor(checkboxRipple)
                            setOnCheckedChangeListener { _, b ->
                                keyStore.setPreference(Constants.SharedPrefKeys.UPLOAD_SCOUT_CARD_INFO_KEY, b)
                            }
                        }

                        RobotMediaCheckBox.apply {
                            isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_MEDIA_KEY, false) as Boolean)
                            buttonTintList = checkboxBackground
                            (background as RippleDrawable).setColor(checkboxRipple)
                            setOnCheckedChangeListener { _, b ->
                                keyStore.setPreference(Constants.SharedPrefKeys.UPLOAD_ROBOT_MEDIA_KEY, b)
                            }
                        }

                        CancelButton.apply {
                            setTextColor(primaryColor)
                            (this as MaterialButton).rippleColor = buttonRipple
                            setOnClickListener {
                                uploadAppDataDialog!!.dismiss()
                            }
                        }

                        UploadButton.apply {
                            backgroundTintList = buttonBackground
                            setOnClickListener {
                                uploadAppDataDialog!!.dismiss()
                                uploadApplicationData(true)
                            }
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

                    EventsCheckBox.apply {
                        isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_EVENTS_KEY, false) as Boolean)
                        buttonTintList = checkboxBackground
                        (background as RippleDrawable).setColor(checkboxRipple)
                        setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_EVENTS_KEY, b)
                        }
                    }

                    MatchesCheckBox.apply {
                        isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_MATCHES_KEY, false) as Boolean)
                        buttonTintList = checkboxBackground
                        (background as RippleDrawable).setColor(checkboxRipple)
                        setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_MATCHES_KEY, b)
                        }
                    }

                    TeamsCheckBox.apply {
                        isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_TEAMS_KEY, false) as Boolean)
                        buttonTintList = checkboxBackground
                        (background as RippleDrawable).setColor(checkboxRipple)
                        setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_TEAMS_KEY, b)
                        }
                    }

                    ChecklistCheckBox.apply {
                        isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_CHECKLISTS_KEY, false) as Boolean)
                        buttonTintList = checkboxBackground
                        (background as RippleDrawable).setColor(checkboxRipple)
                        setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_CHECKLISTS_KEY, b)
                        }
                    }

                    RobotInfoCheckBox.apply {
                        isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_INFO_KEY, false) as Boolean)
                        buttonTintList = checkboxBackground
                        (background as RippleDrawable).setColor(checkboxRipple)
                        setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_INFO_KEY, b)
                        }
                    }

                    ScoutCardInfoCheckBox.apply {
                        isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_SCOUT_CARD_INFO_KEY, false) as Boolean)
                        buttonTintList = checkboxBackground
                        (background as RippleDrawable).setColor(checkboxRipple)
                        setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_SCOUT_CARD_INFO_KEY, b)
                        }
                    }

                    RobotMediaCheckBox.apply {
                        isChecked = (keyStore.getPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_MEDIA_KEY, false) as Boolean)
                        buttonTintList = checkboxBackground
                        (background as RippleDrawable).setColor(checkboxRipple)
                        setOnCheckedChangeListener { _, b ->
                            keyStore.setPreference(Constants.SharedPrefKeys.DOWNLOAD_ROBOT_MEDIA_KEY, b)
                        }
                    }

                    CancelButton.apply {
                        setTextColor(primaryColor)
                        (this as MaterialButton).rippleColor = buttonRipple
                        setOnClickListener {
                            downloadAppDataDialog!!.dismiss()
                        }
                    }

                    DownloadButton.apply {
                        backgroundTintList = buttonBackground
                        setOnClickListener {
                            downloadAppDataDialog!!.dismiss()
                            downloadApplicationData(true)
                        }
                    }
                }

                downloadAppDataDialogBuilder.setView(layout)

                downloadAppDataDialog = downloadAppDataDialogBuilder.create()
                downloadAppDataDialog.show()

                return true
            }

            R.id.SettingsItem ->
            {
                changeFragment(SettingsFragment.newInstance(SettingsFragment.Page.SETTINGS), true)

                return true
            }

            R.id.LogoutItem ->
            {
                RobotInfoKey.clearTable(database)
                RobotInfo.clearTable(database, true)
                ScoutCardInfoKey.clearTable(database)
                ScoutCardInfo.clearTable(database, true)
                ChecklistItem.clearTable(database)
                ChecklistItemResult.clearTable(database, true)
                RobotMedia.clearTable(database, true)
                Team.clearTable(database)
                User.clearTable(database)
                Year.clearTable(database)

                //Purge year media
                var mediaFolder = Constants.getFileDirectory(this, Constants.YEAR_MEDIA_DIRECTORY)
                if (mediaFolder.isDirectory)
                    for (child in mediaFolder.listFiles())
                        child.delete()

                //Purge robot media
                mediaFolder = Constants.getFileDirectory(this, Constants.ROBOT_MEDIA_DIRECTORY)
                if (mediaFolder.isDirectory)
                    for (child in mediaFolder.listFiles())
                        child.delete()

                keyStore.resetData()
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
     * Updates the nav bar text for team name and number
     */
    fun updateNavText(event: Event?)
    {
        navHeader.EventNameTextView.text = if (event?.id != -1) event.toString() else ""
        navHeader.TeamNameTextView.text = "${keyStore.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1)} - ${keyStore.getPreference(Constants.SharedPrefKeys.TEAM_NAME_KEY, "")}"
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
            if (keyStore.validateApiConfig())
            {
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
                    val year = Year(-1, (keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int)).apply { load(database) }
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
