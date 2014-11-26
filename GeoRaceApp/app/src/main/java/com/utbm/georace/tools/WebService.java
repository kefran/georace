package com.utbm.georace.tools;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.utbm.georace.model.Race;
import com.utbm.georace.model.Team;
import com.utbm.georace.model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jojo on 03/11/2014.
 */
//TODO Synchronization check
//Use only in ASyncTask !
public class WebService {

    private static WebService instance = null;

    private DefaultHttpClient httpClient;
    private HttpParams httpParams;
    private HttpPost httpPost;

    private User userLogged;

    private TreeMap<Integer, User> users;//Key = user id Value = User Object
    private TreeMap<Integer, Race> races;//Key = race id Value = Race Object
    private TreeMap<Integer, Team> teams;//Key = teams id Value = Team Object




    private WebService() {

        httpClient = new DefaultHttpClient();
        httpParams = new BasicHttpParams();
        httpPost = new HttpPost();
        users = new TreeMap<Integer, User>();
        races = new TreeMap<Integer, Race>();
        teams = new TreeMap<Integer, Team>();

        userLogged=null;

        HttpConnectionParams.setConnectionTimeout(httpParams, Config.Http.connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParams, Config.Http.socketTimeout);
        httpClient.setParams(httpParams);

    }

    public static WebService getInstance() {

        if (instance == null) instance = new WebService();

        return instance;

    }

    //check about 200 ret code, and correct datatype
    private boolean isResponseOk(HttpResponse resp) {

        HttpEntity httpEntity = resp.getEntity();
        StatusLine statusLine = resp.getStatusLine();
        Log.d("STATUS LINE", Integer.toString(statusLine.getStatusCode()));

        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
            Log.e("WebService", "Connection problem");
            return false;
        }

        String content_type = resp.getFirstHeader("content-type").getValue();

        if (!content_type.contains("application/json")) {
            Log.e("WebService", "Server Problème");
            return false;
        }


        return true;
    }


    private String getStringFromResponse(HttpResponse response) {
        HttpEntity httpEntity = response.getEntity();

        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            httpEntity.writeTo(out);
            out.close();
            return out.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    //Interroge le webservice,
    // Si l'utilisateur est autorisé la fonction retourne l'obj User autorisé,
    // Sinon null
    public User getLogin(String name, String password) {

        try {

            httpPost.setURI(new URI(Config.Service.service_login));//connection au service gerant le login

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("userLogin", name));
            param.add(new BasicNameValuePair("userPassword", password));

            Log.i("Login attempt :", name);

            httpPost.setEntity(new UrlEncodedFormEntity(param));
            HttpResponse response = httpClient.execute(httpPost);


            if (isResponseOk(response)) {

                String respString = getStringFromResponse(response);
                Log.d("REQUEST RESULTS ", respString);

                JSONObject jsonObject = new JSONObject(respString);
                if (jsonObject.isNull("Status")) {
                    User user = new User(jsonObject);
                    Log.d("JSON TEXT", jsonObject.toString());
                    Log.d("JSON to JAVA object", user.getLoginName());

                    users.put(user.getId(), user);
                    userLogged = user;
                    return user;

                } else {

                    Log.w("FAILED LOGIN ATTEMPT", "Accès non autorisé");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public TreeMap<Integer, User> getUsers() {

        TreeMap<Integer, User> userTreeMap = new TreeMap<Integer, User>();

        try {

            httpPost.setURI(new URI(Config.Service.service_data));

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("user", "list"));
            httpPost.setEntity(new UrlEncodedFormEntity(param));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (isResponseOk(httpResponse)) {

                String responseString = getStringFromResponse(httpResponse);
                JSONArray userList = new JSONArray(responseString);
                int size = userList.length();
                User buf=null;


                for(int i=0;i<size;i++)
                {
                    buf =new User(userList.getJSONObject(i));
                    users.put(buf.getId(),buf);
                    userTreeMap.put(buf.getId(),buf);
                }

                Log.d("WebService getUsers", responseString);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userTreeMap;
    }

    public TreeMap<Float,User> getUsersByDistance(){


        if(userLogged==null){
           Log.e("Web Service","User by distance failed, no logged user");
            return null;

        }
        users = getUsers();
        TreeMap<Float,User> userDistance = new TreeMap<Float, User>();//Key Distance with the logged user

        LatLng userLoggedLatLng = userLogged.getPosition();

        LatLng bufLatLng = null;
        Location bufLocation =new Location("User to test");

        Location userLoggedLocation = new Location("userLoggedPosition");
        userLoggedLocation.setLatitude(userLoggedLatLng.latitude);
        userLoggedLocation.setLongitude(userLoggedLatLng.longitude);


        //then the magic occurs , sort the user by distance with the logged one
        for(Map.Entry<Integer,User> u : users.entrySet()){

            User bufUser = u.getValue();
            bufLatLng = bufUser.getPosition();
            bufLocation.setLongitude(bufLatLng.longitude);
            bufLocation.setLatitude(bufLatLng.latitude);

            userDistance.put(userLoggedLocation.distanceTo(bufLocation),bufUser);
            Log.d("Distance to ", String.valueOf(userLoggedLocation.distanceTo(bufLocation))+"User"+bufUser.getLoginName());

        }


        return userDistance;
    }


}
