package com.utbm.georace.activity;

//region Imports
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;
import android.widget.Toast;

import com.utbm.georace.R;
import com.utbm.georace.adapter.ParticipationAdapter;
import com.utbm.georace.model.Check;
import com.utbm.georace.model.Participation;
import com.utbm.georace.model.User;
import com.utbm.georace.tools.Globals;
import com.utbm.georace.tools.WebService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
//endregion

public class MainActivity extends Activity {


    //region Variables
    private String[] mMenuList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private View mProgressView;

    private TreeSet<User> friends;
    private TreeSet<Participation> participations;
    private TreeSet<Participation> friendsParticipations;

    private ListView lastParticipationList;
    private String[] lastParticipationListData;

    private ListView friendsParticipationList;
    private String[] friendsParticipationListData;

    private BuildMainPageTask builder;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Globals g = Globals.getInstance();
        g.setCurrentRace(null);

        lastParticipationList = (ListView) findViewById(R.id.listLastRace);
        friendsParticipationList = (ListView) findViewById(R.id.listFriends);

        builder = new BuildMainPageTask();
        builder.execute();
    }

    //region action bar
/*
        Manage the actionbar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        participations.clear();
        WebService.getInstance().disconnect();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;

        switch (item.getItemId())
        {
            case R.id.actionAccueil:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.actionRace:
                Globals g = Globals.getInstance();

                if (g.getCurrentRace() == null) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Vous n'avez pas lancé de course. Veuillez utiliser la recherche de courses.",
                            Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    intent = new Intent(this, CourseTabActivity.class);
                    startActivity(intent);
                }
                return true;
            case R.id.actionSearchRace:
                intent = new Intent(this, SearchRaceActivity.class);
                startActivity(intent);
                return true;
            case R.id.actionCreateRace:
                intent = new Intent(this, CreateRaceActivity.class);
                startActivity(intent);
                return true;
            case R.id.actionScore:
                intent = new Intent(this, ScoreActivity.class);
                startActivity(intent);
                //action
                return true;
            case R.id.actionAccount:
                intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                //action
                return true;
            case R.id.actionLogOff:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
    //endregion



 class BuildMainPageTask extends AsyncTask<Void,Void,Boolean> {


     @Override
     protected Boolean doInBackground(Void... voids) {
        WebService ws = WebService.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        participations = ws.getUserParticipation(ws.getUserLogged());
        friendsParticipations = ws.getFriendParticipation();
        friends = ws.getFriends();
        if (!participations.isEmpty())
        {

            TreeSet<Check> checks = ws.getChecks(ws.getUserLogged(), participations.first().getRace());

            for (Check c : checks)
            {
                Log.d("Main ACTIVITY CHECKS", c.getUser().getFirstName());
            }
        }
         for(User u : friends){
             Log.d("MAIN COPAIN",u.getFirstName());
         }
         for(Participation p : participations){
             Log.d("PARTICIPATION TRACK",p.getRace().getTrack().getName());
         }

         return true;
    }

     @Override
     protected void onPostExecute(Boolean aBoolean) {
         super.onPostExecute(aBoolean);
         lastParticipationList.setAdapter(new ParticipationAdapter(getApplicationContext(),participations));

     }
 }
}