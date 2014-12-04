package com.utbm.georace.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.internal.de;
import com.utbm.georace.R;
import com.utbm.georace.fragment.CheckpointList;
import com.utbm.georace.fragment.ParticipantsList;
import com.utbm.georace.fragment.RaceMap;
import com.utbm.georace.fragment.UserPortrait;


public class courseTabActivity extends FragmentActivity implements ActionBar.TabListener, RaceMap.OnFragmentInteractionListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current tab position.
     */

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_tab);

        // Set up the action bar to show tabs.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // for each of the sections in the app, add a tab to the action bar.

         /*
        TODO: changer ça par une sequence déjà stocké dans un fichier plus structurée
         */

        actionBar.addTab(actionBar.newTab().setText(R.string.current_race_tab_map)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.current_race_tab_checkpoints)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.current_race_tab_participants)
                .setTabListener(this));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current tab position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current tab position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
                .getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, show the tab contents in the
        // container view.

        /*
        TODO: changer ça  http://stackoverflow.com/questions/7958458/actionbar-3-tabs-and-3-fragments-this-is-killing-me
         */
        int tabId = tab.getPosition();
        Fragment fragment;
        switch (tabId) {
            case 0:
                fragment  = new RaceMap();
            case 1:
                fragment  = new CheckpointList();
            case 2:
                fragment  = new ParticipantsList();
            default:
                fragment  = new RaceMap();
                //action
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
