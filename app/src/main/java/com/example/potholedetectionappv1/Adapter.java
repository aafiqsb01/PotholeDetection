package com.example.potholedetectionappv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    ArrayList<PotholeClass> arrayList;
    Context context;

    public Adapter(ArrayList<PotholeClass> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_design, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        holder.screen_DateLine.setText(arrayList.get(position).Date);
        holder.screen_DateTime.setText(arrayList.get(position).Time);
        holder.screen_DateLatitude.setText(arrayList.get(position).Latitude);
        holder.screen_Longitude.setText(arrayList.get(position).Longitude);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView screen_DateLine;
        TextView screen_DateTime;
        TextView screen_DateLatitude;
        TextView screen_Longitude;
        RelativeLayout relativeLayout_DateLine;
        RelativeLayout relativeLayout_DescriptionLine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            screen_DateLine = itemView.findViewById(R.id.reportDate);
            screen_DateTime = itemView.findViewById(R.id.reportDateTime);
            screen_Longitude = itemView.findViewById(R.id.reportDateLongitude);
            screen_DateLatitude = itemView.findViewById(R.id.reportDateLatitude);
            relativeLayout_DateLine = itemView.findViewById(R.id.reportDate_Line);
            relativeLayout_DescriptionLine = itemView.findViewById(R.id.reportDate_Description);
        }
    }
}
