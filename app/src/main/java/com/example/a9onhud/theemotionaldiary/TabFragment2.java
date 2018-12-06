package com.example.a9onhud.theemotionaldiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TabFragment2 extends Fragment {
    private EventDatabase db ;

    private CalendarView calendarView;
    private static ListView listView;
    private static List<Event> eventList;
    private static EventAdapter adapter;
    private String pickedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View tap2View = inflater.inflate(R.layout.tab_fragment_2, container, false);

        pickedDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        calendarView = tap2View.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                pickedDate = String.format("%02d/%02d/%4d", dayOfMonth, month+1,year);
                Log.i("MyLog", "pickedDate:"+pickedDate);

                eventList = db.daoAccess().getEventsByDate(pickedDate);
                Log.i("MyLog", "onSelectedDayChange, size: " + eventList.size());
                adapter = new EventAdapter(getActivity(), 0, eventList);

                listView = tap2View.findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }
        });

        db = EventDatabase.getEventDatabase(getActivity());
        eventList = db.daoAccess().getEventsByDate(pickedDate);
        adapter = new EventAdapter(getActivity(), 0, eventList);

        listView = tap2View.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        return tap2View;
    }
}
