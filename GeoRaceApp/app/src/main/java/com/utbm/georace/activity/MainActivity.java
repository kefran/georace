package com.utbm.georace.activity;

//region Imports
import android.app.Activity;

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
            Navigation drawer initialisation
         */
        mMenuList = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mMenuList));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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

   //region Navigation Drawer

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the menu item to show based on position
        //Fragment fragment = new CourseFragment();
        //Bundle args = new Bundle();
        //args.putInt(CourseFragment.ARG_PLANET_NUMBER, position);
        //fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
       // FragmentManager fragmentManager = getFragmentManager();
        //fragmentManager.beginTransaction()
          //      .replace(R.id.content_frame, fragment)
            //    .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mMenuList[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /*
        Define the actionbar title
     */
    @Override
    public void setTitle(CharSequence title) {
       // mTitle = title;
       // getActionBar().setTitle(mTitle);
    }
    //endregion

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
}