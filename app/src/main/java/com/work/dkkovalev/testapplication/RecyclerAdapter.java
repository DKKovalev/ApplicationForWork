package com.work.dkkovalev.testapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {

    private List<Point> pointList;
    private Context context;
    private OnRecyclerItemClick recyclerItemClick;

    public RecyclerAdapter(List<Point> points, Context context) {
        super();
        this.pointList = points;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view, context);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Point point = pointList.get(position);
        holder.titleTV.setText(point.getTitle());
        holder.descriptionTV.setText(point.getDescription());
        holder.latTV.setText(String.valueOf(point.getLat()));
        holder.lngTV.setText(String.valueOf(point.getLng()));
    }

    @Override
    public int getItemCount() {
        return pointList.size();
    }

    public void setClickListener(OnRecyclerItemClick clickListener) {
        this.recyclerItemClick = clickListener;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private TextView titleTV;
        private TextView descriptionTV;
        private TextView latTV;
        private TextView lngTV;

        private Context context;

        public CustomViewHolder(View itemView, Context context) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            titleTV = (TextView) itemView.findViewById(R.id.tv_title);
            descriptionTV = (TextView) itemView.findViewById(R.id.tv_description);
            latTV = (TextView) itemView.findViewById(R.id.tv_lat);
            lngTV = (TextView) itemView.findViewById(R.id.tv_lng);
            this.context = context;
        }

        @Override
        public boolean onLongClick(View v) {
            if (recyclerItemClick != null) {
                recyclerItemClick.itemClicked(v, getAdapterPosition());
                //removeAt(getAdapterPosition());
            }
            return true;
        }

        public void removeAt(int position) {
            pointList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(getAdapterPosition(), pointList.size());
        }
    }

    public interface OnRecyclerItemClick {
        void itemClicked(View view, int pos);
    }
}
