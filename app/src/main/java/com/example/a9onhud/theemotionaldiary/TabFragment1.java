package com.example.a9onhud.theemotionaldiary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TabFragment1 extends Fragment {
    private EventDatabase db ;

    private static ListView listView;
    private static List<Event> eventList;
    private static EventAdapter adapter;
    private GestureDetector gestureDetector;
    private int index;
    private String currentDate;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View tap1View = inflater.inflate(R.layout.tab_fragment_1, container, false);

        currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        db = EventDatabase.getEventDatabase(getActivity());
        eventList = db.daoAccess().getEventsByDate(currentDate);

        gestureDetector = new GestureDetector(getActivity(), new GestureDetector.OnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            /**
             * edit event information
             * @param e
             * @return
             */
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                final View dialog = getLayoutInflater().inflate(R.layout.add_story_dialog, null);

                final EditText storyEt = dialog.findViewById(R.id.storyEt);

                final int id = eventList.get(index).getId();
                String story = eventList.get(index).getStory();

                storyEt.setText(story);

                new AlertDialog.Builder(getActivity()).setView(dialog).setCancelable(false)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                View v = listView.getChildAt(index-listView.getFirstVisiblePosition());
                                Log.i("MyLog", "index: "+(index));

                                TextView storyTv = v.findViewById(R.id.storyTv);
                                TextView timeTv = v.findViewById(R.id.timeTv);
                                TextView emoTv = v.findViewById(R.id.emoTv);

                                storyTv.setText(storyEt.getText().toString());

                                RadioButton depressedRbtn = dialog.findViewById(R.id.depressedRbtn);
                                RadioButton sadRbtn = dialog.findViewById(R.id.sadRbtn);
                                RadioButton sosoRbtn = dialog.findViewById(R.id.sosoRbtn);
                                RadioButton goodRbtn = dialog.findViewById(R.id.goodRbtn);
                                RadioButton happyRbtn = dialog.findViewById(R.id.happyRbtn);

                                String feeling = "";
                                if (depressedRbtn.isChecked())
                                    feeling = "Depressed";
                                else if (sadRbtn.isChecked())
                                    feeling = "Sad";
                                else if (sosoRbtn.isChecked())
                                    feeling = "Soso";
                                else if (goodRbtn.isChecked())
                                    feeling = "Good";
                                else if (happyRbtn.isChecked())
                                    feeling = "Happy";

                                emoTv.setText(feeling);

                                String dateAndTime = eventList.get(index).getDate()+" "+eventList.get(index).getTime();
                                timeTv.setText(dateAndTime);


                                // update data in database
                                db.daoAccess().updateEvent(id, storyEt.getText().toString()
                                        , eventList.get(index).getFeeling());
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

                eventList = db.daoAccess().getEventsByDate(currentDate);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            /**
             * delete event
             * @param e
             */
            @Override
            public void onLongPress(MotionEvent e) {
                final int id = eventList.get(index).getId();

                new AlertDialog.Builder(getActivity()).setMessage("Do you want to delete this event?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db.daoAccess().deleteEvent(id);

                                // just add item to list because don't wanna load new data from db.
                                eventList.remove(index);
                                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        adapter = new EventAdapter(getActivity(), 0, eventList);

        TextView currentDateTv = tap1View.findViewById(R.id.currentDateTv);
        currentDateTv.setText(currentDate);

        listView = tap1View.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                index = listView.pointToPosition(0, (int) motionEvent.getY());
                if (index >=0 && index<eventList.size()) {
                    gestureDetector.onTouchEvent(motionEvent);
                }
                return false;
            }
        });

        // add new event
        FloatingActionButton addBtn = tap1View.findViewById(R.id.floatingActionButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View dialog = getLayoutInflater().inflate(R.layout.add_story_dialog, null);

                new AlertDialog.Builder(getActivity()).setView(dialog).setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                EditText storyEt = dialog.findViewById(R.id.storyEt);

                                RadioButton depressedRbtn = dialog.findViewById(R.id.depressedRbtn);
                                RadioButton sadRbtn = dialog.findViewById(R.id.sadRbtn);
                                RadioButton sosoRbtn = dialog.findViewById(R.id.sosoRbtn);
                                RadioButton goodRbtn = dialog.findViewById(R.id.goodRbtn);
                                RadioButton happyRbtn = dialog.findViewById(R.id.happyRbtn);

                                String feeling = "";
                                if (depressedRbtn.isChecked())
                                    feeling = "Depressed";
                                else if (sadRbtn.isChecked())
                                    feeling = "Sad";
                                else if (sosoRbtn.isChecked())
                                    feeling = "Soso";
                                else if (goodRbtn.isChecked())
                                    feeling = "Good";
                                else if (happyRbtn.isChecked())
                                    feeling = "Happy";

                                Event newEvent = new Event();
                                newEvent.setStory(storyEt.getText().toString());
                                newEvent.setDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                                newEvent.setTime(new SimpleDateFormat("HH:mm").format(new Date()));
                                newEvent.setFeeling(feeling);

                                db.daoAccess().insertEvent(newEvent);
                                // just add item to list because don't wanna load new data from db.
                                eventList.add(newEvent);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });

        return tap1View;
    }

}