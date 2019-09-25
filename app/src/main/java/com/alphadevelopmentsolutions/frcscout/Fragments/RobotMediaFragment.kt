package com.alphadevelopmentsolutions.frcscout.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_robot_media.view.*
import java.io.File
import java.util.*


class RobotMediaFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }
    
    private var robotMediaJson: String? = null
    
    private lateinit var robotMedia: RobotMedia

    private lateinit var robotMediaImageView: ImageView

    private lateinit var mediaFilePath: String



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            robotMediaJson = arguments!!.getString(ARG_ROBOT_MEDIA)

        if (robotMediaJson != null && robotMediaJson != "")
            robotMedia = Gson().fromJson(robotMediaJson, RobotMedia::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_media, container, false)
        context.lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })
        view.z = zIndex

        robotMediaImageView = view.RobotMediaImageView

        //robot media loaded, load image into the imageview
        if (::robotMedia.isInitialized)
        {
            view.RobotMediaSaveButton.visibility = View.GONE

            robotMediaImageView.setImageBitmap(robotMedia.imageBitmap)
        }
        else
        {
            //save the new image
            view.RobotMediaSaveButton.setOnClickListener {

                robotMedia.save(database)

                context.supportFragmentManager.popBackStackImmediate()
            }

            val tempFile = File.createTempFile(
                    UUID.randomUUID().toString(),
                    ".jpeg",
                    context.getExternalFilesDir(Constants.ROBOT_MEDIA_DIRECTORY)).apply {
                mediaFilePath = absolutePath
            }

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(context.packageManager)?.also {
                        tempFile.also {

                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, "com.alphadevelopmentsolutions.frcscout.provider", it))
                            startActivityForResult(takePictureIntent, Constants.ROBOT_MEDIA_REQUEST_CODE)
                        }
                }
            }
        }

        loadingThread.join()

        context.setToolbarTitle("${team!!.id} Robot Media")

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode)
        {
            Constants.ROBOT_MEDIA_REQUEST_CODE ->
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    robotMedia = RobotMedia(
                            -1,
                            year!!.serverId!!,
                            event!!.blueAllianceId!!,
                            team!!.id!!,
                            mediaFilePath,
                            true)

                    robotMediaImageView.setImageBitmap(robotMedia.imageBitmap)
                }

                else if(resultCode == Activity.RESULT_CANCELED)
                    context.supportFragmentManager.popBackStackImmediate()
            }
        }
    }

    override fun onDetach()
    {
        //robot media never saved, delete image
        if ((!::robotMedia.isInitialized && ::mediaFilePath.isInitialized) || (::robotMedia.isInitialized && robotMedia.id == -1))
            File(mediaFilePath).apply {
                if(exists())
                    delete()
            }

        super.onDetach()
    }

    companion object
    {
        private const val ARG_ROBOT_MEDIA = "robot_media_json"

        /**
         * Creates a new instance
         * @param robotMedia to show on the fragment
         * @param team to get robot media from
         * @return A new instance of fragment [RobotMediaFragment].
         */
        fun newInstance(robotMedia: RobotMedia?, team: Team): RobotMediaFragment
        {
            val fragment = RobotMediaFragment()
            val args = Bundle()
            args.putString(ARG_ROBOT_MEDIA, toJson(robotMedia))
            args.putString(ARG_TEAM_JSON, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}
