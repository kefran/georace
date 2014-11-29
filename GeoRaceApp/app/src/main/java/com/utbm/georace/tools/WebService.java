package com.utbm.georace.tools;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.utbm.georace.model.Checkpoint;
import com.utbm.georace.model.Participation;
import com.utbm.georace.model.Race;
import com.utbm.georace.model.Team;
import com.utbm.georace.model.Track;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by jojo on 03/11/2014.
 */
//TODO Synchronization check
//The idea was to learn how to use android, so, full implementation of model in java, not in php

public class WebService {

    private static WebService instance = null;

    private DefaultHttpClient httpClient;
    private HttpParams httpParams;
    private HttpPost httpPost;

    private User userLogged;

    private TreeMap<Integer, User> users;//Key = user id Value = User Object
    private TreeMap<Integer, Race> races;//Key = race id Value = Race Object
    private TreeMap<Integer, Team> teams;//Key = teams id Value = Team Object
    private TreeMap<Integer, Track> tracks;

    private TreeMap<Pair<Integer,Integer>, Participation> participation;


    private WebService() {

        httpClient = new DefaultHttpClient();
        httpParams = new BasicHttpParams();
        httpPost = new HttpPost();
        users = new TreeMap<Integer, User>();
        races = new TreeMap<Integer, Race>();
        tracks = new TreeMap<Integer, Track>();
        participation = new TreeMap<Pair<Integer,Integer>, Participation>();
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

            httpPost.setURI(new URI(Config.Service.service_user));

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

        users = getUsers();//TODO check synchronisation with server data
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

    public User getUser(Integer id){
        if(userLogged==null)
        {
           Log.e("Web Service","User by id failed, no logged user");
           return null;
        }
        return users.get(id);

    }


    public TreeMap<Integer,Checkpoint> getCheckpointsTrack(Integer trackId){
            TreeMap<Integer,Checkpoint> cpList = new TreeMap<Integer, Checkpoint>();

        try
        {
            httpPost.setURI(new URI(Config.Service.service_checkpoints));
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("checkpoint",String.valueOf(trackId)));
            httpPost.setEntity(new UrlEncodedFormEntity(param));//Bind parameter to the query
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (isResponseOk(httpResponse)) {

                String responseString = getStringFromResponse(httpResponse);
                JSONArray checkpointList = new JSONArray(responseString);
                int size = checkpointList.length();
                Checkpoint buf=null;
                JSONObject jbuf=null;

                for(int i=0;i<size;i++)
                {
                    jbuf = checkpointList.getJSONObject(i);
                    buf= new Checkpoint(
                            jbuf.getInt(Checkpoint.TAG_CHECKPOINT_ID)
                            ,jbuf.getString(Checkpoint.TAG_CHECKPOINT_NAME)
                            ,jbuf.getLong(Checkpoint.TAG_CHECKPOINT_LATITUDE)
                            ,jbuf.getLong(Checkpoint.TAG_CHECKPOINT_LONGITUDE)
                            ,getUser(jbuf.getInt(Track.TAG_TRACK_CREATOR))
                    );
                    cpList.put(jbuf.getInt(Checkpoint.TAG_CHECKPOINT_ID),buf);

                }

                Log.d("WebService getTrackCheckpoints ", responseString);

            }

        }catch (Exception e){

            e.printStackTrace();
        }

        return  cpList;
    }

