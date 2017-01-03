package com.nutlabs.keonics.Temporary_sub_chapter_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

//import com.nutlabs.keonics.Activity.ComputerSkillsActivity;
import com.nutlabs.keonics.Activity.ComputerSkillsActivity;
import com.nutlabs.keonics.Activity.EnglishEducatonActivity;
import com.nutlabs.keonics.Activity.FarmingAndFishingActivity;
import com.nutlabs.keonics.Activity.HealthActivity;
import com.nutlabs.keonics.Activity.KhanAcademyActivity;
import com.nutlabs.keonics.Activity.MCQTestActivity;
import com.nutlabs.keonics.Activity.NdlmActivity;
import com.nutlabs.keonics.Activity.SafetyAndSecurityActivity;
import com.nutlabs.keonics.Activity.VocationalSkillsActivity;
import com.nutlabs.keonics.Activity.WikipediaActivity;
import com.nutlabs.keonics.R;
import com.nutlabs.keonics.SharedConstants;

/**
 * Created by Shubham on 9/17/2016.
 */
public class Activity1GridView extends AppCompatActivity {
    Toolbar androidToolbar;
   // private NavigationDrawerFragment navigationDrawerFragment;

    CollapsingToolbarLayout mCollapsingToolbarLayout;
   // DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    CoordinatorLayout mRootLayout;
    GridView mGridView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.testing_mainlayout_tiles);
//        navigationDrawerFragment.setDrawerIndicatorEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

       // actionBar.setDisplayHomeAsUpEnabled(true);


            actionBar.setTitle(R.string.app_name);

     /*   navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        navigationDrawerFragment.setDrawerIndicatorEnabled(true);
*/

        ImageView button = (ImageView) findViewById(R.id.computer_skills);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1GridView.this
                        ,ComputerSkillsActivity.class);
                intent.putExtra(SharedConstants.BASE_TAG_STRING,"computerskills");
                startActivity(intent);
            }
        });


        ImageView button1 = (ImageView) findViewById(R.id.english_education);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1GridView.this, EnglishEducatonActivity.class);
                intent.putExtra(SharedConstants.BASE_TAG_STRING,"englishandlifeskills");
                startActivity(intent);
            }
        });

        ImageView button2 = (ImageView) findViewById(R.id.vocational_skills);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1GridView.this, VocationalSkillsActivity.class);
                intent.putExtra(SharedConstants.BASE_TAG_STRING,"learnaboutjobs");
                startActivity(intent);
            }
        });
        ImageView button3 = (ImageView) findViewById(R.id.fishing_and_farming);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1GridView.this, FarmingAndFishingActivity.class);
                intent.putExtra(SharedConstants.BASE_TAG_STRING, "fishingandfarming");
                startActivity(intent);
            }
        });
        ImageView button4 = (ImageView) findViewById(R.id.oriyacontent);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1GridView.this, NdlmActivity.class);
                intent.putExtra(SharedConstants.BASE_TAG_STRING, "NDLM");
                startActivity(intent);
            }
        });
      /*  ImageView button5 = (ImageView) findViewById(R.id.safety_and_security);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1GridView.this, SafetyAndSecurityActivity.class);
                intent.putExtra(SharedConstants.BASE_TAG_STRING,"safetyandsecurity");
                startActivity(intent);
            }
        });*/

      /*  ImageView button7 = (ImageView) findViewById(R.id.health);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1GridView.this, HealthActivity.class);
                intent.putExtra(SharedConstants.BASE_TAG_STRING,"health");
                startActivity(intent);
            }
        });*/
        ImageView button8 = (ImageView) findViewById(R.id.wikipedia);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1GridView.this, WikipediaActivity.class);
                startActivity(intent);
            }
        });
      /*  ImageView button9 = (ImageView) findViewById(R.id.khan_academy);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1GridView.this, KhanAcademyActivity.class);
                startActivity(intent);

            }
        });*/
    }



}