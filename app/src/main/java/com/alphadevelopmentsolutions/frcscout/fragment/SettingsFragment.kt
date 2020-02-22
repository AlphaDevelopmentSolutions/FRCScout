package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.BuildConfig

import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : MasterFragment()
{
    enum class Page
    {
        SETTINGS,
        LICENSES;

        companion object
        {
            /**
             * Gets the page from a string value
             * @param pageString [String] of page to get
             */
            fun fromString(pageString: String): Page
            {
                var defaultState: Page? = null

                for(state in values())
                {
                    if(defaultState != null)
                        defaultState = state


                    if(pageString == state.name)
                        return state
                }

                return defaultState!!
            }
        }
    }



    private lateinit var page: Page

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if(arguments != null)
        {
            page = Page.fromString(arguments!!.getString(ARG_SETTINGS_PAGE, ""))
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        when(page)
        {
            Page.LICENSES -> return super.onCreateView(inflater.inflate(R.layout.layout_licenses, container, false))

            else ->
            {
                activityContext.lockDrawerLayout(true, View.OnClickListener { activityContext.supportFragmentManager.popBackStackImmediate() })

                return super.onCreateView(
                        inflater.inflate(R.layout.fragment_settings, container, false).apply{
                            AppVersionTextView.text = String.format(context.getString(R.string.app_version), BuildConfig.VERSION_NAME)
                            BuildNumberTextView.text = String.format(context.getString(R.string.build_number), BuildConfig.VERSION_CODE)
                            BuildTypeTextView.text = String.format(context.getString(R.string.build_type), BuildConfig.BUILD_TYPE)

                            LicensesButton.setOnClickListener{
                                this@SettingsFragment.activityContext.changeFragment(newInstance(Page.LICENSES), true)
                            }
                        })
            }

        }
    }

    override fun onBackPressed(): Boolean
    {
        return false
    }

    override fun onDetach()
    {
        if(page == Page.SETTINGS)
            activityContext.unlockDrawerLayout()

        super.onDetach()
    }

    companion object
    {
        const val ARG_SETTINGS_PAGE = "SETTINGS_PAGE"

        /**
         * Creates a new instance
         * @param settingsPage [Page] to be shown
         * @return A new instance of fragment [SettingsFragment].
         */

        @JvmStatic
        fun newInstance(settingsPage: Page) =
                SettingsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_SETTINGS_PAGE, settingsPage.name)
                    }
                }
    }
}
