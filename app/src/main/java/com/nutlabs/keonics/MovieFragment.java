package com.nutlabs.keonics;

/**
 * Created by mdimran on 12/26/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutlabs.keonics.kore.Settings;
import com.nutlabs.keonics.kore.host.HostInfo;
import com.nutlabs.keonics.kore.host.HostManager;
import com.nutlabs.keonics.kore.provider.MediaContract;
import com.nutlabs.keonics.kore.provider.MediaDatabase;
import com.nutlabs.keonics.kore.service.LibrarySyncService;
import com.nutlabs.keonics.kore.ui.AbstractListFragment;
import com.nutlabs.keonics.kore.utils.UIUtils;

/**
 * Fragment that presents the movie list
 */
public class MovieFragment extends AbstractListFragment {
    //private static final String TAG = LogUtils.makeLogTag(MovieListFragment.class);
    private static final String TAG = "PiNut";
    private static Resources resources;
    private static DisplayMetrics displayMetrics;



    public interface OnMovieSelectedListener {
        public void onMovieSelected(int movieId, String movieTitle);
    }

    // Activity listener
    private OnMovieSelectedListener listenerActivity;

    @Override
    protected String getListSyncType() { return LibrarySyncService.SYNC_ALL_MOVIES; }

    @Override
    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the movie id from the tag
                ViewHolder tag = (ViewHolder) view.getTag();

