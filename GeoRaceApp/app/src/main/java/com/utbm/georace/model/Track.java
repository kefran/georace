package com.utbm.georace.model;

import com.utbm.georace.tools.ISerializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jojo on 22/10/2014.
 */
public class Track implements ISerializable {
    //La valeur des TAG doit être indentique au colonne de la bdd
    final static public String TAG_TRACK_ID = "id";
    final static public String TAG_TRACK_NAME = "name";
    final static public String TAG_TRACK_CREATOR = "creator";
    final static public String TAG_TRACK_CHECKPOINT = "checkpoint";


    private int id;
    private String name;
    //private Photo photo;
    private User creator;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    private TreeMap<Integer, Checkpoint> checkpoints;


    public Track() {
    }

    ;

    public Track(int id, String name, TreeMap<Integer, Checkpoint> checkpoints) {
        this.id = id;
        this.name = name;
        this.checkpoints = checkpoints;
    }

    public Track(JSONObject json) {
        try
        {

            checkpoints = new TreeMap<Integer, Checkpoint>();
            Checkpoint buf = null;

            name = json.getString(TAG_TRACK_NAME);
            id = json.getInt(TAG_TRACK_ID);
            JSONArray jsonArray = json.getJSONArray(TAG_TRACK_CHECKPOINT);

            int size = jsonArray.length();
            for (int i = 0; i < size; i++)
            {

                buf = new Checkpoint(jsonArray.getJSONObject(i));
                checkpoints.put(buf.getId(), buf);
            }


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


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

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try
        {
            jsonObject.put(TAG_TRACK_ID, id);
            jsonObject.put(TAG_TRACK_NAME, name);

            for (Map.Entry<Integer, Checkpoint> e : checkpoints.entrySet())
            {
                jsonArray.put(e.getValue().toJson());
            }
            jsonObject.put(TAG_TRACK_CHECKPOINT, jsonArray);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public TreeMap<Integer, Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(TreeMap<Integer, Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    @Override
    public boolean fromJson(JSONObject jsonObject) {

        TreeMap<Integer, Checkpoint> treeBuf = new TreeMap<Integer, Checkpoint>();
        Checkpoint buf=null;
        int size ;

        try
        {
            this.setName(jsonObject.getString(TAG_TRACK_NAME));
            this.setId(jsonObject.getInt(TAG_TRACK_ID));
            this.setCreator(new User(jsonObject.getJSONObject(TAG_TRACK_CREATOR)));

            JSONArray jsonArray = jsonObject.getJSONArray(TAG_TRACK_CHECKPOINT);
            size = jsonArray.length();


            for (int i = 0; i < size; i++)
            {
                buf = new Checkpoint(jsonArray.getJSONObject(i));
                treeBuf.put(buf.getId(), buf);
            }
            this.setCheckpoints(treeBuf);


        } catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
