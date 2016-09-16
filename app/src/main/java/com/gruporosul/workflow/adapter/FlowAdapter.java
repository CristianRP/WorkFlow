package com.gruporosul.workflow.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gruporosul.workflow.R;
import com.gruporosul.workflow.bean.Item;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by soporte on 11/11/2015.
 */
public class FlowAdapter extends RecyclerView.Adapter<FlowAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ViewHolder item, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener {

        public CircleImageView imgItem;
        public TextView txtItem;

        private FlowAdapter parent = null;

        public ViewHolder(View v, FlowAdapter parent) {
            super(v);

            v.setOnClickListener(this);
            this.parent = parent;

            imgItem = (CircleImageView) v.findViewById(R.id.imgItem);
            txtItem = (TextView) v.findViewById(R.id.txtItem);

        }

        @Override
        public void onClick(View view) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition());
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return Item.ITEMS.get(position).getId();
    }

    public FlowAdapter() {

    }

    @Override
    public int getItemCount() {
        Log.e("FA-71", Item.ITEMS.size() + "");
        return Item.ITEMS.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item, parent, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = Item.ITEMS.get(position);

        holder.txtItem.setText(item.getTexto());
        Glide.with(holder.imgItem.getContext())
                .load(item.getImagen())
                .centerCrop()
                .into(holder.imgItem);

    }
}