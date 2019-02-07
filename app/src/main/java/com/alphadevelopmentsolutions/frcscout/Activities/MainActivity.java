package com.alphadevelopmentsolutions.frcscout.Activities;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open the database as soon as the app starts
        database = new Database(this);
        database.open();

        generateFakeData();

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

        (new Team(5885, "Villanova WiredCats", "city", "statte", "country", 2013, "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "test")).save(getDatabase());
        (new Team(610, "Villanova WiredCats", "city", "statte", "country", 2013, "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "test")).save(getDatabase());
        (new Team(1234, "Villanova WiredCats", "city", "statte", "country", 2013, "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "test")).save(getDatabase());
        (new Team(123, "Villanova WiredCats", "city", "statte", "country", 2013, "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "wiredcats5885.ca", "test")).save(getDatabase());
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
}
