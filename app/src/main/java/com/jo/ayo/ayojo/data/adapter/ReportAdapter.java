package com.jo.ayo.ayojo.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.data.model.post.list.Report;
import com.jo.ayo.ayojo.ui.postdetail.PostDetailActivity;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private List<Report> reportList;

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }

    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReportAdapter.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        String houseOwner = reportList.get(position).getName();
        String date = reportList.get(position).getCreatedAt();
        String id = reportList.get(position).getId();

        holder.tvHouseOwner.setText(houseOwner);
        holder.tvDate.setText(date);
        holder.setItemClickListener((v, pos) -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("ID", id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvHouseOwner, tvDate;
        ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            tvHouseOwner = itemView.findViewById(R.id.tvItemHouseOwner);
            tvDate = itemView.findViewById(R.id.tvItemDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(view, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;

        }
    }
}
