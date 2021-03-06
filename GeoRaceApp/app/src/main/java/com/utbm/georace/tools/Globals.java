package com.utbm.georace.tools;

import com.utbm.georace.model.Participation;
import com.utbm.georace.model.Race;
import com.utbm.georace.model.User;

public class Globals{
    private static Globals instance;

    // Global variable
    private Race currentRace;
    private User thisUser;
    private Participation currentParticipation;

    // Restrict the constructor from being instantiated
    private Globals(){}


    public Participation getCurrentParticipation() {
        return currentParticipation;
    }
    public void setCurrentParticipation(Participation currentParticipation) {
        this.currentParticipation = currentParticipation;
    }
    public User getThisUser() {
        return thisUser;
    }
    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }
    public void setCurrentRace(Race thisRace){
        this.currentRace=thisRace;
    }
    public Race getCurrentRace(){
        return this.currentRace;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}