package com.nutlabs.keonics.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.nutlabs.keonics.R;
import com.nutlabs.keonics.SharedConstants;
import com.nutlabs.keonics.Temporary_sub_chapter_Activities.Activity1GridView;
import com.nutlabs.keonics.dlna.CustomListAdapter;
import com.nutlabs.keonics.dlna.CustomListItem;
import com.nutlabs.keonics.kore.ui.BaseActivity;
import com.nutlabs.keonics.kore.ui.FolderListFragment;
import com.nutlabs.keonics.kore.ui.MovieDetailsFragment;
import com.nutlabs.keonics.kore.ui.MoviesActivity;
import com.nutlabs.keonics.kore.ui.NavigationDrawerFragment;
import com.nutlabs.keonics.kore.ui.VideoFolderListFragment;
import com.nutlabs.keonics.kore.ui.VideoListFragment;
import com.nutlabs.keonics.kore.utils.LogUtils;
import com.nutlabs.keonics.kore.utils.Utils;

/**
 * Created by Shubham on 9/22/2016.
 */
public class FarmingAndFishingActivity extends BaseActivity
        implements VideoListFragment.OnMovieSelectedListener,VideoFolderListFragment.OnVideoGenreSelectedListener {
    private static final String TAG = LogUtils.makeLogTag(MoviesActivity.class);

    public static final String MOVIEID = "movie_id";
    public static final String MOVIETITLE = "movie_title";

    private int selectedMovieId = -1;
    private String selectedMovieTitle;

    //private NavigationDrawerFragment navigationDrawerFragment;
    public ArrayAdapter<CustomListItem> mItemListAdapter;

    private int selectedGenreId = -1;
    private String selectedGenreTitle = null;
    String baseTag;

    @TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request transitions on lollipop
        if (Utils.isLollipopOrLater()) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);
        mItemListAdapter = new CustomListAdapter(this);
        setContentView(R.layout.activity_generic_media);
        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        baseTag = bundle.getString(SharedConstants.BASE_TAG_STRING, "");

        // Set up the drawer.
 /*       navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
*/
        if (savedInstanceState == null) {
            FolderListFragment movieListFragment = new FolderListFragment();
            Bundle args = new Bundle();
            args.putString(SharedConstants.MOVIE_TAG, SharedConstants.MOVIE_TAG_FARMINGANDFISHING);
            args.putString(SharedConstants.BASE_TAG_STRING, baseTag);
            movieListFragment.setArguments(args);

            // Setup animations
            if (Utils.isLollipopOrLater()) {
                movieListFragment.setExitTransition(null);
                movieListFragment.setReenterTransition(TransitionInflater
                        .from(this)
                        .inflateTransition(android.R.transition.fade));
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, movieListFragment)
                    .commit();
        } else {
            selectedMovieId = savedInstanceState.getInt(MOVIEID, -1);
            selectedMovieTitle = savedInstanceState.getString(MOVIETITLE, null);
        }

        setupActionBar(selectedMovieTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MOVIEID, selectedMovieId);
        outState.putString(MOVIETITLE, selectedMovieTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!navigationDrawerFragment.isDrawerOpen()) {
//            getMenuInflater().inflate(R.menu.media_info, menu);
//        }
        MenuItem menu1 = menu.add("HOME");
        menu1.setIcon(R.drawable.home_icon);
        menu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(FarmingAndFishingActivity.this, NewNdlmMainActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_show_remote:
                // Starts remote
                Intent launchIntent = new Intent(this, RemoteActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(launchIntent);
                return true;*/
            case android.R.id.home:
                // Only respond to this if we are showing the movie details in portrait mode,
                // which can be checked by checking if selected movie != -1, in which case we
                // should go back to the previous fragment, which is the list.
                if (selectedMovieId != -1) {
                    selectedMovieId = -1;
                    selectedMovieTitle = null;
                    setupActionBar(null);
                    getSupportFragmentManager().popBackStack();
                    return true;
                } else if (selectedGenreId != -1) {
                    selectedGenreId = -1;
                    selectedGenreTitle = null;
                    setupActionBar(null);
                    getSupportFragmentManager().popBackStack();
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If we are showing movie details in portrait, clear selected and show action bar
        if (selectedMovieId != -1) {
            selectedMovieId = -1;
            selectedMovieTitle = null;
            setupActionBar(null);
        }
        super.onBackPressed();
    }

    private void setupActionBar(String movieTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (movieTitle != null) {
           // navigationDrawerFragment.setDrawerIndicatorEnabled(false);
            actionBar.setTitle(movieTitle);
        } else {
            //navigationDrawerFragment.setDrawerIndicatorEnabled(true);
            actionBar.setTitle("Kids Content");
        }
    }

    /**
     * Callback from movielist fragment when a movie is selected.
     * Switch fragment in portrait
     *
     * @param movieId    Movie selected
     * @param movieTitle Title
     */
    @TargetApi(21)
    public void onMovieSelected(int movieId, String movieTitle) {
        selectedMovieId = movieId;
        selectedMovieTitle = movieTitle;

        MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.newInstance(movieId);
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();

        // Set up transitions
        if (Utils.isLollipopOrLater()) {
            movieDetailsFragment.setEnterTransition(TransitionInflater
                    .from(this)
                    .inflateTransition(R.transition.media_details));
            movieDetailsFragment.setReturnTransition(null);
        } else {
            fragTrans.setCustomAnimations(R.anim.fragment_details_enter, 0,
                    R.anim.fragment_list_popenter, 0);
        }

        fragTrans.replace(R.id.fragment_container, movieDetailsFragment)
                .addToBackStack(null)
                .commit();
        setupActionBar(selectedMovieTitle);
    }

    public void onVideoGenreSelected(int genreId, String genreTitle, String baseTag, boolean bIsLastLevel) {

        selectedGenreId = genreId;
        selectedGenreTitle = genreTitle;
        if (bIsLastLevel) {
            // Replace list fragment
            VideoListFragment movieListFragment = VideoListFragment.newInstanceForGenre(genreId, genreTitle, baseTag);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_details_enter, 0, R.anim.fragment_list_popenter, 0)
                    .replace(R.id.fragment_container, movieListFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            FolderListFragment movieListFragment = new FolderListFragment();
            Bundle args = new Bundle();
            args.putString(SharedConstants.MOVIE_TAG, SharedConstants.MOVIE_TAG_FARMINGANDFISHING);
            args.putString(SharedConstants.BASE_TAG_STRING, baseTag);
            movieListFragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, movieListFragment)
                    .addToBackStack(null)
                    .commit();
            // Setup animations
            /*
            if (Utils.isLollipopOrLater()) {
                movieListFragment.setExitTransition(null);
                movieListFragment.setReenterTransition(TransitionInflater
                        .from(this)
                        .inflateTransition(android.R.transition.fade));
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, movieListFragment)
                    .commit();
                    */
        }
        setupActionBar(genreTitle);
    }
}
