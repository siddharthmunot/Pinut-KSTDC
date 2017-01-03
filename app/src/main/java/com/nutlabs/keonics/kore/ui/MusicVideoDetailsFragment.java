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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.nutlabs.keonics.R;
import com.nutlabs.keonics.kore.Settings;
import com.nutlabs.keonics.kore.jsonrpc.ApiCallback;
import com.nutlabs.keonics.kore.jsonrpc.event.MediaSyncEvent;
import com.nutlabs.keonics.kore.jsonrpc.method.Player;
import com.nutlabs.keonics.kore.jsonrpc.method.Playlist;
import com.nutlabs.keonics.kore.jsonrpc.type.PlaylistType;
import com.nutlabs.keonics.kore.provider.MediaContract;
import com.nutlabs.keonics.kore.service.LibrarySyncService;
import com.nutlabs.keonics.kore.utils.FileDownloadHelper;
import com.nutlabs.keonics.kore.utils.LogUtils;
import com.nutlabs.keonics.kore.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Presents music videos details
 */
public class MusicVideoDetailsFragment extends AbstractDetailsFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = LogUtils.makeLogTag(MusicVideoDetailsFragment.class);

    public static final String MUSICVIDEOID = "music_video_id";

    // Loader IDs
    private static final int LOADER_MUSIC_VIDEO = 0;

    /**
     * Handler on which to post RPC callbacks
     */
    private Handler callbackHandler = new Handler();

    // Displayed music video id
    private int musicVideoId = -1;

    // Info for downloading the music video
    private FileDownloadHelper.MusicVideoInfo musicVideoDownloadInfo = null;

    // Controls whether the finished refreshing message is shown
    private boolean showRefreshStatusMessage = true;

    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @InjectView(R.id.exit_transition_view)
    View exitTransitionView;
    // Buttons
    @InjectView(R.id.fab)
    ImageButton fabButton;
    @InjectView(R.id.add_to_playlist)
    ImageButton addToPlaylistButton;
    //@InjectView(R.id.download) ImageButton downloadButton;

    // Detail views
    @InjectView(R.id.media_panel)
    ScrollView mediaPanel;

    @InjectView(R.id.art)
    ImageView mediaArt;
    @InjectView(R.id.poster)
    ImageView mediaPoster;

    @InjectView(R.id.media_title)
    TextView mediaTitle;
    @InjectView(R.id.media_undertitle)
    TextView mediaUndertitle;

    @InjectView(R.id.year)
    TextView mediaYear;
    @InjectView(R.id.genres)
    TextView mediaGenres;

    @InjectView(R.id.media_description)
    TextView mediaDescription;

    /**
     * Create a new instance of this, initialized to show the video musicVideoId
     */
    public static MusicVideoDetailsFragment newInstance(int musicVideoId) {
        MusicVideoDetailsFragment fragment = new MusicVideoDetailsFragment();

        Bundle args = new Bundle();
        args.putInt(MUSICVIDEOID, musicVideoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        musicVideoId = getArguments().getInt(MUSICVIDEOID, -1);

        if (musicVideoId == -1) {
            // There's nothing to show
            return null;
        }

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_music_video_details, container, false);
        ButterKnife.inject(this, root);

        //UIUtils.setSwipeRefreshLayoutColorScheme(swipeRefreshLayout);

        // Setup dim the fanart when scroll changes. Full dim on 4 * iconSize dp
        Resources resources = getActivity().getResources();
        final int pixelsToTransparent  = 4 * resources.getDimensionPixelSize(R.dimen.default_icon_size);
        mediaPanel.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                float y = mediaPanel.getScrollY();
                float newAlpha = Math.min(1, Math.max(0, 1 - (y / pixelsToTransparent)));
                mediaArt.setAlpha(newAlpha);
            }
        });

        FloatingActionButton fab = (FloatingActionButton)fabButton;
        fab.attachToScrollView((ObservableScrollView) mediaPanel);

        // Pad main content view to overlap with bottom system bar
