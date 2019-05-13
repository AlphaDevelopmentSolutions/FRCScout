package com.alphadevelopmentsolutions.frcscout.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Api.Server;
import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItem;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItemResult;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.EventTeamList;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.PitCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.User;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Years;
import com.alphadevelopmentsolutions.frcscout.Fragments.ChecklistFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ConfigFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.EventListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.LoginFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.MatchListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.PitCardFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.PitCardListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.QuickStatsFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.RobotMediaFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.RobotMediaListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardAutoFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardEndGameFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardPostGameFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardPreGameFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardTeleopFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.SplashFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.YearListFragment;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MatchListFragment.OnFragmentInteractionListener,
        TeamListFragment.OnFragmentInteractionListener,
        TeamFragment.OnFragmentInteractionListener,
        ScoutCardFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        PitCardFragment.OnFragmentInteractionListener,
        ScoutCardListFragment.OnFragmentInteractionListener,
        PitCardListFragment.OnFragmentInteractionListener,
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
    private MainActivity context;

    private Database database;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private FrameLayout mainFrame;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private AppBarLayout appBarLayout;
    private TextView teamNumberTextView;
    private TextView teamNameTextView;

    private Thread updateThread;

    private final int ACTION_BAR_ELEVATION = 11;
    private int progressDialogProgess;

    private boolean isOnline;

    private ProgressDialog progressDialog;

    private Button changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign ass base layout vars
        mainFrame = findViewById(R.id.MainFrame);
        appBarLayout = findViewById(R.id.AppBarLayout);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //set the action bar
        setSupportActionBar(toolbar);

        changeButton = findViewById(R.id.ChangeButton);
        View navHeader = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        teamNumberTextView = navHeader.findViewById(R.id.TeamNumberTextView);
        teamNameTextView = navHeader.findViewById(R.id.TeamNameTextView);

        //set context
        context = this;

        //open the database as soon as the app starts
        database = new Database(context);
        database.open();

        //add the listener for the drawer
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //set the item selected listener
        navigationView.setNavigationItemSelectedListener(this);

        //change event button logic
        changeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Years year = new Years((Integer) getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)));
                year.load(getDatabase());

                //send to eventlist frag
                changeFragment(EventListFragment.newInstance(year), false);
            }
        });

        //updates the nav text to the current team saved in shared pref
        updateNavText();

        //android >= marshmallow, permission needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //write permission not granted, request
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5885);

            //permission granted, continue
            else
                loadView(savedInstanceState);
        }

        //android < marshmallow, permission not needed
        else
            loadView(savedInstanceState);

    }

    //region Data Management Methods

    /**
     * Returns the active database
     * @return database instance
     */
    public Database getDatabase()
    {
        if(database == null)
            database = new Database(this);

        if(!database.isOpen())
            database.open();

        return database;
    }


    /**
     * Sets or adds a preference into the shared preferences
     * @param key sharedpref key
     * @param value sharedpref value
     */
    public void setPreference(String key, Object value)
    {
        sharedPreferencesEditor = sharedPreferences.edit();

        if(value instanceof String)
            sharedPreferencesEditor.putString(key, (String) value);

        else if(value instanceof Integer)
            sharedPreferencesEditor.putInt(key, (Integer) value);

        else if(value instanceof Boolean)
            sharedPreferencesEditor.putBoolean(key, (Boolean) value);

        else if(value instanceof Long)
            sharedPreferencesEditor.putLong(key, (Long) value);

        else if(value instanceof Float)
            sharedPreferencesEditor.putFloat(key, (Float) value);

        sharedPreferencesEditor.apply();
    }

    /**
     * Sets or adds a preference into the shared preferences
     * @param key sharedpref key
     * @param defaultValue sharedpref defaultVal
     */
    public Object getPreference(String key, Object defaultValue)
    {
        if(sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(defaultValue instanceof String)
            return sharedPreferences.getString(key, (String) defaultValue);

        else if(defaultValue instanceof Integer)
            return sharedPreferences.getInt(key, (Integer) defaultValue);

        else if(defaultValue instanceof Boolean)
            return sharedPreferences.getBoolean(key, (Boolean) defaultValue);

        else if(defaultValue instanceof Long)
            return sharedPreferences.getLong(key, (Long) defaultValue);

        else if(defaultValue instanceof Float)
            return sharedPreferences.getFloat(key, (Float) defaultValue);

        else return null;
    }

    /**
     * Clears all api configs from the phone
     */
    public void clearApiConfig()
    {
        setPreference(Constants.SharedPrefKeys.API_KEY_KEY, "");
        setPreference(Constants.SharedPrefKeys.API_URL_KEY, "");
        setPreference(Constants.SharedPrefKeys.WEB_URL_KEY, "");
    }

    /**
     * Check all the shared pref settings to validate the app is setup with the required info
     * @return boolean if config is valid
     */
    private boolean validateConfig()
    {
        return !getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").equals("") &&
                !getPreference(Constants.SharedPrefKeys.WEB_URL_KEY, "").equals("") &&
                !getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").equals("");
    }

    //endregion

    //region Internet Methods

    /**
     * Downloads all app data from the server
     * @param downloadMedia whether or not the app should download robot media from server
     */
    public void downloadApplicationData(final boolean downloadMedia)
    {
        //update all app data
        if(isOnline())
        {
            progressDialog = new ProgressDialog(this, R.style.CustomProgressDialog);
            progressDialog.setTitle("Downloading data...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.show();

            updateThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {

                    File mediaFolder;

                    progressDialogProgess = 0;

                    context.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            progressDialog.setTitle("Downloading users...");
                            progressDialog.setProgress(progressDialogProgess);
                        }
                    });


                    //update users
                    Server.GetUsers getUsers = new Server.GetUsers(context);

                    if (getUsers.execute())
                    {
                        User.clearTable(getDatabase());
                        for (User user : getUsers.getUsers())
                        {
                            user.save(getDatabase());
                        }
                    }

                    progressDialogProgess = 5;

                    context.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            progressDialog.setTitle("Downloading years...");
                            progressDialog.setProgress(progressDialogProgess);
                        }
                    });


                    //Get the folder and purge all files
                    mediaFolder = new File(Constants.YEAR_MEDIA_DIRECTORY);
                    if (mediaFolder.isDirectory())
                        for (File child : mediaFolder.listFiles())
                            child.delete();

                    //update years
                    Server.GetYears getYears = new Server.GetYears(context);
                    if (getYears.execute())
                    {
                        Years.clearTable(getDatabase());
                        for (Years year : getYears.getYears())
                            year.save(getDatabase());
                    }


                    //update events
                    Server.GetEvents getEvents = new Server.GetEvents(context);
                    if (getEvents.execute())
                    {
                        Event.clearTable(getDatabase());
                        for (Event event : getEvents.getEvents())
                            event.save(getDatabase());
                    }

                    progressDialogProgess = 10;

                    context.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            progressDialog.setTitle("Downloading events...");
                            progressDialog.setProgress(progressDialogProgess);
                        }
                    });

                    //clear the saved data
                    EventTeamList.clearTable(getDatabase());
                    Team.clearTable(getDatabase());
                    ScoutCard.clearTable(getDatabase(), false);
                    PitCard.clearTable(getDatabase(), false);
                    Match.clearTable(getDatabase());
                    ChecklistItem.clearTable(getDatabase());
                    ChecklistItemResult.clearTable(getDatabase(), false);

                    progressDialogProgess = 20;
                    context.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            progressDialog.setTitle("Downloading checklist data");
                            progressDialog.setProgress(progressDialogProgess);
                        }
                    });

                    //download checklist data
                    Server.GetChecklistItems getChecklistItems = new Server.GetChecklistItems(context);
                    if(getChecklistItems.execute())
                    {
                        for(ChecklistItem checklistItem : getChecklistItems.getChecklistItems())
                            checklistItem.save(getDatabase());
                    }

                    Server.GetChecklistItemResults getChecklistItemResults = new Server.GetChecklistItemResults(context);
                    if(getChecklistItemResults.execute())
                    {
                        for(ChecklistItemResult checklistItemResult : getChecklistItemResults.getChecklistItemResults())
                            checklistItemResult.save(getDatabase());
                    }

                    final int remainingPercent = (downloadMedia) ? 90 : 100 - progressDialogProgess; //max amount of percentage we have before we can't add anymore to the progress dialog

                    final ArrayList<Event> events = Event.getEvents(null, null, getDatabase());

                    //iterate through each event and get its data
                    for (int i = 0; i < events.size(); i++)
                    {
                        final Event event = events.get(i);

                        context.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressDialog.setTitle("Downloading teams at " + event.getBlueAllianceId());
                            }
                        });

                        //update teams
                        Server.GetTeamsAtEvent getTeamsAtEvent = new Server.GetTeamsAtEvent(context, event);

                        //get teams at current event
                        if (getTeamsAtEvent.execute())
                        {
                            //get the teams from API
                            ArrayList<Team> teams = getTeamsAtEvent.getTeams();

                            for (Team team : teams)
                            {
                                //check if the team exists in the database
                                //if not, save it
                                if (!team.load(getDatabase()))
                                    team.save(getDatabase());

                                //save a new eventteamlist to the database
                                (new EventTeamList(-1, team.getId(), event.getBlueAllianceId())).save(getDatabase());
                            }
                        }

                        context.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressDialog.setTitle("Downloading matches");
                            }
                        });

                        //update matches
                        Server.GetMatches getMatches = new Server.GetMatches(context, event);

                        //get teams at current event
                        if (getMatches.execute())
                        {
                            //get the teams from API
                            for (Match match : getMatches.getMatches())
                                match.save(getDatabase());

                        }

                        context.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressDialog.setTitle("Downloading scout card data");
                            }
                        });

                        //update scout cards
                        Server.GetScoutCards getScoutCards = new Server.GetScoutCards(context, event);

                        if (getScoutCards.execute())
                        {
                            for (ScoutCard scoutCard : getScoutCards.getScoutCards())
                                scoutCard.save(getDatabase());
                        }

                        context.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressDialog.setTitle("Downloading teams at pit card data");

                            }
                        });

                        //update pit cards
                        Server.GetPitCards getPitCards = new Server.GetPitCards(context, event);

                        if (getPitCards.execute())
                        {
                            for (PitCard pitCard : getPitCards.getPitCards())
                                pitCard.save(getDatabase());
                        }

                        final int finalI = i;
                        context.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressDialog.setProgress((remainingPercent / (events.size() - finalI))  + progressDialogProgess);
                            }
                        });
                    }


                    //download robot media from server
                    if (downloadMedia)
                    {
                        progressDialogProgess = 90;

                        context.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressDialog.setTitle("Download robot media...");
                                progressDialog.setProgress(progressDialogProgess);
                            }
                        });

                        //Get the folder and purge all files
                        mediaFolder = new File(Constants.ROBOT_MEDIA_DIRECTORY);
                        if (mediaFolder.isDirectory())
                            for (File child : mediaFolder.listFiles())
                                child.delete();

                        RobotMedia.clearTable(getDatabase(), false);
                        Server.GetRobotMedia getRobotMedia;

                        for (final Team team : Team.getTeams(null, null, null, getDatabase()))
                        {
                            context.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    progressDialog.setTitle("Downloading teams " + team.getId() + " robot media");
                                }
                            });

                            getRobotMedia = new Server.GetRobotMedia(context, team.getId());

                            if (getRobotMedia.execute())
                            {
                                for (RobotMedia robotMedia : getRobotMedia.getRobotMedia())
                                {
                                    robotMedia.save(getDatabase());

                                    //save the image for the team
                                    team.setImageFileURI(robotMedia.getFileUri());
                                    team.save(getDatabase());
                                }
                            }
                        }

                    }

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            Years year = new Years((Integer) getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)));
                            year.load(getDatabase());

                            //set the year when showing the event list frag
                            setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, year.getServerId());

                            progressDialog.dismiss();
                            changeFragment(EventListFragment.newInstance(year), false);
                        }
                    });
                }
            });

            updateThread.start();
        }

        else
        {
            showSnackbar(getString(R.string.no_internet));
        }
    }

    /**
     * Uploads all stored app data to server
     */
    private void uploadApplicationData()
    {
        progressDialog = new ProgressDialog(context);

        final ArrayList<Team> teams = Team.getTeams(null, null, null, getDatabase());
        final int totalTeams = teams.size();

        progressDialog.setMax(totalTeams);
        progressDialog.setProgress(0);
        progressDialog.setTitle("Uploading data...");
        progressDialog.show();

        Thread uploadThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                boolean success = true;

                for(Event event : Event.getEvents(null, null, getDatabase()))
                {

                    //upload team specific data
                    for (Team team : teams)
                    {
                        for (ScoutCard scoutCard : ScoutCard.getScoutCards(event, null, team, null,true, getDatabase()))
                        {
                            Server.SubmitScoutCard submitScoutCard = new Server.SubmitScoutCard(context, scoutCard);
                            if (submitScoutCard.execute())
                            {
                                scoutCard.setDraft(false);
                                scoutCard.save(getDatabase());
                            } else
                                success = false;
                        }

                        for (PitCard pitCard : PitCard.getPitCards(event, team, null, true, getDatabase()))
                        {
                            Server.SubmitPitCard submitScoutCard = new Server.SubmitPitCard(context, pitCard);
                            if (submitScoutCard.execute())
                            {
                                pitCard.setDraft(false);
                                pitCard.save(getDatabase());
                            } else
                                success = false;
                        }

                        for (RobotMedia robotMedia : RobotMedia.getRobotMedia(null, team, true, getDatabase()))
                        {
                            Server.SubmitRobotMedia submitRobotMedia = new Server.SubmitRobotMedia(context, robotMedia);
                            if (submitRobotMedia.execute())
                            {
                                robotMedia.setDraft(false);
                                robotMedia.save(getDatabase());
                            } else
                                success = false;
                        }
                    }

                    //Checklist item results
                    for(ChecklistItem checklistItem : ChecklistItem.getChecklistItems(null, getDatabase()))
                    {
                        for(ChecklistItemResult checklistItemResult : checklistItem.getResults(null,true, getDatabase()))
                        {
                            Server.SubmitChecklistItemResult submitChecklistItemResult = new Server.SubmitChecklistItemResult(context, checklistItemResult);
                            if(submitChecklistItemResult.execute())
                            {
                                checklistItemResult.setDraft(false);
                                checklistItemResult.save(getDatabase());
                            }
                            else
                                success = false;

                        }
                    }

                }

                final boolean finalSuccess = success;
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressDialog.hide();

                        if(finalSuccess)
                            context.recreate();
                    }
                });
            }
        });

        uploadThread.start();
    }


    /**
     * Attempts to ping the server with a hello request to verify connection state
     * @return boolean true if connection valid
     */
    public boolean isOnline()
    {
        isOnline = false;

        Thread isOnlineThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Server.Hello hello = new Server.Hello(context);

                if(hello.execute())
                    isOnline = true;
            }
        });

        isOnlineThread.start();

        try
        {
            isOnlineThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return isOnline;
    }

    //endregion

    //region Overrides

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch(id)
        {
            case R.id.nav_matches:
                changeFragment(MatchListFragment.newInstance(null), false);
                break;

            case R.id.nav_teams:
                changeFragment(TeamListFragment.newInstance(null, null), false);
                break;

            case R.id.nav_checklist:
                changeFragment(ChecklistFragment.newInstance(new Team((Integer) context.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1), database), null), false);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5885);
            }
            else
            {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.UploadDataItem:
                if(isOnline())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.upload_scout_cards)
                            .setMessage(R.string.upload_scout_cards_warning)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    uploadApplicationData();

                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else
                {
                    showSnackbar(getString(R.string.no_internet));
                }

                return true;

            case R.id.RefreshDataItem:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.download_media)
                        .setMessage(R.string.download_media_desc)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                downloadApplicationData(true);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                downloadApplicationData(false);

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
        }

        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    //endregion

    //region Layout Methods

    /**
     * Sets the title of the nav bar
     * @param title to set the nav bar title to
     */
    public void setTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    /**
     * Sets the title of the nav bar
     * @param titleId to set the nav bar title to
     */
    public void setTitle(int titleId)
    {
        getSupportActionBar().setTitle(titleId);
    }

    /**
     * Updates the nav bar text for team name and number
     */
    public void updateNavText()
    {
        teamNumberTextView.setText(String.valueOf(getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1)));
        teamNameTextView.setText(getPreference(Constants.SharedPrefKeys.TEAM_NAME_KEY, "").toString());
    }

    /**
     * Locks the drawer layout
     */
    public void lockDrawerLayout()
    {
        toolbar.setNavigationIcon(null);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * Unlocks the drawer layout
     */
    public void unlockDrawerLayout()
    {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_dehaze_white_24dp, null));
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    /**
     * Displays the snackbar
     * @param message to be displayed
     */
    public void showSnackbar(String message)
    {
        hideKeyboard();
        (Snackbar.make(mainFrame, message, Snackbar.LENGTH_SHORT)).show();
    }

    /**
     * Hides the keyboard from view if it is showing
     */
    public void hideKeyboard()
    {
        //hide the keyboard before showing the snackbar
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * Changes the current fragment to the specified one
     * @param fragment to change to
     * @param addToBackstack if frag should be added to backstack, if false all frags pop and specified one is shown
     */
    public void changeFragment(Fragment fragment, boolean addToBackstack)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.MainFrame, fragment);
        if(addToBackstack) fragmentTransaction.addToBackStack(null); //add to the backstack
        else getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //pop all backstacks
        fragmentTransaction.commit();

        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);

        hideKeyboard();

