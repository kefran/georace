package com.utbm.georace.model;

import com.utbm.georace.tools.ISerializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by jojo on 29/10/2014.
 */
public class Team implements ISerializable {


    final static public String TAG_TEAM_ID = "id";
    final static public String TAG_TEAM_NAME = "name";
    final static public String TAG_TEAM_MEMBER = "user";


    private int id;
    private String name;
    private TreeSet<User> members;

    public Team(){
        members = new TreeSet<User>();
    };

    public Team(JSONObject obj) {

        members = new TreeSet<User>();
        JSONArray jsArray;


        try {

            this.setId(obj.getInt(TAG_TEAM_ID));
            this.setName(obj.getString(TAG_TEAM_NAME));

            jsArray = obj.getJSONArray(TAG_TEAM_MEMBER);

            int nbMember = jsArray.length();

            for(int i =0;i<nbMember;i++)
            {
                members.add(new User(jsArray.getJSONObject(i)));
            }


        } catch (JSONException e) {
            e.printStackTrace();
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
        User buf ;


        for( User e : members)
        {
            jsArray.put(e.toJson());
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
    public boolean fromJson(JSONObject jsonObject) {

        JSONArray jsArray;
        User buf;

        try {
            this.setId(jsonObject.getInt(TAG_TEAM_ID));
            this.setName(jsonObject.getString(TAG_TEAM_NAME));
            jsArray = jsonObject.getJSONArray(TAG_TEAM_MEMBER);
            int nbMember = jsArray.length();


            for(int i=0;i<nbMember+1;i++)
            {
               buf = new User(jsArray.getJSONObject(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