//        UIUtils.setPaddingForSystemBars(getActivity(), mediaPanel, false, false, true);
//        mediaPanel.setClipToPadding(false);

        return root;
    }

    @Override
    protected String getSyncType() {
        return LibrarySyncService.SYNC_ALL_MUSIC_VIDEOS;
    }

    @Override
    protected String getSyncID() {
        return null;
    }

    @Override
    protected int getSyncItemID() {
        return -1;
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Start the loaders
        getLoaderManager().initLoader(LOADER_MUSIC_VIDEO, null, this);
    }

    @Override
    public void onResume() {
        // Force the exit view to invisible
        exitTransitionView.setVisibility(View.INVISIBLE);
        super.onResume();
    }

    @Override
    protected void onSyncProcessEnded(MediaSyncEvent event) {
        if (event.status == MediaSyncEvent.STATUS_SUCCESS) {
            getLoaderManager().restartLoader(LOADER_MUSIC_VIDEO, null, this);
        }
    }

    /**
     * Loader callbacks
     */
    /** {@inheritDoc} */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri;
        switch (i) {
            case LOADER_MUSIC_VIDEO:
                uri = MediaContract.MusicVideos.buildMusicVideoUri(getHostInfo().getId(), musicVideoId);
                return new CursorLoader(getActivity(), uri,
                        MusicVideoDetailsQuery.PROJECTION, null, null, null);
            default:
                return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            switch (cursorLoader.getId()) {
                case LOADER_MUSIC_VIDEO:
                    displayMusicVideoDetails(cursor);
                    break;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // Release loader's data
    }

    /**
     * Callbacks for button bar
     */
    @OnClick(R.id.fab)
    public void onFabClicked(View v) {
        PlaylistType.Item item = new PlaylistType.Item();
        item.musicvideoid = musicVideoId;
        Player.Open action = new Player.Open(item);
        action.execute(getHostManager().getConnection(), new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!isAdded()) return;
                // Check whether we should switch to the remote
                boolean switchToRemote = PreferenceManager
                        .getDefaultSharedPreferences(getActivity())
                        .getBoolean(Settings.KEY_PREF_SWITCH_TO_REMOTE_AFTER_MEDIA_START,
                                Settings.DEFAULT_PREF_SWITCH_TO_REMOTE_AFTER_MEDIA_START);
                if (switchToRemote) {
                    int cx = (fabButton.getLeft() + fabButton.getRight()) / 2;
                    int cy = (fabButton.getTop() + fabButton.getBottom()) / 2;
                    UIUtils.switchToRemoteWithAnimation(getActivity(), cx, cy, exitTransitionView);
                }
            }

            @Override
            public void onError(int errorCode, String description) {
                if (!isAdded()) return;
                // Got an error, show toast
                Toast.makeText(getActivity(), R.string.unable_to_connect_to_xbmc, Toast.LENGTH_SHORT)
                        .show();
            }
        }, callbackHandler);
    }

    @OnClick(R.id.add_to_playlist)
    public void onAddToPlaylistClicked(View v) {
        Playlist.GetPlaylists getPlaylists = new Playlist.GetPlaylists();

        getPlaylists.execute(getHostManager().getConnection(), new ApiCallback<ArrayList<PlaylistType.GetPlaylistsReturnType>>() {
            @Override
            public void onSuccess(ArrayList<PlaylistType.GetPlaylistsReturnType> result) {
                if (!isAdded()) return;
                // Ok, loop through the playlists, looking for the video one
                int videoPlaylistId = -1;
                for (PlaylistType.GetPlaylistsReturnType playlist : result) {
                    if (playlist.type.equals(PlaylistType.GetPlaylistsReturnType.VIDEO)) {
                        videoPlaylistId = playlist.playlistid;
                        break;
                    }
                }
                // If found, add to playlist
                if (videoPlaylistId != -1) {
                    PlaylistType.Item item = new PlaylistType.Item();
                    item.musicvideoid = musicVideoId;
                    Playlist.Add action = new Playlist.Add(videoPlaylistId, item);
                    action.execute(getHostManager().getConnection(), new ApiCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (!isAdded()) return;
                            // Got an error, show toast
                            Toast.makeText(getActivity(), R.string.item_added_to_playlist, Toast.LENGTH_SHORT)
                                    .show();
                        }

                        @Override
                        public void onError(int errorCode, String description) {
                            if (!isAdded()) return;
                            // Got an error, show toast
                            Toast.makeText(getActivity(), R.string.unable_to_connect_to_xbmc, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }, callbackHandler);
                } else {
                    Toast.makeText(getActivity(), R.string.no_suitable_playlist, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onError(int errorCode, String description) {
                if (!isAdded()) return;
                // Got an error, show toast
                Toast.makeText(getActivity(), R.string.unable_to_connect_to_xbmc, Toast.LENGTH_SHORT)
                        .show();
            }
        }, callbackHandler);
    }

   // @OnClick(R.id.download)
    public void onDownloadClicked(View v) {
        if (musicVideoDownloadInfo == null) {
            // Nothing to download
            Toast.makeText(getActivity(), R.string.no_files_to_download, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the directory exists and whether to overwrite it
        File file = new File(musicVideoDownloadInfo.getAbsoluteFilePath());
        if (file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.download)
                    .setMessage(R.string.download_file_exists)
                    .setPositiveButton(R.string.overwrite,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FileDownloadHelper.downloadFiles(getActivity(), getHostInfo(),
                                            musicVideoDownloadInfo, FileDownloadHelper.OVERWRITE_FILES,
                                            callbackHandler);
                                }
                            })
                    .setNeutralButton(R.string.download_with_new_name,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FileDownloadHelper.downloadFiles(getActivity(), getHostInfo(),
                                            musicVideoDownloadInfo, FileDownloadHelper.DOWNLOAD_WITH_NEW_NAME,
                                            callbackHandler);
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Nothing to do
                                }
                            })
                    .show();
        } else {
            FileDownloadHelper.downloadFiles(getActivity(), getHostInfo(),
                    musicVideoDownloadInfo, FileDownloadHelper.DOWNLOAD_WITH_NEW_NAME,
                    callbackHandler);
        }
    }

    /**
     * Display the video details
     *
     * @param cursor Cursor with the data
     */
    private void displayMusicVideoDetails(Cursor cursor) {
        cursor.moveToFirst();
        String musicVideoTitle = cursor.getString(MusicVideoDetailsQuery.TITLE);
        mediaTitle.setText(musicVideoTitle);
        String artistAlbum = cursor.getString(MusicVideoDetailsQuery.ARTIST) + "  |  " +
                cursor.getString(MusicVideoDetailsQuery.ALBUM);
        mediaUndertitle.setText(artistAlbum);

        int runtime = cursor.getInt(MusicVideoDetailsQuery.RUNTIME);
        String durationYear =  runtime > 0 ?
                UIUtils.formatTime(runtime) + "  |  " +
                        String.valueOf(cursor.getInt(MusicVideoDetailsQuery.YEAR)) :
                String.valueOf(cursor.getInt(MusicVideoDetailsQuery.YEAR));
        mediaYear.setText(durationYear);
        mediaGenres.setText(cursor.getString(MusicVideoDetailsQuery.GENRES));

        mediaDescription.setText(cursor.getString(MusicVideoDetailsQuery.PLOT));

        // Images
        Resources resources = getActivity().getResources();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        String fanart = cursor.getString(MusicVideoDetailsQuery.FANART),
                poster = cursor.getString(MusicVideoDetailsQuery.THUMBNAIL);

        int artHeight = resources.getDimensionPixelOffset(R.dimen.now_playing_art_height),
                artWidth = displayMetrics.widthPixels;
        if (!TextUtils.isEmpty(fanart)) {
            int posterWidth = resources.getDimensionPixelOffset(R.dimen.musicvideodetail_poster_width);
            int posterHeight = resources.getDimensionPixelOffset(R.dimen.musicvideodetail_poster_heigth);
            mediaPoster.setVisibility(View.VISIBLE);
            UIUtils.loadImageWithCharacterAvatar(getActivity(), getHostManager(),
                    poster, musicVideoTitle,
                    mediaPoster, posterWidth, posterHeight);
            UIUtils.loadImageIntoImageview(getHostManager(),
                    fanart,
                    mediaArt, artWidth, artHeight);
        } else {
            // No fanart, just present the poster
            mediaPoster.setVisibility(View.GONE);
            UIUtils.loadImageIntoImageview(getHostManager(),
                    poster,
                    mediaArt, artWidth, artHeight);
            // Reset padding
            int paddingLeft = mediaTitle.getPaddingRight(),
                    paddingRight = mediaTitle.getPaddingRight(),
                    paddingTop = mediaTitle.getPaddingTop(),
                    paddingBottom = mediaTitle.getPaddingBottom();
            mediaTitle.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            mediaUndertitle.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }





        // Setup download info
        musicVideoDownloadInfo = new FileDownloadHelper.MusicVideoInfo(
                musicVideoTitle, cursor.getString(MusicVideoDetailsQuery.FILE));

        // Check if downloaded file exists
     //   downloadButton.setVisibility(View.VISIBLE);
        if (musicVideoDownloadInfo.downloadFileExists()) {
            Resources.Theme theme = getActivity().getTheme();
            TypedArray styledAttributes = theme.obtainStyledAttributes(new int[]{
                    R.attr.colorAccent});
       //     downloadButton.setColorFilter(
         //           styledAttributes.getColor(0,
           //                 getActivity().getResources().getColor(R.color.accent_default)));
            styledAttributes.recycle();
        } else {
            //downloadButton.clearColorFilter();
        }
    }

    /**
     * Video details query parameters.
     */
    private interface MusicVideoDetailsQuery {
        String[] PROJECTION = {
                BaseColumns._ID,
                MediaContract.MusicVideos.TITLE,
                MediaContract.MusicVideos.ALBUM,
                MediaContract.MusicVideos.ARTIST,
                MediaContract.MusicVideos.THUMBNAIL,
                MediaContract.MusicVideos.FANART,
                MediaContract.MusicVideos.YEAR,
                MediaContract.MusicVideos.GENRES,
                MediaContract.MusicVideos.RUNTIME,
                MediaContract.MusicVideos.PLOT,
                MediaContract.MusicVideos.FILE,
        };

        final int ID = 0;
        final int TITLE = 1;
        final int ALBUM = 2;
        final int ARTIST = 3;
        final int THUMBNAIL =4;
        final int FANART = 5;
        final int YEAR = 6;
        final int GENRES = 7;
        final int RUNTIME = 8;
        final int PLOT = 9;
        final int FILE = 10;
    }
}
