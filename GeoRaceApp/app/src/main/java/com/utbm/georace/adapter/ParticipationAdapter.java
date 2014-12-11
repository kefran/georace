package com.utbm.georace.adapter;

/**
 * Created by Franck on 27/11/2014.
 */

import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
        import android.widget.TextView;

import com.utbm.georace.R;
import com.utbm.georace.model.Participation;
import com.utbm.georace.model.Track;

import java.util.Map;
import java.util.TreeSet;

public class ParticipationAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final TreeSet<Participation> values;


    public ParticipationAdapter(Context c,TreeSet<Participation> v){
        mInflater = LayoutInflater.from(c);
        values = v;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Participation getItem(int i) {
        int z=0;
        for(Participation p : values)
        {
            if(z==i)return p;
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view==null)
        {
            view= mInflater.inflate(R.layout.participation_row_layout,null);
            holder = new ViewHolder();
            holder.tvNameRace = (TextView) view.findViewById(R.id.nameRowRaceActivity);
            holder.tvInfoRace = (TextView) view.findViewById(R.id.infoRowRaceActivity);
            view.setTag(holder);


        }else {

            holder = (ViewHolder)  view.getTag();

        }

        holder.tvInfoRace.setText(getItem(i).getRace().getTrack().getName());
        holder.tvNameRace.setText(getItem(i).getRace().getTrack().getName());


        return view;
    }

    static class ViewHolder{
            TextView tvNameRace;
            TextView tvInfoRace;
            ImageView ivIconRace;

    }
}