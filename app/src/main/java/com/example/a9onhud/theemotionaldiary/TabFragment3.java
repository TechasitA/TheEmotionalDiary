package com.example.a9onhud.theemotionaldiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabFragment3 extends Fragment {
    private EventDatabase db;
    private String currentMonthAndYear;
    private int daysInMonth;

    private GraphView graph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View tap3View = inflater.inflate(R.layout.tab_fragment_3, container, false);

        db = EventDatabase.getEventDatabase(getActivity());
        currentMonthAndYear = new SimpleDateFormat("MM/yyyy").format(new Date());

        double[] averages = calAverageFeeling();

        graph = tap3View.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        int month = Integer.parseInt(currentMonthAndYear.split("/")[0]);
        int year = Integer.parseInt(currentMonthAndYear.split("/")[1]);
        daysInMonth = month == 2 ?
                28 + (year % 4 == 0 ? 1:0) - (year % 100 == 0 ? (year % 400 == 0 ? 0 : 1) : 0) :
                31 - (month-1) % 7 % 2;

        double x = 1, y;
        for (; x<=daysInMonth; x++) {

            y = averages[((int)x)-1];
            if (y != -1)
                series.appendData(new DataPoint(x, y), true, 31);
        }

        graph.getViewport().setMaxX(33);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(4);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);

        graph.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setVerticalLabels(new String[] {"Depressed", "Sad", "Soso", "Good", "Happy"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        int[] feelings = getCurrentFeelingTotal();
        TextView depressedTotalTv = tap3View.findViewById(R.id.depressedTotalTv);
        TextView sadTotalTv = tap3View.findViewById(R.id.sadTotalTv);
        TextView sosoTotalTv = tap3View.findViewById(R.id.sosoTotalTv);
        TextView goodTotalTv = tap3View.findViewById(R.id.goodTotalTv);
        TextView happyTotalTv = tap3View.findViewById(R.id.happyTotalTv);

        depressedTotalTv.setText(Integer.toString(feelings[0]));
        sadTotalTv.setText(Integer.toString(feelings[1]));
        sosoTotalTv.setText(Integer.toString(feelings[2]));
        goodTotalTv.setText(Integer.toString(feelings[3]));
        happyTotalTv.setText(Integer.toString(feelings[4]));

        return tap3View;
    }

    public double[] calAverageFeeling() {
        double[] averages = new double[31];
        for (int i=1; i<=31; i++) {
            List<Event> eventList = db.daoAccess().getEventsByDate(String.format("%02d/%s", i, currentMonthAndYear));

            float total = 0;
            for (Event e: eventList) {
                if (e.getFeeling().equals("Depressed"))
                    total += 0;
                else if (e.getFeeling().equals("Sad"))
                    total += 1;
                else if (e.getFeeling().equals("Soso"))
                    total += 2;
                else if (e.getFeeling().equals("Good"))
                    total += 3;
                else if (e.getFeeling().equals("Happy"))
                    total += 4;
            }

            if (!eventList.isEmpty())
                averages[i-1] = total/eventList.size();
            else
                averages[i-1] = -1;
        }

        return averages;
    }

    private int[] getCurrentFeelingTotal() {
        List<Event> currentMonthEvents = new ArrayList<>();
        for (int i=1; i<=daysInMonth; i++) {
            currentMonthEvents.addAll(db.daoAccess().getEventsByDate(String.format("%02d/%s", i, currentMonthAndYear)));
        }
        int[] feelings = new int[5];

        for (Event event: currentMonthEvents) {
            if (event.getFeeling().equals("Depressed"))
                feelings[0]++;
            else if (event.getFeeling().equals("Sad"))
                feelings[1]++;
            else if (event.getFeeling().equals("Soso"))
                feelings[2]++;
            else if (event.getFeeling().equals("Good"))
                feelings[3]++;
            else if (event.getFeeling().equals("Happy"))
                feelings[4]++;
        }
        return feelings;
    }
}
