package com.alphadevelopmentsolutions.frcscout.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.alphadevelopmentsolutions.frcscout.Api.ScoutingWiredcats;
import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
import com.alphadevelopmentsolutions.frcscout.Fragments.ChangeEventFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.EventFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.LoginFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.MatchFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.MatchListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamListFragment;
import com.alphadevelopmentsolutions.frcscout.Interfaces.ApiParams;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        MatchListFragment.OnFragmentInteractionListener,
        TeamListFragment.OnFragmentInteractionListener,
        EventFragment.OnFragmentInteractionListener,
        MatchFragment.OnFragmentInteractionListener,
        TeamFragment.OnFragmentInteractionListener,
        ScoutCardFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        ChangeEventFragment.OnFragmentInteractionListener
{

    private Database database;

    private FrameLayout mainFrame;

    private MainActivity context;

    private Thread updateThread;

    private SearchView searchView;

    private Menu menu;

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(ApiParams.EVENT_ID, "2019onosh").apply();

        if(getDatabase().getTeams().size() == 0 && isOnline())
            updateApplicationData(PreferenceManager.getDefaultSharedPreferences(this).getString(ApiParams.EVENT_ID, ""));

        if(updateThread != null)
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
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
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
                                            for (Team team : teams)
                                            {
                                                for (ScoutCard scoutCard : getDatabase().getScoutCards(team))
                                                {
                                                    ScoutingWiredcats.SubmitScoutCard submitScoutCard = new ScoutingWiredcats.SubmitScoutCard(context, scoutCard);
                                                    if(submitScoutCard.execute())
                                                    {
                                                        scoutCard.delete(getDatabase());
                                                    }
                                                }

                                            }

                                            runOnUiThread(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    progressDialog.hide();
                                                    context.recreate();
                                                }
                                            });
                                        }
                                    });

                                    uploadThread.start();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
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
                updateApplicationData(PreferenceManager.getDefaultSharedPreferences(this).getString(ApiParams.EVENT_ID, ""));
                return true;

            case R.id.ChangeEventItem:
                changeFragment(new ChangeEventFragment(), false);
                return true;
        }

        return false;
    }

    public void updateApplicationData(final String event)
    {
        if(isOnline())
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Downloading data...");
            progressDialog.show();

//            getDatabase().clear();
            updateThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    //update teams
                    ScoutingWiredcats.GetTeamsAtEvent getTeamsAtEvent = new ScoutingWiredcats.GetTeamsAtEvent(context, event);

                    //get teams at current event
                    if (getTeamsAtEvent.execute())
                    {
                        ArrayList<Team> databaseTeams = getDatabase().getTeams();
                        ArrayList<Integer> databaseTeamIds = new ArrayList<>();
                        ArrayList<Team> teams = getTeamsAtEvent.getTeams();
                        ArrayList<Integer> teamIds = new ArrayList<>();

                        ArrayList<Integer> removeTeamIds = new ArrayList<>();

                        for(Team team : databaseTeams)
                        {
                            databaseTeamIds.add(team.getId());
                        }

                        for(Team team : teams)
                        {
                            teamIds.add(team.getId());
                        }

                        for(int teamId : databaseTeamIds)
                        {
                            if(!teamIds.contains(teamId))
                                removeTeamIds.add(teamId);
                        }

                        for(int teamId : removeTeamIds)
                        {
                            Team team = new Team(teamId);
                            if(team.load(getDatabase()))
                                team.delete(getDatabase());
                        }

                        for (Team team : teams)
                            team.save(getDatabase());

                    }

                    //update users
                    ScoutingWiredcats.GetUsers getUsers = new ScoutingWiredcats.GetUsers(context);

                    if (getUsers.execute())
                    {
                        for (User user : getUsers.getUsers())
                        {
                            user.save(getDatabase());
                        }
                    }

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            progressDialog.cancel();
                            context.recreate();
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
        if(!database.isOpen()) database.open();
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

        elevateActionBar();

    }
}