                /*Object model = parent.getItemAtPosition(position);

                ItemModel item = (ItemModel)model;
                Uri uri = Uri.parse(item.getUrl());
                Log.i(TAG, "DLNA URL" + item.getUrl());*/
                // Notify the activity
                listenerActivity.onMovieSelected(tag.movieId, tag.movieTitle);
            }
        };
    }

    @Override
    protected CursorAdapter createAdapter() {
        return new MoviesAdapter(getActivity());
    }

    @Override
    protected CursorLoader createCursorLoader() {
        HostInfo hostInfo = HostManager.getInstance(getActivity()).getHostInfo();
        Uri uri = MediaContract.Movies.buildMoviesListUri(hostInfo != null? hostInfo.getId() : -1);

        StringBuilder selection = new StringBuilder();
        String selectionArgs[] = null;
        String searchFilter = getSearchFilter();
        if (!TextUtils.isEmpty(searchFilter)) {
            selection.append(MediaContract.MoviesColumns.TITLE + " LIKE ?");
            selectionArgs = new String[] {"%" + searchFilter + "%"};
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences.getBoolean(Settings.KEY_PREF_MOVIES_FILTER_HIDE_WATCHED, Settings.DEFAULT_PREF_MOVIES_FILTER_HIDE_WATCHED)) {
            if (selection.length() != 0)
                selection.append(" AND ");
            selection.append(MediaContract.MoviesColumns.PLAYCOUNT)
                    .append("=0");
        }

        String sortOrderStr;
        int sortOrder = preferences.getInt(Settings.KEY_PREF_MOVIES_SORT_ORDER, Settings.DEFAULT_PREF_MOVIES_SORT_ORDER);
        if (sortOrder == Settings.SORT_BY_DATE_ADDED) {
            sortOrderStr = MovieListQuery.SORT_BY_DATE_ADDED;
        } else {
            // Sort by name
            if (preferences.getBoolean(Settings.KEY_PREF_MOVIES_IGNORE_PREFIXES, Settings.DEFAULT_PREF_MOVIES_IGNORE_PREFIXES)) {
                sortOrderStr = MovieListQuery.SORT_BY_NAME_IGNORE_ARTICLES;
            } else {
                sortOrderStr = MovieListQuery.SORT_BY_NAME;
            }
        }

        return new CursorLoader(getActivity(), uri,
                MovieListQuery.PROJECTION, selection.toString(), selectionArgs, sortOrderStr);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listenerActivity = (OnMovieSelectedListener) activity;
            //Toast.makeText(getActivity(),"On attach",Toast.LENGTH_LONG).show();
            resources = getActivity().getResources();
            displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMovieSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Toast.makeText(getActivity(),"On detach",Toast.LENGTH_LONG).show();
        listenerActivity = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_list, menu);

        // Setup search view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.action_search_movies));

        // Setup filters
        MenuItem hideWatched = menu.findItem(R.id.action_hide_watched),
                ignoreArticles = menu.findItem(R.id.action_ignore_prefixes),
                sortByName = menu.findItem(R.id.action_sort_by_name),
                sortByDateAdded = menu.findItem(R.id.action_sort_by_date_added);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        hideWatched.setChecked(preferences.getBoolean(Settings.KEY_PREF_MOVIES_FILTER_HIDE_WATCHED, Settings.DEFAULT_PREF_MOVIES_FILTER_HIDE_WATCHED));
        ignoreArticles.setChecked(preferences.getBoolean(Settings.KEY_PREF_MOVIES_IGNORE_PREFIXES, Settings.DEFAULT_PREF_MOVIES_IGNORE_PREFIXES));

        int sortOrder = preferences.getInt(Settings.KEY_PREF_MOVIES_SORT_ORDER, Settings.DEFAULT_PREF_MOVIES_SORT_ORDER);
        switch (sortOrder) {
            case Settings.SORT_BY_DATE_ADDED:
                sortByDateAdded.setChecked(true);
                break;
            default:
                sortByName.setChecked(true);
                break;
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        switch (item.getItemId()) {
            case R.id.action_hide_watched:
                item.setChecked(!item.isChecked());
                preferences.edit()
                        .putBoolean(Settings.KEY_PREF_MOVIES_FILTER_HIDE_WATCHED, item.isChecked())
                        .apply();
                refreshList();
                break;
            case R.id.action_ignore_prefixes:
                item.setChecked(!item.isChecked());
                preferences.edit()
                        .putBoolean(Settings.KEY_PREF_MOVIES_IGNORE_PREFIXES, item.isChecked())
                        .apply();
                refreshList();
                break;
            case R.id.action_sort_by_name:
                item.setChecked(true);
                preferences.edit()
                        .putInt(Settings.KEY_PREF_MOVIES_SORT_ORDER, Settings.SORT_BY_NAME)
                        .apply();
                refreshList();
                break;
            case R.id.action_sort_by_date_added:
                item.setChecked(true);
                preferences.edit()
                        .putInt(Settings.KEY_PREF_MOVIES_SORT_ORDER, Settings.SORT_BY_DATE_ADDED)
                        .apply();
                refreshList();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Movie list query parameters.
     */
    private interface MovieListQuery {
        String[] PROJECTION = {
                BaseColumns._ID,
                MediaContract.Movies.MOVIEID,
                MediaContract.Movies.TITLE,
                MediaContract.Movies.THUMBNAIL,
                MediaContract.Movies.YEAR,
                MediaContract.Movies.GENRES,
                MediaContract.Movies.RUNTIME,
                MediaContract.Movies.RATING,
                MediaContract.Movies.TAGLINE,
        };

        String SORT_BY_NAME = MediaContract.Movies.TITLE + " ASC";
        String SORT_BY_DATE_ADDED = MediaContract.Movies.DATEADDED + " DESC";
        String SORT_BY_NAME_IGNORE_ARTICLES = MediaDatabase.sortCommonTokens(MediaContract.Movies.TITLE) + " ASC";

        final int ID = 0;
        final int MOVIEID = 1;
        final int TITLE = 2;
        final int THUMBNAIL = 3;
        final int YEAR = 4;
        final int GENRES = 5;
        final int RUNTIME = 6;
        final int RATING = 7;
        final int TAGLINE = 8;
    }

    private static class MoviesAdapter extends CursorAdapter {

        private HostManager hostManager;
        private int artWidth, artHeight;

        public MoviesAdapter(Context context) {
            super(context, null, false);
            this.hostManager = HostManager.getInstance(context);

            // Get the art dimensions
            // Use the same dimensions as in the details fragment, so that it hits Picasso's cache when
            // the user transitions to that fragment, avoiding another call and imediatelly showing the image


            artWidth = (int)(resources.getDimension(R.dimen.now_playing_poster_width) /
                    UIUtils.IMAGE_RESIZE_FACTOR);
            artHeight = (int)(resources.getDimension(R.dimen.now_playing_poster_height) /
                    UIUtils.IMAGE_RESIZE_FACTOR);
        }

        /** {@inheritDoc} */
        @Override
        public View newView(Context context, final Cursor cursor, ViewGroup parent) {
            final View view = LayoutInflater.from(context)
                    .inflate(R.layout.grid_item_movie_new, parent, false);

            // Setup View holder pattern
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView)view.findViewById(R.id.title);
            viewHolder.detailsView = (TextView)view.findViewById(R.id.details);
//            viewHolder.yearView = (TextView)view.findViewById(R.id.year);
            viewHolder.durationView = (TextView)view.findViewById(R.id.duration);
            viewHolder.artView = (ImageView)view.findViewById(R.id.art);

            view.setTag(viewHolder);
            return view;
        }

        /** {@inheritDoc} */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final ViewHolder viewHolder = (ViewHolder)view.getTag();
            //String tag=cursor.getString(MovieListQuery.TAG);
            // Save the movie id
            viewHolder.movieId = cursor.getInt(MovieListQuery.MOVIEID);
            viewHolder.movieTitle = cursor.getString(MovieListQuery.TITLE);

            viewHolder.titleView.setText(viewHolder.movieTitle);
            //String details = TextUtils.isEmpty(cursor.getString(MovieListQuery.TAGLINE)) ?
            //      cursor.getString(MovieListQuery.GENRES) :
            //     cursor.getString(MovieListQuery.TAGLINE);
            viewHolder.detailsView.setText("("+cursor.getString(MovieListQuery.GENRES)+")");
//            viewHolder.yearView.setText(String.valueOf(cursor.getInt(MovieListQuery.YEAR)));
            int runtime = cursor.getInt(MovieListQuery.RUNTIME) / 60;
            if (runtime>0) {
                String duration = String.format(context.getString(R.string.minutes_abbrev), String.valueOf(runtime));
                viewHolder.durationView.setText(duration);
            }



            int artHeight = resources.getDimensionPixelOffset(R.dimen.now_playing_art_height);
            UIUtils.loadImageIntoImageview(hostManager,
                    cursor.getString(MovieListQuery.THUMBNAIL),
                    viewHolder.artView, displayMetrics.widthPixels, artHeight);

            //UIUtils.loadImageWithCharacterAvatar(context, hostManager,
            //        cursor.getString(MovieListQuery.THUMBNAIL), viewHolder.movieTitle,
            //        viewHolder.artView, artWidth, artHeight);
        }
    }

    /**
     * View holder pattern
     */
    private static class ViewHolder {
        TextView titleView;
        TextView detailsView;
        //        TextView yearView;
        TextView durationView;
        ImageView artView;

        int movieId;
        String movieTitle;
    }
}
