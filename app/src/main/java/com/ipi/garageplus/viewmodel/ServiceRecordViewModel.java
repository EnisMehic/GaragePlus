package com.ipi.garageplus.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ipi.garageplus.model.ServiceRecord;
import com.ipi.garageplus.repository.VehicleRepository;

import java.util.List;

public class ServiceRecordViewModel extends AndroidViewModel {

    private VehicleRepository repository;

    public ServiceRecordViewModel(Application application) {
        super(application);
        repository = new VehicleRepository(application);
    }

    public LiveData<List<ServiceRecord>> getServicesByVehicle(int vehicleId) {
        return repository.getServicesByVehicle(vehicleId);
    }

    public LiveData<List<ServiceRecord>> getServicesByVehicleSortedByType(int vehicleId) {
        return repository.getServicesByVehicleSortedByType(vehicleId);
    }

    public LiveData<List<ServiceRecord>> getServicesByVehicleSortedByPrice(int vehicleId) {
        return repository.getServicesByVehicleSortedByPrice(vehicleId);
    }

    public LiveData<List<ServiceRecord>> searchServices(int vehicleId, String query) {
        return repository.searchServices(vehicleId, query);
    }

    public LiveData<Double> getTotalCost(int vehicleId) {
        return repository.getTotalCostByVehicle(vehicleId);
    }

    public void insertServiceRecord(ServiceRecord record) {
        repository.insertServiceRecord(record);
    }

    public void deleteServiceRecord(ServiceRecord record) {
        repository.deleteServiceRecord(record);
    }
}