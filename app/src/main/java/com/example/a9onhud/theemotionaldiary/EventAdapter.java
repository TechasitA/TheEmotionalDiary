package com.example.a9onhud.theemotionaldiary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(Context context, int resource, List<Event> objects) {
        super(context, R.layout.list_view_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Event event = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(MyApp.getContext()).inflate(R.layout.list_view_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.timeTv.setText(event.getDate() + " " + event.getTime());
        holder.storyTv.setText(event.getStory());
        holder.emoTv.setText(event.getFeeling());
        return convertView;
    }

    public class ViewHolder {
        TextView timeTv, storyTv, emoTv;
        public ViewHolder(View view) {
            timeTv = view.findViewById(R.id.timeTv);
            storyTv = view.findViewById(R.id.storyTv);
            emoTv = view.findViewById(R.id.emoTv);
        }
    }
}