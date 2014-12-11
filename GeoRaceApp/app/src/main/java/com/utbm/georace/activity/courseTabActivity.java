package com.utbm.georace.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

import com.utbm.georace.R;
import com.utbm.georace.fragment.CheckpointList;
import com.utbm.georace.fragment.ParticipantsList;
import com.utbm.georace.fragment.RaceMap;

public class courseTabActivity extends FragmentActivity implements RaceMap.OnFragmentInteractionListener, CheckpointList.OnFragmentInteractionListener, ParticipantsList.OnFragmentInteractionListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    public  RaceMap fragmentRaceMap;
    public CheckpointList fragmentCheckpointList;
    public ParticipantsList fragmentParticipantsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_tab);

        /*
            Navigation par onglets
         */
        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0,ActionBar.DISPLAY_SHOW_TITLE);

        bar.addTab(bar.newTab()
                .setText(R.string.current_race_tab_map)
                .setTabListener(new TabListener<RaceMap>(this,"RaceMap",RaceMap.class)));
        bar.addTab(bar.newTab()
                .setText(R.string.current_race_tab_checkpoints)
                .setTabListener(new TabListener<CheckpointList>(this,"CheckpointList",CheckpointList.class)));
        bar.addTab(bar.newTab()
                .setText(R.string.current_race_tab_participants)
                .setTabListener(new TabListener<ParticipantsList>(this,"ParticipantsList",ParticipantsList.class)));
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
