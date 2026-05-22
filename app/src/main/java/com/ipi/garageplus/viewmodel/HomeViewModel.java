package com.ipi.garageplus.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ipi.garageplus.model.Vehicle;
import com.ipi.garageplus.repository.VehicleRepository;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private VehicleRepository repository;
    private LiveData<List<Vehicle>> vehicles;

    public HomeViewModel(Application application) {
        super(application);
        repository = new VehicleRepository(application);
    }

    public LiveData<List<Vehicle>> getVehiclesByUser(String userId) {
        if (vehicles == null) {
            vehicles = repository.getVehiclesByUser(userId);
        }
        return vehicles;
    }

    public void insertVehicle(Vehicle vehicle) {
        repository.insertVehicle(vehicle);
    }

    public void updateVehicle(Vehicle vehicle) {
        repository.updateVehicle(vehicle);
    }

    public void deleteVehicle(Vehicle vehicle) {
        repository.deleteVehicle(vehicle);
    }
}