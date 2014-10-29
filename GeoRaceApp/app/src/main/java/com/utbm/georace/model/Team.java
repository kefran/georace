package com.utbm.georace.model;

import com.utbm.georace.tools.ISerializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by jojo on 29/10/2014.
 */
public class Team implements ISerializable {


    final static public String TAG_TEAM_ID = "id";
    final static public String TAG_TEAM_NAME = "name";
    final static public String TAG_TEAM_MEMBER = "user";//TODO mise au point du format d'echange php <-> json <-> java -user+users?


    private int id;
    private String name;
    private TreeSet<User> members;

    public Team(JSONObject obj) {

        JSONArray jsArray;
        User buf;

        try {
            this.setId(jsonObject.getInt(TAG_TEAM_ID));
            this.setName(jsonObject.getString(TAG_TEAM_NAME));
            jsArray = jsonObject.getJSONArray(TAG_TEAM_MEMBER);
            int nbMember = jsArray.length();
            int i = 0, it = 0;

            if (nbMember != 0) {
                while (i != it) {
                    buf = new User(jsArray.getJSONObject(i));
                    this.addMember(buf);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}


    public boolean addMember(User u) {
        return members.add(u);
    }

    public boolean removeMember(User u) {
        return members.remove(u);
    }


    public TreeSet<User> getMembers() {
        return members;
    }

    public void setMembers(TreeSet<User> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsObj = new JSONObject();
        JSONArray jsArray = new JSONArray();
        Iterator<User> it = members.iterator();

        User buf;
        while (it.hasNext()) {

            buf = it.next();
            jsArray.put(buf);

        }

        try {

            jsObj.put(TAG_TEAM_ID, id);
            jsObj.put(TAG_TEAM_NAME, name);
            jsObj.put(TAG_TEAM_MEMBER, jsArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsObj;
    }

    @Override
    public Team fromJson(JSONObject jsonObject) {

        Team team = new Team();
        JSONArray jsArray;
        User buf;

        try {
            team.setId(jsonObject.getInt(TAG_TEAM_ID));
            team.setName(jsonObject.getString(TAG_TEAM_NAME));
            jsArray = jsonObject.getJSONArray(TAG_TEAM_MEMBER);
            int nbMember = jsArray.length();
            int i = 0, it = 0;

            if (nbMember != 0) {
                while (i != it) {
                    buf = new User(jsArray.getJSONObject(i));
                    team.addMember(buf);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return team;
    }
}
