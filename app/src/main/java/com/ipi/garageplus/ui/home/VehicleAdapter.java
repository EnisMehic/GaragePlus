package com.ipi.garageplus.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ipi.garageplus.R;
import com.ipi.garageplus.model.Vehicle;
import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private List<Vehicle> vehicles = new ArrayList<>();
    private OnVehicleClickListener listener;

    public interface OnVehicleClickListener {
        void onVehicleClick(Vehicle vehicle);
        void onVehicleLongClick(Vehicle vehicle);
    }

    public VehicleAdapter(OnVehicleClickListener listener) {
        this.listener = listener;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        holder.tvMarkaModel.setText(vehicle.getMarka() + " " + vehicle.getModel());
        holder.tvGodina.setText("Godina: " + vehicle.getGodina());
        holder.tvKilometraza.setText("Kilometraža: " + vehicle.getKilometraza() + " km");
        holder.tvRegistracija.setText("Reg: " + vehicle.getRegistracija());

        holder.itemView.setOnClickListener(v -> listener.onVehicleClick(vehicle));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onVehicleLongClick(vehicle);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView tvMarkaModel, tvGodina, tvKilometraza, tvRegistracija;

        VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMarkaModel = itemView.findViewById(R.id.tvMarkaModel);
            tvGodina = itemView.findViewById(R.id.tvGodina);
            tvKilometraza = itemView.findViewById(R.id.tvKilometraza);
            tvRegistracija = itemView.findViewById(R.id.tvRegistracija);
        }
    }
}