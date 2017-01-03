package com.nutlabs.keonics.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nutlabs.keonics.FeedbackActivity;
import com.nutlabs.keonics.R;
import com.nutlabs.keonics.SharedConstants;
import com.nutlabs.keonics.Activity.ComputerSkillsActivity;
import com.nutlabs.keonics.Temporary_sub_chapter_Activities.Activity1GridView;


public class NewNdlmMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newndlm_mainactivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

      // actionBar.setDisplayHomeAsUpEnabled(true);


        actionBar.setTitle(R.string.app_name);

        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNdlmMainActivity.this, Activity1GridView.class);
                startActivity(intent);
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.thumbnail);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNdlmMainActivity.this, SafetyAndSecurityActivity.class);
                intent.putExtra(SharedConstants.BASE_TAG_STRING,"safetyandsecurity");
                startActivity(intent);
            }
        });
        Button button1 = (Button) findViewById(R.id.btn2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNdlmMainActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
        ImageView imageView1 = (ImageView) findViewById(R.id.thumbnail1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNdlmMainActivity.this, Activity1GridView.class);
                startActivity(intent);
            }
        });
        Button button2 = (Button) findViewById(R.id.btn1);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNdlmMainActivity.this, SafetyAndSecurityActivity.class);
                intent.putExtra(SharedConstants.BASE_TAG_STRING,"safetyandsecurity");
                startActivity(intent);
            }
        });
        ImageView imageView2 = (ImageView) findViewById(R.id.thumbnail2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNdlmMainActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }

}