    public TreeMap<Integer,Track> getTracks(){
        TreeMap<Integer, Track> trackTreeMap = new TreeMap<Integer, Track>();

        try {

            httpPost.setURI(new URI(Config.Service.service_track));
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("track", "list"));
            httpPost.setEntity(new UrlEncodedFormEntity(param));//Bind parameter to the query
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (isResponseOk(httpResponse)) {

                String responseString = getStringFromResponse(httpResponse);
                JSONArray trackList = new JSONArray(responseString);
                int size = trackList.length();
                Track buf=null;


                for(int i=0;i<size;i++)
                {
                    JSONObject jbuf = trackList.getJSONObject(i);

                    buf =new Track(jbuf.getInt(Track.TAG_TRACK_ID)
                    ,jbuf.getString(Track.TAG_TRACK_NAME)
                    ,getCheckpointsTrack(jbuf.getInt(Track.TAG_TRACK_ID)));
                    trackTreeMap.put(jbuf.getInt(Track.TAG_TRACK_ID),buf);
                }
                Log.d("WebService getTracks", responseString);
            }
        } catch (Exception e) {

            Log.d("WebService getTracks", "Echec de la récuperation des données depuis le serveur");
            e.printStackTrace();

        }
        return trackTreeMap;

    }

    public Track getTrack(Integer trackid){
        if(tracks==null)tracks=getTracks();

        return tracks.get(trackid);


    }

    public TreeMap<Integer,Race> getRaces(){

       TreeMap<Integer, Race> raceTreeMap = new TreeMap<Integer, Race>();
       if(tracks.isEmpty())tracks = getTracks();
       if(users.isEmpty())users =getUsers();

        try {

            httpPost.setURI(new URI(Config.Service.service_race));
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("race", "list"));
            httpPost.setEntity(new UrlEncodedFormEntity(param));//Bind parameter to the query
            HttpResponse httpResponse = httpClient.execute(httpPost);



            if (isResponseOk(httpResponse)) {

                String responseString = getStringFromResponse(httpResponse);
                JSONArray raceList = new JSONArray(responseString);
                int size = raceList.length();
                Race buf=null;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

                for(int i=0;i<size;i++)
                {

                    JSONObject jbuf = raceList.getJSONObject(i);
                    buf =new Race(jbuf.getInt(Race.TAG_RACE_ID)
                          ,sdf.parse(jbuf.getString(Race.TAG_RACE_START))
                          ,sdf.parse(jbuf.getString(Race.TAG_RACE_END))
                          ,getTrack(jbuf.getInt(Race.TAG_RACE_TRACK))
                          ,getUser(jbuf.getInt(Race.TAG_RACE_ORGANIZER))
                    );

                    races.put(buf.getId(), buf);
                    raceTreeMap.put(buf.getId(),buf);
                }

                Log.d("WebService getRaces", responseString);

            }

        } catch (Exception e) {

            Log.d("WebService getRaces", "Echec de la récuperation des données depuis le serveur");
            e.printStackTrace();

        }
        return raceTreeMap;

    }

    public Race getRace(Integer raceid){

        if(races==null)races = getRaces();

        return races.get(raceid);
    }

    public TreeMap<Pair<Integer,Integer>,Participation>  getParticipation(){


        TreeMap<Pair<Integer,Integer>, Participation> participationTreeMap = new TreeMap<Pair<Integer,Integer>, Participation>();

        try {

            httpPost.setURI(new URI(Config.Service.service_participation));
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("participation", "1"));
            httpPost.setEntity(new UrlEncodedFormEntity(param));//Bind parameter to the query
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (isResponseOk(httpResponse)) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                String responseString = getStringFromResponse(httpResponse);
                JSONArray participationList = new JSONArray(responseString);
                int size = participationList.length();
                Participation buf=null;


                for(int i=0;i<size;i++)
                {
                    JSONObject jbuf = participationList.getJSONObject(i);
                    buf = new Participation(getUser(jbuf.getInt(Participation.TAG_PARTICIPATION_USER))
                            ,getRace(jbuf.getInt(Participation.TAG_PARTICIPATION_RACE))
                            ,sdf.parse(jbuf.getString(Participation.TAG_PARTICIPATION_START))
                            ,sdf.parse(jbuf.getString(Participation.TAG_PARTICIPATION_END))
                            ,jbuf.getInt(Participation.TAG_PARTICIPATION_FINISHED)
                    );

                    participationTreeMap.put(
                            new Pair<Integer, Integer>(jbuf.getInt(Participation.TAG_PARTICIPATION_USER)
                            ,jbuf.getInt(Participation.TAG_PARTICIPATION_RACE))
                            ,buf);

                }

                Log.d("WebService getParticipation", responseString);

            }

        } catch (Exception e) {

            Log.d("WebService getParticipation", "Echec de la récuperation des données depuis le serveur");
            e.printStackTrace();

        }
        return participationTreeMap;


    }

}
