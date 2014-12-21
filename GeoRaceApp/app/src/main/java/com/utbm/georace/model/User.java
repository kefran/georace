package com.utbm.georace.model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.utbm.georace.tools.ISerializable;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jojo on 22/10/2014.
 */
public class User  implements ISerializable
                            ,Comparable<User> {

    //La valeur des TAG doit être indentique au colonne de la bdd
    final static public String TAG_USER_ID = "id";
    final static public String TAG_USER_LOGIN = "login";
    final static public String TAG_USER_PASSWORD = "password";
    final static public String TAG_USER_FIRSTNAME = "firstname";
    final static public String TAG_USER_LASTNAME = "lastname";
    final static public String TAG_USER_EMAIL = "email";
    final static public String TAG_USER_LATITUDE = "latitude";
    final static public String TAG_USER_LONGITUDE = "longitude";


    private int id=-1;
    private String loginName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private double latitude;
    private double longitude;
    private boolean modified; //use to synchronize with server



    public User() {
        id=-1;
    };
    public User(int i) {
        id=i;
    };

    public User(int id, String loginName, String password, String firstName, String lastName, String email, double latitude, double longitude) {
        this.id = id;
        this.loginName = loginName;
        this.setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public User(String loginName, String password, String firstName, String lastName, String email, double latitude, double longitude) {
        this.loginName = loginName;
        this.setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /* Utilisation de ce constructeur car j'ai pas encore implémenté la recupération de la position par défaut de l'utilisateur et la fonction sha1 de jo est nimp*/
    public User(String loginName, String password, String firstName, String lastName, String email) {
        this.loginName = loginName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.latitude = 1.123;
        this.longitude = 1.123;
    }

    public User(JSONObject jsonObject) {

        try {
            id = jsonObject.getInt(TAG_USER_ID);
            loginName =jsonObject.getString(TAG_USER_LOGIN);
            firstName= jsonObject.getString(TAG_USER_FIRSTNAME);
           // password = jsonObject.getString(TAG_USER_PASSWORD); password is send, not received
            lastName = jsonObject.getString(TAG_USER_LASTNAME);
            this.setPosition(jsonObject.getDouble(TAG_USER_LATITUDE), jsonObject.getDouble(TAG_USER_LONGITUDE));
            email = jsonObject.getString(TAG_USER_EMAIL);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        modified=true;
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(password.getBytes());
            this.password = new String( md.digest());
            modified=true;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
        modified=true;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        modified=true;
        this.firstName = firstName;

    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        modified=true;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        modified=true;
        this.email = email;
    }

    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public void setPosition(double latitude, double longitude) {
        modified=true;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(TAG_USER_ID, id);
            jsonObject.put(TAG_USER_LOGIN, loginName);
            jsonObject.put(TAG_USER_FIRSTNAME, firstName);
            jsonObject.put(TAG_USER_LASTNAME, lastName);
         //   jsonObject.put(TAG_USER_PASSWORD,  password);
            jsonObject.put(TAG_USER_EMAIL, email);
            jsonObject.put(TAG_USER_LATITUDE, latitude);
            jsonObject.put(TAG_USER_LONGITUDE, longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public boolean fromJson(JSONObject jsonObject) {


        try {
            this.setId(jsonObject.getInt(TAG_USER_ID));
            this.setLoginName(jsonObject.getString(TAG_USER_LOGIN));
            this.setFirstName(jsonObject.getString(TAG_USER_FIRSTNAME));
            //user.setPassword(jsonObject.getString(TAG_USER_PASSWORD)); password is send , not received
            this.setLastName(jsonObject.getString(TAG_USER_LASTNAME));
            this.setPosition(jsonObject.getDouble(TAG_USER_LATITUDE), jsonObject.getDouble(TAG_USER_LONGITUDE));
            this.setEmail(jsonObject.getString(TAG_USER_EMAIL));

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

    @Override
    public int compareTo(User user) {

        if(user==null)throw new NullPointerException();

        if(id==user.getId())
            return 0;

        if(id>user.getId())
            return -1;

            return 1;

    }

    @Override
    public String toString(){

        return new String(this.getLoginName());
    }


}
