package com.nutlabs.keonics.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.nutlabs.keonics.R;
import com.nutlabs.keonics.Temporary_sub_chapter_Activities.Activity1GridView;

/**
 * Created by Shubham on 9/29/2016.
 */
public class KhanAcademyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.khan_academy_activity);
        final ProgressDialog pd = ProgressDialog.show(KhanAcademyActivity.this, "", "Loading.", true);

        final WebView wv1=(WebView)findViewById(R.id.webview);
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // wv1.setWebViewClient(new MyBrowser());
        wv1.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(KhanAcademyActivity.this,description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
                String webUrl = wv1.getUrl();

            }
        });

        wv1.loadUrl("http://192.168.0.1:8008/learn/khan/");


      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);


        actionBar.setTitle(R.string.app_name);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem menu1 = menu.add("HOME");
        menu1.setIcon(R.drawable.home_icon);
        menu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(KhanAcademyActivity.this, Activity1GridView.class);
                startActivity(intent);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
       /* if (!mWebView.onBackPressed()) { return; }*/
        // ...
        super.onBackPressed();
    }


}
