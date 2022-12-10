package com.example.timezoneapp

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var timeZoneAdapter: TimeZoneAdapter? = null
    var timezonelist: MutableList<TimeZoneData>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timezonelist = ArrayList()
        timeZoneAdapter = TimeZoneAdapter(this, R.layout.timezoneview, timezonelist)
        val lv = findViewById<ListView>(R.id.list)
        lv.adapter = timeZoneAdapter
    }

    @SuppressLint("SimpleDateFormat")
    override fun onStart() {
        super.onStart()
        val listItems = TimeZone.getAvailableIDs()
        var timeZone: TimeZone?
        val format = SimpleDateFormat("EEE, dddd, yyyy h:mm a")
        val now = Date()
        for (listItem in listItems) {
            timeZone = TimeZone.getTimeZone(listItem)
            format.setTimeZone(timeZone)
            timezonelist!!.add(TimeZoneData(getDisplayName(listItem), format.format(now)))
        }
    }

    private fun getDisplayName(timezonename: String): String {
        var displayname = timezonename
        val sep = timezonename.indexOf('/')
        if (-1 != sep) {
            displayname = timezonename.substring(0, sep) + " , " + timezonename.substring(sep + 1)
            displayname = displayname.replace("_", "")
        }
        return displayname
    }

    inner class TimeZoneAdapter(
        context: Context?,
        textViewResourceId: Int,
        objects: List<TimeZoneData>?
    ) : ArrayAdapter<TimeZoneData?>(
        context!!, textViewResourceId, objects!!) {
        var objects: List<TimeZoneData>? = null
        override fun getCount(): Int {
            return if (null != objects) objects!!.size else 0
        }

        override fun getItem(position: Int): TimeZoneData? {
            return if (null != objects) objects!![position] else null
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("InflateParams")
        override fun getView(position: Int, convertview: View?, parent: ViewGroup): View {
            var view = convertview
            if (null == view) {
                val vi =
                    this@MainActivity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = vi.inflate(R.layout.timezoneview, null)
            }
            val data = objects!![position]
            val textName = view!!.findViewById<TextView>(R.id.timezone_name)
            val textTime = view.findViewById<TextView>(R.id.timezone_time)
            textName.text = data.name
            textTime.text = data.time
            return view
        }
    }
    private fun SimpleDateFormat.setTimeZone(timeZone: TimeZone?) {
        
    }
}