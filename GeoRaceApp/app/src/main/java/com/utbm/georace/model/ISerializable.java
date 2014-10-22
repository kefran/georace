package com.utbm.georace.model;

import org.json.JSONObject;

/**
 * Created by jojo on 22/10/2014.
 */
public interface ISerializable<T> {

    public String toJson();

    public T fromJson(JSONObject jsonObject);
}
