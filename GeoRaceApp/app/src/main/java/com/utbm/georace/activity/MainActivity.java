package com.utbm.georace.activity;

//region Imports
import android.app.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;

import com.utbm.georace.R;
import com.utbm.georace.adapter.ParticipationAdapter;
import com.utbm.georace.model.Participation;
import com.utbm.georace.model.User;
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
        /*
            Renplacer ici par les données réelles
            lastRaceListData doit être remplacé par un tableau de participation issue de
            getMyLastParticipation
         */
        lastParticipationListData = getResources().getStringArray(R.array.test_course_list);
        lastParticipationList = (ListView) findViewById(R.id.listLastRace);


          /*
            Renplacer ici par les données réelles
            friendsActivityListData doit être remplacé par un tableau de participation issue de
            getFriendsLastParticipation
         */
        friendsParticipationListData = getResources().getStringArray(R.array.test_friends_activity);
        friendsParticipationList = (ListView) findViewById(R.id.listFriends);
//        friendsParticipationList.setAdapter(new ParticipationAdapter(this,friendsParticipationListData));

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
                intent = new Intent(this, CourseTabActivity.class);
                startActivity(intent);
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

        participations = ws.getUserParticipation();
        friendsParticipations = ws.getFriendParticipation();
        friends = ws.getFriends();

         for(User u : friends){
             Log.d("MAIN COPAIN",u.getFirstName());
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