package com.utbm.georace.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.utbm.georace.R;

import java.util.ArrayList;
import java.util.List;

public class SearchRaceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_race);


        ListView lv = (ListView) findViewById(R.id.SearchListResults);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("foo");
        your_array_list.add("bar");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        lv.setAdapter(arrayAdapter);


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
}
