package com.ipi.garageplus.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipi.garageplus.R;
import com.ipi.garageplus.model.ServiceRecord;

import java.util.ArrayList;
import java.util.List;

public class ServiceRecordAdapter extends RecyclerView.Adapter<ServiceRecordAdapter.ServiceViewHolder> {

    public interface OnServiceClickListener {
        void onServiceLongClick(ServiceRecord record);
    }

    private final OnServiceClickListener listener;
    private final List<ServiceRecord> records = new ArrayList<>();

    public ServiceRecordAdapter(OnServiceClickListener listener) {
        this.listener = listener;
    }

    public void setRecords(List<ServiceRecord> newRecords) {
        records.clear();
        if (newRecords != null) {
            records.addAll(newRecords);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_record, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.bind(records.get(position));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipServisa, tvCijena, tvOpis, tvDatum, tvKilometraza;

        ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipServisa = itemView.findViewById(R.id.tvTipServisa);
            tvCijena = itemView.findViewById(R.id.tvCijena);
            tvOpis = itemView.findViewById(R.id.tvOpis);
            tvDatum = itemView.findViewById(R.id.tvDatum);
            tvKilometraza = itemView.findViewById(R.id.tvKilometraza);
        }

        void bind(ServiceRecord record) {
            tvTipServisa.setText(record.getTipServisa());
            tvCijena.setText(String.format("%.2f KM", record.getCijena()));
            tvOpis.setText(record.getOpis());
            tvDatum.setText(record.getDatum());
            tvKilometraza.setText(record.getKilometrazaServisa() + "km");

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onServiceLongClick(record);
                }
                return true;
            });
        }
    }
}