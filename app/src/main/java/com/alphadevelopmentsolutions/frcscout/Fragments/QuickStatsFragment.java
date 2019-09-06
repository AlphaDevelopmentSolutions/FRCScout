package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Interfaces.StatsKeys;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuickStatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuickStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuickStatsFragment extends MasterFragment
{
    private OnFragmentInteractionListener mListener;

    public QuickStatsFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param team
     * @return A new instance of fragment QuickStatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuickStatsFragment newInstance(@NonNull Team team)
    {
        QuickStatsFragment fragment = new QuickStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM_JSON, toJson(team));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        loadStatsThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                joinLoadingThread();

                //get the stats from the team object
                HashMap<String, HashMap<String, Double>> allStats = team.getStats(database, event);
                minTableRows = new ArrayList<>();
                avgTableRows = new ArrayList<>();
                maxTableRows = new ArrayList<>();

                //generate padding based on screen size
                int padding = context.getResources().getDimensionPixelSize(R.dimen.material_padding);

                //iterate through each category in the stats interface
                for(final String CATEGORY_KEY : StatsKeys.STATS_CATEGORY_ORDER_KEYS)
                {
                    //get the stats for each category
                    HashMap<String, Double> stats = allStats.get(CATEGORY_KEY);

                    //iterate through each stat in the stats interface
                    for(String KEY : StatsKeys.STATS_ORDER_KEYS)
                    {
                        //get the stat
                        double stat = stats.get(KEY);

                        //generate the textview
                        TextView textView = new TextView(context);
                        textView.setText(String.valueOf(((!CATEGORY_KEY.equals(StatsKeys.AVG)) ? (int) Math.ceil(stat) : Math.round(stat * 100.00) / 100.00)));
                        textView.setPadding(padding, padding, padding, padding);

                        //create the tablerow to add to the table layout
                        final TableRow tableRow = new TableRow(context);

                        //color logic
                        if(KEY.equals(StatsKeys.AUTO_EXIT_HAB))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                        }

                        else if(KEY.equals(StatsKeys.AUTO_HATCH))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                        }

                        else if(KEY.equals(StatsKeys.AUTO_HATCH_DROPPED))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                        }

                        else if(KEY.equals(StatsKeys.AUTO_CARGO))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                        }

                        else if(KEY.equals(StatsKeys.AUTO_CARGO_DROPPED))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                        }

                        else if(KEY.equals(StatsKeys.TELEOP_HATCH))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                        }

                        else if(KEY.equals(StatsKeys.TELEOP_HATCH_DROPPED))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                        }

                        else if(KEY.equals(StatsKeys.TELEOP_CARGO))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                        }

                        else if(KEY.equals(StatsKeys.TELEOP_CARGO_DROPPED))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                        }

                        else if(KEY.equals(StatsKeys.RETURNED_HAB))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                        }

                        else if(KEY.equals(StatsKeys.RETURNED_HAB_FAILS))
                        {
                            if(stat > 0)
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                        }

                        else if(KEY.equals(StatsKeys.DEFENSE_RATING))
                        {
                            if(stat >= 0 && stat <= 1.6)
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                            else if (stat <= 3.2)
                                tableRow.setBackgroundColor(context.getColor(R.color.warning));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                        }

                        else if(KEY.equals(StatsKeys.OFFENSE_RATING))
                        {
                            if(stat >= 0 && stat <= 1.6)
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                            else if (stat <= 3.2)
                                tableRow.setBackgroundColor(context.getColor(R.color.warning));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                        }

                        else if(KEY.equals(StatsKeys.DRIVE_RATING))
                        {
                            if(stat >= 0 && stat <= 1.6)
                                tableRow.setBackgroundColor(context.getColor(R.color.bad));
                            else if (stat <= 3.2)
                                tableRow.setBackgroundColor(context.getColor(R.color.warning));
                            else
                                tableRow.setBackgroundColor(context.getColor(R.color.good));
                        }

                        //set the gravity to center the text and add it
                        tableRow.setGravity(Gravity.CENTER);
                        tableRow.addView(textView);

                        //add each tablerow to the array
                        if(CATEGORY_KEY.equals(StatsKeys.MIN))
                            minTableRows.add(tableRow);
                        else if(CATEGORY_KEY.equals(StatsKeys.AVG))
                            avgTableRows.add(tableRow);
                        else
                            maxTableRows.add(tableRow);
                    }

                }

            }

        });

        loadStatsThread.start();
    }

    private TableLayout statsMinTableLayout;
    private TableLayout statsMaxTableLayout;
    private TableLayout statsAvgTableLayout;

    private Thread loadStatsThread;

    private ArrayList<TableRow> minTableRows;
    private ArrayList<TableRow> avgTableRows;
    private ArrayList<TableRow> maxTableRows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quick_stats, container, false);

        statsMinTableLayout = view.findViewById(R.id.StatsMinTableLayout);
        statsMaxTableLayout = view.findViewById(R.id.StatsMaxTableLayout);
        statsAvgTableLayout = view.findViewById(R.id.StatsAvgTableLayout);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    //make sure the load stats thread has finish before attempting
                    loadStatsThread.join();

                    //check if the table rows have a parent, if they do remove every view
                    //this includes table rows from min, max and avg
                    //mainly has to be done when the view is created on a rotate or backstack pop
                    if(minTableRows.get(0).getParent() != null)
                        ((ViewGroup) minTableRows.get(0).getParent()).removeAllViews();

                    if(avgTableRows.get(0).getParent() != null)
                        ((ViewGroup) avgTableRows.get(0).getParent()).removeAllViews();

                    if(maxTableRows.get(0).getParent() != null)
                        ((ViewGroup) maxTableRows.get(0).getParent()).removeAllViews();

                    //iterate through each of the table rows
                    for(int i = 0; i < minTableRows.size(); i++)
                    {

                        //add each category row
                        final int finalI = i;
                        context.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //add each view to the respective tablelayout
                                statsMinTableLayout.addView(minTableRows.get(finalI));
                                statsAvgTableLayout.addView(avgTableRows.get(finalI));
                                statsMaxTableLayout.addView(maxTableRows.get(finalI));

                            }
                        });
                    }
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }
        }).start();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
