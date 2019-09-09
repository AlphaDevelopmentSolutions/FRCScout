package com.alphadevelopmentsolutions.frcscout.Fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_quick_stats.view.*
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap


class QuickStatsFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private lateinit var loadStatsThread: Thread

    private lateinit var tempStatChart: LineChart

    private val statCharts = ArrayList<LinearLayout>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        loadStatsThread = Thread(Runnable {
            loadingThread.join()


            val matches = Match.getObjects(event!!, null, team!!, database, Database.SortDirection.ASC) //matches for this team
            val scoutCardInfoKeys = ScoutCardInfoKey.getObjects(year!!, null, database) //scout card info keys for this year
            val scoutCardInfos = ScoutCardInfo.getObjects(event, null, null, null, null, false, database) //scout card infos for this event

            //gets the height in DP to display
            val dp = fun(height: Int): Int
            {
                return (height * context.resources.displayMetrics.density + 0.5f).toInt()
            }

            if (!scoutCardInfoKeys.isNullOrEmpty() && !matches.isNullOrEmpty() && !scoutCardInfos.isNullOrEmpty())
            {
                val eventStatsHashMap = event!!.getStats(year, scoutCardInfoKeys, scoutCardInfos, database) //get event avg data

                //get match stats data for each match
                val matchStatsHashMap = HashMap<String, HashMap<String, Double>>()
                for(match in matches)
                    matchStatsHashMap[match.toString()] = match.getStats(year, event, scoutCardInfoKeys, scoutCardInfos, database)

                val teamStatsHashMap = team!!.getStats(year, event, matches, scoutCardInfoKeys, scoutCardInfos, database) // get team stats data
                
                // get all the states from the scout card info keys
                val scoutInfoKeyStates = LinkedHashMap<String, ArrayList<ScoutCardInfoKey>>().apply {
                    
                    for(scoutCardInfoKey in scoutCardInfoKeys)
                    {
                        if(scoutCardInfoKey.includeInStats)
                            this[scoutCardInfoKey.keyState] = (this[scoutCardInfoKey.keyState] ?: ArrayList()).apply {
                                add(scoutCardInfoKey)
                            }
                    }
                }
                
                //iterate through all the info key states
                for(scoutCardInfoKeyState in scoutInfoKeyStates.values)
                {
                    //latch used to hold the thread until the runOnUiThread has finished
                    val latch = CountDownLatch(1)

                    context.runOnUiThread {

                        tempStatChart = LineChart(context)

                        latch.countDown()

                    }

                    latch.await()

                    val statChart = tempStatChart

                    //set stat chart properties
                    with(statChart)
                    {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        layoutParams.height = dp(250)
                        setTouchEnabled(false)
                        setDrawGridBackground(true)
                        isDragEnabled = false
                        setPinchZoom(false)
                        isDoubleTapToZoomEnabled = false
                        description.isEnabled = false
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        xAxis.granularity = 1f
                        xAxis.labelRotationAngle = -45f
                        xAxis.labelCount = matches.size
                        xAxis.valueFormatter = object: ValueFormatter(){

                            override fun getFormattedValue(value: Float): String
                            {
                                return matches[value.toInt()].toString()
                            }
                        }
                        axisLeft.granularity = 1f
                        axisRight.isEnabled = false
                        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                        legend.yOffset = 15f
                    }
                    
                    //list of entries to show on the stat chart
                    val teamStats = ArrayList<Entry>()
                    val matchStats = ArrayList<Entry>()
                    val eventAvg = ArrayList<Entry>()
                    
                    //array to hold entries
                    val dataSets = ArrayList<ILineDataSet>()
                    
                    //data to hold data sets
                    var data: LineData
                    val filler = IFillFormatter { _, _ -> statChart.axisLeft.axisMinimum }
                    
                    //sets default data set properties
                    val formatDataSets = fun (dataSet: LineDataSet)
                    {
                        with(dataSet)
                        {
                            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                            setDrawValues(false)
                            lineWidth = 2f
                            circleRadius = 3f
                            setDrawCircleHole(false)
                            valueTextSize = 9f
                            setDrawFilled(true)
                            fillFormatter = filler
                        }
                        
                    }

                    val teamDataSet = LineDataSet(ArrayList<Entry>(), getString(R.string.team_average)).apply{
                        formatDataSets(this)
                        color = Color.parseColor("#03A9F4")
                        setCircleColor(Color.parseColor("#03A9F4"))
                        fillColor = Color.argb(49, 3, 169, 244)
                    }
                    

                    val matchDataSet = LineDataSet(ArrayList<Entry>(), getString(R.string.match_average)).apply{
                        formatDataSets(this)
                        color = Color.parseColor("#FF0000")
                        setCircleColor(Color.parseColor("#FF0000"))
                        fillColor = Color.argb(49, 244, 0, 0)
                    }
                    

                    val eventAvgDataSet = LineDataSet(ArrayList<Entry>(), getString(R.string.event_average)).apply{
                        formatDataSets(this)
                        color = Color.parseColor("#005685")
                        setDrawCircles(false)
                        setDrawCircleHole(false)
                        setDrawFilled(false)
                        enableDashedLine(25f, 20f, 1f)
                    }
                    

                    //add stat chart to the statcharts arraylist
                    statCharts.add(
                            //add the linear layout to the stat chart
                            LinearLayout(context).apply{
                                orientation = LinearLayout.VERTICAL
                                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                                setPadding(0, 0, 0, dp(8))

                                //add the textview with the title to the linear layout
                                addView(
                                        TextView(context).apply {
                                            text = scoutCardInfoKeyState[0].keyState
                                            setTypeface(null, Typeface.BOLD)
                                            gravity = Gravity.CENTER
                                            setTextColor(Color.BLACK)
                                        })

                                //add the spinner selector to the linear layout
                                addView(
                                        Spinner(context).apply {

                                            val spinnerArray = ArrayList<String>().apply {

                                                for(scoutCardInfo in scoutInfoKeyStates[scoutCardInfoKeyState[0].keyState]!!)
                                                    add(scoutCardInfo.keyName)
                                            }

                                            adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerArray)

                                            onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                                                override fun onNothingSelected(p0: AdapterView<*>?)
                                                {

                                                }

                                                //when new item selected, update the chart
                                                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long)
                                                {
                                                    //used to find min / max values
                                                    val chartPoints = ArrayList<Float>()

                                                    teamStats.clear()
                                                    matchDataSet.clear()
                                                    eventAvgDataSet.clear()

                                                    for (i in 0 until matches.size)
                                                    {
                                                        val teamStat = teamStatsHashMap[matches[i].toString()]!![scoutCardInfoKeyState[p2].toString()]!!.toFloat()
                                                        val matchStat = matchStatsHashMap[matches[i].toString()]!![scoutCardInfoKeyState[p2].toString()]!!.toDouble().toFloat()

                                                        chartPoints.add(teamStat)
                                                        chartPoints.add(matchStat)

                                                        teamStats.add(Entry(i.toFloat(), teamStat))
                                                        matchStats.add(Entry(i.toFloat(), matchStat))

                                                        eventAvg.add(Entry(i.toFloat(), eventStatsHashMap[scoutCardInfoKeyState[p2].toString()]!!.toFloat()))
                                                    }

                                                    teamDataSet.values = teamStats
                                                    matchDataSet.values = matchStats
                                                    eventAvgDataSet.values = eventAvg

                                                    dataSets.clear()
                                                    dataSets.add(teamDataSet)
                                                    dataSets.add(matchDataSet)
                                                    dataSets.add(eventAvgDataSet)

                                                    data = LineData(dataSets)

                                                    statChart.data = data
                                                    statChart.axisLeft.axisMinimum = Collections.min(chartPoints)
                                                    statChart.invalidate()
                                                }
                                            }
                                        }
                                )

                                addView(statChart)
                    })
                }
            }
        })

        loadStatsThread.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quick_stats, container, false)

        Thread(Runnable {
            loadStatsThread.join()

            context.runOnUiThread{
                for(statChart in statCharts)
                {
                    val chartParent = if(statChart.parent != null) statChart.parent as LinearLayout else null
                    if(chartParent != null && chartParent.childCount > 0)
                        chartParent.removeAllViews()

                    view.StatChartsLinearLayout.addView(statChart)
                }
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
