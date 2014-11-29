package com.utbm.georace.model;

import com.utbm.georace.tools.ISerializable;

import org.json.JSONObject;

/**
 * Created by jojo on 29/10/2014.
 */
public class Checkpoint implements ISerializable {

    //La valeur des TAG doit être indentique au colonne de la bdd
    final static public String TAG_CHECKPOINT_ID ="id";
    final static public String TAG_CHECKPOINT_NAME ="name";
    final static public String TAG_CHECKPOINT_LATITUDE="latitude";
    final static public String TAG_CHECKPOINT_LONGITUDE="longitude";
    final static public String TAG_CHECKPOINT_CREATOR="creator";


    private int id;
    private String name;
    private double latitude;
    private double longitude;

    public Checkpoint(int id, String name, double latitude, double longitude, User creator) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creator = creator;
    }

    private User creator;


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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
    Checkpoint(JSONObject jsonObject){

        try{

            this.id=jsonObject.getInt(TAG_CHECKPOINT_ID);
            this.name=jsonObject.getString(TAG_CHECKPOINT_NAME);
            this.latitude=jsonObject.getDouble(TAG_CHECKPOINT_LATITUDE);
            this.longitude=jsonObject.getDouble(TAG_CHECKPOINT_LONGITUDE);
            this.creator=new User(jsonObject.getJSONObject(TAG_CHECKPOINT_CREATOR));//TODO integrity check with WS cache

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public JSONObject toJson() {
       JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.put(TAG_CHECKPOINT_ID, this.id);
            jsonObject.put(TAG_CHECKPOINT_NAME,this.name);
            jsonObject.put(TAG_CHECKPOINT_LATITUDE,this.latitude);
            jsonObject.put(TAG_CHECKPOINT_LONGITUDE,this.longitude);
            jsonObject.put(TAG_CHECKPOINT_CREATOR,this.creator.toJson());
            return jsonObject;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean fromJson(JSONObject jsonObject) {

        try
        {
            this.setId(jsonObject.getInt(TAG_CHECKPOINT_ID));
            this.setName(jsonObject.getString(TAG_CHECKPOINT_NAME));
            this.setCreator(new User(jsonObject.getJSONObject(TAG_CHECKPOINT_CREATOR)));
            this.setLatitude(jsonObject.getDouble(TAG_CHECKPOINT_LATITUDE));
            this.setLongitude(jsonObject.getDouble(TAG_CHECKPOINT_LONGITUDE));

        }catch (Exception e){
            e.printStackTrace();
            return false;

        }


        return true;
    }
}
