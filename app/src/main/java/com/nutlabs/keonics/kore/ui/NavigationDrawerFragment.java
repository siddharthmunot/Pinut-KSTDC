/*
 * Copyright 2015 Synced Synapse. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nutlabs.keonics.kore.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nutlabs.keonics.AboutUsActivity;
import com.nutlabs.keonics.Activity.ComputerSkillsActivity;
import com.nutlabs.keonics.Activity.EnglishEducatonActivity;
import com.nutlabs.keonics.Activity.FarmingAndFishingActivity;
import com.nutlabs.keonics.Activity.FinanceActivity;
import com.nutlabs.keonics.Activity.HealthActivity;
import com.nutlabs.keonics.Activity.MCQTestActivity;
import com.nutlabs.keonics.Activity.NdlmActivity;
import com.nutlabs.keonics.Activity.SafetyAndSecurityActivity;
import com.nutlabs.keonics.Activity.VocationalSkillsActivity;
import com.nutlabs.keonics.FeedbackActivity;
import com.nutlabs.keonics.R;
import com.nutlabs.keonics.SharedConstants;
import com.nutlabs.keonics.Temporary_sub_chapter_Activities.Activity1GridView;
import com.nutlabs.keonics.kore.Settings;
import com.nutlabs.keonics.kore.host.HostInfo;
import com.nutlabs.keonics.kore.host.HostManager;
import com.nutlabs.keonics.kore.ui.hosts.HostManagerActivity;
import com.nutlabs.keonics.kore.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//import com.nutlabs.keonics.R;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */

public class NavigationDrawerFragment extends Fragment {
    private static final String TAG = LogUtils.makeLogTag(NavigationDrawerFragment.class);

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private static final int ACTIVITY_HOSTS = 0,
            //ACTIVITY_REMOTE = 1,
            //ACTIVITY_MOVIES = 2,
            //ACTIVITY_TVSHOWS = 3,
            //ACTIVITY_MUSIC = 4,
            ACTIVITY_HOME=1,
          //  ACTIVITY_SHORT_MOVIES = 2,
            ACTIVITY_COMPUTER_SKILLS = 2,
            ACTIVITY_ENGLISH = 3,
                  ACTIVITY_FARMING = 4,
            ACTIVITY_VOCATIONAL = 5,

                  ACTIVITY_NDLM=6,
                  ACTIVITY_SAFETYANDSECURITY=7,
    ACTIVITY_FINANCE = 8,
    ACTIVITY_HEALTH = 9,
            //ACTIVITY_PVR = 5,
            // ACTIVITY_FILES = 6,
            //ACTIVITY_ADDONS = 7,
            //ACTIVITY_SETTINGS = 8;
            ACTIVITY_MCQ = 10,
            ACTIVITY_FEEDBACK=11,

            ACTIVITY_ABOUT_US=12;

    // The current selected item id (based on the activity)
    private static int selectedItemId = -1;

