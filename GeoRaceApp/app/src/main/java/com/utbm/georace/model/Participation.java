package com.utbm.georace.model;

import com.utbm.georace.tools.ISerializable;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by jojo on 27/11/2014.
 */
public class Participation implements ISerializable, Comparable<Participation> {

    final static public String TAG_PARTICIPATION_USER="user";
    final static public String TAG_PARTICIPATION_RACE ="race";
    final static public String TAG_PARTICIPATION_END ="start";
    final static public String TAG_PARTICIPATION_START ="end";
    final static public String TAG_PARTICIPATION_FINISHED ="finished";



    private User user;
    private Race race;
    private Date end;
    private Date start;
    private int finished;


    public Participation(User user, Race race, Date end, Date start,int finished) {
        this.user = user;
        this.race = race;
        this.end = end;
        this.start = start;
        this.finished=finished;
    }

    public Participation(User user){
        this.user=user;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    public boolean fromJson(JSONObject jsonObject) {
        return false;
    }

    @Override
    public int compareTo(Participation participation) {
        if(user==null)throw new NullPointerException();

        int partId = participation.getUser().getId();
        int raceId = participation.getRace().getId();

        if(partId<user.getId())return 1;

        if(partId==user.getId())
        {
            int rid= race.getId();
            int prid = participation.getRace().getId();

                if(rid<prid)return -1;

                if(prid==rid)return 0;

                if(rid>prid)return 1;
        }
        if(partId>user.getId())
            return -1;

        return 1;
    }
}
