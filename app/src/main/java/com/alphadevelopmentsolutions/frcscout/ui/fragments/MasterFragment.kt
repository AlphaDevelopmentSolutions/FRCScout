package com.alphadevelopmentsolutions.frcscout.ui.fragments

import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.callbacks.OnConfirmationCallback
import com.alphadevelopmentsolutions.frcscout.classes.*
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutKingFragmentBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.enums.NavbarState
import kotlinx.android.synthetic.main.activity_main.*

abstract class MasterFragment : Fragment() {
    /***********************************************************************************
     * Required Vars Section
     ************************************************************************************/

    /**
     * Required fragment interaction listener
     */
    private var listener: OnFragmentInteractionListener? = null

    /**
     * Required method
     */
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    /**
     * Required interface
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    /**
     * Required setting of [listener]
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * Required listener handling
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
            listener = context

        else
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
    }

    /***********************************************************************************
     * Useful Vars Section
     ************************************************************************************/

    /**
     * Holds the instance of the [MainActivity]
     */
    lateinit var activityContext: MainActivity

    /**
     * [Config] throughout the app
     */
    protected val config: Config by lazy {
        activityContext.config
    }

    /**
     * [Account] object for the current logged in user
     */
    protected val account: Account? by lazy {
        Account.getInstance(activityContext)
    }

    /**
     * @see MainActivity.navController
     */
    protected val navController: NavController by lazy {
        findNavController()
    }

    /**
     * Default [RepositoryProvider] for fragments
     */
    protected val repositoryProvider by lazy {
        RepositoryProvider.getInstance(activityContext)
    }

    /***********************************************************************************
     * Abstract Fields Section
     ************************************************************************************/

    abstract val TAG: FragmentTag

    /***********************************************************************************
     * Functions Section
     ************************************************************************************/

    /**
     * Removes the current fragment from the navigation controller
     * @see MainActivity.removeCurrentFragment
     */
    fun remove() =
        activityContext.removeCurrentFragment()

    /**
     * Navigates to the specified [NavDirections] using the [navController]
     * @param directions [NavDirections] direction to use when navigating
     */
    protected fun navigate(directions: NavDirections) = navController.navigate(directions)

    /**
     * Variable that holds whether the action bar should be dropped or not
     * When set it will attempt to drop the action bar if the [kingView] is initialized
     */
    protected var isActionBarDropped: Boolean = false
        set(value) {

            kingView?.appbarLayout?.let {
                if (value) {
                    it.stateListAnimator =
                        StateListAnimator().apply {
                            addState(listOf<Int>().toIntArray(), ObjectAnimator.ofFloat(it, "elevation", 0f))
                        }
                } else {
                    it.stateListAnimator =
                        StateListAnimator().apply {
                            addState(listOf<Int>().toIntArray(), ObjectAnimator.ofFloat(it, "elevation", 4f))
                        }
                }
            }

            field = value
        }

    /**
     * [SaveButtonWrapper] object called when the [LayoutKingFragmentBinding.saveTextView] is visible or clicked
     */
    protected var onSaveWrapper: SaveButtonWrapper? = null
        set(value) {
            kingView?.saveTextView?.text = value?.title ?: ""
            kingView?.saveTextView?.setOnClickListener(value?.clickListener)

            field = value
        }

    /**
     * [View.OnClickListener] for when the [LayoutKingFragmentBinding.toolbar] is clicked
     */
    protected var onCancelListener: View.OnClickListener = View.OnClickListener { undoChangesDialog.show(activityContext) }
        set(value) {
            kingView?.toolbar?.setNavigationOnClickListener(onCancelListener)

            field = value
        }

    /**
     * [ConfirmDialogFragment] object that is shown when the [onCancelListener] is called
     */
    protected val undoChangesDialog: ConfirmDialogFragment by lazy {
        ConfirmDialogFragment.newInstance(
            object : OnConfirmationCallback {
                override fun onConfirm() {
                    remove()
                }

                override fun onCancel() {}
            },
            getString(R.string.undo_changes),
            getString(R.string.undo_changes_desc)
        )
    }

    /**
     * [Menu] object that gets inflated when the [MainActivity.onCreateView] is called
     */
    protected open fun getMenu(): Menu =
        Menu(
            null,
            null
        )

