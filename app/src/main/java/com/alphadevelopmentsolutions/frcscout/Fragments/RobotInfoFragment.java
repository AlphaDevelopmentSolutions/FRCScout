package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotInfo;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotInfoKey;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Year;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RobotInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RobotInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotInfoFragment extends MasterFragment
{
    private OnFragmentInteractionListener mListener;

    public RobotInfoFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param team
     * @return A new instance of fragment RobotInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RobotInfoFragment newInstance(@NonNull Team team)
    {
        RobotInfoFragment fragment = new RobotInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM_JSON, toJson(team));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }





    private LinearLayout robotInfoLinearLayout;
    private Button saveButton;

    private ArrayList<EditText> editTexts;
    private ArrayList<Integer> infoIds;
    private ArrayList<String> infoKeys;
    private ArrayList<String> infoStates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_robot_info, container, false);

        //gets rid of the shadow on the actionbar
        context.dropActionBar();



        robotInfoLinearLayout = view.findViewById(R.id.RobotInfoLinearLayout);
        saveButton = view.findViewById(R.id.SaveButton);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                editTexts = new ArrayList<>();
                infoIds = new ArrayList<>();
                infoKeys = new ArrayList<>();
                infoStates = new ArrayList<>();

                Year year = new Year(event.getYearId());
                year.load(database);

                ArrayList<RobotInfoKey> robotInfoKeys = RobotInfoKey.getRobotInfoKeys(year, null, database);

                RobotInfoKey robotInfoKey;
                RobotInfoKey nextRobotInfoKey;

                ArrayList<RobotInfo> robotInfos;
                RobotInfo robotInfo;

                LinearLayout linearLayout = new LinearLayout(context);

                String currentInfoKeyState = "";

                float scale = getResources().getDisplayMetrics().density;

                int j = 1;

                for(int i = 0; i < robotInfoKeys.size(); i++)
                {
                    robotInfoKey = robotInfoKeys.get(i);
                    nextRobotInfoKey = (( i + 1 < robotInfoKeys.size()) ?  robotInfoKeys.get(i + 1) : null);

                    robotInfos = RobotInfo.getRobotInfo(null, event, team, robotInfoKey, null, false, database);
                    robotInfo = (robotInfos.size() > 0) ? robotInfos.get(robotInfos.size() - 1) : null;

                    if(!currentInfoKeyState.equals(robotInfoKey.getKeyState()))
                    {
                        if(i > 0)
                        {
                            int padding = (int) (1*scale + 0.5f);
                            final LinearLayout divider = new LinearLayout(context);
                            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, padding));
                            divider.setBackgroundColor(getResources().getColor(R.color.divider));

                            context.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    robotInfoLinearLayout.addView(divider);
                                }
                            });
                        }

                        final TextView textView = new TextView(context);
                        textView.setText(robotInfoKey.getKeyState());
                        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

                        int padding = (int) (8*scale + 0.5f);
                        textView.setPadding(padding, padding, 0, 0);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                        context.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                robotInfoLinearLayout.addView(textView);
                            }
                        });

                        currentInfoKeyState = robotInfoKey.getKeyState();

                    }

                    TextInputLayout textInputLayout = new TextInputLayout(context);

                    EditText editText = new EditText(context);
                    editText.setHint(robotInfoKey.getKeyName());
                    if(robotInfo != null)
                    {
                        infoIds.add(robotInfo.getId());
                        editText.setText(robotInfo.getPropertyValue());
                    }
                    else
                        infoIds.add(-1);


                    textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

                    textInputLayout.addView(editText);

                    editTexts.add(editText);
                    infoKeys.add(robotInfoKey.getKeyName());
                    infoStates.add(robotInfoKey.getKeyState());

                    linearLayout.addView(textInputLayout);

                    if((j % 2 == 0 && currentInfoKeyState.equals(robotInfoKey.getKeyState())) || (nextRobotInfoKey == null || !currentInfoKeyState.equals(nextRobotInfoKey.getKeyState())))
                    {
                        j = 0;
                        int padding = (int) (8*scale + 0.5f);
                        linearLayout.setPadding(padding, padding, padding, padding);

                        final LinearLayout finalLinearLayout = linearLayout;
                        context.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                robotInfoLinearLayout.addView(finalLinearLayout);
                            }
                        });

                        linearLayout = new LinearLayout(context);
                    }

                    j++;
                }

                saveButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        for(int i = 0; i < editTexts.size(); i++)
                        {
                            RobotInfo robotInfo = new RobotInfo(
                                    -1,
                                    event.getYearId(),
                                    event.getBlueAllianceId(),
                                    team.getId(),
                                    infoStates.get(i),
                                    infoKeys.get(i),
                                    editTexts.get(i).getText().toString(),
                                    true);

                            robotInfo.save(database);
                        }

                        context.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                context.showSnackbar("Robot Info Saved!");
                            }
                        });
                    }
                });

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
