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

    private List<ServiceRecord> records = new ArrayList<>();
    private OnServiceClickListener listener;

    public interface OnServiceClickListener {
        void onServiceLongClick(ServiceRecord record);
    }

    public ServiceRecordAdapter(OnServiceClickListener listener) {
        this.listener = listener;
    }

    public void setRecords(List<ServiceRecord> records) {
        this.records = records;
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
        ServiceRecord record = records.get(position);
        holder.tvTipServisa.setText(record.getTipServisa());
        holder.tvOpis.setText(record.getOpis());
        holder.tvDatum.setText(record.getDatum());
        holder.tvCijena.setText(String.format("%.2f KM", record.getCijena()));
        holder.tvKilometraza.setText(record.getKilometrazaServisa() + " km");

        holder.itemView.setOnLongClickListener(v -> {
            listener.onServiceLongClick(record);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipServisa, tvOpis, tvDatum, tvCijena, tvKilometraza;

        ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipServisa = itemView.findViewById(R.id.tvTipServisa);
            tvOpis = itemView.findViewById(R.id.tvOpis);
            tvDatum = itemView.findViewById(R.id.tvDatum);
            tvCijena = itemView.findViewById(R.id.tvCijena);
            tvKilometraza = itemView.findViewById(R.id.tvKilometraza);
        }
    }
}