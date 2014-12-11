/*

 TODO : JOHANNY
    getMyLastParticipation()
        return array of Participation
        scope: lnumber of participations order by date desc from the logged user
        getFriendsLastParticipation()
        return array of Participation
        scope: number of participations order by date desc from all my friends
 */

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
import java.util.TreeSet;
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

    private TreeSet<User> users;//Key = user id Value = User Object
    private TreeSet<Race> races;//Key = race id Value = Race Object
    private TreeSet<Team> teams;//Key = teams id Value = Team Object
    private TreeSet<Track> tracks;//Key = track id Value = Track Object

    private TreeSet<Participation> participation;//Key = userid value = Participation


    private WebService() {

        httpClient = new DefaultHttpClient();
        httpParams = new BasicHttpParams();
        httpPost = new HttpPost();
        users = new TreeSet< User>();
        races = new TreeSet< Race>();
        tracks = new TreeSet< Track>();
        participation = new TreeSet< Participation>();
        teams = new TreeSet<Team>();

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

                    users.add(user);
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

    public TreeSet< User> getUsers() {

        TreeSet< User> userTree = new TreeSet< User>();

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
                    users.add(buf);
                }

                Log.d("WebService getUsers", responseString);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
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
        for(User u : users){
            User bufUser = u;
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
        return users.ceiling(new User(id));

    }


    public TreeSet<Checkpoint> getCheckpointsTrack(Integer trackId){
            TreeSet<Checkpoint> cpList = new TreeSet<Checkpoint>();

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
                    cpList.add(buf);
                }

                Log.d("WebService getTrackCheckpoints ", responseString);

            }

        }catch (Exception e){

            e.printStackTrace();
        }

        return  cpList;
    }

    public TreeSet<Track> getTracks(){
        TreeSet<Track> trackTree = new TreeSet<Track>();

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
                TreeSet<Checkpoint> checkpointTrack = null;

                for(int i=0;i<size;i++)
                {
                    JSONObject jbuf = trackList.getJSONObject(i);
                    checkpointTrack = getCheckpointsTrack(jbuf.getInt(Track.TAG_TRACK_ID));

                    buf =new Track(jbuf.getInt(Track.TAG_TRACK_ID)
                    ,jbuf.getString(Track.TAG_TRACK_NAME)
                    ,checkpointTrack);

                    trackTree.add(buf);
                }
                Log.d("WebService getTracks", responseString);
            }
        } catch (Exception e) {

            Log.d("WebService getTracks", "Echec de la récuperation des données depuis le serveur");
            e.printStackTrace();

        }
        return trackTree;

    }

    public Track getTrack(Integer trackid){
        if(tracks.isEmpty())tracks=getTracks();

        return tracks.ceiling(new Track(trackid));

    }

    public TreeSet<Race> getRaces(){

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

                    races.add(buf);

                }

                Log.d("WebService getRaces", responseString);

            }

        } catch (Exception e) {

            Log.d("WebService getRaces", "Echec de la récuperation des données depuis le serveur");
            e.printStackTrace();

        }
        return races;

    }

    public Race getRace(Integer raceid){

        if(races.isEmpty())races = getRaces();

        return races.ceiling(new Race(raceid));
    }

    public TreeSet<Participation>  getParticipationByUser(Integer userId){
        if(participation.isEmpty())participation= getParticipation();

            return null;

    }
    public TreeSet<Participation>  getParticipation(){

        TreeSet<Participation> participationTree = new TreeSet<Participation>();

        try {

            httpPost.setURI(new URI(Config.Service.service_participation));
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("participation", "*"));
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
                    Race race = getRace(jbuf.getInt(Participation.TAG_PARTICIPATION_RACE));
                    buf = new Participation(getUser(jbuf.getInt(Participation.TAG_PARTICIPATION_USER))
                            ,race
                            ,sdf.parse(jbuf.getString(Participation.TAG_PARTICIPATION_START))
                            ,sdf.parse(jbuf.getString(Participation.TAG_PARTICIPATION_END))
                            ,jbuf.getInt(Participation.TAG_PARTICIPATION_FINISHED)
                    );

                    participationTree.add(buf);

                }

                Log.d("WebService getParticipation", responseString);

            }

        } catch (Exception e) {

            Log.d("WebService getParticipation", "Echec de la récuperation des données depuis le serveur");
            e.printStackTrace();

        }
        return participationTree;

    }

}
