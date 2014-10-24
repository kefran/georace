package com.utbm.georace.model;

import com.utbm.georace.tools.ISerializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jojo on 22/10/2014.
 */
public class Track implements ISerializable{
    //La valeur des TAG doit être indentique au colonne de la bdd
    final static public String TAG_TRACK_ID="id";
    final static public String TAG_TRACK_NAME="name";

    private int id;
    private String name;

    public Track(JSONObject json) {
        try {

            this.name(jsonObject.getString(TAG_TRACK_NAME));
            this.id(jsonObject.getInt(TAG_TRACK_ID));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(TAG_TRACK_ID,id);
            jsonObject.put(TAG_TRACK_NAME,name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Track fromJson(JSONObject jsonObject) {
        Track track = new Track();

        try {
            track.setName(jsonObject.getString(TAG_TRACK_NAME));
            track.setId(jsonObject.getInt(TAG_TRACK_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return track;
    }
}
