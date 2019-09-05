package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Adapters.TeamViewPagerAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.FontAwesomeIcon;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private static final String ARG_PARAM2 = "eventJson";

    private String teamJson;
    private String eventJson;

    private OnFragmentInteractionListener mListener;

    public TeamFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param teamJson json of team
     * @param eventJson json of event
     * @return A new instance of fragment TeamFragment.
     */
    public static TeamFragment newInstance(String teamJson, String eventJson)
    {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, teamJson);
        args.putString(ARG_PARAM2, eventJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            teamJson = getArguments().getString(ARG_PARAM1);
            eventJson = getArguments().getString(ARG_PARAM2);
        }

        loadThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                team = new Gson().fromJson(teamJson, Team.class);
                event = new Gson().fromJson(eventJson, Event.class);
            }
        });
        loadThread.start();

    }

    private Team team;
    private Event event;

    private TabLayout teamTabLayout;
    private ViewPager teamViewPager;

    private TextView teamNumberNameTextView;
    private TextView teamLocationTextView;

    private CircleImageView teamLogoImageView;

    private FontAwesomeIcon facebookFontAwesomeBrandIcon;
    private FontAwesomeIcon twitterFontAwesomeBrandIcon;
    private FontAwesomeIcon instagramFontAwesomeBrandIcon;
    private FontAwesomeIcon youtubeFontAwesomeBrandIcon;
    private FontAwesomeIcon websiteFontAwesomeSolidIcon;

    private FloatingActionMenu teamFloatingActionMenu;
    private FloatingActionButton addMatchFloatingActionButton;
    private FloatingActionButton addPitCardFloatingActionButton;
    private FloatingActionButton addRobotPhotoFloatingActionButton;

    private Thread loadThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        //gets rid of the shadow on the actionbar
        context.dropActionBar();

        //assign the vars to the views on the page
        teamNumberNameTextView = view.findViewById(R.id.TeamNumberNameTextView);
        teamLocationTextView = view.findViewById(R.id.TeamLocationTextView);

        teamTabLayout = view.findViewById(R.id.TeamTabLayout);
        teamViewPager = view.findViewById(R.id.TeamViewPager);

        teamLogoImageView = view.findViewById(R.id.TeamLogoImageView);

        facebookFontAwesomeBrandIcon = view.findViewById(R.id.FacebookFontAwesomeBrandIcon);
        twitterFontAwesomeBrandIcon = view.findViewById(R.id.TwitterFontAwesomeBrandIcon);
        instagramFontAwesomeBrandIcon = view.findViewById(R.id.InstagramFontAwesomeBrandIcon);
        youtubeFontAwesomeBrandIcon = view.findViewById(R.id.YoutubeFontAwesomeBrandIcon);
        websiteFontAwesomeSolidIcon = view.findViewById(R.id.WebsiteFontAwesomeSolidIcon);

        teamFloatingActionMenu = view.findViewById(R.id.TeamFloatingActionMenu);
        addMatchFloatingActionButton = view.findViewById(R.id.AddMatchFloatingActionButton);
        addPitCardFloatingActionButton = view.findViewById(R.id.AddPitCardFloatingActingButton);
        addRobotPhotoFloatingActionButton = view.findViewById(R.id.AddRobotPhotoFloatingActionButton);

        //logic for adding a new match
        addMatchFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.changeFragment(ScoutCardFragment.newInstance(null, eventJson, team.getId()), true);
            }
        });

        addPitCardFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.changeFragment(PitCardFragment.newInstance(null, eventJson, team.getId()), true);
            }
        });

        addRobotPhotoFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //Ensure the robot media folder exists
                File mediaFolder = new File(Constants.MEDIA_DIRECTORY);
                if(!mediaFolder.isDirectory())
                {
                    if (!mediaFolder.mkdir())
                        context.showSnackbar(getString(R.string.mkdir_fail));
                    else
                        context.changeFragment(RobotMediaFragment.newInstance(null, team.getId()), true);

                }
                else
                    context.changeFragment(RobotMediaFragment.newInstance(null, team.getId()), true);
            }
        });

        //join back up with the load team thread
        try
        {
            loadThread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        //load the photo if the file exists
        if(!team.getImageFileURI().equals(""))
            Picasso.get()
                    .load(Uri.fromFile(new File(team.getImageFileURI())))
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.frc_logo)
                    .error(R.drawable.frc_logo)
                    .into(teamLogoImageView);

        else
            teamLogoImageView.setImageDrawable(context.getDrawable(R.drawable.frc_logo));

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

        teamNumberNameTextView.setText(team.getId() + " - " + team.getName());
        teamLocationTextView.setText(team.getCity() + ", " + team.getStateProvince() + ", " + team.getCountry());

        TeamViewPagerAdapter teamViewPagerAdapter = new TeamViewPagerAdapter(getChildFragmentManager());

        teamViewPagerAdapter.addFragment(ScoutCardListFragment.newInstance(teamJson, eventJson), "Scout Cards");
        teamViewPagerAdapter.addFragment(PitCardListFragment.newInstance(teamJson, eventJson), "Pit Cards");
        teamViewPagerAdapter.addFragment(RobotMediaListFragment.newInstance(teamJson), "Robot Images");
        teamViewPagerAdapter.addFragment(QuickStatsFragment.newInstance(team.getId(), eventJson), "Quick Stats");

        teamViewPager.setAdapter(teamViewPagerAdapter);
        teamViewPager.setOffscreenPageLimit(5);
        teamTabLayout.setupWithViewPager(teamViewPager);

        teamViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i1)
            {

            }

            @Override
            public void onPageSelected(int i)
            {
                if(i == 3)
                    teamFloatingActionMenu.hideMenu(true);
                else if (teamFloatingActionMenu.isMenuHidden())
                    teamFloatingActionMenu.showMenu(true);


            }

            @Override
            public void onPageScrollStateChanged(int i)
            {

            }
        });

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
