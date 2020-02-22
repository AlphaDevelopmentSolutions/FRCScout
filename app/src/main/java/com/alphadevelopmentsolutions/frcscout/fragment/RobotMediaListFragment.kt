package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alphadevelopmentsolutions.frcscout.adapter.RobotMediaListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.classes.table.account.RobotMedia
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.fragment_robot_media_list.view.*

class RobotMediaListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private lateinit var robotMediaListRecyclerViewAdapter: RobotMediaListRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_media_list, container, false)

        Thread(Runnable {

            loadingThread.join()

            val oldCount = if(::robotMediaListRecyclerViewAdapter.isInitialized) robotMediaListRecyclerViewAdapter.itemCount else 0

            val robotMediaList = RobotMedia.getObjects(null, yearId, eventId, teamId, false, database)

            if(robotMediaList.size != oldCount)
                robotMediaListRecyclerViewAdapter = RobotMediaListRecyclerViewAdapter(teamId!!, robotMediaList, context)

            if(::robotMediaListRecyclerViewAdapter.isInitialized)
            {
                context.runOnUiThread {
                    view.RobotMediaRecyclerView.layoutManager = LinearLayoutManager(context)
                    view.RobotMediaRecyclerView.adapter = robotMediaListRecyclerViewAdapter
                }
            }
        }).start()


        return view
    }

    companion object
    {

        /**
         * Creates a new instance
         * @param team to get robot media from
         * @return A new instance of fragment [RobotMediaListFragment].
         */
        fun newInstance(team: Team): RobotMediaListFragment
        {
            val fragment = RobotMediaListFragment()
            val args = Bundle()
            args.putString(ARG_TEAM_ID, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}
