package com.utbm.georace.adapter;

/**
 * Created by Franck on 27/11/2014.
 */

import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.utbm.georace.R;

public class participationArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    /*
        TODO:
        changer le parametre du constructeur
        le string devient un array de participations
        Participation[]
     */
    public participationArrayAdapter(Context context, String[] values) {
        super(context, R.layout.participation_row_layout, values);
        this.context = context;
        this.values = values;
    }

    /*
       TODO:
       Après changement du paramettre du constructeur on change l'affectation des valeurs ici
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.participation_row_layout, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.nameRowRaceActivity);
        TextView textView2 = (TextView) rowView.findViewById(R.id.infoRowRaceActivity);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iconRowRaceActivity);
        textView1.setText(values[position]);
        textView2.setText(values[position]);
        String s = values[position];
        imageView.setImageResource(R.drawable.ic_map);
        return rowView;
    }
}