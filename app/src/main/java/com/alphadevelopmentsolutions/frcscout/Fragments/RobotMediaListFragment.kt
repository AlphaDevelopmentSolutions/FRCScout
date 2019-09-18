package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.RobotMediaListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.fragment_robot_media_list.view.*

class RobotMediaListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        loadMediaThread = Thread(Runnable {
            loadingThread.join()

            robotMediaList = RobotMedia.getObjects(null, team, false, database)

            robotMediaListRecyclerViewAdapter = RobotMediaListRecyclerViewAdapter(team!!, robotMediaList, context)

        })

        loadMediaThread.start()
    }

    private lateinit var loadMediaThread: Thread

    private lateinit var robotMediaListRecyclerViewAdapter: RobotMediaListRecyclerViewAdapter

    private lateinit var robotMediaList: ArrayList<RobotMedia>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_media_list, container, false)

        Thread(Runnable {

            loadMediaThread.join()

//            with(RobotMedia.getObjects(null, team, true, database))
//            {
//                if(size > robotMediaList.size)
//                {
//                    robotMediaList = this
//                    for(i in robotMediaList.size - 1 until size)
//                    {
//                        robotMediaListRecyclerViewAdapter.notifyItemInserted(i)
//                    }
//                }
//            }

            context.runOnUiThread {
                view.RobotMediaRecyclerView.layoutManager = LinearLayoutManager(context)
                view.RobotMediaRecyclerView.adapter = robotMediaListRecyclerViewAdapter
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
