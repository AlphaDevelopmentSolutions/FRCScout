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
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.extension.init
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import kotlinx.android.synthetic.main.fragment_robot_media_list.view.*

class RobotMediaListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private var robotMediaListRecyclerViewAdapter: RobotMediaListRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_media_list, container, false)

        val oldCount = robotMediaListRecyclerViewAdapter?.itemCount ?: 0

        VMProvider.getInstance(activityContext).robotMediaViewModel.objs.init()
                .subscribe(
                        { robotMediaList ->

                            if(robotMediaList.size != oldCount)
                                robotMediaListRecyclerViewAdapter = RobotMediaListRecyclerViewAdapter(teamId!!, robotMediaList.toMutableList(), activityContext)

                            robotMediaListRecyclerViewAdapter?.let {
                                activityContext.runOnUiThread {
                                    view.RobotMediaRecyclerView.layoutManager = LinearLayoutManager(activityContext)
                                    view.RobotMediaRecyclerView.adapter = it
                                }
                            }
                        },
                        {
                            AppLog.error(it)
                        }
                )
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
