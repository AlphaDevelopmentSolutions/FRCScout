package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Adapters.TeamViewPagerAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.FontAwesomeIcon;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends MasterFragment
{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "teamId";

    private int teamId;

    private OnFragmentInteractionListener mListener;

    public TeamFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param teamId id of team to show
     * @return A new instance of fragment TeamFragment.
     */
    public static TeamFragment newInstance(int teamId)
    {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, teamId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            teamId = getArguments().getInt(ARG_PARAM1);
        }
    }

    private Team team;

    private TabLayout teamTabLayout;
    private ViewPager teamViewPager;

    private TextView teamNumberNameTextView;
    private TextView teamLocationTextView;

    private FontAwesomeIcon facebookFontAwesomeBrandIcon;
    private FontAwesomeIcon twitterFontAwesomeBrandIcon;
    private FontAwesomeIcon instagramFontAwesomeBrandIcon;
    private FontAwesomeIcon youtubeFontAwesomeBrandIcon;
    private FontAwesomeIcon websiteFontAwesomeSolidIcon;

    private FloatingActionButton addMatchFloatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        //gets rid of the shadow on the actionbar
        context.dropActionBar();

        //load the current team you are viewing
        team = new Team(teamId);
        team.load(database);

        //assign the vars to the views on the page
        teamNumberNameTextView = view.findViewById(R.id.TeamNumberNameTextView);
        teamLocationTextView = view.findViewById(R.id.TeamLocationTextView);

        teamTabLayout = view.findViewById(R.id.TeamTabLayout);
        teamViewPager = view.findViewById(R.id.TeamViewPager);

        facebookFontAwesomeBrandIcon = view.findViewById(R.id.FacebookFontAwesomeBrandIcon);
        twitterFontAwesomeBrandIcon = view.findViewById(R.id.TwitterFontAwesomeBrandIcon);
        instagramFontAwesomeBrandIcon = view.findViewById(R.id.InstagramFontAwesomeBrandIcon);
        youtubeFontAwesomeBrandIcon = view.findViewById(R.id.YoutubeFontAwesomeBrandIcon);
        websiteFontAwesomeSolidIcon = view.findViewById(R.id.WebsiteFontAwesomeSolidIcon);

        addMatchFloatingActionButton = view.findViewById(R.id.AddMatchFloatingActionButton);

        //checks to see if the team has a valid URL for each social media, if not hide the icon
        if(team.getFacebookURL() != null && !team.getFacebookURL().equals("")) facebookFontAwesomeBrandIcon.setURL(team.getFacebookURL(), context);
        else facebookFontAwesomeBrandIcon.hide();

        if(team.getTwitterURL() != null && !team.getTwitterURL().equals("")) twitterFontAwesomeBrandIcon.setURL(team.getTwitterURL(), context);
        else twitterFontAwesomeBrandIcon.hide();

        if(team.getInstagramURL() != null && !team.getInstagramURL().equals("")) instagramFontAwesomeBrandIcon.setURL(team.getInstagramURL(), context);
        else instagramFontAwesomeBrandIcon.hide();

        if(team.getYoutubeURL() != null && !team.getYoutubeURL().equals("")) youtubeFontAwesomeBrandIcon.setURL(team.getYoutubeURL(), context);
        else youtubeFontAwesomeBrandIcon.hide();

        if(team.getWebsiteURL() != null && !team.getWebsiteURL().equals("")) websiteFontAwesomeSolidIcon.setURL(team.getWebsiteURL(), context);
        else websiteFontAwesomeSolidIcon.hide();

        //logic for adding a new match
        addMatchFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (teamTabLayout.getSelectedTabPosition() == 0)
                    context.changeFragment(ScoutCardFragment.newInstance(-1, team.getId()), true);

                else
                    context.changeFragment(PitCardFragment.newInstance(-1, team.getId()), true);
            }
        });

        teamNumberNameTextView.setText(team.getId() + " - " + team.getName());
        teamLocationTextView.setText(team.getCity() + ", " + team.getStateProvince() + ", " + team.getCountry());

        TeamViewPagerAdapter teamViewPagerAdapter = new TeamViewPagerAdapter(getChildFragmentManager());

        teamViewPagerAdapter.addFragment(ScoutCardListFragment.newInstance(teamId), "Scout Cards");
        teamViewPagerAdapter.addFragment(PitCardListFragment.newInstance(teamId), "Pit Cards");

        teamViewPager.setAdapter(teamViewPagerAdapter);
        teamTabLayout.setupWithViewPager(teamViewPager);

        return view;
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
