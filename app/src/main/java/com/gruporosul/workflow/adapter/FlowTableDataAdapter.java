package com.gruporosul.workflow.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gruporosul.workflow.R;
import com.gruporosul.workflow.bean.Flow;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by soporte on 12/11/2015.
 */
public class FlowTableDataAdapter extends TableDataAdapter<Flow> {

    private static final int TEXT_SIZE = 14;

    public FlowTableDataAdapter(Context context, List<Flow> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {

        Flow item = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderIdFlujo(item);
                break;
            case 1:
                renderedView = renderDescripcion(item);
                break;
            case 2:
                renderedView = renderCumplimiento(item, parentView);
                break;
        }

        return renderedView;

    }

    private View renderIdFlujo(Flow item) {

        String idFlujo = item.getIdFlujo();

        TextView textView = new TextView(getContext());
        textView.setText(idFlujo);
        textView.setTextColor(getColor(getContext(), R.color.textColorPrimary));
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);

        return textView;

    }

    private View renderDescripcion(Flow item) {

        String descripcion = item.getDescripcion();

        TextView textView = new TextView(getContext());
        textView.setText(descripcion);
        textView.setTextColor(getColor(getContext(), R.color.textColorPrimary));
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);

        return textView;

    }

    private View renderCumplimiento(Flow item, ViewGroup parentView) {

        View view = getLayoutInflater().inflate(R.layout.table_cell_cumplimiento, parentView, false);

        TextView diasActuales = (TextView) view.findViewById(R.id.kw_view);
        TextView diasTotales = (TextView) view.findViewById(R.id.ps_view);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.parentCell);
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.circleCumplimiento);

        switch (item.getCumplimiento()) {
            case "En tiempo":
                setImagen(circleImageView, R.drawable.en_tiempo, getContext());
                //relativeLayout.setBackgroundColor(getColor(getContext(), R.color.en_tiempo));
                break;
            case "Por vencer":
                setImagen(circleImageView, R.drawable.por_vencer, getContext());
                //relativeLayout.setBackgroundColor(getColor(getContext(), R.color.por_vencer));
                break;
            case "Vencido":
                setImagen(circleImageView, R.drawable.vencido, getContext());
                //relativeLayout.setBackgroundColor(getColor(getContext(), R.color.vencido));
                break;
            default:
                setImagen(circleImageView, R.drawable.dot, getContext());
                //relativeLayout.setBackgroundColor(getColor(getContext(), R.color.windowBackground));
                break;
        }
        diasActuales.setText(item.getDiasActuales());
        diasActuales.setTextColor(getColor(getContext(), R.color.textColorPrimary));
        diasTotales.setText(item.getTotalDias());
        diasTotales.setTextColor(getColor(getContext(), R.color.textColorPrimary));

        return view;

    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static final Drawable getImagen(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static void setImagen(CircleImageView circleImageView, int imagen, Context context) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < Build.VERSION_CODES.LOLLIPOP) {
            circleImageView.setBackgroundResource(imagen);
        } else {
            circleImageView.setBackgroundDrawable(getImagen(context.getApplicationContext(), imagen));
        }
    }

}
