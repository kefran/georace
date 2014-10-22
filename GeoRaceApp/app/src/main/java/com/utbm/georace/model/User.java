package com.utbm.georace.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jojo on 22/10/2014.
 */
public class User implements ISerializable {

    final static String TAG_ID = "id";
    final static String TAG_LOGIN = "login";
    final static String TAG_PASSWORD = "password";
    final static String TAG_FIRSTNAME = "firstname";
    final static String TAG_LASTNAME = "lastname";
    final static String TAG_EMAIL = "email";
    final static String TAG_LATITUDE = "latitude";
    final static String TAG_LONGITUDE = "longitude";


    private int id;
    private String loginName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private double latitude;
    private double longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public void setPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(TAG_ID, id);
            jsonObject.put(TAG_LOGIN, loginName);
            jsonObject.put(TAG_FIRSTNAME, firstName);
            jsonObject.put(TAG_LASTNAME, lastName);
            jsonObject.put(TAG_PASSWORD, password);
            jsonObject.put(TAG_EMAIL, email);
            jsonObject.put(TAG_LATITUDE, latitude);
            jsonObject.put(TAG_LONGITUDE, longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    public User fromJson(JSONObject jsonObject) {

        User user = new User();
        try {
            user.setId(jsonObject.getInt(TAG_ID));
            user.setLoginName(jsonObject.getString(TAG_LOGIN));
            user.setFirstName(jsonObject.getString(TAG_FIRSTNAME));
            user.setPassword(jsonObject.getString(TAG_PASSWORD));
            user.setLastName(jsonObject.getString(TAG_LASTNAME));
            user.setPosition(jsonObject.getDouble(TAG_LATITUDE), jsonObject.getDouble(TAG_LONGITUDE));
            user.setEmail(jsonObject.getString(TAG_EMAIL));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return user;
    }
}
