package com.utbm.georace.model;

import com.utbm.georace.tools.ISerializable;

import org.json.JSONObject;

/**
 * Created by jojo on 29/10/2014.
 */
public class Checkpoint implements ISerializable {

    //La valeur des TAG doit être indentique au colonne de la bdd
    final static String TAG_CHECKPOINT_ID ="id";
    final static String TAG_CHECKPOINT_NAME ="name";
    final static String TAG_CHECKPOINT_LATITUDE="latitude";
    final static String TAG_CHECKPOINT_LONGITUDE="longitude";
    final static String TAG_CHECKPOINT_CREATOR="creator";


    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private User creator;




    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    public Object fromJson(JSONObject jsonObject) {
        return null;
    }
}
