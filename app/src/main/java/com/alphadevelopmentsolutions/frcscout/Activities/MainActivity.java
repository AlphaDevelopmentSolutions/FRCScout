package com.alphadevelopmentsolutions.frcscout.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Api.Server;
import com.alphadevelopmentsolutions.frcscout.Classes.ChecklistItem;
import com.alphadevelopmentsolutions.frcscout.Classes.ChecklistItemResult;
import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.EventTeamList;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.PitCard;
import com.alphadevelopmentsolutions.frcscout.Classes.RobotMedia;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
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
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;

import java.io.File;
import java.util.ArrayList;

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
        ChecklistFragment.OnFragmentInteractionListener
{

    private Database database;

    private FrameLayout mainFrame;

    private MainActivity context;

    private Thread updateThread;

    private final int ACTION_BAR_ELEVATION = 11;
    
    private int progressDialogProgess;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private AppBarLayout appBarLayout;

    private TextView teamNumberTextView;
    private TextView teamNameTextView;

    private NavigationView navigationView;

    private Toolbar toolbar;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open the database as soon as the app starts
        database = new Database(this);
        database.open();

        mainFrame = findViewById(R.id.MainFrame);
        appBarLayout = findViewById(R.id.AppBarLayout);

        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);

        teamNumberTextView = navHeader.findViewById(R.id.TeamNumberTextView);
        teamNameTextView = navHeader.findViewById(R.id.TeamNameTextView);

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

            navigationView.setCheckedItem(R.id.nav_teams);

            //validate the app config to ensure all properties are filled
            if (validateConfig())
            {
                //check any teams are on device and if the device is online
                //if no teams, update data
                if (getDatabase().getTeams().size() == 0 && isOnline())
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

                //change the frag to the eventlist
                changeFragment(new EventListFragment(), false);
            } else
            {
                changeFragment(new ConfigFragment(), false);
            }
        }
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_teams)
        {
            changeFragment(TeamListFragment.newInstance(), false);
        }
        else if(id == R.id.nav_checklist)
        {
            changeFragment(ChecklistFragment.newInstance("", ""), false);
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

    public void dropActionBar()
    {
        appBarLayout.setElevation(0);
    }

    public void elevateActionBar()
    {
        appBarLayout.setElevation(ACTION_BAR_ELEVATION);
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

    /**
     * Downloads all app data from the server
     * @param downloadMedia whether or not the app should download robot media from server
     */
    public void downloadApplicationData(final boolean downloadMedia)
    {
        //update all app data
        if(isOnline())
        {
            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.CustomProgressDialog);
            progressDialog.setTitle("Downloading data...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.show();

            updateThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {

                    progressDialogProgess = 10;
                    
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
                        getDatabase().clearUsers();
                        for (User user : getUsers.getUsers())
                        {
                            user.save(getDatabase());
                        }
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


                    //update events
                    Server.GetEvents getEvents = new Server.GetEvents(context);
                    if (getEvents.execute())
                    {
                        getDatabase().clearEvents();
                        for (Event event : getEvents.getEvents())
                            event.save(getDatabase());
                    }

                    //clear the saved data
                    getDatabase().clearEventTeamList();
                    getDatabase().clearTeams();
                    getDatabase().clearScoutCards(false);
                    getDatabase().clearPitCards(false);
                    getDatabase().clearMatches();
                    getDatabase().clearChecklistItems();
                    getDatabase().clearChecklistItemResults(false);

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

                    final ArrayList<Event> events = getDatabase().getEvents();

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
                        File mediaFolder = new File(Constants.MEDIA_DIRECTORY);
                        if (mediaFolder.isDirectory())
                            for (File child : mediaFolder.listFiles())
                                child.delete();

                        getDatabase().clearRobotMedia(false);
                        Server.GetRobotMedia getRobotMedia;

                        for (final Team team : getDatabase().getTeams())
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
                            progressDialog.dismiss();
                            changeFragment(new EventListFragment(), false);
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
        final ProgressDialog progressDialog = new ProgressDialog(context);

        final ArrayList<Team> teams = getDatabase().getTeams();
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

                for(Event event : getDatabase().getEvents())
                {

                    //upload team specific data
                    for (Team team : teams)
                    {
                        for (ScoutCard scoutCard : getDatabase().getScoutCards(team, event,true))
                        {
                            Server.SubmitScoutCard submitScoutCard = new Server.SubmitScoutCard(context, scoutCard);
                            if (submitScoutCard.execute())
                            {
                                scoutCard.setDraft(false);
                                scoutCard.save(getDatabase());
                            } else
                                success = false;
                        }

                        for (PitCard pitCard : getDatabase().getPitCards(team, event, true))
                        {
                            Server.SubmitPitCard submitScoutCard = new Server.SubmitPitCard(context, pitCard);
                            if (submitScoutCard.execute())
                            {
                                pitCard.setDraft(false);
                                pitCard.save(getDatabase());
                            } else
                                success = false;
                        }

                        for (RobotMedia robotMedia : getDatabase().getRobotMedia(team, true))
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
                    for(ChecklistItem checklistItem : getDatabase().getChecklistItems())
                    {
                        for(ChecklistItemResult checklistItemResult : checklistItem.getResults(getDatabase(), true))
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

    private boolean isOnline()
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

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

        hideKeyboard();

//        elevateActionBar();
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


}
