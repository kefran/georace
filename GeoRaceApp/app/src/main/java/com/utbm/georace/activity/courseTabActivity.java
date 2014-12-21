package com.utbm.georace.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.utbm.georace.R;
import com.utbm.georace.fragment.CheckpointList;
import com.utbm.georace.fragment.ParticipantsList;
import com.utbm.georace.fragment.RaceMap;
import com.utbm.georace.model.Check;
import com.utbm.georace.model.Checkpoint;
import com.utbm.georace.model.Participation;
import com.utbm.georace.model.Race;
import com.utbm.georace.model.Track;
import com.utbm.georace.tools.Globals;
import com.utbm.georace.tools.WebService;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CourseTabActivity extends FragmentActivity implements
        RaceMap.OnFragmentInteractionListener,
        CheckpointList.OnFragmentInteractionListener,
        ParticipantsList.OnFragmentInteractionListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    public  RaceMap fragmentRaceMap;
    public CheckpointList fragmentCheckpointList;
    public ParticipantsList fragmentParticipantsList;

    private final String CARTE_TAG = "MAP";
    private final String CHECKPOINTS_TAG = "CHECKPOINTS_TAG";
    private final String PARTICIPANT_TAG = "PARTICIPANT_TAG";

    private Participation currentParticipation;
    private Race currentRace;
    private TreeSet<Check> currentChecks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_tab);

        /*
            Navigation par onglets
         */
        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.addTab(bar.newTab()
                .setText(R.string.current_race_tab_map)
                .setTabListener(new TabListener<RaceMap>(this, CARTE_TAG, RaceMap.class)));
        bar.addTab(bar.newTab()
                .setText(R.string.current_race_tab_checkpoints)
                .setTabListener(new TabListener<CheckpointList>(this,CHECKPOINTS_TAG,CheckpointList.class)));
        bar.addTab(bar.newTab()
                .setText(R.string.current_race_tab_participants)
                .setTabListener(new TabListener<ParticipantsList>(this,PARTICIPANT_TAG,ParticipantsList.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ActionBar bar = getActionBar();
        fragmentRaceMap = (RaceMap) getFragmentManager().findFragmentByTag(CARTE_TAG);
        fragmentCheckpointList = (CheckpointList) getFragmentManager().findFragmentByTag(CHECKPOINTS_TAG);
        fragmentParticipantsList = (ParticipantsList) getFragmentManager().findFragmentByTag(PARTICIPANT_TAG);

        Globals g = Globals.getInstance();
        currentRace = g.getCurrentRace();
        WebService ws = WebService.getInstance();
        currentChecks = new TreeSet<Check>();

        setTitle("Course en cours : " + currentRace.getTrack().getName());

        setCheckPointCompteur();
        setMapChekpoints();
        setUserPosition();
        //setCheckpointlist();
    }

    //region checkpointlist
        public void setCheckpointlist(){
            List<String> checkpointList = new ArrayList<String>();
            TreeSet<Checkpoint> cps = currentRace.getTrack().getCheckpoints();
            for(Checkpoint cp : cps){
                checkpointList.add("Checkpoint : " + cp.getName());
            }
            fragmentCheckpointList.setChekpointList(checkpointList);
        }
    //endregion

    //region racemap
    @Override
    public void setUserPosition(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
        fragmentRaceMap.setCameraTo(userLocation);

    }

    public void setMapChekpoints() {
        TreeSet<Checkpoint> cps = currentRace.getTrack().getCheckpoints();
        for(Checkpoint cp : cps){
            fragmentRaceMap.setMarker(new LatLng(cp.getLatitude(),cp.getLongitude()),cp.getName());
        }
    }

    public void setCheckPointCompteur(){
        Integer i = 0;
        for(Check c : currentChecks){
            i++;
        }
        fragmentRaceMap.setCheckpointsCompteur(currentRace.getTrack().getCheckpoints().size(),i);
    }
    //endregion

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

    //region Gestion fragment
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
    public void onFragmentInteraction(Uri uri) {

    }



    //endregion

    //region   Tablistener custom pour la navigation par onglets

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
                ft.hide(mFragment);
                ft.commit();
            }
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.show(mFragment);
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.hide(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
        }

    }
    //endregion
}
