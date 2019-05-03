package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.FontAwesomeIcon;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
    private OnFragmentInteractionListener mListener;

    public TeamFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param team
     * @return A new instance of fragment TeamFragment.
     */
    public static TeamFragment newInstance(@NonNull Team team)
    {
        TeamFragment fragment = new TeamFragment();
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
    private FloatingActionButton addPitCardFloatingActionButton;
    private FloatingActionButton addRobotPhotoFloatingActionButton;

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
        addPitCardFloatingActionButton = view.findViewById(R.id.AddPitCardFloatingActingButton);
        addRobotPhotoFloatingActionButton = view.findViewById(R.id.AddRobotPhotoFloatingActionButton);

        joinLoadingThread();

        addPitCardFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.changeFragment(PitCardFragment.newInstance(null, team), true);
            }
        });

        addRobotPhotoFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.changeFragment(RobotMediaFragment.newInstance(null, team), true);
            }
        });

        //update the app bar title to the team name
        context.getSupportActionBar().setTitle(team.getId() + " - " + team.getName());

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

        FragmentViewPagerAdapter teamViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager());

        teamViewPagerAdapter.addFragment(MatchListFragment.newInstance(team), getString(R.string.scout_cards));
        teamViewPagerAdapter.addFragment(PitCardListFragment.newInstance(team), getString(R.string.pit_cards));
        teamViewPagerAdapter.addFragment(RobotMediaListFragment.newInstance(team), getString(R.string.robot_images));
        teamViewPagerAdapter.addFragment(QuickStatsFragment.newInstance(team), getString(R.string.quick_stats));

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
