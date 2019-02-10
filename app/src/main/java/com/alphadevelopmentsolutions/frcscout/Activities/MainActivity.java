package com.alphadevelopmentsolutions.frcscout.Activities;

import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.alphadevelopmentsolutions.frcscout.Api.TheBlueAllianceApi;
import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
import com.alphadevelopmentsolutions.frcscout.Fragments.EventFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.LoginFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.MatchFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.MatchListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamListFragment;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        MatchListFragment.OnFragmentInteractionListener,
        TeamListFragment.OnFragmentInteractionListener,
        EventFragment.OnFragmentInteractionListener,
        MatchFragment.OnFragmentInteractionListener,
        TeamFragment.OnFragmentInteractionListener,
        ScoutCardFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener
{

    private Database database;

    private FrameLayout mainFrame;

    private MainActivity context;

    private Thread updateThread;

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

//        generateFakeData();
//        updateApplicationData("2019onosh");

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

        //Swap to the events fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MainFrame, new TeamListFragment());
        fragmentTransaction.commit();
    }

    private void generateFakeData()
    {
        getDatabase().clear();

        (new User(-1, "Griffin", "Sorrentino")).save(getDatabase());
        (new User(-1, "Bob", "Hedrick")).save(getDatabase());
        (new User(-1, "Alex", "ABRUZESZEZEZEEZEZE")).save(getDatabase());
        (new User(-1, "Stacey", "Greenwood")).save(getDatabase());


        (new ScoutCard(-1, 2, 5885, "Griffin Sorrentino", 1, 2, true, 4, 6, 7, 8, 9, "Level 3", "Test Notes", new Date(System.currentTimeMillis()))).save(getDatabase());
        (new ScoutCard(-1, 3, 5885, "Stacey Sorrentino", 1, 2, false, 4, 6, 7, 8, 9, "Level 3", "Test Notes", new Date(System.currentTimeMillis()))).save(getDatabase());
        (new ScoutCard(-1, 4, 5885, "Bob Sorrentino", 1, 2, true, 4, 6, 7, 8, 9, "Level 3", "Test Notes", new Date(System.currentTimeMillis()))).save(getDatabase());
    }

    private void updateApplicationData(final String event)
    {
        updateThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                TheBlueAllianceApi.GetTeamsAtEvent getTeamsAtEvent = new TheBlueAllianceApi.GetTeamsAtEvent(context, event);

                //get teams at current event
                if(getTeamsAtEvent.execute())
                {
                    for(Team team : getTeamsAtEvent.getTeams())
                    {
                        team.save(getDatabase());
                    }
                }


            }
        });

        updateThread.start();
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
}
