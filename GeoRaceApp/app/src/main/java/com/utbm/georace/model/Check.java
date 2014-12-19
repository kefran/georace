package com.utbm.georace.model;

import java.util.Date;

/**
 * Created by jojo on 19/12/2014.
 */
public class Check implements Comparable<Check>{

    final static public String TAG_CHECK_USER ="user";
    final static public String TAG_CHECK_RACE="race";
    final static public String TAG_CHECK_CHECKPOINT="checkpoint";
    final static public String TAG_CHECK_DATE="date_check";


    private User user;
    private Race race;
    private Checkpoint checkpoint;
    private Date date;

    public Check(User user, Race race, Checkpoint checkpoint,Date date) {
        this.user = user;
        this.race = race;
        this.checkpoint = checkpoint;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(Checkpoint checkpoint) {
        this.checkpoint = checkpoint;
    }


    @Override
    public int compareTo(Check check) {

        int uid = user.getId();
        int cuid = check.getUser().getId();

        if(uid<cuid)return  -1;

        if(uid==cuid){
            int rid = race.getId();
            int crid = check.getRace().getId();

            if(rid<crid)return -1;

            if(rid==crid){
                int cid = checkpoint.getId();
                int ccid = check.getCheckpoint().getId();

                if(cid<ccid)return -1;

                if(cid==ccid)return 0;

                if(ccid>cid)return 1;
            }
            if(rid>crid)return 1;
        }

        return  1;


    }
}
