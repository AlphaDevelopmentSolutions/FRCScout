package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alphadevelopmentsolutions.frcscout.Api.Server;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends MasterFragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ConfigFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2)
    {
        ConfigFragment fragment = new ConfigFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private EditText webUrlEditText;

    private Button connectButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        //hide the actionbar while connecting
        context.getSupportActionBar().hide();

        webUrlEditText = view.findViewById(R.id.WebUrlEditText);
        connectButton = view.findViewById(R.id.ConnectButton);

        connectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String url = webUrlEditText.getText().toString();

                if(!url.contains("http"))
                    url = "http://" + url;

                context.setPreference(Constants.SharedPrefKeys.WEB_URL_KEY, url);
                context.setPreference(Constants.SharedPrefKeys.API_URL_KEY, url + "/api/api.php");
                context.setPreference(Constants.SharedPrefKeys.API_KEY_KEY, "TEMP");

                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //validate connection to server
                        Server.Hello hello = new Server.Hello(context);

                        //valid connection
                        if(hello.execute())
                        {
                            //gather server configs
                            Server.GetServerConfig getServerConfig = new Server.GetServerConfig(context);

                            //valid config
                            if(getServerConfig.execute())
                            {
                                context.runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        context.updateNavText();
                                        context.updateApplicationData(false);
                                        context.changeFragment(new EventListFragment(), false);
                                    }
                                });
                            }

                            //invalid config
                            else
                            {
                                context.clearApiConfig();
                                context.showSnackbar(getString(R.string.invalid_url));
                            }
                        }

                        //invalid connection
                        else
                        {
                            context.clearApiConfig();
                            context.showSnackbar(getString(R.string.invalid_url));
                        }
                    }
                }).start();
            }
        });

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
        context.getSupportActionBar().show();
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
