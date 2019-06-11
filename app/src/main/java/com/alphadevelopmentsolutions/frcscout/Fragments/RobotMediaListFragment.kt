package com.alphadevelopmentsolutions.frcscout.Fragments

import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alphadevelopmentsolutions.frcscout.Adapters.RobotMediaListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RobotMediaListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RobotMediaListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RobotMediaListFragment : MasterFragment()
{

    private var mListener: OnFragmentInteractionListener? = null

    private var robotMediaRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_media_list, container, false)

        robotMediaRecyclerView = view.findViewById(R.id.RobotMediaRecyclerView)

        Thread(Runnable {
            joinLoadingThread()

            val robotMediaListRecyclerViewAdapter = RobotMediaListRecyclerViewAdapter(team!!, team!!.getRobotMedia(null, false, database)!!, context)

            context.runOnUiThread {
                robotMediaRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                robotMediaRecyclerView!!.adapter = robotMediaListRecyclerViewAdapter
            }
        }).start()


        return view
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

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param team
         * @return A new instance of fragment RobotMediaListFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(team: Team): RobotMediaListFragment
        {
            val fragment = RobotMediaListFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_TEAM_JSON, MasterFragment.toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