//        elevateActionBar();
    }

    /**
     * All view loading logic is stored here
     * used for when the activity initially starts up
     * @param savedInstanceState
     */
    private void loadView(Bundle savedInstanceState)
    {
        //if not previous saved state, eg not rotation
        if (savedInstanceState == null)
        {

            //default to the splash frag until changed
            changeFragment(new SplashFragment(), false);

            //validate the app config to ensure all properties are filled
            if (validateConfig())
            {
                //check any teams are on device and if the device is online
                //if no teams, update data
                if (Team.getTeams(null, null, null, getDatabase()).size() == 0 && isOnline())
                    downloadApplicationData(false);

                //join back up with the update thread if it is not null
                if (updateThread != null)
                {
                    try
                    {
                        updateThread.join();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                //event previously selected, switch to team list
                if((Integer) getPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1) > 0)
                {
                    navigationView.setCheckedItem(R.id.nav_matches);
                    changeFragment(MatchListFragment.newInstance(null), false);
                }

                else
                {
                    Years year = new Years((Integer) getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)));
                    year.load(getDatabase());

                    //send to eventlist frag
                    changeFragment(EventListFragment.newInstance(year), false);
                }

            } else
            {
                changeFragment(new ConfigFragment(), false);
            }
        }
    }

    /**
     * Drops the elevation of the actionbar so that specific pages don't have a shadow
     */
    public void dropActionBar()
    {
        appBarLayout.setElevation(0);
    }

    /**
     * Elevates the action bar after dropping to preserve the shadow
     */
    public void elevateActionBar()
    {
        appBarLayout.setElevation(ACTION_BAR_ELEVATION);
    }

    public void setChangeButtonOnClickListener(View.OnClickListener onClickListener, String buttonText, boolean showMenuItems)
    {
        //change event button logic
        changeButton.setOnClickListener(onClickListener);
        changeButton.setText(buttonText);

        if(!showMenuItems)
            navigationView.getMenu().clear();
        else
            navigationView.inflateMenu(R.menu.activity_main_drawer);



    }

    //endregion

}
