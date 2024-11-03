package com.example.cinemamanagerapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.NotificationResponse


class Notification_Adapter(private val notificationList: List<NotificationResponse>?) :
    BaseAdapter() {
    override fun getCount(): Int {
        return 7
        // if (notificationList == null) 0 else notificationList.size
    }

    override fun getItem(p0: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(p2?.context).inflate(R.layout.notification_message, null);
        return view
    }
}