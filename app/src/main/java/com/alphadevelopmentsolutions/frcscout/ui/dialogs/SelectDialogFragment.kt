package com.alphadevelopmentsolutions.frcscout.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.adapters.SelectDialogRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.callbacks.OnClickListener
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import com.alphadevelopmentsolutions.frcscout.classes.Config
import com.alphadevelopmentsolutions.frcscout.databinding.FragmentDialogSelectBinding
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog

class SelectDialogFragment : MasterDialogFragment(), TextWatcher {

    /**
     * @see MasterDialogFragment.width
     */
    override val width by lazy { getDimension(R.dimen.dialog_width) }

    /**
     * @see MasterDialogFragment.height
     */
    override val height by lazy { getDimension(R.dimen.dialog_select_height) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            it.getSerializable(ARG_SEARCHABLE_ITEMS)?.let { items ->
                try {

                    @Suppress("UNCHECKED_CAST")
                    (items as Array<Any>).toList().let { searchableItems ->
                        this.searchableItems = searchableItems
                        searchedItems = searchableItems.toMutableList()
                        context?.let { context ->
                            searchRecyclerViewAdapter = SelectDialogRecyclerViewAdapter(context, searchedItems, localItemSelectedListener)
                        }
                    }
                } catch (exception: ClassCastException) {
                    AppLog.e(exception)
                }
            }

            it.getSerializable(ARG_ITEM_SELECTED_CALLBACK)?.let { item ->
                if (item is OnItemSelectedListener)
                    itemSelectedListener = item
            }

            it.getSerializable(ARG_CREATE_NEW_CALLBACK)?.let { item ->
                if (item is OnClickListener)
                    createNewListener = item
            }
        }
    }

    /**
     * [OnItemSelectedListener] for when an item is selected from the [SelectDialogRecyclerViewAdapter]
     */
    private var itemSelectedListener: OnItemSelectedListener? = null

    /**
     * [OnClickListener] for when the [FragmentDialogSelectBinding.CreateNewButton] is clicked
     */
    private var createNewListener: OnClickListener? = null

    /**
     * [List] of searchable items
     */
    private var searchableItems: List<Any>? = null

    /**
     * [List] of items that match the search query
     */
    private lateinit var searchedItems: MutableList<Any>

    /**
     * The previous search length, used for when to filter more or re-add items
     */
    private var previousSearchLength = 0

    /**
     * [SelectDialogRecyclerViewAdapter] recycler view for displaying [searchableItems]
     */
    private lateinit var searchRecyclerViewAdapter: SelectDialogRecyclerViewAdapter

    /**
     * Holds a [Boolean] if the text change is from the system, primarily when calling the setText() function
     */
    private var textChangeFromSystem: Boolean = false

    /**
     * [OnItemSelectedListener] for when a [searchableItems] is selected
     */
    private val localItemSelectedListener =
        object : OnItemSelectedListener {
            override fun onItemSelected(selectedItem: Any) {
                itemSelectedListener?.onItemSelected(selectedItem)
                dismiss()
            }
        }

    /**
     * [Config] from the sales_main app
     */
    private lateinit var config: Config

    /**
     * Holds the view that is inflated
     */
    private lateinit var view: FragmentDialogSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = FragmentDialogSelectBinding.inflate(inflater, container, false)
        view.config = config

        createNewListener?.let { createNewCallback ->
            view.createNewButton.apply {
                this.visibility = View.VISIBLE
                this.setOnClickListener {
                    createNewCallback.onClick(it)
                    dismiss()
                }
            }
        }

        context?.let { context ->
            view.searchTextInput.addTextChangedListener(this)

            view.searchItemsRecyclerview.layoutManager = LinearLayoutManager(context)
            view.searchItemsRecyclerview.adapter = searchRecyclerViewAdapter
        }

        return view.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        // Wipe it as if it was new
        textChangeFromSystem = true
        previousSearchLength = 0
        searchableItems?.toMutableList()?.let { items ->
            searchedItems = items
            searchRecyclerViewAdapter.notifyDataSetChanged()
        }
        view.searchTextInput.text?.clear()
        textChangeFromSystem = false

        super.onDismiss(dialog)
    }

    override fun show(context: MainActivity) {
        config = context.config

        super.show(context)
    }

    companion object {

        private const val ARG_SEARCHABLE_ITEMS = "ARG_SEARCHABLE_ITEMS"
        private const val ARG_ITEM_SELECTED_CALLBACK = "ARG_ITEM_SELECTED_CALLBACK"
        private const val ARG_CREATE_NEW_CALLBACK = "ARG_CREATE_NEW_CALLBACK"

        @JvmStatic
        fun newInstance(
            onItemSelectedListener: OnItemSelectedListener,
            searchableItems: List<Any>,
            createNewListener: OnClickListener? = null
        ) =
            SelectDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SEARCHABLE_ITEMS, searchableItems.toTypedArray())
                    putSerializable(ARG_ITEM_SELECTED_CALLBACK, onItemSelectedListener)
                    createNewListener?.let {
                        putSerializable(ARG_CREATE_NEW_CALLBACK, it)
                    }
                }
            }
    }

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(searchText: CharSequence, p1: Int, p2: Int, p3: Int) {
        searchableItems?.let { searchableItems ->
            if (!textChangeFromSystem) {
                val searchLength = searchText.length

                // You only need to reset the list if you are removing from your search, adding the objects back
                if (searchLength < previousSearchLength) {
                    // Reset the list
                    for (i in searchableItems.indices) {
                        val item = searchableItems[i]

                        // check if the contact doesn't exist in the viewable list
                        if (!searchedItems.contains(item)) {
                            // add it and notify the recyclerview
                            searchedItems.add(i, item)
//                            searchAdapter.notifyItemInserted(searchedItems.size - 1)
//                            searchAdapter.notifyItemRangeChanged(searchedItems.size - 1, searchedItems.size)
                            searchRecyclerViewAdapter.notifyDataSetChanged()
                        }
                    }
                }

                // Delete from the list
                var i = 0
                while (i < searchedItems.size) {
                    val item = searchedItems[i]

                    // If the contacts name doesn't equal the searched name
                    if (!item.toString().toLowerCase().contains(searchText.toString().toLowerCase())) {
                        // remove it from the list and notify the recyclerview
                        searchedItems.removeAt(i)
//                        searchAdapter.notifyItemRemoved(i)
//                        searchAdapter.notifyItemRangeChanged(i, searchedItems.size)
                        searchRecyclerViewAdapter.notifyDataSetChanged()

                        // this prevents the index from passing the size of the list,
                        // stays on the same index until you NEED to move to the next one
                        i--
                    }
                    i++
                }
//                searchAdapter.notifyDataSetChanged()
                previousSearchLength = searchLength
            }
        }
    }
}