package com.example.a9onhud.theemotionaldiary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        Drawable drawable = null;

        if (event.getFeeling().equals("Depressed")) {
            drawable = getContext().getResources().getDrawable(R.drawable.ic_action_depressed, getContext().getTheme());
        } else if (event.getFeeling().equals("Sad")) {
            drawable = getContext().getResources().getDrawable(R.drawable.ic_action_sad, getContext().getTheme());
        } else if (event.getFeeling().equals("Soso")) {
            drawable = getContext().getResources().getDrawable(R.drawable.ic_action_soso, getContext().getTheme());
        } else if (event.getFeeling().equals("Good")) {
            drawable = getContext().getResources().getDrawable(R.drawable.ic_action_good, getContext().getTheme());
        } else if (event.getFeeling().equals("Happy")) {
            drawable = getContext().getResources().getDrawable(R.drawable.ic_action_happy, getContext().getTheme());
        }
        holder.emoIv.setImageDrawable(drawable);

        return convertView;
    }

    public class ViewHolder {
        TextView timeTv, storyTv;
        ImageView emoIv;
        public ViewHolder(View view) {
            timeTv = view.findViewById(R.id.timeTv);
            storyTv = view.findViewById(R.id.storyTv);
            emoIv = view.findViewById(R.id.emoIv);
        }
    }
}