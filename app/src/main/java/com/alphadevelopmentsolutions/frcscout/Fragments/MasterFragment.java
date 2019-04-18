package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.R;

public class MasterFragment extends Fragment
{
    //store the context and database in the master fragment which all other fragmets extend from
    protected MainActivity context;
    protected Database database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = (MainActivity) getActivity();
        database = context.getDatabase();
    }

    @Override
    public void onDetach()
    {
        //reset the title of the app upon detach
        context.getSupportActionBar().setTitle(R.string.app_name);
        super.onDetach();
    }
}
