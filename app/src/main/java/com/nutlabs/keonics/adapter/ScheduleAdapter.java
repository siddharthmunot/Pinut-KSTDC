package com.nutlabs.keonics.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutlabs.keonics.R;
import com.nutlabs.keonics.utils.Schedulegeter;

import java.util.List;

/**
 * Created by Shubham on 10/20/2016.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>
{
private Context mContext;
    private List<Schedulegeter> listschedule;
    private RecyclerView mRecyclerView;
    int[] gridColor ={

             Color.rgb(174,184,87),
            Color.rgb(51,182,121),
            Color.rgb(223,89,72),
            Color.rgb(133,94,134),
            Color.rgb(174,184,87),
            Color.rgb(51,182,121),
            Color.rgb(223,89,72),
             Color.rgb(174,184,87),
            Color.rgb(51,182,121),
            Color.rgb(223,89,72),
            Color.rgb(174,184,87),
            Color.rgb(51,182,121),
            Color.rgb(223,89,72),
            Color.rgb(133,94,134),
            Color.rgb(174,184,87),
            Color.rgb(51,182,121),
            Color.rgb(223,89,72),
            Color.rgb(174,184,87),
            Color.rgb(51,182,121),
            Color.rgb(223,89,72),

    };

    public ScheduleAdapter(List<Schedulegeter> listschedule, Context mContext){
        this.listschedule = listschedule;
        this.mContext = mContext;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;


        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_schedule_adapter, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);

        return myViewHolder;
    }
/*public int getRandomColor() {
    //Random rnd = new Random();
   *//* int[] androidColors = mContext.getResources().getIntArray(R.array.character_avatar_colors);
    int randomAndroidColor = androidColors[R.array.character_avatar_colors];*//*
    int[] androidColor = {R.array.character_avatar_colors};
    for (int i = 0; i < androidColor.length; i++) {
        // randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        // int androidColor = androidColors[i];
        return androidColor[i];
    }
    return ;
    // return randomAndroidColor;
}*/
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Schedulegeter schedule = listschedule.get(position);
        holder.textViewVenue.setText(schedule.getVenue());
        holder.textViewTime.setText(schedule.getTime());
        holder.textViewDescription.setText(schedule.getDescription());
         holder.textViewTime.setBackgroundColor(gridColor[position]);
           // holder.imageView.setBackgroundColor(gridColor[position]);


    }

    @Override
    public int getItemCount() {
        return listschedule.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewVenue;
        public TextView textViewTime;
        public TextView textViewDescription;
        public ImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            textViewDescription = (TextView) view.findViewById(R.id.description);
            textViewTime = (TextView) view.findViewById(R.id.time);
            textViewVenue =(TextView) view.findViewById(R.id.venue);
            imageView = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }
}
