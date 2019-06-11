package com.alphadevelopmentsolutions.frcscout.Fragments

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

import com.alphadevelopmentsolutions.frcscout.Classes.Image
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.google.gson.Gson

import java.io.File

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RobotMediaFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RobotMediaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RobotMediaFragment : MasterFragment()
{

    // TODO: Rename and change types of parameters
    private var robotMediaJson: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    private var robotMedia: RobotMedia? = null

    private var robotMediaImageView: ImageView? = null

    private var robotMediaSaveButton: Button? = null

    //    private Bitmap robotThumbBitmap;

    private var mediaFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments != null)
        {
            robotMediaJson = arguments!!.getString(ARG_ROBOT_MEDIA)
        }

        if (robotMediaJson != null && robotMediaJson != "")
            robotMedia = Gson().fromJson(robotMediaJson, RobotMedia::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_media, container, false)

        robotMediaImageView = view.findViewById(R.id.RobotMediaImageView)
        robotMediaSaveButton = view.findViewById(R.id.RobotMediaSaveButton)

        //robot media loaded, load image
        if (robotMedia != null)
        {
            robotMediaSaveButton!!.visibility = View.GONE

            val robotImage = File(robotMedia!!.fileUri)

            if (robotImage.exists())
            {
                val robotImageBitmap = BitmapFactory.decodeFile(robotImage.absolutePath)
                robotMediaImageView!!.setImageBitmap(robotImageBitmap)
            }
        } else
        {
            mediaFile = File(Image.generateFileUri(Constants.ROBOT_MEDIA_DIRECTORY).absolutePath) //get file URI

            //save the new image
            robotMediaSaveButton!!.setOnClickListener {
                //                    if(robotThumbBitmap != null)
                //                    {
                //                        try {

                //code for thumbnails
                //                            FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);
                //                            robotThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream); //write data to file

                joinLoadingThread()
                robotMedia = RobotMedia(
                        -1,
                        team!!.id!!,
                        mediaFile!!.absolutePath,
                        true)

                robotMedia!!.save(database)

                context.supportFragmentManager.popBackStackImmediate()
                //                        }
                //                        catch (FileNotFoundException e)
                //                        {
                //                            e.printStackTrace();
                //                        }
                //                    }
            }

            //launch intent to take picture if none supplied
            //            if(robotThumbBitmap == null)
            //            {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.packageName + ".provider", mediaFile!!))
            startActivityForResult(takePictureIntent, Constants.ROBOT_MEDIA_REQUEST_CODE)

            //            }


        }//allow the user to save the new image


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode)
        {
            Constants.ROBOT_MEDIA_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK)
            {
                //code for thumbnails
                //                    Bundle extras = data.getExtras();
                //                    robotThumbBitmap = (Bitmap) extras.get("data");

                robotMediaImageView!!.setImageBitmap(BitmapFactory.decodeFile(mediaFile!!.absolutePath))
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri)
    {
        if (mListener != null)
        {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
        {
            mListener = context
        } else
        {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach()
    {
        //robot media never saved, delete image
        if (robotMedia == null)
            if (mediaFile!!.exists())
                mediaFile!!.delete()

        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object
    {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_ROBOT_MEDIA = "robot_media_json"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param robotMedia
         * @param team
         * @return A new instance of fragment RobotMediaFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(robotMedia: RobotMedia?, team: Team): RobotMediaFragment
        {
            val fragment = RobotMediaFragment()
            val args = Bundle()
            args.putString(ARG_ROBOT_MEDIA, MasterFragment.toJson(robotMedia))
            args.putString(MasterFragment.ARG_TEAM_JSON, MasterFragment.toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
