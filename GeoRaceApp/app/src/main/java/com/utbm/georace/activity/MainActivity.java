package com.utbm.georace.activity;

//region Imports
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.utbm.georace.R;
import com.utbm.georace.adapter.participationArrayAdapter;
//endregion

public class MainActivity extends Activity {


    //region Variables
    private String[] mMenuList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private View mProgressView;

    private ListView lastParticipationList;
    private String[] lastParticipationListData;

    private ListView friendsParticipationList;
    private String[] friendsParticipationListData;

    //endregion

    /*
        onCreate method
     */
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
        lastParticipationList.setAdapter(new participationArrayAdapter(this,lastParticipationListData));

          /*
            Renplacer ici par les données réelles
            friendsActivityListData doit être remplacé par un tableau de participation issue de
            getFriendsLastParticipation
         */
        friendsParticipationListData = getResources().getStringArray(R.array.test_friends_activity);
        friendsParticipationList = (ListView) findViewById(R.id.listFriends);
        friendsParticipationList.setAdapter(new participationArrayAdapter(this,friendsParticipationListData));
    }

   //region ActionBar
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
        switch (item.getItemId()){
            case R.id.actionAccueil:
                //action
                return true;
            case R.id.actionRace:
                Intent intent = new Intent(this, courseTabActivity.class);
                startActivity(intent);
                return true;
            case R.id.actionSearchRace:
                //action
                return true;
            case R.id.actionCreateRace:
                //action
                return true;
            case R.id.actionScore:
                //action
                return true;
            case R.id.actionAccount:
                //action
                return true;
            case R.id.actionLogOff:
                //action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion
}