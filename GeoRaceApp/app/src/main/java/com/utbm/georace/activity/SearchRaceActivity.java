package com.utbm.georace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.utbm.georace.R;
import com.utbm.georace.adapter.ParticipationAdapter;
import com.utbm.georace.fragment.CheckpointList;
import com.utbm.georace.model.Check;
import com.utbm.georace.model.Checkpoint;
import com.utbm.georace.model.Participation;
import com.utbm.georace.model.Race;
import com.utbm.georace.model.Track;
import com.utbm.georace.model.User;
import com.utbm.georace.tools.Globals;
import com.utbm.georace.tools.WebService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class SearchRaceActivity extends Activity {


    private TreeSet<Track> tracks;
    private ListView trackList;
    private String[] trackListData;
    private Context thisContext ;
    private BuildTrackListsTasks builderTrackList;
    private BuildNewRaceTasks builderRace;
    private Race newRace = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_race);
        thisContext = this;
        trackList = (ListView) findViewById(R.id.SearchListResults);

        builderTrackList = new BuildTrackListsTasks();
        builderTrackList.execute();


        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

                Globals g = Globals.getInstance();
                final String selectedTrackName = trackList.getItemAtPosition(position).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);

                if (g.getCurrentRace() != null){
                    builder.setMessage("Une course est déjà en cours, si vous commencez une" +
                            " nouvelles course, votre progression sera perdu!\n" +
                            "Circuit " + selectedTrackName)
                            .setTitle("Commencer la course avec ce circuit?");
                }else{
                    builder.setMessage("Circuit " + selectedTrackName).setTitle("Commencer la course avec ce circuit?");
                }

                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Track selectedTrack = null;
                        for(Track t : tracks){
                            if (t.getName() == selectedTrackName){
                                selectedTrack = t;
                            }
                        }
                        Globals g = Globals.getInstance();
                        newRace = new Race(new Date(),null,selectedTrack, g.getThisUser());

                        /*Enregistrer la nouvelle course dans la abse de données : */
                        /*builderRace = new BuildNewRaceTasks();
                        builderRace.execute();*/

                        /*Création de la participation de l'utilisateur à la nouvelle course*/
                        Participation newParticipation = new Participation(g.getThisUser(),newRace,new Date());
                        g.setCurrentParticipation(newParticipation);
                        g.setCurrentRace(newRace);
                        Intent intent = new Intent(thisContext, CourseTabActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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

    class BuildTrackListsTasks extends AsyncTask<Void,Void,Boolean> {


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
                /*TreeSet<Checkpoint> checkpointsTs = t.getCheckpoints();
                int i=0;
                for (Checkpoint cp : checkpointsTs){
                    i++;
                }*/
                trackStringList.add(t.getName()/* + " - " + i + " Checkpoints"*/);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    thisContext,
                    android.R.layout.simple_list_item_1,
                    trackStringList );
            trackList.setAdapter(arrayAdapter);
        }
    }
    class BuildNewRaceTasks extends AsyncTask<Void,Void,Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {
            WebService ws = WebService.getInstance();
            ws.setRace(newRace);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            /*après l'execution du poste on devrait récupérer la race qui a été crée et remplacé la
            * Globals Current Race par celle ci.*/
        }
    }


}
