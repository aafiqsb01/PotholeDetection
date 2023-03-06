package com.example.potholedetectionappv1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.VersionVH> {

    List<PotholeClass> potholeList;

    public ReportsAdapter(List<PotholeClass> potholeList) {
        this.potholeList = potholeList;
    }

    @NonNull
    @Override
    public ReportsAdapter.VersionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new VersionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.VersionVH holder, int position) {
        PotholeClass potholeReport = potholeList.get(position);
        holder.label_reportDate.setText(potholeReport.getDate());
        holder.label_reportTime.setText(potholeReport.getTime());
        holder.label_reportLatitude.setText(potholeReport.getLatitude());
        holder.label_reportLongitude.setText(potholeReport.getLongitude());

        boolean isExpandable = potholeList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return potholeList.size();
    }

    public class VersionVH extends RecyclerView.ViewHolder {

        TextView label_reportDate, label_reportTime, label_reportLatitude, label_reportLongitude;
        RelativeLayout linearLayout;
        RelativeLayout expandableLayout;

        public VersionVH(@NonNull View itemView) {
            super(itemView);

            label_reportDate = itemView.findViewById(R.id.reportDate);
            label_reportTime = itemView.findViewById(R.id.reportDateTime);
            label_reportLatitude = itemView.findViewById(R.id.reportDateLatitude);
            label_reportLongitude = itemView.findViewById(R.id.reportDateLongitude);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_Layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PotholeClass potholeClass = potholeList.get(getAdapterPosition());
                    potholeClass.setExpandable(!potholeClass.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
