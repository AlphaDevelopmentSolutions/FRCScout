package com.alphadevelopmentsolutions.frcscout.Activities;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Fragments.EventFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.EventListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.MatchFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.MatchListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamListFragment;
import com.alphadevelopmentsolutions.frcscout.R;

public class MainActivity extends AppCompatActivity implements
        EventListFragment.OnFragmentInteractionListener,
        MatchListFragment.OnFragmentInteractionListener,
        TeamListFragment.OnFragmentInteractionListener,
        EventFragment.OnFragmentInteractionListener,
        MatchFragment.OnFragmentInteractionListener,
        TeamFragment.OnFragmentInteractionListener
{

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open the database as soon as the app starts
        database = new Database(this);
        openDatabase();

        //Swap to the events fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MainFrame, new EventFragment());
        fragmentTransaction.commit();
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
        return database;
    }

    /**
     * Opens the current database instance
     * @return boolean if the open was successful
     */
    public boolean openDatabase()
    {
        return database.open();
    }

    /**
     * Closes the current database instance
     * @return boolean if the close was successful
     */
    public boolean closeDatabase()
    {
        return database.close();
    }
}
