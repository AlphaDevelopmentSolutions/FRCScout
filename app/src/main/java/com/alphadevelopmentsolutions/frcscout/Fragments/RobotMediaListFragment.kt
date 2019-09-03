package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.RobotMediaListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R

class RobotMediaListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }
    
    private var robotMediaRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_media_list, container, false)

        robotMediaRecyclerView = view.findViewById(R.id.RobotMediaRecyclerView)

        Thread(Runnable {
            loadingThread.join()

            val robotMediaListRecyclerViewAdapter = RobotMediaListRecyclerViewAdapter(team!!, team!!.getRobotMedia(null, false, database)!!, context)

            context.runOnUiThread {
                robotMediaRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                robotMediaRecyclerView!!.adapter = robotMediaListRecyclerViewAdapter
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
            args.putString(ARG_TEAM_JSON, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}
