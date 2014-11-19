package com.utbm.georace.tools;

import org.json.JSONObject;

/**
 * Created by jojo on 22/10/2014.
 */
public interface ISerializable<T> {

    public JSONObject toJson();
    public  boolean fromJson(JSONObject jsonObject);//Refresh the object value
}
