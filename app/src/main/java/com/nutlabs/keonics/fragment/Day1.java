package com.nutlabs.keonics.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nutlabs.keonics.R;
import com.nutlabs.keonics.adapter.ScheduleAdapter;
import com.nutlabs.keonics.utils.Config;
import com.nutlabs.keonics.utils.Schedulegeter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Day1 extends Fragment {

    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<Schedulegeter> listofFav;

    private RecyclerView.LayoutManager layoutManager;
    private View mRootView;
    private Context mContext;
    public boolean isGrid;
    RecyclerView.LayoutManager mLayoutManager;
    String[] gridColor ={

            "#008B8B",
            "#00FF00",
            "#48D1CC",
            "#556B2F",
            "#696969",
            "#6B8E23",
            "#8FBC8F",
            "#AFEEEE",
            "#B8860B",
            "#BDB76B",
            "#D8BFD8",
            "#DEB887",
            "#FFFF00",
            "#FFF0F5",
            "#EE82EE",
            "#DC143C",
            "#C0C0C0"
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_day1, container, false);
        mContext = getActivity();

        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);


        listofFav = new ArrayList<>();
        getData();

        return mRootView;
    }
    public void getData() {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...", false, false);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Config.URL_SCHEDULE_DAY1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        loading.dismiss();
                        JSONArray responseJson = new JSONArray();
                        try {
                            responseJson = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        parseData(responseJson);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        loading.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


        requestQueue.add(postRequest);

    }


    private void parseData(JSONArray array) {


        for (int i = 0; i < array.length(); i++) {
            Schedulegeter schedule = new Schedulegeter();
            JSONObject jsonObject = null;
            try {
                jsonObject = array.getJSONObject(i);
                schedule.setTime(jsonObject.getString(Config.TAG_TIME));
                schedule.setDescription(jsonObject.getString(Config.TAG_DESCRIPTION));
                schedule.setVenue(jsonObject.getString(Config.TAG_VENUE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listofFav.add(schedule);
        }
        adapter = new ScheduleAdapter(listofFav, getActivity());
        //recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);

    }
}
