package com.gruporosul.workflow.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruporosul.workflow.R;
import com.gruporosul.workflow.bean.Agrupador;

/**
 * Created by soporte on 17/11/2015.
 */
public class GrouperAdapter extends RecyclerView.Adapter<GrouperAdapter.ViewHolder> {

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

        public TextView txtAgrupador;

        private GrouperAdapter parent = null;

        public ViewHolder(View v, GrouperAdapter parent) {
            super(v);

            v.setOnClickListener(this);
            this.parent = parent;

            txtAgrupador = (TextView) v.findViewById(R.id.txtAgrupador);

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
        return Integer.parseInt(Agrupador.AGRUPADORES.get(position).getCodFlujo());
    }

    public GrouperAdapter() {

    }

    @Override
    public int getItemCount() {
        Log.e("GA-71", Agrupador.AGRUPADORES.size() + "");
        return Agrupador.AGRUPADORES.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_agrupador, parent, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Agrupador agrupador = Agrupador.AGRUPADORES.get(position);

        holder.txtAgrupador.setText(agrupador.getAgrupador());


    }

}
