package com.alphadevelopmentsolutions.frcscout.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.KeyStore
import com.alphadevelopmentsolutions.frcscout.enums.NavBarState
import com.alphadevelopmentsolutions.frcscout.extension.getUUIDOrNull
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_master.view.*
import kotlinx.android.synthetic.main.layout_view_loading.view.*
import java.util.*

abstract class MasterFragment : Fragment()
{
    protected lateinit var activityContext: MainActivity

    protected var yearId: UUID? = null
    protected var eventId: UUID? = null
    protected var teamId: UUID? = null
    protected var matchId: UUID? = null
    protected var scoutCardId: UUID? = null
    protected var pitCardId: UUID? = null
    protected var scoutCardInfoId: UUID? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
        {
            mListener = context
        }
        else
        {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach()
    {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener
    {
        fun onFragmentInteraction(uri: Uri)
    }

    protected var isLoading = false
    set(value)
    {
        if(value != field)
        {
            masterLayout?.let { masterLayout ->

                loadingView?.let { loadingView->
                    if (value)
                        masterLayout.MasterLinearLayout.addView(loadingView)
                    else
                        masterLayout.MasterLinearLayout.removeView(loadingView)
                }
            }

            field = value
        }
    }

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var saveButton: ImageButton

    private var masterLayout: View? = null
    private var loadingView: View? = null

    protected fun onCreateView(
            view: View,
            state: NavBarState = NavBarState.DRAWER,
            title: String? = null
    ): View {

        loadingView = LayoutInflater.from(activityContext).inflate(R.layout.layout_view_loading, null).apply {

            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            MainLoadingProgressBar.indeterminateDrawable.setColorFilter(this@MasterFragment.activityContext.primaryColor, PorterDuff.Mode.SRC_IN)

        }

        masterLayout = LayoutInflater.from(activityContext).inflate(R.layout.layout_master, null).apply {

            MasterLinearLayout.addView(view)

            appBarLayout = AppBarLayout
            toolbar = Toolbar
            saveButton = SaveImageViewButton

            toolbar.title = title ?: getString(R.string.app_name)

            if(isLoading)
                MasterLinearLayout.addView(loadingView)

            MasterLinearLayout.addView(view)
        }

        return masterLayout!!
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //assign context and database vars
        activityContext = activity as MainActivity

        //check if any args were passed, specifically for teamId and matchId json
        arguments?.let { arguments ->

            teamId = arguments.getUUIDOrNull(ARG_TEAM_ID)
            matchId = arguments.getUUIDOrNull(ARG_MATCH_ID)
            scoutCardId = arguments.getUUIDOrNull(ARG_SCOUT_CARD_ID)
            pitCardId = arguments.getUUIDOrNull(ARG_PIT_CARD_ID)

            yearId = KeyStore.getInstance(activityContext).selectedYearId
            eventId = KeyStore.getInstance(activityContext).selectedEventId

        }
    }

    /**
     * Used to override the activities back pressed eventId
     * @return [Boolean] true to override activities back press eventId
     */
    open fun onBackPressed(): Boolean = false

    companion object
    {
        @JvmStatic
        protected val ARG_TEAM_ID = "TEAM_ID"

        @JvmStatic
        protected val ARG_MATCH_ID = "MATCH_ID"

        @JvmStatic
        protected val ARG_SCOUT_CARD_ID = "SCOUT_CARD_ID"

        @JvmStatic
        protected val ARG_PIT_CARD_ID = "PIT_CARD_ID"

        @JvmStatic
        protected var staticGson: Gson? = null

        /**
         * Converts fields to json regardless if they are null or not
         * @param object to convert to json
         * @return null | string json object
         */
        @JvmStatic
        protected fun toJson(`object`: Any?): String?
        {
            if (staticGson == null)
                staticGson = Gson()

            return if (`object` == null)
                null
            else
                staticGson!!.toJson(`object`)
        }
    }
}