    /**
     * State of the [LayoutKingFragmentBinding.toolbar] and [SalesActivity.MainDrawerLayout]
     */
    private var navbarState: NavbarState = NavbarState.LOCKED
        set(value) {

            activityContext.navbarState = value

            kingView?.let { kingView ->

                val toolbar = kingView.toolbar

                // Inflate the menu passed from the fragment
                getMenu().let { menu ->
                    menu.menuResId?.let { resId ->
                        toolbar.inflateMenu(resId)
                        toolbar.setOnMenuItemClickListener(menu.onClick)
                    }
                }

                // If a search query listener was passed, attach that to the search action
                searchItemQueryListener?.let { listener ->
                    toolbar.menu.findItem(R.id.action_search).let { searchItem ->
                        val searchView = searchItem.actionView as SearchView

                        searchView.setOnQueryTextListener(listener)
                    }
                }

                // If a filter listener was passed, attach that to the filter action
                filterItemOnClickListener?.let { listener ->
                    toolbar.menu.findItem(R.id.action_filter)?.setOnMenuItemClickListener(listener)
                }

                @Suppress("NON_EXHAUSTIVE_WHEN")
                when (value) {

                    /**
                     * Drawer is locked
                     * [LayoutKingFragmentBinding.saveTextView] is hidden
                     * [toolbar.getNavigationIcon] is hidden
                     */
                    NavbarState.LOCKED ->
                    {
                        // Make the textview invisible
                        if (kingView.saveTextView.isVisible)
                            kingView.saveTextView.isVisible = false

                        // Remove the nav icon & remove click listener
                        toolbar.navigationIcon = null
                        toolbar.setNavigationOnClickListener(null)
                    }

                    /**
                     * Drawer is locked with a back button
                     * [LayoutKingFragmentBinding.saveTextView] is hidden
                     * [toolbar.getNavigationIcon] shown as a [R.drawable.ic_arrow_back_white_24dp] and [KingActivity.onBackPressed] is called on click
                     */
                    NavbarState.LOCKED_WITH_BACK ->
                    {
                        // Make the textview invisible
                        if (kingView.saveTextView.isVisible)
                            kingView.saveTextView.isVisible = false

                        // Set the icon to the back arrow & and add remove the current fragment when pressed
                        toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_back_white_24dp, null)
                        toolbar.setNavigationOnClickListener {
                            remove()
                        }
                    }

                    /**
                     * Drawer is unlocked
                     * [LayoutKingFragmentBinding.saveTextView] is hidden
                     * [toolbar.getNavigationIcon] shown as [R.drawable.ic_dehaze_white_24dp] and the drawer is opened on click
                     */
                    NavbarState.DRAWER ->
                    {
                        // Make the textview invisible
                        if (kingView.saveTextView.isVisible)
                            kingView.saveTextView.isVisible = false

                        // Set the icon to the dehaze icon & and set the drawer to open on click
                        toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_dehaze_white_24dp, null)
                        toolbar.setNavigationOnClickListener {
                            activityContext.binding.mainDrawerLayout.openDrawer(
                                GravityCompat.START
                            )
                        }
                    }

                    /**
                     * [toolbar] is in edit mode
                     * [LayoutKingFragmentBinding.saveTextView] shown and onclick is set to [onSaveWrapper]
                     * [toolbar.getNavigationIcon] is shown as [R.drawable.ic_close_white_24dp] and onclick is set to [onCancelListener]
                     */
                    NavbarState.EDIT ->
                    {
                        // Make the textview invisible
                        if (!kingView.saveTextView.isVisible)
                            kingView.saveTextView.isVisible = true

                        // Set the icon to the close icon & and set the oncancellistener to be called on click
                        kingView.toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_close_white_24dp, null)
                        kingView.toolbar.setNavigationOnClickListener(onCancelListener)

                        // Set the save wrapper contents to the save text view
                        onSaveWrapper?.let { wrapper ->
                            kingView.saveTextView.text = wrapper.title
                            kingView.saveTextView.setOnClickListener { wrapper.clickListener.onClick(it) }
                        }
                    }

                    NavbarState.INVISIBLE -> {
                        // Do Nothing
                    }
                }
            }

            field = value
        }

    /**
     * Holds the [LayoutKingFragmentBinding] for all fragments
     */
    protected var kingView: LayoutKingFragmentBinding? = null

    /**
     * Holds the listener of what to do when the search view is clicked
     */
    protected var searchItemQueryListener: SearchView.OnQueryTextListener? = null
        set(value) {

            (kingView?.toolbar?.menu?.findItem(R.id.action_search)?.actionView as SearchView?)?.setOnQueryTextListener(value)

            field = value
        }

    /**
     * Holds the listener of what to do when the filter view is clicked
     */
    protected var filterItemOnClickListener: MenuItem.OnMenuItemClickListener? = null
        set(value) {

            kingView?.toolbar?.menu?.findItem(R.id.action_filter)?.setOnMenuItemClickListener(value)

            field = value
        }

    /**
     * Initializes the [activityContext] with the [KingActivity]
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityContext = activity as MainActivity
    }

    /**
     * Takes the [view] passed and appends it to the [kingView]
     * @param view [View] view to add to [kingView]
     * @param state [NavbarState] state of the navigation bar
     * @param title [String] if not null, sets the title of the [LayoutKingFragmentBinding.toolbar]
     */
    protected open fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        view: View,
        state: NavbarState,
        title: String? = null
    ): View {

        LayoutKingFragmentBinding.inflate(inflater, container, false).let { kingView ->

            this.kingView = kingView

            kingView.kingLayout.addView(view)
            kingView.config = config
            
            if (state == NavbarState.INVISIBLE)
                kingView.appbarLayout.visibility = View.GONE
            else
                kingView.toolbar.title = title ?: ""


            isActionBarDropped = isActionBarDropped
            navbarState = state

            return kingView.root
        }
    }

    /**
     * Restarts the current [MainActivity]
     * Useful when handling orientation changes
     */
    fun restart() = activityContext.restartFragment(this)

    fun onBackPressed(): Boolean {

        // If the fragment is currently in edit mode, call the onCancelListener
        if (navbarState == NavbarState.EDIT) {

            onCancelListener.onClick(null)

            return true
        }

        return false
    }
}
