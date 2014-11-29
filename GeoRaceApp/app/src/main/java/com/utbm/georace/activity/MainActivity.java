package com.utbm.georace.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.utbm.georace.R;
import com.utbm.georace.fragment.UserPortrait;
import com.utbm.georace.model.Checkpoint;
import com.utbm.georace.model.Participation;
import com.utbm.georace.model.Track;
import com.utbm.georace.model.User;
import com.utbm.georace.tools.WebService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class MainActivity extends Activity implements UserPortrait.OnFragmentInteractionListener{

    private String[] mMenuList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;


    private BuildMainPageTask buildMainPageTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mMenuList = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);



        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMenuList));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        buildMainPageTask = new BuildMainPageTask();
        buildMainPageTask.execute();
      //  showProgress(true);


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

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

    @Override
    public void setTitle(CharSequence title) {
       // mTitle = title;
       // getActionBar().setTitle(mTitle);
    }

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

 class BuildMainPageTask extends AsyncTask<Void,Void,Boolean>{

     @Override
     protected Boolean doInBackground(Void... voids) {
        WebService ws = WebService.getInstance();

        TreeMap<Integer,User> users = ws.getUsers();
        ArrayList<User> usersArray = new ArrayList<User>();

        TreeMap<Integer,Checkpoint> cp = ws.getCheckpointsTrack(2);
        TreeMap<Integer,Track> t = ws.getTracks();

         for(Map.Entry<Integer,Track> e : t.entrySet())
         {
             Track buf = e.getValue();
             Log.d("Main activity TRACKS", buf.getName());
             for(Map.Entry<Integer,Checkpoint> f : buf.getCheckpoints().entrySet())
                Log.d("Main Activity CHECKPOINTS",f.getValue().getName());

         }

         TreeMap<Pair<Integer,Integer>,Participation> part = ws.getParticipation();

         for(Map.Entry<Pair<Integer,Integer>,Participation> e : part.entrySet())
          Log.d("MAIN ACTIVITY PARTICIPATION",e.getValue().getUser().getFirstName());

         return true;
     }
 }


}
