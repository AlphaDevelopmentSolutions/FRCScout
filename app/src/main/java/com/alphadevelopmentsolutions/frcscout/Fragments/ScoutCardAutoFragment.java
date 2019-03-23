package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoutCardAutoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoutCardAutoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutCardAutoFragment extends MasterFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String scoutCardJson;

    private OnFragmentInteractionListener mListener;

    public ScoutCardAutoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param scoutCardJson scout card json
     * @return A new instance of fragment ScoutCardAutoFragment.
     */
    public static ScoutCardAutoFragment newInstance(String scoutCardJson) {
        ScoutCardAutoFragment fragment = new ScoutCardAutoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, scoutCardJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scoutCardJson = getArguments().getString(ARG_PARAM1);
        }

        //load the passed scout card
        if(scoutCardJson != null && !scoutCardJson.equals(""))
            scoutCard = new Gson().fromJson(scoutCardJson, ScoutCard.class);
    }

    private ScoutCard scoutCard;

    //Exit Habitat
    private TextView autonomousExitHabitatTextView;

    private Button autonomousExitHabitatNoButton;
    private Button autonomousExitHabitatYesButton;

    //Hatch Panels
    private TextView autonomousHatchPanelsPickupTextView;
    private TextView autonomousHatchPanelsSecuredAttemptsTextView;
    private TextView autonomousHatchPanelsSecuredTextView;

    private Button autonomousHatchPanelsPickupMinusOneButton;
    private Button autonomousHatchPanelsPickupPlusOneButton;

    private Button autonomousHatchPanelsSecuredMinusOneButton;
    private Button autonomousHatchPanelsSecuredPlusOneButton;

    private Button autonomousHatchPanelsSecuredAttemptsMinusOneButton;
    private Button autonomousHatchPanelsSecuredAttemptsPlusOneButton;

    //Cargo
    private TextView autonomousCargoPickupTextView;
    private TextView autonomousCargoStoredAttemptsTextView;
    private TextView autonomousCargoStoredTextView;


    private Button autonomousCargoPickupMinusOneButton;
    private Button autonomousCargoPickupPlusOneButton;

    private Button autonomousCargoStoredAttemptsMinusOneButton;
    private Button autonomousCargoStoredAttemptsPlusOneButton;

    private Button autonomousCargoStoredMinusOneButton;
    private Button autonomousCargoStoredPlusOneButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card_auto, container, false);

        //Exit Habitat
        autonomousExitHabitatTextView = view.findViewById(R.id.AutonomousExitHabitatTextView);

        autonomousExitHabitatNoButton = view.findViewById(R.id.AutonomousExitHabitatNoButton);
        autonomousExitHabitatYesButton = view.findViewById(R.id.AutonomousExitHabitatYesButton);

        autonomousExitHabitatNoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousExitHabitatTextView.setText(R.string.no);
            }
        });

        autonomousExitHabitatYesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousExitHabitatTextView.setText(R.string.yes);
            }
        });


        //Hatch Panels
        autonomousHatchPanelsPickupTextView = view.findViewById(R.id.AutonomousHatchPanelsPickupTextView);
        autonomousHatchPanelsSecuredAttemptsTextView = view.findViewById(R.id.AutonomousHatchPanelsSecuredAttemptsTextView);
        autonomousHatchPanelsSecuredTextView = view.findViewById(R.id.AutonomousHatchPanelsSecuredTextView);


        autonomousHatchPanelsPickupMinusOneButton = view.findViewById(R.id.AutonomousHatchPanelsPickupMinusOneButton);
        autonomousHatchPanelsPickupPlusOneButton = view.findViewById(R.id.AutonomousHatchPanelsPickupPlusOneButton);

        autonomousHatchPanelsSecuredAttemptsMinusOneButton = view.findViewById(R.id.AutonomousHatchPanelsSecuredAttemptsMinusOneButton);
        autonomousHatchPanelsSecuredAttemptsPlusOneButton = view.findViewById(R.id.AutonomousHatchPanelsSecuredAttemptsPlusOneButton);

        autonomousHatchPanelsSecuredMinusOneButton = view.findViewById(R.id.AutonomousHatchPanelsSecuredMinusOneButton);
        autonomousHatchPanelsSecuredPlusOneButton = view.findViewById(R.id.AutonomousHatchPanelsSecuredPlusOneButton);

        autonomousHatchPanelsPickupMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousHatchPanelsPickupTextView.setText((Integer.parseInt(autonomousHatchPanelsPickupTextView.getText().toString()) - 1) + "");
            }
        });

        autonomousHatchPanelsPickupPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousHatchPanelsPickupTextView.setText((Integer.parseInt(autonomousHatchPanelsPickupTextView.getText().toString()) + 1) + "");
            }
        });

        autonomousHatchPanelsSecuredAttemptsMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousHatchPanelsSecuredAttemptsTextView.setText((Integer.parseInt(autonomousHatchPanelsSecuredAttemptsTextView.getText().toString()) - 1) + "");
            }
        });

        autonomousHatchPanelsSecuredAttemptsPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousHatchPanelsSecuredAttemptsTextView.setText((Integer.parseInt(autonomousHatchPanelsSecuredAttemptsTextView.getText().toString()) + 1) + "");
            }
        });

        autonomousHatchPanelsSecuredMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousHatchPanelsSecuredTextView.setText((Integer.parseInt(autonomousHatchPanelsSecuredTextView.getText().toString()) - 1) + "");
            }
        });

        autonomousHatchPanelsSecuredPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousHatchPanelsSecuredTextView.setText((Integer.parseInt(autonomousHatchPanelsSecuredTextView.getText().toString()) + 1) + "");
            }
        });


        //Cargo
        autonomousCargoPickupTextView = view.findViewById(R.id.AutonomousCargoPickupTextView);
        autonomousCargoStoredAttemptsTextView = view.findViewById(R.id.AutonomousCargoStoredAttemptsTextView);
        autonomousCargoStoredTextView = view.findViewById(R.id.AutonomousCargoStoredTextView);


        autonomousCargoPickupMinusOneButton = view.findViewById(R.id.AutonomousCargoPickupMinusOneButton);
        autonomousCargoPickupPlusOneButton = view.findViewById(R.id.AutonomousCargoPickupPlusOneButton);

        autonomousCargoStoredAttemptsPlusOneButton = view.findViewById(R.id.AutonomousCargoStoredAttemptsPlusOneButton);
        autonomousCargoStoredAttemptsMinusOneButton = view.findViewById(R.id.AutonomousCargoStoredAttemptsMinusOneButton);

        autonomousCargoStoredMinusOneButton = view.findViewById(R.id.AutonomousCargoStoredMinusOneButton);
        autonomousCargoStoredPlusOneButton = view.findViewById(R.id.AutonomousCargoStoredPlusOneButton);

        autonomousCargoPickupMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousCargoPickupTextView.setText((Integer.parseInt(autonomousCargoPickupTextView.getText().toString()) - 1) + "");
            }
        });

        autonomousCargoPickupPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousCargoPickupTextView.setText((Integer.parseInt(autonomousCargoPickupTextView.getText().toString()) + 1) + "");
            }
        });

        autonomousCargoStoredAttemptsMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousCargoStoredAttemptsTextView.setText((Integer.parseInt(autonomousCargoStoredAttemptsTextView.getText().toString()) - 1) + "");
            }
        });

        autonomousCargoStoredAttemptsPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousCargoStoredAttemptsTextView.setText((Integer.parseInt(autonomousCargoStoredAttemptsTextView.getText().toString()) + 1) + "");
            }
        });

        autonomousCargoStoredMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousCargoStoredTextView.setText((Integer.parseInt(autonomousCargoStoredTextView.getText().toString()) - 1) + "");
            }
        });

        autonomousCargoStoredPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousCargoStoredTextView.setText((Integer.parseInt(autonomousCargoStoredTextView.getText().toString()) + 1) + "");
            }
        });

        return view;
    }

    //region Getters

    public boolean getAutonomousExitHab()
    {
        return autonomousExitHabitatTextView.getText().toString().equals(context.getResources().getString(R.string.yes));
    }

    public int getAutonomousHatchPanelsSecured()
    {
        return Integer.parseInt(autonomousHatchPanelsSecuredTextView.getText().toString());
    }

    public int getAutonomousHatchPanelsDropped()
    {
        return Integer.parseInt(autonomousHatchPanelsSecuredAttemptsTextView.getText().toString());
    }

    public int getAutonomousHatchPanelsPickedUp()
    {
        return Integer.parseInt(autonomousHatchPanelsPickupTextView.getText().toString());
    }

    public int getAutonomousCargoStored()
    {
        return Integer.parseInt(autonomousCargoStoredTextView.getText().toString());
    }

    public int getAutonomousCargoDropped()
    {
        return Integer.parseInt(autonomousCargoStoredAttemptsTextView.getText().toString());
    }

    public int getAutonomousCargoPickedUp()
    {
        return Integer.parseInt(autonomousCargoPickupTextView.getText().toString());
    }

    //endregion

    /**
     * Validates all fields on the form for saving
     * @return boolean if fields valid
     */
    public boolean validateFields()
    {
        //placeholder in case of future validation needed
        return true;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
