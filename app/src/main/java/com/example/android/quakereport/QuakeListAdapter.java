package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phartmann on 30/01/2018.
 */

public class QuakeListAdapter extends ArrayAdapter<QuakeList> {

    public QuakeListAdapter( @NonNull Context context, @NonNull ArrayList<QuakeList> quakeList ) {
        super(context, 0, quakeList);
    }

    @NonNull
    @Override
    public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent ) {
        View listViewItem = convertView;
        if (listViewItem == null){
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        QuakeList currentWord = getItem(position);

        TextView mag = (TextView) listViewItem.findViewById(R.id.mag_tv);
        mag.setText(String.valueOf(currentWord.getMag()));

        TextView local = (TextView) listViewItem.findViewById(R.id.local_tv);
        local.setText(currentWord.getLocal());

        TextView date = (TextView) listViewItem.findViewById(R.id.date_tv);
        date.setText(currentWord.getDate());

        return listViewItem;
    }
}
