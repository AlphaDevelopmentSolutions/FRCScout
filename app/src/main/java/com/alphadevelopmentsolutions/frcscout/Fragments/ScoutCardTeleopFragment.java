package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoutCardTeleopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoutCardTeleopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutCardTeleopFragment extends MasterFragment {

    private OnFragmentInteractionListener mListener;

    public ScoutCardTeleopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param scoutCard
     * @return A new instance of fragment ScoutCardTeleopFragment.
     */
    public static ScoutCardTeleopFragment newInstance(@Nullable ScoutCard scoutCard) {
        ScoutCardTeleopFragment fragment = new ScoutCardTeleopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_SCOUT_CARD_JSON, toJson(scoutCard));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Hatch Panels
    private TextView teleopHatchPanelsPickupTextView;
    private TextView teleopHatchPanelsSecuredAttemptsTextView;
    private TextView teleopHatchPanelsSecuredTextView;

    private Button teleopHatchPanelsPickupMinusOneButton;
    private Button teleopHatchPanelsPickupPlusOneButton;

    private Button teleopHatchPanelsSecuredMinusOneButton;
    private Button teleopHatchPanelsSecuredPlusOneButton;

    private Button teleopHatchPanelsSecuredAttemptsMinusOneButton;
    private Button teleopHatchPanelsSecuredAttemptsPlusOneButton;

    //Cargo
    private TextView teleopCargoPickupTextView;
    private TextView teleopCargoStoredAttemptsTextView;
    private TextView teleopCargoStoredTextView;


    private Button teleopCargoPickupMinusOneButton;
    private Button teleopCargoPickupPlusOneButton;

    private Button teleopCargoStoredAttemptsMinusOneButton;
    private Button teleopCargoStoredAttemptsPlusOneButton;

    private Button teleopCargoStoredMinusOneButton;
    private Button teleopCargoStoredPlusOneButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card_teleop, container, false);

        joinLoadingThread();

        //Hatch Panels
        teleopHatchPanelsPickupTextView = view.findViewById(R.id.TeleopHatchPanelsPickupTextView);
        teleopHatchPanelsSecuredAttemptsTextView = view.findViewById(R.id.TeleopHatchPanelsSecuredAttemptsTextView);
        teleopHatchPanelsSecuredTextView = view.findViewById(R.id.TeleopHatchPanelsSecuredTextView);


        teleopHatchPanelsPickupMinusOneButton = view.findViewById(R.id.TeleopHatchPanelsPickupMinusOneButton);
        teleopHatchPanelsPickupPlusOneButton = view.findViewById(R.id.TeleopHatchPanelsPickupPlusOneButton);

        teleopHatchPanelsSecuredAttemptsMinusOneButton = view.findViewById(R.id.TeleopHatchPanelsSecuredAttemptsMinusOneButton);
        teleopHatchPanelsSecuredAttemptsPlusOneButton = view.findViewById(R.id.TeleopHatchPanelsSecuredAttemptsPlusOneButton);

        teleopHatchPanelsSecuredMinusOneButton = view.findViewById(R.id.TeleopHatchPanelsSecuredMinusOneButton);
        teleopHatchPanelsSecuredPlusOneButton = view.findViewById(R.id.TeleopHatchPanelsSecuredPlusOneButton);

        teleopHatchPanelsPickupMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopHatchPanelsPickupTextView.setText((Integer.parseInt(teleopHatchPanelsPickupTextView.getText().toString()) - 1) + "");
            }
        });

        teleopHatchPanelsPickupPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopHatchPanelsPickupTextView.setText((Integer.parseInt(teleopHatchPanelsPickupTextView.getText().toString()) + 1) + "");
            }
        });

        teleopHatchPanelsSecuredAttemptsMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopHatchPanelsSecuredAttemptsTextView.setText((Integer.parseInt(teleopHatchPanelsSecuredAttemptsTextView.getText().toString()) - 1) + "");
            }
        });

        teleopHatchPanelsSecuredAttemptsPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopHatchPanelsSecuredAttemptsTextView.setText((Integer.parseInt(teleopHatchPanelsSecuredAttemptsTextView.getText().toString()) + 1) + "");
            }
        });

        teleopHatchPanelsSecuredMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopHatchPanelsSecuredTextView.setText((Integer.parseInt(teleopHatchPanelsSecuredTextView.getText().toString()) - 1) + "");
            }
        });

        teleopHatchPanelsSecuredPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopHatchPanelsSecuredTextView.setText((Integer.parseInt(teleopHatchPanelsSecuredTextView.getText().toString()) + 1) + "");
            }
        });


        //Cargo
        teleopCargoPickupTextView = view.findViewById(R.id.TeleopCargoPickupTextView);
        teleopCargoStoredAttemptsTextView = view.findViewById(R.id.TeleopCargoStoredAttemptsTextView);
        teleopCargoStoredTextView = view.findViewById(R.id.TeleopCargoStoredTextView);


        teleopCargoPickupMinusOneButton = view.findViewById(R.id.TeleopCargoPickupMinusOneButton);
        teleopCargoPickupPlusOneButton = view.findViewById(R.id.TeleopCargoPickupPlusOneButton);

        teleopCargoStoredAttemptsPlusOneButton = view.findViewById(R.id.TeleopCargoStoredAttemptsPlusOneButton);
        teleopCargoStoredAttemptsMinusOneButton = view.findViewById(R.id.TeleopCargoStoredAttemptsMinusOneButton);

        teleopCargoStoredMinusOneButton = view.findViewById(R.id.TeleopCargoStoredMinusOneButton);
        teleopCargoStoredPlusOneButton = view.findViewById(R.id.TeleopCargoStoredPlusOneButton);

        teleopCargoPickupMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopCargoPickupTextView.setText((Integer.parseInt(teleopCargoPickupTextView.getText().toString()) - 1) + "");
            }
        });

        teleopCargoPickupPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopCargoPickupTextView.setText((Integer.parseInt(teleopCargoPickupTextView.getText().toString()) + 1) + "");
            }
        });

        teleopCargoStoredAttemptsMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopCargoStoredAttemptsTextView.setText((Integer.parseInt(teleopCargoStoredAttemptsTextView.getText().toString()) - 1) + "");
            }
        });

        teleopCargoStoredAttemptsPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopCargoStoredAttemptsTextView.setText((Integer.parseInt(teleopCargoStoredAttemptsTextView.getText().toString()) + 1) + "");
            }
        });

        teleopCargoStoredMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopCargoStoredTextView.setText((Integer.parseInt(teleopCargoStoredTextView.getText().toString()) - 1) + "");
            }
        });

        teleopCargoStoredPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(scoutCard == null || scoutCard.isDraft())
                    teleopCargoStoredTextView.setText((Integer.parseInt(teleopCargoStoredTextView.getText().toString()) + 1) + "");
            }
        });

        //scoutcard loaded, populate fields
        if(scoutCard != null)
        {
            teleopHatchPanelsPickupTextView.setText(String.valueOf(scoutCard.getTeleopHatchPanelsPickedUp()));
            teleopHatchPanelsSecuredAttemptsTextView.setText(String.valueOf(scoutCard.getTeleopHatchPanelsSecuredAttempts()));
            teleopHatchPanelsSecuredTextView.setText(String.valueOf(scoutCard.getTeleopHatchPanelsSecured()));

            teleopCargoPickupTextView.setText(String.valueOf(scoutCard.getTeleopCargoPickedUp()));
            teleopCargoStoredAttemptsTextView.setText(String.valueOf(scoutCard.getTeleopCargoStoredAttempts()));
            teleopCargoStoredTextView.setText(String.valueOf(scoutCard.getTeleopCargoStored()));
        }

        return view;
    }

    //region Getters

    public int getTeleopHatchPanelsSecured()
    {
        return Integer.parseInt(teleopHatchPanelsSecuredTextView.getText().toString());
    }

    public int getTeleopHatchPanelsDropped()
    {
        return Integer.parseInt(teleopHatchPanelsSecuredAttemptsTextView.getText().toString());
    }

    public int getTeleopHatchPanelsPickedUp()
    {
        return Integer.parseInt(teleopHatchPanelsPickupTextView.getText().toString());
    }

    public int getTeleopCargoStored()
    {
        return Integer.parseInt(teleopCargoStoredTextView.getText().toString());
    }

    public int getTeleopCargoDropped()
    {
        return Integer.parseInt(teleopCargoStoredAttemptsTextView.getText().toString());
    }

    public int getTeleopCargoPickedUp()
    {
        return Integer.parseInt(teleopCargoPickupTextView.getText().toString());
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
