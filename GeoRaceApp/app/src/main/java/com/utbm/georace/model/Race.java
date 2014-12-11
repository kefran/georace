package com.utbm.georace.model;

import com.utbm.georace.tools.ISerializable;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jojo on 22/10/2014.
 *
 *
 */


public class Race implements ISerializable{
    //La valeur des TAG doit être indentique au colonne de la bdd
    final static public String TAG_RACE_ID="id";
    final static public String TAG_RACE_START="date_start";
    final static public String TAG_RACE_END="date_end";
    final static public String TAG_RACE_TRACK="track";
    final static public String TAG_RACE_ORGANIZER="organizer";

    private int id;
    private Date date_start;
    private Date date_end;
    private Track track;
    private User organizer;

    public Race() {
    }

    ;

    public Race(int id, Date date_start, Date date_end, Track track, User organizer) {
        this.id = id;
        this.date_start = date_start;
        this.date_end = date_end;
        this.track = track;
        this.organizer = organizer;
    }

    public Race(JSONObject json) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy kk:mm:ss");

            this.setId(json.getInt(TAG_RACE_ID));
            this.setDate_start(sdf.parse(json.getString(TAG_RACE_START)));
            this.setDate_end(sdf.parse(json.getString(TAG_RACE_END)));
            this.setOrganizer(new User(json.getJSONObject(TAG_RACE_ORGANIZER)));
            this.setTrack(new Track(json.getJSONObject(TAG_RACE_TRACK)));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate_start() {
        return date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    @Override
    public Race fromJson(JSONObject jsonObject) {
        Race race = new Race();
        try {

            race.setId(jsonObject.getInt(TAG_RACE_ID));
            race.setOrganizer(new User(jsonObject.getJSONObject(TAG_RACE_ORGANIZER)));
            race.setTrack(new Track(jsonObject.getJSONObject(TAG_RACE_TRACK)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return race;
    }

    @Override
    public JSONObject toJson() {

        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");

        try {

            jsonObject.put(TAG_RACE_ID,id);
            jsonObject.put(TAG_RACE_START, sdf.format(date_start));
            jsonObject.put(TAG_RACE_END, sdf.format(date_end));
            jsonObject.put(TAG_RACE_TRACK, track.toJson());
            jsonObject.put(TAG_RACE_ORGANIZER,organizer.toJson());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
