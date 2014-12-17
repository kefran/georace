package com.utbm.georace.adapter;

/**
 * Created by Franck on 27/11/2014.
 */

import android.content.Context;
import android.provider.Telephony;
import android.util.Log;
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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeSet;

public class ParticipationAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final TreeSet<Participation> values;
    private final SimpleDateFormat sdf;

    public ParticipationAdapter(Context c,TreeSet<Participation> v){
        mInflater = LayoutInflater.from(c);
        values =v;
        sdf= new SimpleDateFormat("dd-MM-yyyy");
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Participation getItem(int i) {
        int z=0;

        for(Participation p : values){
            if(z==i)return p;
            z+=1;
        }
        Log.e("ADAPTER PARTICIPATION GETITEM","No item found for id :"+String.valueOf(i)+"Max reached : "+String.valueOf(z));
        return null;
    }

    @Override
    public long getItemId(int i) {

        int z=0;
        for(Participation p : values)
        {
            if(z==i)return z;
            z+=1;
        }
        Log.e("Participation ADATAPTER : ","Pas d'objet trouvé à cette id"+String.valueOf(i));
        return -1;
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

        holder.tvNameRace.setText("course du "+sdf.format(getItem(i).getStart()));

        return view;
    }

    static class ViewHolder{
            TextView tvNameRace;
            TextView tvInfoRace;
            ImageView ivIconRace;
    }
}