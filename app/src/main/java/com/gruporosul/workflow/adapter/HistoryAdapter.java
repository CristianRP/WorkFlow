package com.gruporosul.workflow.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gruporosul.workflow.R;
import com.gruporosul.workflow.bean.FlowDetail;

/**
 * Created by soporte on 24/11/2015.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

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

        public TextView tituloPaso;
        public TextView fechaInicial;
        public TextView fechaFinal;
        public TextView diasEje;
        public TextView duracionPres;
        public TextView estado;
        public TextView usuario;
        public RelativeLayout relativeLayout;

        private HistoryAdapter parent = null;

        public ViewHolder(View v, HistoryAdapter parent) {
            super(v);

            v.setOnClickListener(this);
            this.parent = parent;

            tituloPaso = (TextView) v.findViewById(R.id.txtPaso);
            fechaInicial = (TextView) v.findViewById(R.id.txtFechaInicial);
            fechaFinal = (TextView) v.findViewById(R.id.txtFechaFinal);
            diasEje = (TextView) v.findViewById(R.id.txtDiasEje);
            duracionPres = (TextView) v.findViewById(R.id.txtDiasPres);
            estado = (TextView) v.findViewById(R.id.txtEstado);
            usuario = (TextView) v.findViewById(R.id.txtUsuario);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeDetalle);

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
        return Integer.parseInt(FlowDetail.DETALLE.get(position).getCorrelativo());
    }

    public HistoryAdapter() {

    }

    @Override
    public int getItemCount() {
        //Log.e("HA-79", FlowDetail.DETALLE.size() + "");
        return FlowDetail.DETALLE.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_flow_detail, parent, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FlowDetail flowDetail = FlowDetail.DETALLE.get(position);

        holder.tituloPaso.setText(flowDetail.getDescripcion());
        holder.fechaInicial.setText("Fecha Inicial: " + "\n" + flowDetail.getFechaInicial().replace(" ", "\n"));
        holder.fechaFinal.setText("Fecha Final: " + "\n" + flowDetail.getFechaFinal().replace(" ", "\n"));
        holder.diasEje.setText("Dias Ejecu.: " + "\n" + flowDetail.getEjectuadoDias());
        holder.duracionPres.setText("Dias Presup.: " + "\n" + flowDetail.getDuracionPresupuestada());
        holder.estado.setText("Estado: " + "\n" +  flowDetail.getEstado());
        holder.usuario.setText("Usuario: " + "\n" + flowDetail.getUsuario());

        if (Integer.parseInt(flowDetail.getEjectuadoDias()) > Integer.parseInt(flowDetail.getDuracionPresupuestada())) {
            holder.relativeLayout.setBackgroundResource(R.color.vencido);
        } else if (Integer.parseInt(flowDetail.getEjectuadoDias()) - Integer.parseInt(flowDetail.getDuracionPresupuestada()) == 0) {
            holder.relativeLayout.setBackgroundResource(R.color.windowBackground);
        } else {
            holder.relativeLayout.setBackgroundResource(R.color.en_tiempo);
        }

    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

}
