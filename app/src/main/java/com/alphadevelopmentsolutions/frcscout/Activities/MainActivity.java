package com.alphadevelopmentsolutions.frcscout.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.alphadevelopmentsolutions.frcscout.Api.Server;
import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.EventTeamList;
import com.alphadevelopmentsolutions.frcscout.Classes.PitCard;
import com.alphadevelopmentsolutions.frcscout.Classes.RobotMedia;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
import com.alphadevelopmentsolutions.frcscout.Fragments.EventFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.EventListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.LoginFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.MatchFragment;
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
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamListFragment;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MatchListFragment.OnFragmentInteractionListener,
        TeamListFragment.OnFragmentInteractionListener,
        EventFragment.OnFragmentInteractionListener,
        MatchFragment.OnFragmentInteractionListener,
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
        EventListFragment.OnFragmentInteractionListener
{

    private Database database;

    private FrameLayout mainFrame;

    private MainActivity context;

    private Thread updateThread;

    private final int ACTION_BAR_ELEVATION = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open the database as soon as the app starts
        database = new Database(this);
        database.open();

        mainFrame = findViewById(R.id.MainFrame);

        context = this;

        //we need write permission before anything
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5885);
            }
            else
            {

                if (savedInstanceState == null)
                {
                    if (getDatabase().getTeams().size() == 0 && isOnline())
                        updateApplicationData(false);

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

                    changeFragment(new EventListFragment(), false);
                }
            }
        }
        else
        {

            if (savedInstanceState == null)
            {
                if (getDatabase().getTeams().size() == 0 && isOnline())
                    updateApplicationData(false);

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

                changeFragment(new TeamListFragment(), false);
            }
        }

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
        getSupportActionBar().setElevation(0);
    }

    public void elevateActionBar()
    {
        getSupportActionBar().setElevation(ACTION_BAR_ELEVATION);
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
                                updateApplicationData(true);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                updateApplicationData(false);

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
        }

        return false;
    }

    public void updateApplicationData(final boolean downloadMedia)
    {
        //update all app data
        if(isOnline())
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Downloading data...");
            progressDialog.show();

            updateThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {

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



                    for (Event event : getDatabase().getEvents())
                    {

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

                        //update scout cards
                        Server.GetScoutCards getScoutCards = new Server.GetScoutCards(context, event);

                        if (getScoutCards.execute())
                        {
                            for (ScoutCard scoutCard : getScoutCards.getScoutCards())
                                scoutCard.save(getDatabase());
                        }

                        //update pit cards
                        Server.GetPitCards getPitCards = new Server.GetPitCards(context, event);

                        if (getPitCards.execute())
                        {
                            for (PitCard pitCard : getPitCards.getPitCards())
                                pitCard.save(getDatabase());
                        }
                    }

                    //download robot media from server
                    if (downloadMedia)
                    {

                        //Get the folder and purge all files
                        File mediaFolder = new File(Constants.MEDIA_DIRECTORY);
                        if (mediaFolder.isDirectory())
                            for (File child : mediaFolder.listFiles())
                                child.delete();

                        getDatabase().clearRobotMedia(false);
                        Server.GetRobotMedia getRobotMedia;

                        for (Team team : getDatabase().getTeams())
                        {
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
                            progressDialog.cancel();
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

    public void showSnackbar(String message)
    {
        (Snackbar.make(mainFrame, message, Snackbar.LENGTH_SHORT)).show();
    }

    public void changeFragment(Fragment fragment, boolean addToBackstack)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.MainFrame, fragment);
        if(addToBackstack) fragmentTransaction.addToBackStack(null); //add to the backstack
        else getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //pop all backstacks
        fragmentTransaction.commit();

//        elevateActionBar();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }
}
