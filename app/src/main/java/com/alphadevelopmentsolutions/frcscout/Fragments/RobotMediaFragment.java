package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.alphadevelopmentsolutions.frcscout.Classes.Image;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RobotMediaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RobotMediaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotMediaFragment extends MasterFragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ROBOT_MEDIA = "robot_media_json";

    // TODO: Rename and change types of parameters
    private String robotMediaJson;

    private OnFragmentInteractionListener mListener;

    public RobotMediaFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param robotMedia
     * @param team
     * @return A new instance of fragment RobotMediaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RobotMediaFragment newInstance(@Nullable RobotMedia robotMedia, @NonNull Team team)
    {
        RobotMediaFragment fragment = new RobotMediaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROBOT_MEDIA, toJson(robotMedia));
        args.putString(ARG_TEAM_JSON, toJson(team));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            robotMediaJson = getArguments().getString(ARG_ROBOT_MEDIA);
        }

        if(robotMediaJson != null && !robotMediaJson.equals(""))
            robotMedia = new Gson().fromJson(robotMediaJson, RobotMedia.class);
    }

    private RobotMedia robotMedia;

    private ImageView robotMediaImageView;

    private Button robotMediaSaveButton;

//    private Bitmap robotThumbBitmap;

    private File mediaFile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_robot_media, container, false);

        robotMediaImageView = view.findViewById(R.id.RobotMediaImageView);
        robotMediaSaveButton = view.findViewById(R.id.RobotMediaSaveButton);

        //robot media loaded, load image
        if(robotMedia != null)
        {
            robotMediaSaveButton.setVisibility(View.GONE);

            File robotImage = new File(robotMedia.getFileUri());

            if(robotImage.exists())
            {
                Bitmap robotImageBitmap = BitmapFactory.decodeFile(robotImage.getAbsolutePath());
                robotMediaImageView.setImageBitmap(robotImageBitmap);
            }
        }

        //allow the user to save the new image
        else
        {
            mediaFile = new File(Image.generateFileUri(Constants.ROBOT_MEDIA_DIRECTORY).getAbsolutePath()); //get file URI

            //save the new image
            robotMediaSaveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    if(robotThumbBitmap != null)
//                    {
//                        try {

                            //code for thumbnails
//                            FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);
//                            robotThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream); //write data to file

                    joinLoadingThread();
                            robotMedia = new RobotMedia(
                                    -1,
                                    team.getId(),
                                    mediaFile.getAbsolutePath(),
                                    true);

                            robotMedia.save(database);

                            context.getSupportFragmentManager().popBackStackImmediate();
//                        }
//                        catch (FileNotFoundException e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
                }
            });

            //launch intent to take picture if none supplied
//            if(robotThumbBitmap == null)
//            {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getPackageName() + ".provider", mediaFile));
                startActivityForResult(takePictureIntent, Constants.ROBOT_MEDIA_REQUEST_CODE);

//            }


        }


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case Constants.ROBOT_MEDIA_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK)
                {
                    //code for thumbnails
//                    Bundle extras = data.getExtras();
//                    robotThumbBitmap = (Bitmap) extras.get("data");

                    robotMediaImageView.setImageBitmap(BitmapFactory.decodeFile(mediaFile.getAbsolutePath()));
                }

                break;
        }
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
        //robot media never saved, delete image
        if(robotMedia == null)
            if(mediaFile.exists())
                mediaFile.delete();

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
