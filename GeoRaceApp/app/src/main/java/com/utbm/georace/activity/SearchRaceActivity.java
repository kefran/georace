package com.utbm.georace.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.utbm.georace.R;
import com.utbm.georace.adapter.ParticipationAdapter;
import com.utbm.georace.fragment.CheckpointList;
import com.utbm.georace.model.Check;
import com.utbm.georace.model.Checkpoint;
import com.utbm.georace.model.Participation;
import com.utbm.georace.model.Track;
import com.utbm.georace.model.User;
import com.utbm.georace.tools.WebService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class SearchRaceActivity extends Activity {


    private TreeSet<Track> tracks;
    private ListView trackList;
    private String[] trackListData;
    private Context thisContext ;
    private BuilTrackListsTasks builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_race);
        thisContext = this;
        trackList = (ListView) findViewById(R.id.SearchListResults);

        builder = new BuilTrackListsTasks();
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
                finish();
                return true;
            case R.id.actionRace:
                intent = new Intent(this, CourseTabActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.actionSearchRace:
                intent = new Intent(this, SearchRaceActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.actionCreateRace:
                intent = new Intent(this, CreateRaceActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.actionScore:
                intent = new Intent(this, ScoreActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.actionAccount:
                intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.actionLogOff:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    class BuilTrackListsTasks extends AsyncTask<Void,Void,Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {
            WebService ws = WebService.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            tracks = ws.getTracks();

            for(Track t :tracks)
            {
                Log.d("SEARCH TRACK : ", t.getName());
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            List<String> trackStringList = new ArrayList<String>();
            for (Track t :tracks)
            {
                TreeSet<Checkpoint> checkpointsTs = t.getCheckpoints();
                int i=0;
                for (Checkpoint cp : checkpointsTs){
                    i++;
                }
                trackStringList.add(t.getName() + " - " + i + " Checkpoints");
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    thisContext,
                    android.R.layout.simple_list_item_1,
                    trackStringList );
            trackList.setAdapter(arrayAdapter);
        }
    }

}
