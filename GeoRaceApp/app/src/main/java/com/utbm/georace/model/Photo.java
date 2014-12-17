package com.utbm.georace.model;

import com.utbm.georace.tools.ISerializable;

import org.json.JSONObject;

/**
 * Created by jojo on 29/10/2014.
 */
public class Photo {


    final static public String TAG_PHOTO_ID="id";



    private int id;


    public Photo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String getTagPhotoId() {
        return TAG_PHOTO_ID;
    }
}
