package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by phartmann on 30/01/2018.
 */

public class QuakeListAdapter extends ArrayAdapter<QuakeList> {

    Date dateObj;
    String splitPlace[];

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
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        double magValue = currentWord.getMag();
        mag.setText(decimalFormat.format(magValue));

        String place = currentWord.getLocal();
        TextView localOf = (TextView) listViewItem.findViewById(R.id.localOf_tv);
        if (place.contains(" of ")) {
            splitPlace = place.split(" of ");
            localOf.setText(splitPlace[0].concat(" of"));
        } else {
            localOf.setText(getContext().getText(R.string.near_of));
        }


        TextView local = (TextView) listViewItem.findViewById(R.id.local_tv);
        local.setText(splitPlace[1]);

        dateObj = new Date(currentWord.getTimeInMilliseconds());

        TextView date = (TextView) listViewItem.findViewById(R.id.date_tv);
        String formattedDate = formatDate(dateObj);
        date.setText(formattedDate);

        TextView hour = (TextView) listViewItem.findViewById(R.id.hour_tv);
        String formattedHour = formatTime(dateObj);
        hour.setText(formattedHour);

        // Configure a cor de fundo apropriada no círculo de magnitude.
        // Busque o fundo do TextView, que é um GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();

        // Obtenha a cor de fundo apropriada, baseada na magnitude do terremoto atual
        int magnitudeColor = getMagnitudeColor(currentWord.getMag());

        // Configure a cor no círculo de magnitude
        magnitudeCircle.setColor(magnitudeColor);

        return listViewItem;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
    /**
     * Retorna a data string formatada (i.e. "Mar 3, 1984") de um objeto Date.
     */
    private String formatDate(Date dateObj) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObj);
    }

    /**
     * Retorna a data string formatada (i.e. "4:30 PM") de um objeto Date.
     */
    private String formatTime(Date dateObj) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
        return timeFormat.format(dateObj);
    }
}
