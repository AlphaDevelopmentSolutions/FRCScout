package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Interfaces.StatsKeys
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*

class QuickStatsFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }
    
    private var statsMinTableLayout: TableLayout? = null
    private var statsMaxTableLayout: TableLayout? = null
    private var statsAvgTableLayout: TableLayout? = null

    private var loadStatsThread: Thread? = null

    private var minTableRows: ArrayList<TableRow>? = null
    private var avgTableRows: ArrayList<TableRow>? = null
    private var maxTableRows: ArrayList<TableRow>? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        loadStatsThread = Thread(Runnable {
            loadingThread.join()

            //get the stats from the team object
            val allStats = team!!.getStats(database, event!!)
            minTableRows = ArrayList()
            avgTableRows = ArrayList()
            maxTableRows = ArrayList()

            //generate padding based on screen size
            val padding = context.resources.getDimensionPixelSize(R.dimen.material_padding)

            //iterate through each category in the stats interface
            for (CATEGORY_KEY in StatsKeys.STATS_CATEGORY_ORDER_KEYS)
            {
                //get the stats for each category
                val stats = allStats[CATEGORY_KEY]

                //iterate through each stat in the stats interface
                for (KEY in StatsKeys.STATS_ORDER_KEYS)
                {
                    //get the stat
                    val stat = stats!![KEY]!!

                    //generate the textview
                    val textView = TextView(context)
                    textView.text = (if (CATEGORY_KEY != StatsKeys.AVG) Math.ceil(stat).toInt() else Math.round(stat * 100.00) / 100.00).toString()
                    textView.setPadding(padding, padding, padding, padding)

                    //create the tablerow to add to the table layout
                    val tableRow = TableRow(context)

                    //color logic
                    if (KEY == StatsKeys.AUTO_EXIT_HAB)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                    } else if (KEY == StatsKeys.AUTO_HATCH)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                    } else if (KEY == StatsKeys.AUTO_HATCH_DROPPED)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                    } else if (KEY == StatsKeys.AUTO_CARGO)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                    } else if (KEY == StatsKeys.AUTO_CARGO_DROPPED)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                    } else if (KEY == StatsKeys.TELEOP_HATCH)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                    } else if (KEY == StatsKeys.TELEOP_HATCH_DROPPED)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                    } else if (KEY == StatsKeys.TELEOP_CARGO)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                    } else if (KEY == StatsKeys.TELEOP_CARGO_DROPPED)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                    } else if (KEY == StatsKeys.RETURNED_HAB)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                    } else if (KEY == StatsKeys.RETURNED_HAB_FAILS)
                    {
                        if (stat > 0)
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                    } else if (KEY == StatsKeys.DEFENSE_RATING)
                    {
                        if (stat >= 0 && stat <= 1.6)
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                        else if (stat <= 3.2)
                            tableRow.setBackgroundColor(context.getColor(R.color.warning))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                    } else if (KEY == StatsKeys.OFFENSE_RATING)
                    {
                        if (stat >= 0 && stat <= 1.6)
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                        else if (stat <= 3.2)
                            tableRow.setBackgroundColor(context.getColor(R.color.warning))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                    } else if (KEY == StatsKeys.DRIVE_RATING)
                    {
                        if (stat >= 0 && stat <= 1.6)
                            tableRow.setBackgroundColor(context.getColor(R.color.bad))
                        else if (stat <= 3.2)
                            tableRow.setBackgroundColor(context.getColor(R.color.warning))
                        else
                            tableRow.setBackgroundColor(context.getColor(R.color.good))
                    }

                    //set the gravity to center the text and add it
                    tableRow.gravity = Gravity.CENTER
                    tableRow.addView(textView)

                    //add each tablerow to the array
                    if (CATEGORY_KEY == StatsKeys.MIN)
                        minTableRows!!.add(tableRow)
                    else if (CATEGORY_KEY == StatsKeys.AVG)
                        avgTableRows!!.add(tableRow)
                    else
                        maxTableRows!!.add(tableRow)
                }

            }
        })

        loadStatsThread!!.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quick_stats, container, false)

        statsMinTableLayout = view.findViewById(R.id.StatsMinTableLayout)
        statsMaxTableLayout = view.findViewById(R.id.StatsMaxTableLayout)
        statsAvgTableLayout = view.findViewById(R.id.StatsAvgTableLayout)

        Thread(Runnable {
            try
            {
                //make sure the load stats thread has finish before attempting
                loadStatsThread!!.join()

                //check if the table rows have a parent, if they do remove every view
                //this includes table rows from min, max and avg
                //mainly has to be done when the view is created on a rotate or backstack pop
                if (minTableRows!![0].parent != null)
                    (minTableRows!![0].parent as ViewGroup).removeAllViews()

                if (avgTableRows!![0].parent != null)
                    (avgTableRows!![0].parent as ViewGroup).removeAllViews()

                if (maxTableRows!![0].parent != null)
                    (maxTableRows!![0].parent as ViewGroup).removeAllViews()

                //iterate through each of the table rows
                for (i in minTableRows!!.indices)
                {

                    //add each category row
                    context.runOnUiThread {
                        //add each view to the respective tablelayout
                        statsMinTableLayout!!.addView(minTableRows!![i])
                        statsAvgTableLayout!!.addView(avgTableRows!![i])
                        statsMaxTableLayout!!.addView(maxTableRows!![i])
                    }
                }
            } catch (e: InterruptedException)
            {
                e.printStackTrace()
            }
        }).start()

        return view
    }
    
    companion object
    {

        /**
         * Creates a new instance
         * @param team to show stats for
         * @return A new instance of fragment [QuickStatsFragment].
         */
        fun newInstance(team: Team): QuickStatsFragment
        {
            val fragment = QuickStatsFragment()
            val args = Bundle()
            args.putString(ARG_TEAM_JSON, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}