    // Delay to close the drawer (ms)
    private static final int CLOSE_DELAY = 250;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, true);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer,
                container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerItem item = (DrawerItem)parent.getItemAtPosition(position);
                selectItem(item, position);
            }
        });
        ////////////////////////////////
      /*  Bundle args = new Bundle();
        args.putInt(ComputerSkillsActivity.,0);
        args.putSerializable(ComputerSkillsActivity.MOVIETITLE,0);*/

        Resources.Theme theme = getActivity().getTheme();
        TypedArray styledAttributes = theme.obtainStyledAttributes(new int[] {
               R.attr.iconhome,
                R.attr.iconhome,
           /*     R.attr.iconcomputer,
                R.attr.iconenglisheduaction,
        R.attr.iconfarmingandfishing,
                R.attr.iconvocabularyskills,
                R.attr.iconoriya,

                R.attr.iconsafetyandsecurity,
                R.attr.iconfinance,
                R.attr.iconhospital,*/

                R.attr.iconFeedback,
                R.attr.iconAboutUs,
                R.attr.iconAboutUs,
            /*    R.attr.iconvocabularyskills,
                R.attr.iconvocabularyskills,
                R.attr.iconvocabularyskills,*/

          /*      R.attr.iconsafetyandsecurity,
                R.attr.iconfarmingandfishing,

                R.attr.iconfinance,
                R.attr.iconhospital,

                R.attr.iconKids,*/

              //  R.attr.iconSettings,

               // R.attr.iconViralVideos,
               // R.attr.iconNewAddition,

        });

        HostInfo hostInfo = HostManager.getInstance(getActivity()).getHostInfo();
        String hostName = (hostInfo != null) ? hostInfo.getName() : getString(R.string.xbmc_media_center);

        Set<String> shownItems = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getStringSet(Settings.KEY_PREF_NAV_DRAWER_ITEMS,
                              new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.entry_values_nav_drawer_items))));

        ArrayList<DrawerItem> items = new ArrayList<>(15);
        items.add(new DrawerItem(DrawerItem.TYPE_HOST, ACTIVITY_HOSTS, hostName,
                                 styledAttributes.getResourceId(ACTIVITY_HOSTS, 0)));
        //items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_REMOTE,
        //                       getString(R.string.remote),
        //                     styledAttributes.getResourceId(ACTIVITY_REMOTE, 0)));
        if (shownItems.contains(String.valueOf(ACTIVITY_HOME)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_HOME,
                    SharedConstants.ACTIVITY_HOME,
                                     styledAttributes.getResourceId(ACTIVITY_HOME, 0)));
       /* if (shownItems.contains(String.valueOf(ACTIVITY_COMPUTER_SKILLS)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_COMPUTER_SKILLS,
                                     SharedConstants.ACTIVITY_COMPUTER_SKILLS,
                                     styledAttributes.getResourceId(ACTIVITY_COMPUTER_SKILLS, 0)));
        if (shownItems.contains(String.valueOf(ACTIVITY_ENGLISH)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_ENGLISH,
                                     SharedConstants.ACTIVITY_ENGLISH_EDUCATION ,
                                     styledAttributes.getResourceId(ACTIVITY_ENGLISH, 0)));
        if (shownItems.contains(String.valueOf(ACTIVITY_VOCATIONAL)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_VOCATIONAL,
                    SharedConstants.ACTIVITY_VOCATIONAL_SKILLS ,
                    styledAttributes.getResourceId(ACTIVITY_VOCATIONAL, 0)));

        if (shownItems.contains(String.valueOf(ACTIVITY_FARMING)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_FARMING,
                    SharedConstants.ACTIVITY_FARMING_AND_FISHING ,
                    styledAttributes.getResourceId(ACTIVITY_FARMING, 0)));

        if (shownItems.contains(String.valueOf(ACTIVITY_FINANCE)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_FINANCE,
                                     SharedConstants.ACTIVITY_FINANCE,
                                     styledAttributes.getResourceId(ACTIVITY_FINANCE, 0)));
        if (shownItems.contains(String.valueOf(ACTIVITY_HEALTH)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_HEALTH,
                                     SharedConstants.ACTIVITY_HEALTH,
                                     styledAttributes.getResourceId(ACTIVITY_HEALTH, 0)));*/
        /*if (shownItems.contains(String.valueOf(ACTIVITY_ADDONS)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_ADDONS,
                                     getString(R.string.addons),
                                     styledAttributes.getResourceId(ACTIVITY_ADDONS, 0)));*/
       // items.add(new DrawerItem()); // Divider
        //items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_SETTINGS,
        //                        getString(R.string.settings),
        //                        styledAttributes.getResourceId(ACTIVITY_SETTINGS, 0)));
      /*  if (shownItems.contains(String.valueOf(ACTIVITY_NDLM)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_NDLM,
                    SharedConstants.ACTIVITY_NDLM_CONTENT,
                    styledAttributes.getResourceId(ACTIVITY_NDLM, 0)));

        if (shownItems.contains(String.valueOf(ACTIVITY_SAFETYANDSECURITY)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_SAFETYANDSECURITY,
                    SharedConstants.ACTIVITY_SAFETY_AND_SECURITY,
                    styledAttributes.getResourceId(ACTIVITY_SAFETYANDSECURITY, 0)));*/


        if (shownItems.contains(String.valueOf(ACTIVITY_FEEDBACK)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_FEEDBACK,
                    getString(R.string.feedback),
                    styledAttributes.getResourceId(ACTIVITY_FEEDBACK, 0)));




      /*  if(shownItems.contains(String.valueOf(ACTIVITY_MCQ)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM,ACTIVITY_MCQ,
                    getString(R.string.mcq),
                    styledAttributes.getResourceId(ACTIVITY_MCQ,0)));
*/

        if (shownItems.contains(String.valueOf(ACTIVITY_ABOUT_US)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_ABOUT_US,
                    getString(R.string.aboutus),
                    styledAttributes.getResourceId(ACTIVITY_ABOUT_US, 0)));

        styledAttributes.recycle();
        mDrawerListView.setAdapter(new DrawerItemAdapter(
                getActivity(),
                R.layout.list_item_navigation_drawer,
                items.toArray(new DrawerItem[items.size()])));

        return mDrawerListView;
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedItemId = getItemIdFromActivity();
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        //mDrawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                saveUserLearnedDrawer();
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                saveUserLearnedDrawer();
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        selectedItemId = getItemIdFromActivity();
    }

    /**
     * Auxiliary method to show/hide the drawer indicator
     * @param isEnabled Show/hide enable drawer indicator
     */
    public void setDrawerIndicatorEnabled(boolean isEnabled) {
        mDrawerToggle.setDrawerIndicatorEnabled(isEnabled);
    }

    public boolean isDrawerIndicatorEnabled() {
        return mDrawerToggle.isDrawerIndicatorEnabled();
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    private void saveUserLearnedDrawer() {
        if (!mUserLearnedDrawer) {
            mUserLearnedDrawer = true;
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
        }
    }

    /**
     * Maps from the current activity to the private Item Id on the drawer
     * @return Item if of the current activity
     */
    private int getItemIdFromActivity() {
        Activity activity = getActivity();

        if (activity instanceof HostManagerActivity)
            return ACTIVITY_HOSTS;
       // else if (activity instanceof RemoteActivity)
        //    return ACTIVITY_REMOTE;
        if (activity instanceof Activity1GridView)
            return ACTIVITY_HOME;
        else if (activity instanceof ComputerSkillsActivity)

            return ACTIVITY_COMPUTER_SKILLS;
        else if (activity instanceof EnglishEducatonActivity)
            return ACTIVITY_ENGLISH;
        else if (activity instanceof VocationalSkillsActivity)
            return ACTIVITY_VOCATIONAL;
                else if (activity instanceof FarmingAndFishingActivity)
            return ACTIVITY_FARMING;
                else if (activity instanceof NdlmActivity)
            return ACTIVITY_NDLM;
                else if (activity instanceof SafetyAndSecurityActivity)
            return ACTIVITY_SAFETYANDSECURITY;
                else if (activity instanceof FinanceActivity)
            return ACTIVITY_FINANCE;
                else if (activity instanceof HealthActivity)
            return ACTIVITY_HEALTH;
        //else if (activity instanceof PVRActivity)
         //   return ACTIVITY_PVR;
       // else if (activity instanceof FileActivity)
        //    return ACTIVITY_FILES;
        //else if (activity instanceof AddonsActivity)
          //  return ACTIVITY_ADDONS;
        //else if (activity instanceof SettingsActivity)
          //  return ACTIVITY_SETTINGS;
        else if (activity instanceof MCQTestActivity)
            return ACTIVITY_MCQ;

        else if (activity instanceof FeedbackActivity)
            return ACTIVITY_FEEDBACK;
        else if (activity instanceof FeedbackActivity)
            return ACTIVITY_FEEDBACK;
        else if (activity instanceof FeedbackActivity)
            return ACTIVITY_FEEDBACK;
        else if (activity instanceof AboutUsActivity)
            return ACTIVITY_ABOUT_US;


        return -1;
    }

    /**
     * Map from the Item Id to the activities
     */
    private static final SparseArray<Class> activityItemIdMap;
    static {
        activityItemIdMap = new SparseArray<>(12);
        activityItemIdMap.put(ACTIVITY_HOSTS, HostManagerActivity.class);
        //activityItemIdMap.put(ACTIVITY_REMOTE, RemoteActivity.class);
        activityItemIdMap.put(ACTIVITY_HOME, Activity1GridView.class);
        activityItemIdMap.put(ACTIVITY_COMPUTER_SKILLS, ComputerSkillsActivity.class);
        activityItemIdMap.put(ACTIVITY_ENGLISH, EnglishEducatonActivity.class);
        activityItemIdMap.put(ACTIVITY_VOCATIONAL, VocationalSkillsActivity.class);
        activityItemIdMap.put(ACTIVITY_FARMING,FarmingAndFishingActivity.class);
        activityItemIdMap.put(ACTIVITY_NDLM,NdlmActivity.class);
        activityItemIdMap.put(ACTIVITY_SAFETYANDSECURITY,SafetyAndSecurityActivity.class);
        activityItemIdMap.put(ACTIVITY_FINANCE,FinanceActivity.class);
        activityItemIdMap.put(ACTIVITY_HEALTH, HealthActivity.class);
        //activityItemIdMap.put(ACTIVITY_TVSHOWS, TVShowsActivity.class);
        //activityItemIdMap.put(ACTIVITY_PVR, PVRActivity.class);
        //activityItemIdMap.put(ACTIVITY_ADDONS, AddonsActivity.class);
        //activityItemIdMap.put(ACTIVITY_SETTINGS, SettingsActivity.class);
        activityItemIdMap.put(ACTIVITY_MCQ, MCQTestActivity.class);
        activityItemIdMap.put(ACTIVITY_FEEDBACK, FeedbackActivity.class);
        activityItemIdMap.put(ACTIVITY_ABOUT_US, AboutUsActivity.class);

    }
    private Intent selectItem(DrawerItem item, int position) {
        if (item.type == DrawerItem.TYPE_DIVIDER) return null;

        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        // Same activity, just return
        if (item.id == getItemIdFromActivity())
            return null;

        //no need to show hostmanager activity to user
        if (item.id==0)
            return null;

        final Intent launchIntentFinal = new Intent(getActivity(),
                activityItemIdMap.get(item.id))
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING,"computerskills");
        if(item.id==2)
            launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING,"computerskills");
        else    if(item.id==3)
            launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING,"englishandlifeskills");
        else   if(item.id==5)
            launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING,"learnaboutjobs");
        else  if(item.id==4)
            launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING,"fishingandfarming");
        else  if(item.id==6)
            launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING,"NDLM");
        else  if(item.id==7)
            launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING,"safetyandsecurity");
        else  if(item.id==8)
            launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING,"finance");
        else  if(item.id==9)
            launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING,"health");


        /*else if (activityItemIdMap.get(item.id).equals(2))

            return launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING, "englishandlifeskills");
        else
        if (activityItemIdMap.get(item.id).equals(3))
            return launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING, "learnaboutjobs");
        else
        if (activityItemIdMap.get(item.id).equals(4))
            return launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING, "fishingandfarming");
        else
        if (activityItemIdMap.get(item.id).equals(5))
            return  launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING, "oriyacontent");
        else
        if (activityItemIdMap.get(item.id).equals(6))
            return launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING, "safetyandsecurity");
        else
        if (activityItemIdMap.get(item.id).equals(7))
            return launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING, "finance");
        else
        if (activityItemIdMap.get(item.id).equals(8))
            return  launchIntentFinal.putExtra(SharedConstants.BASE_TAG_STRING, "health"); */
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(launchIntentFinal);
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        }, CLOSE_DELAY);



        return launchIntentFinal;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    /**
     * Auxiliary class to hold the description and icon of a drawer icon
     */
    public static class DrawerItem {
        public static final int TYPE_HOST = 0,
                TYPE_DIVIDER = 1,
                TYPE_NORMAL_ITEM = 2;
        public static final int DEFAULT_DIVIDER_ID = -1;

        public final int id;
        public final int type;
        public final String desc;
        public final int iconResourceId;

        /**
         * Creates a standard drawer item
         * @param desc Name of the item
         * @param icon Icon to show
         */
        public DrawerItem(int type, int id, String desc, int icon) {
            this.type = type;
            this.id = id;
            this.desc = desc;
            this.iconResourceId = icon;
        }

        /**
         * Creates a divider drawer item
         */
        public DrawerItem() {
            this.id = DEFAULT_DIVIDER_ID;
            this.type = TYPE_DIVIDER;
            this.desc = null;
            this.iconResourceId = 0;
        }
    }

    public static class DrawerItemAdapter extends ArrayAdapter<DrawerItem> {

        private int selectedItemColor, hostItemColor;

        public DrawerItemAdapter(Context context, int layoutId, DrawerItem[] objects) {
            super(context, layoutId, objects);
            TypedArray styledAttributes = context
                    .getTheme()
                    .obtainStyledAttributes(new int[] {
                            R.attr.colorAccent,
                            R.attr.textColorOverPrimary
                    });
            Resources resources = context.getResources();
            selectedItemColor = styledAttributes.getColor(0, resources.getColor(R.color.accent_default));
            hostItemColor = styledAttributes.getColor(1, resources.getColor(R.color.dark_blue));
            styledAttributes.recycle();
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            DrawerItem item = getItem(position);

            ImageView icon;
            TextView desc;
            ImageView image;
            switch (item.type) {
                case DrawerItem.TYPE_DIVIDER:
                    if (convertView == null) {
                        convertView = ((LayoutInflater)getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.list_item_navigation_drawer_divider, parent, false);
                    }
                    break;
                case DrawerItem.TYPE_NORMAL_ITEM:
                        if (convertView == null) {
                        convertView = ((LayoutInflater)getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.list_item_navigation_drawer, parent, false);
                    }
                    icon = (ImageView)convertView.findViewById(R.id.drawer_item_icon);
                    icon.setImageResource(item.iconResourceId);
                    desc = (TextView)convertView.findViewById(R.id.drawer_item_title);
                    desc.setText(item.desc);
                    if (selectedItemId == item.id) {
                        icon.setColorFilter(selectedItemColor);
                        desc.setTextColor(selectedItemColor);
                    }
                    break;
                case DrawerItem.TYPE_HOST:
                    if (convertView == null) {
                        convertView = ((LayoutInflater)getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.list_item_navigation_drawer_host, parent, false);
                    }
                    //icon = (ImageView)convertView.findViewById(R.id.drawer_item_icon);
                    image = (ImageView)convertView.findViewById(R.id.drawer_image);
                    //icon.setImageResource(item.iconResourceId);
                    desc = (TextView)convertView.findViewById(R.id.drawer_item_title);
                    String name = PreferenceManager.getDefaultSharedPreferences(this.getContext()).getString(SharedConstants.PREF_NAME,"");
                    String[] names = name.split(" ");

                    String firstName = names[0].substring(0,1).toUpperCase() + names[0].substring(1).toLowerCase();
                    String navIntro = "Hi " + firstName;

                    Log.i(TAG, "name=" + firstName);
                    Log.i(TAG, "name" + navIntro);
                    desc.setText(navIntro);
                    if (selectedItemId == item.id) {
                        //icon.setColorFilter(selectedItemColor);
                        //desc.setTextColor(selectedItemColor);
                        image.setImageResource(R.drawable.navheader1);
                        desc.setTextColor(R.color.navigationBarColor);
                    } else {
                        //icon.setColorFilter(selectedItemColor);
                        //desc.setTextColor(selectedItemColor);
                        switch (selectedItemId) {
                            case 1:
                                image.setImageResource(R.drawable.navheader1);
                                desc.setTextColor(R.color.navigationBarColor);

                                break;
                            //SHORT Movie
                            case 2:
                                image.setImageResource(R.drawable.navheader1);
                                desc.setTextColor(R.color.navigationBarColor);

                                break;
                            //Animated Movie
                            case 3:
                                image.setImageResource(R.drawable.navheader1);
                                desc.setTextColor(R.color.navigationBarColor);

                                break;
                            //TED Talks
                            case 4:
                                image.setImageResource(R.drawable.navheader1);
                                desc.setTextColor(R.color.navigationBarColor);

                                break;
                            //Old is Gold
                            case 5:
                                image.setImageResource(R.drawable.navheader1);
                                desc.setTextColor(R.color.navigationBarColor);

                                break;
                            //Feedback
                            case 9:
                                image.setImageResource(R.drawable.navheader1);
                                desc.setTextColor(R.color.navigationBarColor);

                                break;
                            //About Us
                            case 10:
                                image.setImageResource(R.drawable.navheader1);
                                desc.setTextColor(R.color.navigationBarColor);

                                break;
                            case 11:
                                image.setImageResource(R.drawable.navheader1);
                                desc.setTextColor(R.color.navigationBarColor);

                                break;
                            case 12:
                                image.setImageResource(R.drawable.navheader1);
                                desc.setTextColor(R.color.navigationBarColor);

                                break;
                        }

                    }
                    break;
            }
            return convertView;
        }
    }
}
